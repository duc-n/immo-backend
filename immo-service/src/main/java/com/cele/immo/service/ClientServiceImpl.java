package com.cele.immo.service;

import com.cele.immo.model.Client;
import com.cele.immo.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<Client> save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Mono<Client> findById(String id) {
        return clientRepository.findById(id);
    }

    @Override
    public Flux<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Flux<Client> findByName(String nom) {
        Pageable pageable = PageRequest.of(0, 20);
        return clientRepository.findByNomContainingIgnoreCase(nom, pageable);
    }

    @Override
    public Flux<Client> findFullText(String fullName) {
        TextCriteria textCriteria = new TextCriteria();
        textCriteria.matchingAny(fullName).caseSensitive(false);
        Query query = new TextQuery(textCriteria).sortByScore();
        return reactiveMongoTemplate.find(query, Client.class);
    }
}
