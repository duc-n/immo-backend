package com.cele.immo.config;

import com.cele.immo.model.Activites;
import com.cele.immo.model.AdresseBien;
import com.cele.immo.model.Bien;
import com.cele.immo.model.DetailBien;
import com.cele.immo.repository.BienRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class DataInitializer {
    private final BienRepository bienRepository;

    public DataInitializer(BienRepository bienRepository) {
        this.bienRepository = bienRepository;
    }

    @EventListener(value = ContextRefreshedEvent.class)
    public void init() {
        log.info("start data initialization  ...");
        this.bienRepository
                .deleteAll()
                .thenMany(
                        Flux
                                .range(0, 100)
                                .flatMap(
                                        i -> this.bienRepository.save(
                                                Bien.builder()
                                                        .nomTitulaire("Cele " + i)
                                                        .detailBien(DetailBien.builder()
                                                                .typeBien("Type Bien " + i)
                                                                .nomMagasin("Magasin " + i)
                                                                .activite("Activite " + i)
                                                                .adresseBien(AdresseBien.builder()
                                                                        .adresse("Rue Blanchard " + i)
                                                                        .codePostal("75013")
                                                                        .bus("194")
                                                                        .ville("Paris")
                                                                        .build()
                                                                )
                                                                .activites(Activites.builder()
                                                                        .licence(Boolean.TRUE)
                                                                        .popupStore(Boolean.TRUE)
                                                                        .terrasse(Boolean.TRUE)
                                                                        .build()
                                                                )

                                                                .build()

                                                        )
                                                        .build()
                                        )
                                )
                )
                .log()
                .subscribe(
                        null,
                        null,
                        () -> log.info("done initialization...")
                );

    }

}
