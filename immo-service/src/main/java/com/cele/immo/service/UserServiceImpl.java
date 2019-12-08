package com.cele.immo.service;

import com.cele.immo.config.PBKDF2Encoder;
import com.cele.immo.dto.ConsultantDTO;
import com.cele.immo.model.Role;
import com.cele.immo.model.UserAccount;
import com.cele.immo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PBKDF2Encoder bcryptEncoder;

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

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


    @Override
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

    @Override
    public Mono<UserAccount> save(UserAccount user) {
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Flux<ConsultantDTO> findAllUsers() {

        Aggregation aggregation = Aggregation.newAggregation(getProjectOperation());
        Flux<ConsultantDTO> consultantDTOFlux = reactiveMongoTemplate.aggregate(aggregation, UserAccount.class, ConsultantDTO.class);
        return consultantDTOFlux;
    }

    public static ProjectionOperation getProjectOperation() {
        return project("id", "nom", "prenom", "username", "telephone");
    }
}