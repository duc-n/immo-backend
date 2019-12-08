package com.cele.immo.service;

import com.cele.immo.dto.BienCritere;
import com.cele.immo.dto.BienResult;
import com.cele.immo.helper.BienMatchHelper;
import com.cele.immo.model.bien.Bien;
import com.cele.immo.model.bien.EtatBien;
import com.cele.immo.repository.BienRepository;
import com.cele.immo.repository.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BienServiceImpl implements BienService {
    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    BienRepository bienRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Override
    public Mono<Bien> save(Bien bien) {
        return bienRepository.save(bien);
    }

    @Override
    public Mono<Bien> findById(String id) {
        return bienRepository.findById(id);
    }

    @Override
    public Flux<Bien> findByIdExcludePassword(String id) {
        List<AggregationOperation> matchOperations = new ArrayList<>();
        Criteria idCriteria = Criteria.where("id").is(id);
        matchOperations.add(Aggregation.match(idCriteria));

        //matchOperations.add(BienMatchHelper.excludePasswordProjectOperation());

        // create lookup
        LookupOperation consultantLookupOperation = LookupOperation
                .newLookup().from("userAccount").localField("consultantId").foreignField("username").as("consultant");

        LookupOperation consultantsAssocieLookupOperation = LookupOperation
                .newLookup().from("userAccount").localField("consultantsAssocies.consultantId").foreignField("username").as("consultant");

        //matchOperations.add(consultantLookupOperation);
        //matchOperations.add(consultantsAssocieLookupOperation);

        Aggregation aggregation = Aggregation.newAggregation(matchOperations);


        return reactiveMongoTemplate.aggregate(aggregation, Bien.class, Bien.class);


    }

    @Override
    public Flux<Bien> findAll() {
        return bienRepository.findAll();
    }

    @Override
    public Page<BienResult> getBiensEtatCreation(String username) {
        log.debug("getBiensEtatCreation BEGIN. Username={}", username);

        Pageable pageable = PageRequest.of(0, 10);
        List<AggregationOperation> matchOperations = new ArrayList<>();
        // create lookup
        LookupOperation lookupOperation = LookupOperation
                .newLookup().from("userAccount").localField("consultantId").foreignField("username").as("consultants");

        Criteria consultantId = Criteria.where("consultantId").is(username);
        Criteria etatCreation = Criteria.where("etat").is(EtatBien.CREATION);
        matchOperations.add(Aggregation.match(consultantId));
        matchOperations.add(Aggregation.match(etatCreation));
        matchOperations.add(lookupOperation);

        matchOperations.add(BienMatchHelper.getProjectOperation());
        Aggregation aggregation = Aggregation.newAggregation(matchOperations);
        //Convert the aggregation result into a List
        List<BienResult> biens = mongoTemplate.aggregate(aggregation, Bien.class, BienResult.class).getMappedResults();

        log.debug("BienDTO result {}", biens.size());
        return new PageImpl(biens, pageable, biens.size());
    }

    @Override
    public Mono<Page<Bien>> searchCriteriaReactive(BienCritere bienCritere) {
        log.debug("searchCriteriaReactive begin");
        Pageable pageable = PageRequest.of(0, 10);

        Query query = new Query().with(pageable);

        if (BooleanUtils.isTrue(bienCritere.getPopupStore())) {
            query.addCriteria(Criteria.where("detailBien.activites.popupStore").is(Boolean.TRUE));

        }

        // get number of result and get search result and then merge the two results into the Page object by using zipWhen
        Mono<Page<Bien>> pageBienMono = reactiveMongoTemplate.count(query, Bien.class).zipWhen(c -> reactiveMongoTemplate.find(query, Bien.class).collectList())
                .map(tuple -> PageableExecutionUtils.getPage(tuple.getT2(), pageable, () -> tuple.getT1()));

        log.debug("searchCriteriaReactive end");
        return pageBienMono;

    }

    @Override
    public Page<Bien> searchCriteria(BienCritere bienCritere) {

        return bienRepository.searchBienCriteria(bienCritere);
    }

}
