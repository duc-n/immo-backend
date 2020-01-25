package com.cele.immo.service;

import com.cele.immo.dto.ConsultantDTO;
import com.cele.immo.dto.UserProfile;
import com.cele.immo.dto.UserRegister;
import com.cele.immo.model.UserAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserAccount> findByUsername(String username);

    Mono<UserAccount> save(UserAccount user);

    Flux<ConsultantDTO> findAllUsers();

    Mono<UserAccount> userRegister(UserRegister userRegister);

    Mono<UserProfile> updateUser(UserAccount userAccount, UserProfile userProfile);

}
