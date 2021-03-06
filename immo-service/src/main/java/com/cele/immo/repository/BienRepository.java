package com.cele.immo.repository;

import com.cele.immo.model.bien.Bien;
import com.cele.immo.service.CustomBienRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BienRepository extends ReactiveMongoRepository<Bien, String>, CustomBienRepository {
    Flux<Bien> findByConsultantId(String consultantId, Pageable page);
}
