package com.cele.immo.service;

import com.cele.immo.dto.BienCritere;
import com.cele.immo.dto.BienDTO;
import com.cele.immo.dto.BienResult;
import com.cele.immo.dto.S3FileDescription;
import com.cele.immo.model.bien.Bien;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BienService {
    Mono<Bien> save(Bien bien);

    Mono<Bien> createBien();

    Mono<Bien> findById(String id);

    Mono<BienDTO> findByIdExcludePassword(String id);

    Flux<Bien> findAll();

    Page<BienResult> getBiensEtatCreation(String username);

    Mono<Page<Bien>> searchCriteriaReactive(BienCritere bienCritere);

    Page<Bien> searchCriteria(BienCritere bienCritere);

    Mono<String> savePhoto(S3FileDescription s3FileDescription);

}
