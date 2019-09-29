package com.cele.immo.service;

import com.cele.immo.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserDetailService implements ReactiveUserDetailsService {
    @Autowired
    UserAccountRepository userAccountRepository;
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userAccountRepository.findByUsername(username);
    }

}
