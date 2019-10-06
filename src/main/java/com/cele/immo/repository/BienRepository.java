package com.cele.immo.repository;

import com.cele.immo.model.Bien;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BienRepository extends ReactiveMongoRepository<Bien, String> {

}
