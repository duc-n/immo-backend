package com.cele.immo.config;

import com.cele.immo.model.*;
import com.cele.immo.model.acquereur.Acquereur;
import com.cele.immo.model.acquereur.Contact;
import com.cele.immo.model.acquereur.DetailAcquereur;
import com.cele.immo.model.bien.*;
import com.cele.immo.repository.AcquereurRepository;
import com.cele.immo.repository.BienRepository;
import com.cele.immo.repository.ClientRepository;
import com.cele.immo.repository.UserAccountRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@Slf4j
public class DataInitializer {
    @Autowired
    private final BienRepository bienRepository;
    private final ClientRepository clientRepository;
    private final AcquereurRepository acquereurRepository;
    private final UserAccountRepository userAccountRepository;
    private final ReactiveGridFsTemplate gridFsTemplate;
    private final ResourceLoader resourceLoader;
    private final PBKDF2Encoder passwordEncoder;

    public DataInitializer(BienRepository bienRepository, AcquereurRepository acquereurRepository,
                           ReactiveGridFsTemplate gridFsTemplate, ResourceLoader resourceLoader,
                           ClientRepository clientRepository, UserAccountRepository userAccountRepository,
                           PBKDF2Encoder passwordEncoder
    ) {
        this.bienRepository = bienRepository;
        this.acquereurRepository = acquereurRepository;
        this.gridFsTemplate = gridFsTemplate;
        this.resourceLoader = resourceLoader;
        this.clientRepository = clientRepository;
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(value = ContextRefreshedEvent.class)
    public void init() throws IOException {
        log.info("start data initialization  ...");

        //Delete all test.png file
        gridFsTemplate.delete(query(GridFsCriteria.whereFilename().is("test.png"))).subscribe();

        //InputStream input = new FileInputStream("src/main/resources/pikachu.png");
/*        InputStream input = new ClassPathResource("pikachu.png").getInputStream();
        byte[] inputByte = input.readAllBytes();
        DBObject metaData = new BasicDBObject();
        metaData.put("user", "duc");
        metaData.put("type", "image");*/

        List<String> idList = new ArrayList<>();

        //Save the test.png file
     /*   Mono<ObjectId> objectIdMono = gridFsTemplate.store(AsyncStreamHelper.toAsyncInputStream(input), "test.png", "image/png", metaData);

        objectIdMono.subscribe(objectId -> {
            log.info("ObjectId id {} ", objectId.toString());
            idList.add(objectId.toString());
            log.info("Array list size {}", idList.size());

        });*/

        Mono<List<UserAccount>> users = this.userAccountRepository.deleteAll()
                .thenMany(
                        Flux
                                .range(0, 2)
                                .flatMap(
                                        i -> this.userAccountRepository.save(
                                                UserAccount.builder()
                                                        .username(i % 2 == 1 ? "user@gmail.com" : "admin@gmail.com")
                                                        .password(passwordEncoder.encode(i % 2 == 1 ? "user" : "admin"))
                                                        .telephone("0686955644")
                                                        .prenom("duc")
                                                        .nom("nguyen")
                                                        .roles(i % 2 == 1 ? Arrays.asList(Role.ROLE_USER) : Arrays.asList(Role.ROLE_ADMIN))
                                                        .active(Boolean.TRUE)
                                                        .build()

                                        )
                                )).collectList();

        List<UserAccount> userList = users.block();

        this.clientRepository.deleteAll()
                .thenMany(
                        Flux
                                .range(0, 10)
                                .flatMap(
                                        i -> this.clientRepository.save(
                                                Client.builder()
                                                        .nom("TRAN")
                                                        .prenom("An")
                                                        .civilite(i % 2 == 1 ? Civilite.M : Civilite.MME)
                                                        .adresse("11 Av Mac Mahon")
                                                        .email("test" + i + "@gmail.com")
                                                        .consultantIds(userList.stream().map(userAccount -> userAccount.getUsername()).collect(Collectors.toList()))
                                                        .tel("06865678" + i)
                                                        .build()

                                        )
                                ))
                .log()
                .subscribe(
                        null,
                        null,
                        () -> log.info("done acquereur initialization...")
                );


        this.acquereurRepository.deleteAll()
                .thenMany(
                        Flux
                                .range(0, 10)
                                .flatMap(
                                        i -> this.acquereurRepository.save(
                                                Acquereur.builder()
                                                        .contacts(Lists.newArrayList(Contact.builder().nomPrenom("NGUYEN Duc " + i).build(), Contact.builder().nomPrenom("NGUYEN Cecile " + i).build()))
                                                        .activite("Informatic " + i)
                                                        .civilite(i % 2 == 1 ? Civilite.M : Civilite.MME)
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
                                                        .notes(Lists.newArrayList("Test comment 1", "Test comment 2"))
                                                        .origine("Origine")
                                                        .societe("CELETEST")
                                                        .pourcentHonoraires(new BigDecimal("7.6"))
                                                        .societeEnCreation(i % 2 == 1 ? Boolean.TRUE : Boolean.FALSE)
                                                        .tel("012345678")
                                                        .topAcquereur(i % 2 == 1 ? Boolean.TRUE : Boolean.FALSE)
                                                        .documents(Lists.newArrayList(Document.builder()
                                                                .title("Photo")
                                                                //.id(idList.get(0))
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
                                                        .consultantId("admin@gmail.com")
                                                        .consultant(userList.get(1))
                                                        .consultantsAssocies(Lists.newArrayList(ConsultantAssocie.builder()
                                                                .consultantId("user@gmail.com")
                                                                .consultant(userList.get(0))
                                                                .commission(25)
                                                                .build()))
                                                        .etat(i % 2 == 1 ? EtatBien.CREATION : EtatBien.ACTIVE)
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
                                                                        .licence(i % 2 == 1 ? Boolean.FALSE : Boolean.TRUE)
                                                                        .popupStore(i % 2 == 1 ? Boolean.TRUE : Boolean.FALSE)
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
                                                                .paiementHonoraires("locataire")
                                                                .tvaLoyer(true)
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
                                                                .prixVente(200000L + (i * 100))
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
                                                                .surfaceTotale(100 + (i * 30))
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
                                                        /*
                                                        .photos(Lists.newArrayList(Photo.builder()
                                                                .url("https://cele-immo-sandbox.s3.eu-west-2.amazonaws.com/admin%40gmail.com/5dffde8ed5952a28235b28be/memoire1.png")
                                                                //.image(new Binary(BsonBinarySubType.BINARY, inputByte))
                                                                .build()
                                                        ))*/
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
