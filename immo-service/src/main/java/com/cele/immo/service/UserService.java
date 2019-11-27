package com.cele.immo.service;

import com.cele.immo.config.PBKDF2Encoder;
import com.cele.immo.model.Role;
import com.cele.immo.model.UserAccount;
import com.cele.immo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PBKDF2Encoder bcryptEncoder;

    //username:passwowrd -> user:user
    UserAccount user = UserAccount.builder().username("user")
            .password("kqNGOfikyqJ+IceZ8hkcbeXY5O6VymY3RcB8DGFtX1I=")
            .active(true)
            .roles(Arrays.asList(Role.ROLE_USER))
            .build();

    //username:passwowrd -> admin:admin
    UserAccount admin = UserAccount.builder().username("admin")
            .password("z7gzSlbhkR6AWDl0CP9OwIDg9aafiUyFZpc27kQdL4U=")
            .active(true)
            .roles(Arrays.asList(Role.ROLE_ADMIN))
            .build();


    public Mono<UserAccount> findByUsername(String username) {
        /*
        if (username.equals("user")) {
            return Mono.just(user);
        } else if (username.equals("admin")) {
            return Mono.just(admin);
        } else {
            return Mono.empty();
        }
        */
        return userRepository.findByUsername(username);
    }

    public Mono<UserAccount> save(UserAccount user) {
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}