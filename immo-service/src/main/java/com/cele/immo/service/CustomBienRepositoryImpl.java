package com.cele.immo.service;

import com.cele.immo.dto.BienCritere;
import com.cele.immo.dto.BienMatch;
import com.cele.immo.dto.BienResult;
import com.cele.immo.helper.BienMatchHelper;
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
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomBienRepositoryImpl implements CustomBienRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomBienRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Bien> searchBienCriteria(BienCritere bienCritere) {

        com.cele.immo.model.Page page = bienCritere.getPage();
        Pageable pageable;

        if (Objects.nonNull(page)) {
            pageable = PageRequest.of(page.getPageNumber(), page.getSize() > 0 ? page.getSize() : 10);
        } else {
            pageable = PageRequest.of(0, 10);
        }

        List<AggregationOperation> matchOperations = new ArrayList<>();
        Query countQuery = new Query();

        // consultant name
        if (StringUtils.hasText(bienCritere.getConsultant())) {
            Query consultantNameQuery = new Query();
            Criteria regexLastName = Criteria.where("nom").regex(bienCritere.getConsultant(), "i");// i option for case insensitive.
            consultantNameQuery.addCriteria(regexLastName);

            List<UserAccount> consultants = mongoTemplate.find(consultantNameQuery, UserAccount.class, "userAccount");
            List<String> consultantsIds = new ArrayList<>();

            if (!CollectionUtils.isEmpty(consultants)) {
                consultantsIds = consultants.stream().map(c -> c.getUsername()).collect(Collectors.toList());
            }

            Criteria idsConsultants = Criteria.where("consultantId").in(consultantsIds);

            BienMatchHelper.addCriteria(idsConsultants, countQuery, matchOperations);
        }

        // type de bien
        if (StringUtils.hasText(bienCritere.getTypeBien())) {
            Criteria typeBien = Criteria.where("detailBien.typeBien").is(bienCritere.getTypeBien());
            BienMatchHelper.addCriteria(typeBien, countQuery, matchOperations);
        }

        // popupStore opt
        if (BooleanUtils.isTrue(bienCritere.getPopupStore())) {
            BienMatchHelper.addCriteria(Criteria.where("detailBien.activites.popupStore").is(Boolean.TRUE), countQuery, matchOperations);
        }

        //surface.surfaceTotale
        BienMatch surfaceTotalMatch = BienMatch.builder()
                .criteriaName("surface.surfaceTotale")
                .valueMin(bienCritere.getSurfaceTotaleMin())
                .valueMax(bienCritere.getSurfaceTotaleMax())
                .build();
        BienMatchHelper.matchBetween(surfaceTotalMatch, countQuery, matchOperations);

        // adresse
        if (StringUtils.hasText(bienCritere.getAdresse())) {
            Criteria regexAdresse = Criteria.where("detailBien.adresseBien.adresse").regex(bienCritere.getAdresse(), "i");// i option for case insensitive.
            BienMatchHelper.addCriteria(regexAdresse, countQuery, matchOperations);
        }

        // codePostal
        if (StringUtils.hasText(bienCritere.getCodePostal())) {
            Criteria regexCodePostal = Criteria.where("detailBien.adresseBien.codePostal").regex(bienCritere.getCodePostal(), "i");// i option for case insensitive.
            BienMatchHelper.addCriteria(regexCodePostal, countQuery, matchOperations);
        }

        matchOperations.add(BienMatchHelper.getProjectOperation());

        matchOperations.add(new SkipOperation(pageable.getPageNumber() * pageable.getPageSize()));
        matchOperations.add(new LimitOperation(pageable.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(matchOperations);

        //Convert the aggregation result into a List
        List<BienResult> biens = mongoTemplate.aggregate(aggregation, Bien.class, BienResult.class).getMappedResults();

        long total = mongoTemplate.count(countQuery, Bien.class);
        log.debug("Total bien result : {}", total);

        //return PageableExecutionUtils.getPage(biens, pageable, () -> mongoTemplate.count(query, Bien.class));
        return new PageImpl(biens, pageable, total);
    }


}
