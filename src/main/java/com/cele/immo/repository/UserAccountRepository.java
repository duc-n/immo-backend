package com.cele.immo.repository;

import com.cele.immo.model.bien.UserAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserAccountRepository extends ReactiveMongoRepository<UserAccount, String> {
    Mono<UserAccount> findByUsername(String username);
}
