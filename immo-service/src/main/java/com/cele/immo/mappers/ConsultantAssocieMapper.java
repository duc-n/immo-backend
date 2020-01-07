package com.cele.immo.mappers;

import com.cele.immo.dto.ConsultantAssocieDTO;
import com.cele.immo.model.bien.ConsultantAssocie;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

//@Mapper(componentModel = "spring")
@Mapper
//Creates a Spring Bean automatically
public interface ConsultantAssocieMapper {

    ConsultantAssocieMapper INSTANCE = Mappers.getMapper(ConsultantAssocieMapper.class);

    ConsultantAssocie toConsultantAssocie(ConsultantAssocieDTO consultantAssocieDTO);

    ConsultantAssocieDTO toConsultantAssocieDTO(ConsultantAssocie consultantAssocie);

    List<ConsultantAssocie> toConsultantAssocies(List<ConsultantAssocieDTO> consultantAssocieDTOs);

    List<ConsultantAssocieDTO> toConsultantAssocieDTOs(List<ConsultantAssocie> consultantAssocies);
}
