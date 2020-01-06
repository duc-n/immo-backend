package com.cele.immo.repository;

import com.cele.immo.model.UserAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserAccountRepository extends ReactiveMongoRepository<UserAccount, String> {
    Mono<UserAccount> findByUsername(String username);

    Flux<UserAccount> findByNomLike(String nom);

    Flux<UserAccount> findByNom(String nom);
}
