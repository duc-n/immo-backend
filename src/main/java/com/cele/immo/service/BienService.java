package com.cele.immo.service;

import com.cele.immo.model.Bien;
import reactor.core.publisher.Flux;

public interface BienService {
    Flux<Bien> searchCriteria();
}
