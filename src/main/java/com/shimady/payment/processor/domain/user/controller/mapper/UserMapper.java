package com.shimady.payment.processor.domain.user.controller.mapper;

import com.shimady.payment.processor.config.CommonMapperConfig;
import com.shimady.payment.processor.domain.user.controller.dto.UserDto;
import com.shimady.payment.processor.security.principal.UserPrincipal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfig.class)
public interface UserMapper {

    @Mapping(target = "id", expression = "java(user.id())")
    @Mapping(target = "username", expression = "java(user.username())")
    @Mapping(target = "email", expression = "java(user.email())")
    @Mapping(target = "firstName", expression = "java(user.firstName())")
    @Mapping(target = "lastName", expression = "java(user.lastName())")
    @Mapping(target = "fullName", expression = "java(user.fullName())")
    @Mapping(target = "emailVerified", expression = "java(user.emailVerified())")
    @Mapping(target = "roles", expression = "java(user.roles())")
    UserDto map(UserPrincipal user);
}
