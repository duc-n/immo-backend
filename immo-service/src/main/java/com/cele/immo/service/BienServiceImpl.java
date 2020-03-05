package com.cele.immo.service;

import com.cele.immo.dto.BienCritere;
import com.cele.immo.dto.BienDTO;
import com.cele.immo.dto.BienResult;
import com.cele.immo.dto.S3FileDescription;
import com.cele.immo.helper.BienMatchHelper;
import com.cele.immo.model.Photo;
import com.cele.immo.model.bien.*;
import com.cele.immo.repository.BienRepository;
import com.cele.immo.repository.UserAccountRepository;
import com.google.common.collect.Lists;
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
    public Mono<Bien> createBien(String username) {
        return userAccountRepository.findByUsername(username)
                .flatMap(userAccount -> {
                    Bien bien = Bien.builder()
                            .etat(EtatBien.CREATION)
                            .detailBien(DetailBien.builder()
                                    .activites(Activites.builder()
                                            .build())
                                    .adresseBien(AdresseBien.builder()
                                            .emplacements(Emplacements.builder()
                                                    .build())
                                            .build())
                                    .build()
                            )
                            .bail(Bail.builder().build())
                            .surface(Surface.builder().build())
                            .visite(Visite.builder().build())
                            .communication(Communication.builder().build())
                            .conditionsFinancieres(ConditionsFinancieres.builder().build())
                            .consultantsAssocies(Lists.newArrayList())
                            .descriptif(Descriptif.builder().build())
                            .consultantId(username)
                            .consultant(userAccount)
                            .build();
                    return this.save(bien);
                });
    }

    @Override
    public Mono<Bien> findById(String id) {
        return bienRepository.findById(id);
    }

    @Override
    public Mono<BienDTO> findByIdExcludePassword(String id) {
        List<AggregationOperation> matchOperations = new ArrayList<>();
        Criteria idCriteria = Criteria.where("id").is(id);
        matchOperations.add(Aggregation.match(idCriteria));

        //matchOperations.add(BienMatchHelper.excludePasswordProjectOperation());

        // create lookup
        LookupOperation consultantLookupOperation = LookupOperation
                .newLookup().from("userAccount").localField("consultantId").foreignField("username").as("consultant");

        // UnwindOperation consultantsAssociesUnwindOperation = Aggregation.unwind("consultantsAssocies");

        /*

        LookupOperation consultantsAssocieLookupOperation = LookupOperation
                .newLookup().from("userAccount").localField("consultantsAssocies.consultantId").foreignField("username").as("consultantsAssocies.consultant");

        GroupOperation groupOperation = Aggregation.group("id")
                .push("consultantsAssocies").as("consultantsAssocies")
                .first("consultant").as("consultant")
                .first("etat").as("etat")
                .first("detailBien").as("detailBien")
                .first("mandat").as("mandat")
                .first("bail").as("bail")
                .first("conditionsFinancieres").as("conditionsFinancieres")
                .first("visite").as("visite")
                .first("surface").as("surface")
                .first("descriptif").as("descriptif")
                .first("photos").as("photos")
                .first("videos").as("videos")
                .first("communication").as("communication");

        matchOperations.add(consultantsAssociesUnwindOperation);

        matchOperations.add(consultantsAssocieLookupOperation);
         */

        matchOperations.add(consultantLookupOperation);


        //matchOperations.add(consultantsUnwindOperation);


        //matchOperations.add(groupOperation);


        //matchOperations.add(BienMatchHelper.excludePasswordProjectOperation());
        Aggregation aggregation = Aggregation.newAggregation(matchOperations);

        // Used to test
        //AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, Bien.class, Document.class);
        //List<Document> documents = result.getMappedResults();
        //documents.forEach(document -> log.debug("Bien result {}", document));

        List<BienDTO> documents1 = mongoTemplate.aggregate(aggregation, Bien.class, BienDTO.class).getMappedResults();
        documents1.forEach(document -> log.debug("Bien1 result {}", document));

        return reactiveMongoTemplate.aggregate(aggregation, Bien.class, BienDTO.class).next();


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
                .newLookup().from("userAccount").localField("consultantId").foreignField("username").as("consultant");

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

    @Override
    public Mono<String> savePhoto(S3FileDescription s3FileDescription) {
        return this.findById(s3FileDescription.getBienId()).flatMap(bien -> {
            bien.getPhotos().add(Photo.builder()
                    .key(s3FileDescription.getKey())
                    .url(s3FileDescription.getUrl()).build());
            log.debug("Save photo : {}", s3FileDescription.getUrl());
            return this.save(bien).then(Mono.just(s3FileDescription.getUrl()));
        });
    }

}
