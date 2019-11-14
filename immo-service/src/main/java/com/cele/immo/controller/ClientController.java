package com.cele.immo.controller;

import com.cele.immo.model.Client;
import com.cele.immo.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(value = "/client")
@Slf4j
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping("/{id}")
    public Mono<Client> getClientId(@PathVariable String id) {
        return this.clientService.findById(id);
    }

    @GetMapping("/clientLookup")
    public Flux<Client> clientLookup(@RequestParam String nom) {
        return this.clientService.findByName(nom);
    }


}
