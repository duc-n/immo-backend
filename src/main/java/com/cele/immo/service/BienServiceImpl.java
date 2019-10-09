package com.cele.immo.service;

import com.cele.immo.model.Bien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class BienServiceImpl implements BienService {
    @Autowired
    ReactiveMongoTemplate template;

    @Override
    public Flux<Bien> searchCriteria() {

        Query query = new Query();

        Criteria regexNom = Criteria.where("nomTitulaire").regex("Cele", "i");// i option for case insensitive.
        Criteria descriptifOption = Criteria.where("descriptif.disponible").is(Boolean.TRUE);

        query.addCriteria(regexNom);
        query.addCriteria(descriptifOption);
        return template.find(query, Bien.class);
    }
}
