package com.cele.immo.controller;

import com.cele.immo.model.acquereur.Acquereur;
import com.cele.immo.service.AcquereurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(value = "/acquereur")
@RequiredArgsConstructor
@Slf4j
public class AcquereurController {

    @Autowired
    AcquereurService acquereurService;

    @GetMapping()
    public Flux<Acquereur> getAllAcquereur() {
        return this.acquereurService.findAll();
    }

    @GetMapping("{id}")
    public Mono<Acquereur> getAcquereurById(@PathVariable String id) {
        return this.acquereurService.findById(id);
    }

    @GetMapping("/searchByOwner")
    public Mono<Page<Acquereur>> searchByOwner(
            @RequestParam() String ownerName,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "2") Integer size
    ) {

        return acquereurService.searchCriteria();
    }

}
