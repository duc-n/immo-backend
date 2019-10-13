package com.cele.immo.model.bien;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Activites {
    private Boolean licence;
    private Boolean liquidationJudiciaire;
    private Boolean popupStore;
    private Boolean restaurantConduitChemine;
    private Boolean restaurantSansNuisance;
    private Boolean terrasse;
}
