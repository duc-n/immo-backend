package com.cele.immo.controller;

import com.cele.immo.model.bien.Bien;
import com.cele.immo.service.BienService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(value = "/bien")
@RequiredArgsConstructor
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

    @GetMapping("/searchByOwner")
    public Mono<Page<Bien>> searchByOwner(
            @RequestParam() String ownerName,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "2") Integer size
    ) {

        return bienService.searchCriteria();
    }

}
