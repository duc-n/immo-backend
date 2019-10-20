package com.cele.immo.service;

import com.cele.immo.model.bien.Bien;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BienService {
    Mono<Bien> update(Bien bien);

    Mono<Bien> insert(Bien bien);

    Mono<Bien> findById(String id);

    Flux<Bien> findAll();

    Mono<Page<Bien>> searchCriteria();

}