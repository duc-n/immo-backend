package com.cele.immo.config;

import com.cele.immo.model.*;
import com.cele.immo.repository.BienRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

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
                                .range(0, 10)
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
                                                                .enseigneProximite("Ecole commerce")
                                                                .build()

                                                        )
                                                        .bail(Bail.builder()
                                                                .typeBail("Type Bail")
                                                                .bailNotarie(Boolean.TRUE)
                                                                .cautionSup("Deux mois")
                                                                .chargeMensuel(1000L)
                                                                .depotGarantie("2 mois")
                                                                .paiement606("606")
                                                                .paiementTaxFonctiere("Locataire")
                                                                .periodLoyer("Un an")
                                                                .taxFonctiere(800L)
                                                                .build()
                                                        )
                                                        .communication(Communication.builder()
                                                                .alertesEmailRapprochement(Boolean.TRUE)
                                                                .bureauLocal("Bureau Local")
                                                                .groupeSeloger("Seloger")
                                                                .informationsMandats(Boolean.TRUE)
                                                                .lebonCoin("Le boin coin")
                                                                .parselle(Boolean.TRUE)
                                                                .siteWeb(Boolean.FALSE)
                                                                .territoireMaking("Marketing")
                                                                .transactionCommerce("Transaction")
                                                                .build()
                                                        )
                                                        .conditionsFinancieres(ConditionsFinancieres.builder()
                                                                .conditionsFinancieres("Condition Financières")
                                                                .honorairesAgence(1500L)
                                                                .paiementHonoraires(600L)
                                                                .tvaLoyer(new BigDecimal("19.6"))
                                                                .build()
                                                        )
                                                        .descriptif(Descriptif.builder()
                                                                .description("Description ici")
                                                                .disponible(Boolean.TRUE)
                                                                .espaceExterieur(Boolean.FALSE)
                                                                .facadeAngle(Boolean.TRUE)
                                                                .hsp2m80(Boolean.TRUE)
                                                                .localIdealPour("Restauration")
                                                                .longueurFacade(new BigDecimal("12"))
                                                                .noteConfidentielle("note confidentielle")
                                                                .pointEau(Boolean.TRUE)
                                                                .wc(Boolean.TRUE)
                                                                .build()
                                                        )
                                                        .mandat(Mandat.builder()
                                                                .typeMandat("Mandat type")
                                                                .panneau(Boolean.TRUE)
                                                                .prixVente(20000L)
                                                                .transaction("Transaction")
                                                                .build()
                                                        )
                                                        .surface(Surface.builder()
                                                                .nouvelleSurfaceFonderee(20)
                                                                .surface1Etage(15)
                                                                .surface1Fonderee(12)
                                                                .surfaceAutre(10)
                                                                .surfaceRDC(16)
                                                                .surfaceSousSol(24)
                                                                .surfaceSousSolFonderee(10)
                                                                .surfaceTotale(100)
                                                                .surfaceVente(Boolean.TRUE)
                                                                .build()
                                                        )
                                                        .visite(Visite.builder()
                                                                .cle("Deux jeux de cle")
                                                                .commentaire("Disponible toute de suite")
                                                                .digicode1("2418")
                                                                .etage("3eme etage")
                                                                .interphone("94344")
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
