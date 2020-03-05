package com.cele.immo.mappers;

import com.cele.immo.dto.UserProfile;
import com.cele.immo.model.UserAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

//@Mapper(componentModel = "spring")
@Mapper
//Creates a Spring Bean automatically
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "username", target = "email")
    UserProfile toUserProfile(UserAccount userAccount);

    UserAccount toUserAccount(UserProfile userProfile);

}
