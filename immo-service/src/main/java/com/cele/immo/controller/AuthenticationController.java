package com.cele.immo.controller;

import com.cele.immo.config.JWTUtil;
import com.cele.immo.config.PBKDF2Encoder;
import com.cele.immo.dto.AuthRequest;
import com.cele.immo.dto.AuthResponse;
import com.cele.immo.dto.UserDTO;
import com.cele.immo.model.UserAccount;
import com.cele.immo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@RestController()
public class AuthenticationController {
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    private UserService userService;

    public static Optional<UserAccount> currentUser(Authentication auth) {
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserAccount) // User is your user type that implements UserDetails
                return Optional.of((UserAccount) principal);
        }
        return Optional.empty();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar) {
        log.debug("Login - BEGIN - userName {}, password {}", ar.getUsername(), passwordEncoder.encode(ar.getPassword()));

        return userService.findByUsername(ar.getUsername()).map((userDetails) -> {
            if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
                return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails), new UserDTO(userDetails.getUsername(), userDetails.getNom(), userDetails.getPrenom(), userDetails.hasAdminRole())));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/getUser")
    public Mono<String> getUser(ServerWebExchange exchange) {
       /*
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                //.map(auth -> currentUser(auth))
                .zipWith(exchange.getFormData())
                .doOnNext(tuple -> {
                    // based on some input parameters, amend the current user data to be returned
                    log.debug("TA : {}", tuple.getT1());
                    log.debug("TA t2: {}", tuple.getT2());
                })
                .map(Tuple2::getT1);*/

        return exchange.getPrincipal().map(Principal::getName).switchIfEmpty(Mono.empty());
    }

}
