package com.cele.immo.mappers;

import com.cele.immo.dto.BienDTO;
import com.cele.immo.model.Photo;
import com.cele.immo.model.Role;
import com.cele.immo.model.UserAccount;
import com.cele.immo.model.bien.*;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.Arrays;

public class BienMapperTest {
    @Test
    public void givenBienObject_whenConvertToDTO_thenReturnBienDTO() {

        UserAccount admin = UserAccount.builder()
                .username("admin@gmail.com")
                .password("toto")
                .telephone("0686955644")
                .prenom("duc")
                .nom("nguyen")
                .roles(Arrays.asList(Role.ROLE_ADMIN))
                .active(Boolean.TRUE)
                .build();
        UserAccount user = UserAccount.builder()
                .username("user@gmail.com")
                .password("toto")
                .telephone("0686955644")
                .prenom("duc")
                .nom("nguyen")
                .roles(Arrays.asList(Role.ROLE_ADMIN))
                .active(Boolean.TRUE)
                .build();


        Bien bien = Bien.builder()
                .consultantId("admin@gmail.com")
                .consultantsAssocies(Lists.newArrayList(ConsultantAssocie.builder()
                                .consultantId("user@gmail.com")
                                .consultant(admin)
                                .commission(25)
                                .build(),
                        ConsultantAssocie.builder()
                                .consultantId("admin@gmail.com")
                                .consultant(user)
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
                        .paiementHonoraires(600L)
                        .tvaLoyer(new BigDecimal("19.6"))
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

        BienDTO bienDTO = BienMapper.INSTANCE.toBienDTO(bien);

        Bien bienResult = BienMapper.INSTANCE.toBien(bienDTO);

        bienResult.setId("ID-BIEN");

        bienDTO.setEtat(EtatBien.VENDU);

        BienMapper.INSTANCE.copyToBien(bienDTO, bienResult);

        Assertions.assertEquals(bien.getDetailBien(), bienResult.getDetailBien());

        Assertions.assertEquals(bienResult.getId(), "ID-BIEN");
        Assertions.assertEquals(bienResult.getEtat(), EtatBien.VENDU);


    }
}
