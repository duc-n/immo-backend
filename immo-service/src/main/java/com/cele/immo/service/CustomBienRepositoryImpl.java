package com.cele.immo.service;

import com.cele.immo.dto.BienCritere;
import com.cele.immo.dto.BienDTO;
import com.cele.immo.model.UserAccount;
import com.cele.immo.model.bien.Bien;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Service
public class CustomBienRepositoryImpl implements CustomBienRepository {

    @Autowired
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomBienRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Bien> searchBienCriteria(BienCritere bienCritere) {

        Pageable pageable = PageRequest.of(0, 10);
        List<AggregationOperation> matchOperations = new ArrayList<>();

        //Consultant name
        if (StringUtils.hasText(bienCritere.getConsultant())) {
            Query consultantNameQuery = new Query();
            Criteria regexLastName = Criteria.where("lastName").regex(bienCritere.getConsultant(), "i");// i option for case insensitive.
            consultantNameQuery.addCriteria(regexLastName);

            List<UserAccount> consultants = mongoTemplate.find(consultantNameQuery, UserAccount.class, "userAccount");
            List<String> consultantsIds = new ArrayList<>();

            if (!CollectionUtils.isEmpty(consultants)) {
                consultantsIds = consultants.stream().map(c -> c.getUsername()).collect(Collectors.toList());
            }

            Criteria idsConsultants = Criteria.where("consultantId").in(consultantsIds);
            matchOperations.add(Aggregation.match(idsConsultants));
        }

        // popupStore opt
        if (BooleanUtils.isTrue(bienCritere.getPopupStore())) {
            matchOperations.add(Aggregation.match(Criteria.where("detailBien.activites.popupStore").is(Boolean.TRUE)));
        }

        matchSurfaceCriteria(matchOperations, bienCritere);

        matchOperations.add(getProjectOperation());


        matchOperations.add(new SkipOperation(pageable.getPageNumber() * pageable.getPageSize()));
        matchOperations.add(new LimitOperation(pageable.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperations
        );

        //Convert the aggregation result into a List
        List<BienDTO> biens
                = mongoTemplate.aggregate(aggregation, Bien.class, BienDTO.class).getMappedResults();
        //List<Bien> biens = mongoTemplate.find(query, Bien.class, "bien");

        //return PageableExecutionUtils.getPage(biens, pageable, () -> mongoTemplate.count(query, Bien.class));

        return new PageImpl(biens, pageable, biens.size());
    }


    private ProjectionOperation getProjectOperation() {
        return project("id")
                .and("detailBien.adresseBien.adresse").as("adresse")
                .and("detailBien.adresseBien.codePostal").as("codePostal")
                ;
    }

    private void matchSurfaceCriteria(List<AggregationOperation> matchOperations, BienCritere bienCritere) {

        Criteria surfaceCriteria = where("surface.surfaceTotale");
        boolean hasSurface = false;
        // surfaceTotaleMin
        if (Objects.nonNull(bienCritere.getSurfaceTotaleMin())) {
            surfaceCriteria.gte(bienCritere.getSurfaceTotaleMin());
            hasSurface = true;

        }
        // surfaceTotaleMax
        if (Objects.nonNull(bienCritere.getSurfaceTotaleMax())) {
            surfaceCriteria.lte(bienCritere.getSurfaceTotaleMax());
            hasSurface = true;

        }
        if (hasSurface) {
            matchOperations.add(Aggregation.match(surfaceCriteria));
        }

    }
}
