package com.cele.immo.controller;

import com.cele.immo.dto.ConsultantDTO;
import com.cele.immo.dto.UserProfile;
import com.cele.immo.dto.UserRegister;
import com.cele.immo.exception.UserAlreadyRegisteredException;
import com.cele.immo.exception.UserNotFoundException;
import com.cele.immo.mappers.UserMapper;
import com.cele.immo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Flux<ConsultantDTO> users() {
        return userService.findAllUsers();
    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<?>> signup(@RequestBody UserRegister userRegister) {
        return
                userService.findByUsername(userRegister.getEmail())
                        .flatMap(userAccount -> Mono.error(new UserAlreadyRegisteredException("Utilisateur s'est inscrit")))
                        .switchIfEmpty(userService.userRegister(userRegister))
                        //.doOnNext(log.debug("The user :{} has been registered", userRegister.getEmail())
                        .thenReturn(ResponseEntity.ok(userRegister));
    }

    @PostMapping("/profile")
    public Mono<ResponseEntity<?>> updateProfile(@RequestBody UserProfile userProfile) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(String.class)
                .flatMap(username -> userService.findByUsername(username))
                .switchIfEmpty(Mono.error(new UserNotFoundException("Utilisateur non trouvé")))
                .flatMap(userAccount -> userService.updateUser(userAccount, userProfile))
                .thenReturn(ResponseEntity.ok(userProfile));

    }

    @GetMapping("/profile")
    public Mono<UserProfile> getProfile() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(String.class)
                .flatMap(username -> userService.findByUsername(username))
                .switchIfEmpty(Mono.error(new UserNotFoundException("Utilisateur non trouvé")))
                .flatMap(userAccount -> Mono.just(UserMapper.INSTANCE.toUserProfile(userAccount)));

    }

}
