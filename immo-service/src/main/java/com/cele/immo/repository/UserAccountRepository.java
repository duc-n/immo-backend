package com.cele.immo.repository;

import com.cele.immo.model.UserAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserAccountRepository extends ReactiveMongoRepository<UserAccount, String> {
    Mono<UserAccount> findByUsername(String username);

    Flux<UserAccount> findByLastNameLike(String lastName);

    Flux<UserAccount> findByLastName(String lastName);
}
