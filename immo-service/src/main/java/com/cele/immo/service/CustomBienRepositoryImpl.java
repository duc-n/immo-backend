package com.cele.immo.service;

import com.cele.immo.dto.BienCritere;
import com.cele.immo.model.UserAccount;
import com.cele.immo.model.bien.Bien;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Service
public class CustomBienRepositoryImpl implements CustomBienRepository {

    @Autowired
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomBienRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Bien> searchBienCriteria(BienCritere bienCritere) {

        Pageable pageable = PageRequest.of(0, 10);

        Query query = new Query().with(pageable);

        //Consultant name
        if (StringUtils.hasText(bienCritere.getConsultant())) {
            Query consultantNameQuery = new Query();
            Criteria regexLastName = Criteria.where("lastName").regex(bienCritere.getConsultant(), "i");// i option for case insensitive.
            consultantNameQuery.addCriteria(regexLastName);

            List<UserAccount> consultants = mongoTemplate.find(consultantNameQuery, UserAccount.class, "userAccount");
            List<String> consultantsIds = new ArrayList<>();

            if (!CollectionUtils.isEmpty(consultants)) {
                consultantsIds = consultants.stream().map(c -> c.getUsername()).collect(Collectors.toList());
            }

            Criteria idsConsultants = Criteria.where("consultantId").in(consultantsIds);

            query.addCriteria(idsConsultants);
        }

        // popupStore opt
        if (BooleanUtils.isTrue(bienCritere.getPopupStore())) {
            query.addCriteria(Criteria.where("detailBien.activites.popupStore").is(Boolean.TRUE));

        }

        matchSurfaceCriteria(query, bienCritere);

        List<Bien> biens = mongoTemplate.find(query, Bien.class, "bien");
        log.debug("Bien size : {}", biens.size());

        return PageableExecutionUtils.getPage(biens, pageable, () -> mongoTemplate.count(query, Bien.class));
    }

    private void matchSurfaceCriteria(Query query, BienCritere bienCritere) {

        Criteria surfaceCriteria = where("surface.surfaceTotale");
        boolean hasSurface = false;
        // surfaceTotaleMin
        if (Objects.nonNull(bienCritere.getSurfaceTotaleMin())) {
            surfaceCriteria.gte(bienCritere.getSurfaceTotaleMin());
            hasSurface = true;

        }
        // surfaceTotaleMax
        if (Objects.nonNull(bienCritere.getSurfaceTotaleMax())) {
            surfaceCriteria.lte(bienCritere.getSurfaceTotaleMax());
            hasSurface = true;

        }
        if (hasSurface) {
            query.addCriteria(surfaceCriteria);
        }

    }
}
