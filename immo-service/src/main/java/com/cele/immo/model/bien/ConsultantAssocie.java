package com.cele.immo.model.bien;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class ConsultantAssocie {
    @Indexed
    @NotEmpty
    private String consultantId;
    private Integer commission;
}
