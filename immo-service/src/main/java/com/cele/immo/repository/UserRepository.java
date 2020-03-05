package com.cele.immo.repository;

import com.cele.immo.model.UserAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserAccount, String> {
    Mono<UserAccount> findByUsername(String username);
}
