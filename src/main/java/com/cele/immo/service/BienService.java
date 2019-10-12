package com.cele.immo.service;

import com.cele.immo.model.Bien;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

public interface BienService {
    Mono<Page<Bien>> searchCriteria();
}
