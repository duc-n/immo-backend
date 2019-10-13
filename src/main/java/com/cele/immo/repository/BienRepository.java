package com.cele.immo.repository;

import com.cele.immo.model.bien.Bien;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface BienRepository extends ReactiveMongoRepository<Bien, String> {
    Flux<Bien> findByNomTitulaireLike(String nomTitulaire, Pageable page);
}
