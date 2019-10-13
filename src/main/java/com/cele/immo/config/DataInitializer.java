package com.cele.immo.config;

import com.cele.immo.model.Document;
import com.cele.immo.model.Photo;
import com.cele.immo.model.acquereur.Acquereur;
import com.cele.immo.model.acquereur.Contact;
import com.cele.immo.model.acquereur.DetailAcquereur;
import com.cele.immo.model.bien.*;
import com.cele.immo.repository.AcquereurRepository;
import com.cele.immo.repository.BienRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.reactivestreams.client.gridfs.helpers.AsyncStreamHelper;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@Slf4j
public class DataInitializer {
    private final BienRepository bienRepository;

    private final AcquereurRepository acquereurRepository;
    private final ReactiveGridFsTemplate gridFsTemplate;
    private final ResourceLoader resourceLoader;

    public DataInitializer(BienRepository bienRepository, AcquereurRepository acquereurRepository, ReactiveGridFsTemplate gridFsTemplate, ResourceLoader resourceLoader) {
        this.bienRepository = bienRepository;
        this.acquereurRepository = acquereurRepository;
        this.gridFsTemplate = gridFsTemplate;
        this.resourceLoader = resourceLoader;
    }

    @EventListener(value = ContextRefreshedEvent.class)
    public void init() throws IOException {
        log.info("start data initialization  ...");

        //Delete all test.png file
        gridFsTemplate.delete(query(GridFsCriteria.whereFilename().is("test.png"))).subscribe();

        InputStream input = new FileInputStream("src/main/resources/pikachu.png");
        byte[] inputByte = input.readAllBytes();
        DBObject metaData = new BasicDBObject();
        metaData.put("user", "duc");
        metaData.put("type", "image");

        List<String> idList = new ArrayList<>();

        //Save the test.png file
        Mono<ObjectId> objectIdMono = gridFsTemplate.store(AsyncStreamHelper.toAsyncInputStream(input), "test.png", "image/png", metaData);

        objectIdMono.subscribe(objectId -> {
            log.info("ObjectId id {} ", objectId.toString());
            idList.add(objectId.toString());
            log.info("Array list size {}", idList.size());

        });

        this.acquereurRepository.deleteAll()
                .thenMany(
                        Flux
                                .range(0, 10)
                                .flatMap(
                                        i -> this.acquereurRepository.save(
                                                Acquereur.builder()
                                                        .contacts(List.of(Contact.builder().nomPrenom("NGUYEN Duc " + i).build(), Contact.builder().nomPrenom("NGUYEN Cecile " + i).build()))
                                                        .activite("Informatic " + i)
                                                        .civilite(i % 2 == 1 ? "M." : "Mme")
                                                        .consentementRGPD(i % 2 == 1 ? Boolean.TRUE : Boolean.FALSE)
                                                        .consultantTitulaire("TOTO")
                                                        .demandeRdv(i % 2 == 1 ? Boolean.TRUE : Boolean.FALSE)
                                                        .consultatSociete("CELE")
                                                        .detailAcquereur(DetailAcquereur.builder()
                                                                .activite("Assurance")
                                                                .adresse("24 Rue Blanchard, 75013 Paris")
                                                                .nomSociete("Tech")
                                                                .tel("0685909989")
                                                                .type("Societe")
                                                                .build()
                                                        )
                                                        .email("tototemail@gmail.com")
                                                        .emailSupp("titiemail@gmail.com")
                                                        .newsLettre(i % 2 == 1 ? Boolean.TRUE : Boolean.FALSE)
                                                        .nom("REBER")
                                                        .prenom("Guilaume")
                                                        .notes(List.of("Test comment 1", "Test comment 2"))
                                                        .origine("Origine")
                                                        .societe("CELETEST")
                                                        .pourcentHonoraires(new BigDecimal("7.6"))
                                                        .societeEnCreation(i % 2 == 1 ? Boolean.TRUE : Boolean.FALSE)
                                                        .tel("012345678")
                                                        .topAcquereur(i % 2 == 1 ? Boolean.TRUE : Boolean.FALSE)
                                                        .documents(List.of(Document.builder()
                                                                .title("Photo")
                                                                .id(idList.get(0))
                                                                .build()

                                                        ))
                                                        .build()
                                        )
                                ))
                .log()
                .subscribe(
                        null,
                        null,
                        () -> log.info("done acquereur initialization...")
                );


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
                                                                .chargeMensuel(1000L + i)
                                                                .depotGarantie("2 mois")
                                                                .paiement606("606")
                                                                .paiementTaxFonctiere("Locataire")
                                                                .periodLoyer("Un an")
                                                                .taxFonctiere(800L + i)
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
                                                                .honorairesAgence(1500L + i)
                                                                .paiementHonoraires(600L - i)
                                                                .tvaLoyer(new BigDecimal("19.6"))
                                                                .build()
                                                        )
                                                        .descriptif(Descriptif.builder()
                                                                .description("Description ici")
                                                                .disponible(i % 2 == 1 ? Boolean.TRUE : Boolean.FALSE)
                                                                .espaceExterieur(i % 2 == 1 ? Boolean.FALSE : Boolean.TRUE)
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
                                                                .surface1Etage(15 + i)
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
                                                        .photos(List.of(Photo.builder().title("Image")
                                                                .image(new Binary(BsonBinarySubType.BINARY, inputByte))
                                                                .build()
                                                        ))
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
