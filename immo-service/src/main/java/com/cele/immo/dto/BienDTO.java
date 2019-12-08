package com.cele.immo.dto;

import com.cele.immo.model.Photo;
import com.cele.immo.model.Video;
import com.cele.immo.model.bien.*;

import java.util.List;

public class BienDTO {
    private String id;
    private String consultantId;
    private ConsultantDTO consultant;
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
