package com.shimady.payment.processor.domain.user.controller.mapper;

import com.shimady.payment.processor.domain.user.controller.dto.UserDto;
import com.shimady.payment.processor.security.permission.UserPermissions;
import com.shimady.payment.processor.security.principal.KeycloakUserPrincipal;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    private final String userId = UUID.randomUUID().toString();

    @Test
    void shouldMapToUserDto() {
        var user = new KeycloakUserPrincipal(
                userId,
                "Test Username",
                "email@test.com",
                "Test Firstname",
                "Test Lastname",
                "Test Fullname",
                true,
                Set.of(UserPermissions.READ_USER_INFO)
        );

        var expected = new UserDto(
                userId,
                "Test Username",
                "email@test.com",
                "Test Firstname",
                "Test Lastname",
                "Test Fullname",
                true,
                Set.of(UserPermissions.READ_USER_INFO)
        );

        var result = mapper.map(user);

        assertEquals(expected, result);
    }
}