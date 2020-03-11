package com.cele.immo.mongodb.integration;

import com.cele.immo.config.PBKDF2Encoder;
import com.cele.immo.dto.BienDTO;
import com.cele.immo.model.Photo;
import com.cele.immo.model.Role;
import com.cele.immo.model.UserAccount;
import com.cele.immo.model.bien.*;
import com.cele.immo.repository.BienRepository;
import com.cele.immo.repository.UserAccountRepository;
import com.cele.immo.service.BienService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)//Support Junit 5
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
public class BienServiceIT {
    @Autowired
    BienService bienService;
    @Autowired
    BienRepository bienRepository;
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    PBKDF2Encoder passwordEncoder;

    UserAccount userAdmin = UserAccount.builder()
            .id("id1")
            .username("admin@gmail.com")
            .password("password")
            .telephone("0686955644")
            .prenom("duc")
            .nom("nguyen")
            .roles(Arrays.asList(Role.ROLE_ADMIN))
            .active(Boolean.TRUE)
            .build();

    UserAccount user = UserAccount.builder()
            .id("id2")
            .username("user@gmail.com")
            .password("password")
            .telephone("0686955644")
            .prenom("duc")
            .nom("nguyen")
            .roles(Arrays.asList(Role.ROLE_USER))
            .active(Boolean.TRUE)
            .build();

    @BeforeEach
    public void init() {
        this.userAccountRepository.deleteAll();

    }


    @Test
    public void givenBienObject_whenFindById_thenReturnBienObject() {

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

        Bien bien = Bien.builder()
                .consultantId("admin@gmail.com")
                .consultantsAssocies(Lists.newArrayList(ConsultantAssocie.builder()
                                .consultantId("user@gmail.com")
                                .consultant(userList.get(0))
                                .commission(25)
                                .build(),
                        ConsultantAssocie.builder()
                                .consultantId("admin@gmail.com")
                                .consultant(userList.get(1))
                                .commission(30)
                                .build())
                )
                .etat(EtatBien.CREATION)
                .detailBien(DetailBien.builder()
                        .typeBien("Type Bien ")
                        .nomMagasin("Magasin ")
                        .activite("Activite ")
                        .adresseBien(AdresseBien.builder()
                                .adresse("Rue Blanchard ")
                                .codePostal("75013")
                                .bus("194")
                                .ville("Paris")
                                .build()
                        )
                        .activites(Activites.builder()
                                .licence(Boolean.FALSE)
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
                        .paiementHonoraires("locataire")
                        .tvaLoyer(true)
                        .build()
                )
                .descriptif(Descriptif.builder()
                        .description("Description ici")
                        .disponible(Boolean.FALSE)
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
                        .prixVente(200000L)
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
                .photos(Lists.newArrayList(Photo.builder().url("Image")
                        //.image(new Binary(BsonBinarySubType.BINARY, inputByte))
                        .build()
                ))
                .build();

        Mono<Bien> bienMono = this.bienRepository.save(
                bien
        );

        Mono<BienDTO> setup = bienMono.flatMap(b -> bienService.findByIdExcludePassword(b.getId()));
        StepVerifier
                .create(setup)
                .expectNextMatches(resource -> {
                    log.debug("Resource result :{}", resource);
                    then(resource.getEtat()).isEqualTo(EtatBien.CREATION);
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void givenUserObject_whenSave_thenCreateNewUser() {

        Publisher<UserAccount> setup = this.userAccountRepository.deleteAll()
                .thenMany(this.userAccountRepository.saveAll(Flux.just(userAdmin))
                );

        Publisher<UserAccount> find = this.userAccountRepository.findByNom("nguyen");

        Publisher<UserAccount> composite = Flux
                .from(setup)
                .thenMany(find);

        StepVerifier
                .create(setup)
                .expectNext(userAdmin)
                .verifyComplete();

    }

    @Test
    public void givenUserObject_whenSave_thenCreateNewUser1() {

        Publisher<UserAccount> setup = this.userAccountRepository.deleteAll()
                .thenMany(this.userAccountRepository.saveAll(Flux.just(userAdmin, user))
                );

        Mono<UserAccount> user = this.userAccountRepository.findByUsername("admin@gmail.com");

        StepVerifier
                .create(user)
                .expectNextMatches(resource -> {
                    log.debug("Resource result :{}", resource);
                    then(resource.getNom()).isEqualTo("nguyen");
                    then(resource.isActive()).isTrue();
                    return true;
                })
                .verifyComplete();

    }

    @Test
    public void findAllShouldWork() {

        Publisher<UserAccount> allUsers = this.userAccountRepository.deleteAll()
                .thenMany(this.userAccountRepository.saveAll(Flux.just(userAdmin, user)));


        StepVerifier.create(allUsers)
                .recordWith(ArrayList::new)
                .expectNextCount(2)
                .consumeRecordedWith(results -> {
                    assertThat(results)
                            .extracting(UserAccount::getUsername)
                            .contains(
                                    "admin@gmail.com",
                                    "user@gmail.com");

                })
                .verifyComplete();
    }
}
