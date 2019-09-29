package com.cele.immo.repository;

import com.cele.immo.model.UserAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface UserAccountRepository extends ReactiveMongoRepository<UserAccount, String> {
    Mono<UserDetails> findByUsername(String username);
}
