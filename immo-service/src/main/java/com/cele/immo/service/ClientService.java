package com.cele.immo.service;

import com.cele.immo.model.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {
    Mono<Client> save(Client client);

    Mono<Client> findById(String id);

    Flux<Client> findAll();

    Flux<Client> findByName(String name);

    Flux<Client> findFullText(String fullName);

}
