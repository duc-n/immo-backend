package com.cele.immo.controller;

import com.cele.immo.dto.ConsultantDTO;
import com.cele.immo.dto.UserRegister;
import com.cele.immo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/user")
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

        return userService.userRegister(userRegister).thenReturn(ResponseEntity.ok(userRegister));
    }


}
