package com.cele.immo.mappers;

import com.cele.immo.dto.ConsultantDTO;
import com.cele.immo.model.UserAccount;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

//@Mapper(componentModel = "spring")
@Mapper
//Creates a Spring Bean automatically
public interface ConsultantMapper {

    ConsultantMapper INSTANCE = Mappers.getMapper(ConsultantMapper.class);

    UserAccount toConsultant(ConsultantDTO consultantDTO);

    ConsultantDTO toConsultantDTO(UserAccount consultant);

}
