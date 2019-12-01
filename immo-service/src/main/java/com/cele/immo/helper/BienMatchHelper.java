package com.cele.immo.helper;

import com.cele.immo.dto.BienMatch;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class BienMatchHelper {

    public static ProjectionOperation getProjectOperation() {
        return project("id", "etat")
                .and("detailBien.adresseBien.adresse").as("adresse")
                .and("detailBien.adresseBien.codePostal").as("codePostal")
                ;
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
