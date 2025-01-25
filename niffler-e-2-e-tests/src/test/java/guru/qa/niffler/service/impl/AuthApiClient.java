package guru.qa.niffler.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;

import static guru.qa.niffler.api.core.ThreadSafeCookieStore.INSTANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthApiClient extends RestClient {
    private final AuthApi authApi;
    private static final String CODE = "code";
    private static final String CLIENT_ID = "client";
    private static final String SCOPE = "openid";
    private static final String CODE_CHALLENGE_METHOD = "S256";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String XSRF_TOKEN = "XSRF-TOKEN";
    private static final String ID_TOKEN = "id_token";
    private static final Config CONFIG = Config.getInstance();

    public AuthApiClient() {
        super(CONFIG.authUrl());
        this.authApi = retrofit.create(AuthApi.class);
    }

    @Step("Authorize")
    public void authorize(String codeChallenge) {
        try {
            Response<ResponseBody> response = authApi.authorize(
                    CODE,
                    CLIENT_ID,
                    SCOPE,
                    CONFIG.authUrl(),
                    codeChallenge,
                    CODE_CHALLENGE_METHOD
            ).execute();

            assertEquals(200, response.code(), "Authorization failed with response code: " + response.code());
        } catch (IOException e) {
            throw new AssertionError("Authorization failed due to an IOException: " + e.getMessage(), e);
        }
    }

    @Step("Login")
    public void login(String username, String password) {
        try {
            Response<Void> response = authApi.login(
                    username,
                    password,
                    INSTANCE.cookieValue(XSRF_TOKEN)
            ).execute();

            assertEquals(200, response.code(), "Login failed with response code: " + response.code());
        } catch (IOException e) {
            throw new AssertionError("Login failed due to an IOException: " + e.getMessage(), e);
        }
    }

    @Step("Get token")
    public String token(String codeChallenge, String codeVerifier) {
        try {
            Response<JsonNode> response = authApi.token(
                    codeChallenge,
                    CODE_CHALLENGE_METHOD,
                    codeVerifier,
                    GRANT_TYPE,
                    CLIENT_ID
            ).execute();

            assertEquals(200, response.code(), "Token retrieval failed with response code: " + response.code());
            JsonNode body = response.body();

            if (body != null && body.has(ID_TOKEN)) {
                return body.get(ID_TOKEN).asText();
            } else {
                throw new AssertionError("Token response body is null or missing 'id_token'.");
            }
        } catch (IOException e) {
            throw new AssertionError("Token retrieval failed due to an IOException: " + e.getMessage(), e);
        }
    }
}
