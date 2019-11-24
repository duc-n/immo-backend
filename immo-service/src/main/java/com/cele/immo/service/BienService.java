package com.cele.immo.service;

import com.cele.immo.dto.BienCritere;
import com.cele.immo.model.bien.Bien;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BienService {
    Mono<Bien> save(Bien bien);

    Mono<Bien> findById(String id);

    Flux<Bien> findAll();

    Flux<Bien> getBiensEtatCreation();

    Mono<Page<Bien>> searchCriteriaReactive(BienCritere bienCritere);

    Page<Bien> searchCriteria(BienCritere bienCritere);

}
