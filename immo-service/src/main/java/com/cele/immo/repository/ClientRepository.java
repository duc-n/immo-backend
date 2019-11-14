package com.cele.immo.repository;

import com.cele.immo.model.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ClientRepository extends ReactiveMongoRepository<Client, String> {
    Flux<Client> findByNomContainingIgnoreCase(String nom, Pageable page);
}
