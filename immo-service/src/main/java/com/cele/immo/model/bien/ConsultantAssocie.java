package com.cele.immo.model.bien;

import com.cele.immo.model.UserAccount;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class ConsultantAssocie {
    @DBRef
    UserAccount consultant;
    @Indexed
    @NotEmpty
    private String consultantId;
    private Integer commission;
}
