package com.cele.immo.model.bien;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Surface {
    private Integer surfaceRDC;
    private Integer surfaceRDCFonderee;
    private Boolean surfaceVente;
    private Integer surfaceSousSol;
    private Integer surfaceSousSolFonderee;
    private Integer surface1Etage;
    private Integer surface1Fonderee;
    private Integer surfaceAutre;
    private Integer surfaceTotale;
    private Integer surfaceFonderee;
    private Integer nouvelleSurfaceFonderee;
}
