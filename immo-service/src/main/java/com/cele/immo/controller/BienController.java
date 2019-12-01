package com.cele.immo.controller;

import com.cele.immo.dto.BienCritere;
import com.cele.immo.dto.BienDTO;
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

import java.util.List;

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
    public Mono<Bien> getBienById(@PathVariable String id) {
        return this.bienService.findById(id);
    }

    @GetMapping("/getBiensEtatCreation")
    public Mono<List<BienDTO>> getBiensEtatCreation(ServerWebExchange exchange) {
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

    @PostMapping("/rechercherBien")
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Page<Bien> rechercherBien(@RequestBody BienCritere bienCritere) {
        log.info("rechercherBien called. Bien critere : {}", bienCritere);
        return bienService.searchCriteria(bienCritere);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public Mono<Bien> updateBien(@RequestBody Bien bien) {
        return bienService.save(bien);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Bien>> updateBienById(@PathVariable(value = "id") String id, @RequestBody Bien bien) {
        return bienService.findById(id)
                .flatMap(existingBien -> {
                    existingBien.setMandat(bien.getMandat());
                    return bienService.save(existingBien);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
