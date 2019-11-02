package com.cele.immo.config;

import com.cele.immo.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfiguration {
    @Autowired
    UserAccountRepository userAccountRepository;

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers("/actuator/**").permitAll()
                //.pathMatchers(HttpMethod.GET, "/posts/**").permitAll()
                //.pathMatchers(HttpMethod.DELETE, "/posts/**").hasRole("ADMIN")
                //.pathMatchers("/posts/**").authenticated()
                //.pathMatchers("/users/{user}/**").access(this::currentUserMatchesPath)
                .anyExchange().permitAll()
                .and()
                .csrf().disable()
                .build();
    }


    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return (username) -> userAccountRepository.findByUsername(username).cast(UserDetails.class);
    }

/*    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
       Mono<UserAccount> user =  userAccountRepository.findByUsername("duc");
        return new MapReactiveUserDetailsService(user);
    }*/
}
