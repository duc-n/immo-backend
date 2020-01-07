package com.cele.immo.mappers;

import com.cele.immo.dto.BienDTO;
import com.cele.immo.model.bien.Bien;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

//@Mapper(componentModel = "spring")
@Mapper
//Creates a Spring Bean automatically
public interface BienMapper {

    BienMapper INSTANCE = Mappers.getMapper(BienMapper.class);

    Bien toBien(BienDTO bienDTO);

    //@Mapping(target = "consultantsAssocies", ignore = true)
    BienDTO toBienDTO(Bien bien);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "etat", ignore = true)
    @Mapping(target = "consultantId", ignore = true)
    @Mapping(target = "consultant", ignore = true)
    void copyToBien(BienDTO bienDTO, @MappingTarget Bien entity);

}
