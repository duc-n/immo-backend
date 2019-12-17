package com.cele.immo.service;

import com.cele.immo.dto.BienCritere;
import com.cele.immo.model.bien.Bien;
import org.springframework.data.domain.Page;

public interface CustomBienRepository {
    Page<Bien> searchBienCriteria(BienCritere bienCritere);
}
