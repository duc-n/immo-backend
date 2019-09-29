package com.cele.immo.config;

import com.cele.immo.model.UserAccount;
import com.cele.immo.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
public class SecurityConfiguration {
    @Autowired
    UserAccountRepository userAccountRepository;
    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/login", "/logout").permitAll()
                .pathMatchers("/i18n/**",
                        "/css/**",
                        "/fonts/**",
                        "/icons-reference/**",
                        "/img/**",
                        "/js/**",
                        "/vendor/**").permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .logoutUrl("/logout")
                .and()
                .build();
    }



   /* @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return (username) -> userAccountRepository.findByUsername(username).map();
    }*/

    /*@Bean
    public MapReactiveUserDetailsService userDetailsService() {
       Mono<UserAccount> user =  userAccountRepository.findByUsername("duc");
        return new MapReactiveUserDetailsService(user);
    }*/
}
