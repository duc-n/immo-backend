package com.cele.immo.config;

import com.cele.immo.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN";
    private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS";
    private static final String ALLOWED_Origin = "*";
    private static final long MAX_AGE = 3600;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    /*
    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        //corsConfig.setAllowedOrigins(Arrays.asList(ALLOWED_Origin));
        corsConfig.addAllowedOrigin(ALLOWED_Origin);
        corsConfig.setMaxAge(MAX_AGE);
        corsConfig.addAllowedMethod(ALLOWED_METHODS);
        corsConfig.addAllowedHeader(ALLOWED_HEADERS);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
*/
    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
        return http
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> {
                    return Mono.fromRunnable(() -> {
                        swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        swe.getResponse().getHeaders().setAccessControlAllowOrigin("*");
                    });
                }).accessDeniedHandler((swe, e) -> {
                    return Mono.fromRunnable(() -> {
                        swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        swe.getResponse().getHeaders().setAccessControlAllowOrigin("*");
                    });
                }).and()
                .csrf().disable()
                //.cors().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/login").permitAll()
                .anyExchange().authenticated()
                .and().build();
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
