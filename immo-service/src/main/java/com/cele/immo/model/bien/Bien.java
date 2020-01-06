package com.cele.immo.model.bien;

import com.cele.immo.model.Photo;
import com.cele.immo.model.UserAccount;
import com.cele.immo.model.Video;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document
public class Bien {
    @CreatedDate
    public LocalDateTime createdDate;
    @LastModifiedDate
    public LocalDateTime modifiedDate;
    @Id
    private String id;
    @Indexed
    @NotEmpty
    private String consultantId;
    @DBRef
    private UserAccount consultant;
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

    public List<Photo> getPhotos() {
        if (this.photos == null) {
            this.photos = new ArrayList<>();
        }
        return this.photos;
    }

    public List<Video> getVideos() {
        if (this.videos == null) {
            this.videos = new ArrayList<>();
        }
        return this.videos;
    }

    public List<ConsultantAssocie> getConsultantsAssocies() {
        if (this.consultantsAssocies == null) {
            this.consultantsAssocies = new ArrayList<>();
        }
        return this.consultantsAssocies;
    }
}
