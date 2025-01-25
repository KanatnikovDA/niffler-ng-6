package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.utils.OauthUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OAuthTest {
    private final AuthApiClient authApiClient = new AuthApiClient();

    @User
    @Test
    void testOAuth(UserJson user) {
        String codeVerifier = OauthUtils.generateCodeVerifier();
        String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);

        authApiClient.authorize(codeChallenge);
        authApiClient.login(user.username(), user.testData().password());
        String token = authApiClient.token(codeVerifier, codeChallenge);
        assertNotNull(token);
    }
}
