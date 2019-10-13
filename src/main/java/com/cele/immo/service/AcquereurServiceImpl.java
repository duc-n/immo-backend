package com.cele.immo.service;

import com.cele.immo.model.acquereur.Acquereur;
import com.cele.immo.repository.AcquereurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AcquereurServiceImpl implements AcquereurService {
    @Autowired
    AcquereurRepository acquereurRepository;

    @Override
    public Mono<Acquereur> update(Acquereur acquereur) {
        return acquereurRepository.save(acquereur);
    }

    @Override
    public Mono<Acquereur> insert(Acquereur acquereur) {
        return acquereurRepository.insert(acquereur);
    }

    @Override
    public Mono<Acquereur> findById(String id) {
        return acquereurRepository.findById(id);
    }

    @Override
    public Flux<Acquereur> findAll() {
        return acquereurRepository.findAll();
    }

    @Override
    public Mono<Page<Acquereur>> searchCriteria() {
        return null;
    }
}
