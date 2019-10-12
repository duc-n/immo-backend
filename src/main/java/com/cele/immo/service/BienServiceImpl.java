package com.cele.immo.service;

import com.cele.immo.model.Bien;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class BienServiceImpl implements BienService {
    @Autowired
    ReactiveMongoTemplate template;

    @Override
    public Mono<Page<Bien>> searchCriteria() {
        Pageable pageable = PageRequest.of(0, 10);

        Query query = new Query().with(pageable);

        Criteria regexNom = Criteria.where("nomTitulaire").regex("Cele", "i");// i option for case insensitive.
        Criteria descriptifOption = Criteria.where("descriptif.disponible").is(Boolean.TRUE);

        query.addCriteria(regexNom);
        query.addCriteria(descriptifOption);

        // get number of result and get search result and then merge the two results into the Page object by using zipWhen
        Mono<Page<Bien>> pageBienMono = template.count(query, Bien.class).zipWhen(c -> template.find(query, Bien.class).collectList())
                .map(tuple -> PageableExecutionUtils.getPage(tuple.getT2(), pageable, () -> tuple.getT1()));

        return pageBienMono;
    }
}
