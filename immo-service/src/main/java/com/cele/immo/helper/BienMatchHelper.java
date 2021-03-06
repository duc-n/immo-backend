package com.cele.immo.helper;

import com.cele.immo.dto.BienMatch;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.bind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class BienMatchHelper {

    public static ProjectionOperation getProjectOperation() {
        return project("id", "etat")
                .and("detailBien.adresseBien.adresse").as("adresse")
                .and("detailBien.adresseBien.codePostal").as("codePostal")
                ;
    }

    public static ProjectionOperation excludePasswordProjectOperation() {
        return project("detailBien", "conditionsFinancieres", "bail", "visite", "consultant", "descriptif", "communication", "etat", "surface", "mandat", "photos", "videos")
                //andExclude("consultant.password")
                .and(
                        VariableOperators.mapItemsOf("consultantsAssocies").as("rt")
                                .andApply(
                                        new AggregationExpression() {
                                            @Override
                                            public Document toDocument(AggregationOperationContext aggregationOperationContext) {
                                                return new Document("commission", "$$rt.commission")
                                                        .append("consultant", "$$rt.consultant");
                                            }
                                        }
                                )
                ).as("consultantsAssociesList");

    }

    public static ProjectionOperation excludePasswordProjectOperation1() {
        return project("detailBien", "conditionsFinancieres", "bail", "visite", "consultant", "descriptif", "communication", "etat", "surface", "mandat", "photos", "videos")
                //andExclude("consultant.password")
                .and(
                        VariableOperators.mapItemsOf("consultantsAssocies").as("cas")
                                .andApply(
                                        VariableOperators.mapItemsOf("cas.consultant").as("co")
                                                .andApply(
                                                        new AggregationExpression() {
                                                            @Override
                                                            public Document toDocument(AggregationOperationContext aggregationOperationContext) {
                                                                return new Document("commission", "$$cas.commission")
                                                                        .append("username", "$$co.username");
                                                            }
                                                        }
                                                )
                                )
                ).as("consultantsAssociesList");

    }

    public static ProjectionOperation excludePasswordProjectOperation2() {
        return project("detailBien", "conditionsFinancieres", "bail", "visite", "consultant", "descriptif", "communication", "etat", "surface", "mandat", "photos", "videos")
                //andExclude("consultant.password")
                .and("consultantsAssociesList")
                .nested(bind("commission", "consultantsAssocies.commission").and("username", "consultantsAssocies.consultant.username"))
                ;

    }

    public static ProjectionOperation excludePasswordProjectOperation3() {
        return project("detailBien", "conditionsFinancieres", "bail", "visite", "consultant", "descriptif", "communication", "etat", "surface", "mandat", "photos", "videos")
                //andExclude("consultant.password")
                .and(
                        VariableOperators.mapItemsOf("consultantsAssocies").as("rt")
                                .andApply(
                                        new AggregationExpression() {
                                            @Override
                                            public Document toDocument(AggregationOperationContext aggregationOperationContext) {
                                                return new Document("commission", "$$rt.commission")
                                                        //.append("consultant", "$$rt.consultant");
                                                        .append("consultant", new Document()
                                                                .append("username", "$$rt.consultant.username")
                                                                .append("nom", "$$rt.consultant.nom")
                                                        );
                                            }
                                        }
                                )
                ).as("consultantsAssociesList");

    }


    public static void matchBetween(BienMatch bienMatch, Query query, List<AggregationOperation> matchOperations) {

        Criteria surfaceCriteria = where(bienMatch.getCriteriaName());
        boolean hasCriteria = false;
        if (Objects.nonNull(bienMatch.getValueMin())) {
            surfaceCriteria.gte(bienMatch.getValueMin());
            hasCriteria = true;

        }
        if (Objects.nonNull(bienMatch.getValueMax())) {
            surfaceCriteria.lte(bienMatch.getValueMax());
            hasCriteria = true;

        }
        if (hasCriteria) {
            addCriteria(surfaceCriteria, query, matchOperations);
        }

    }

    public static void addCriteria(Criteria criteria, Query query, List<AggregationOperation> matchOperations) {
        query.addCriteria(criteria);
        matchOperations.add(Aggregation.match(criteria));
    }

}
