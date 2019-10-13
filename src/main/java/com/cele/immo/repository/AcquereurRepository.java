package com.cele.immo.repository;

import com.cele.immo.model.acquereur.Acquereur;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AcquereurRepository extends ReactiveMongoRepository<Acquereur, String> {
}
