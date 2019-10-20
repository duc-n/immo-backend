package com.cele.immo.repository;

import com.cele.immo.model.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ClientRepository extends ReactiveMongoRepository<Client, String> {
}
