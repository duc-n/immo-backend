package com.cele.immo.model.bien;

import com.cele.immo.model.Photo;
import com.cele.immo.model.Video;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document
public class Bien {
    @Id
    private String id;
    @Indexed
    @NotEmpty
    private String consultantId;
    private List<ConsultantAssocie> consultantsAssocies;
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
    @CreatedDate
    public LocalDateTime createdDate;
    @LastModifiedDate
    public LocalDateTime modifiedDate;
}
