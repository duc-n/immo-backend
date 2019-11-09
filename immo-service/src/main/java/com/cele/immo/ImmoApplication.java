package com.cele.immo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
//@EnableEurekaClient
public class ImmoApplication {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    //in case you want to encrypt password
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public static void main(String[] args) {
        SpringApplication.run(ImmoApplication.class, args);
    }


   /* @Bean
    CommandLineRunner start(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            List<Role> roles = new ArrayList<>();
            Role role = new Role();
            role.setId("ADMIN");
            roles.add(role);


            UserAccount user = UserAccount.builder().username("Duc")
                    .firstName("Duc").lastName("NGUYEN").email("duc@gmail.com")
                    .password(passwordEncoder.encode("password"))
                    .roles(roles).build();
            userAccountRepository.save(user).subscribe();

            userAccountRepository.findAll().log().subscribe(u -> log.info("user: {}", u));
        };
    }*/
}

