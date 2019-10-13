package com.cele.immo.service;

import com.cele.immo.model.acquereur.Acquereur;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AcquereurService {
    Mono<Acquereur> update(Acquereur acquereur);

    Mono<Acquereur> insert(Acquereur acquereur);

    Mono<Acquereur> findById(String id);

    Flux<Acquereur> findAll();

    Mono<Page<Acquereur>> searchCriteria();

}
