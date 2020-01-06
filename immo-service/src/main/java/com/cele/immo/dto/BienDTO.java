package com.cele.immo.dto;

import com.cele.immo.model.Photo;
import com.cele.immo.model.UserAccount;
import com.cele.immo.model.Video;
import com.cele.immo.model.bien.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class BienDTO implements Serializable {
    private String id;
    private String consultantId;
    private UserAccount consultant;
    private List<ConsultantAssocieDTO> consultantsAssocies;
    private EtatBien etat;
    private DetailBien detailBien;
    private Mandat mandat;
    private Bail bail;
    private ConditionsFinancieres conditionsFinancieres;
    private Visite visite;
    private Surface surface;
    private Descriptif descriptif;
    private List<Photo> photos;
    private List<Video> videos;
    private Communication communication;


}
