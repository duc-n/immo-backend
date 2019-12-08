package com.cele.immo.service;

import com.cele.immo.dto.BienCritere;
import com.cele.immo.dto.BienResult;
import com.cele.immo.model.bien.Bien;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BienService {
    Mono<Bien> save(Bien bien);

    Mono<Bien> findById(String id);

    Flux<Bien> findByIdExcludePassword(String id);

    Flux<Bien> findAll();

    Page<BienResult> getBiensEtatCreation(String username);

    Mono<Page<Bien>> searchCriteriaReactive(BienCritere bienCritere);

    Page<Bien> searchCriteria(BienCritere bienCritere);

}
