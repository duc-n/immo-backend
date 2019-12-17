package com.cele.immo.mongodb.integration;

import com.cele.immo.config.PBKDF2Encoder;
import com.cele.immo.model.Role;
import com.cele.immo.model.UserAccount;
import com.cele.immo.repository.UserAccountRepository;
import com.cele.immo.service.BienService;
import org.junit.jupiter.api.BeforeEach;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

//@SpringBootTest
//@DataMongoTest
public class BienServiceTest {
    @Autowired
    BienService bienService;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    PBKDF2Encoder passwordEncoder;

    @BeforeEach
    public void init() {


    }

    //@Test
    public void givenUserObject_whenSave_thenCreateNewUser() {

        UserAccount userAdmin = UserAccount.builder()
                .username("admin@gmail.com")
                .password(passwordEncoder.encode("admin"))
                .telephone("0686955644")
                .prenom("duc")
                .nom("nguyen")
                .roles(Arrays.asList(Role.ROLE_ADMIN))
                .active(Boolean.TRUE)
                .build();

        UserAccount user = UserAccount.builder()
                .username("user@gmail.com")
                .password(passwordEncoder.encode("user"))
                .telephone("0686955644")
                .prenom("duc")
                .nom("nguyen")
                .roles(Arrays.asList(Role.ROLE_USER))
                .active(Boolean.TRUE)
                .build();


        Publisher<UserAccount> setup = this.userAccountRepository.deleteAll()
                .thenMany(this.userAccountRepository.saveAll(Flux.just(userAdmin, user))
                );

        Publisher<UserAccount> find = this.userAccountRepository.findByNom("nguyen");

        Publisher<UserAccount> composite = Flux
                .from(setup)
                .thenMany(find);
        StepVerifier
                .create(composite)
                .expectNext(userAdmin, user)
                .verifyComplete();

    }
}
