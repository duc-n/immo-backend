package com.cele.immo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cele.immo.model.Candidate;

public interface CandidateRepository extends MongoRepository<Candidate, String> {

    Optional<Candidate> findByEmail(String email);

    List<Candidate> findByExpGreaterThanEqual(double exp);

    List<Candidate> findByExpBetween(double from, double to);

}
