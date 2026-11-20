package com.shimady.payment.processor.domain.user.controller;

import com.shimady.payment.processor.config.BaseTestcontainersTest;
import com.shimady.payment.processor.domain.user.controller.dto.UserDto;
import com.shimady.payment.processor.security.permission.UserPermissions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static com.shimady.payment.processor.util.KeycloakUtil.getAccessToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest implements BaseTestcontainersTest {
    private static final String BASE_URI = "http://localhost:%d/api/v1/users";

    @LocalServerPort
    private int port;

    @Autowired
    private RestTestClient restTestClient;

    @ParameterizedTest
    @MethodSource("getTokens")
    void shouldReturnAuthenticatedUserPrincipal(String token, String username, String permission) {
        var result = restTestClient.get()
                .uri(BASE_URI.formatted(port))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectBody(UserDto.class)
                .returnResult();

        assertNotNull(result.getResponseBody());
        assertEquals(username, result.getResponseBody().username());
        assertThat(result.getResponseBody().roles()).isNotEmpty()
                .extracting(UserPermissions::getAuthority)
                .contains(permission);
    }

    private static Stream<Arguments> getTokens() {
        return Stream.of(
                Arguments.arguments(getAccessToken("test-admin", "test-admin-password"), "test-admin", "READ_USER_INFO"),
                Arguments.arguments(getAccessToken("test-user", "test-password"), "test-user", "READ_USER_INFO")
        );
    }

    private UserDto createUser(UserPermissions role) {
        return new UserDto(
                UUID.randomUUID().toString(),
                "Test User Username",
                "test@exmaple.com",
                "Test User Firstname",
                "Test User Lastname",
                "Test User Fullname",
                true,
                Set.of(role)
        );
    }
}