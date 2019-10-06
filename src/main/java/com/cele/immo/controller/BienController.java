package com.cele.immo.controller;

import com.cele.immo.model.Bien;
import com.cele.immo.repository.BienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(value = "/bien")
@RequiredArgsConstructor
public class BienController {
    @Autowired
    private BienRepository bienRepository;

    @GetMapping()
    public Flux<Bien> getAllBien() {
        return this.bienRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<Bien> getBienById(@PathVariable String id) {
        return this.bienRepository.findById(id);
    }

    @GetMapping("/searchByOwner")
    public Flux<Bien> searchByOwner(
            @RequestParam() String ownerName,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "2") Integer size
    ) {
        return this.bienRepository.findByNomTitulaireLike(ownerName, PageRequest.of(page, size));
    }

}
