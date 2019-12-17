package com.cele.immo.controller;

import com.cele.immo.dto.BienCritere;
import com.cele.immo.dto.BienDTO;
import com.cele.immo.dto.BienResult;
import com.cele.immo.model.bien.Bien;
import com.cele.immo.service.BienService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(value = "/bien")
@Slf4j
public class BienController {
    @Autowired
    BienService bienService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Bien> getAllBien() {
        return this.bienService.findAll();
    }

    @GetMapping("{id}")
    public Mono<BienDTO> getBienById(@PathVariable String id) {
        return this.bienService.findByIdExcludePassword(id);
    }

    @GetMapping("/getBiensEtatCreation")
    public Mono<Page<BienResult>> getBiensEtatCreation(ServerWebExchange exchange) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(String.class)
                //.log()
                //.zipWith(exchange.getFormData())
                //.doOnNext(tuple -> {
                // based on some input parameters, amend the current user data to be returned
                //})
                //.map(Tuple2::getT1);
                .flatMap(username -> Mono.just(bienService.getBiensEtatCreation(username)));

    }

    @GetMapping("/create")
    public Mono<Bien> createBien(ServerWebExchange exchange) {
        return null;
    }

    @PostMapping("/rechercherBien")
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Page<Bien> rechercherBien(@RequestBody BienCritere bienCritere) {
        log.info("rechercherBien called. Bien critere : {}", bienCritere);
        return bienService.searchCriteria(bienCritere);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public Mono<Bien> updateBien(@RequestBody Bien bien) {
        log.debug("Update bien");
        return bienService.save(bien);
    }

    @PostMapping
    public Mono<ResponseEntity<Bien>> updateBienV2(@RequestBody Bien bien) {
        return bienService.findById(bien.getId())
                .flatMap(existingBien -> {
                    existingBien.setMandat(bien.getMandat());
                    return bienService.save(existingBien);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
