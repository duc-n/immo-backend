package com.cele.immo.controller;

import com.cele.immo.dto.BienCritere;
import com.cele.immo.model.bien.Bien;
import com.cele.immo.service.BienService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(value = "/bien")
@Slf4j
public class BienController {
    @Autowired
    BienService bienService;

    @GetMapping()
    public Flux<Bien> getAllBien() {
        return this.bienService.findAll();
    }

    @GetMapping("{id}")
    public Mono<Bien> getBienById(@PathVariable String id) {
        return this.bienService.findById(id);
    }

    @PostMapping("/rechercherBien")
    public Page<Bien> rechercherBien(@RequestBody BienCritere bienCritere) {
        log.info("rechercherBien called. Bien critere : {}", bienCritere);
        return bienService.searchCriteria(bienCritere);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Bien>> updateBie(@PathVariable(value = "id") String id, @RequestBody Bien bien) {
        return bienService.findById(id)
                .flatMap(existingBien -> {
                    existingBien.setMandat(bien.getMandat());
                    return bienService.save(existingBien);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
