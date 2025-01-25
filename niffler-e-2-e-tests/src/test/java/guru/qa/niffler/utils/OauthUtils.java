package guru.qa.niffler.utils;

import lombok.SneakyThrows;

import java.security.MessageDigest;
import java.security.SecureRandom;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.security.MessageDigest.getInstance;
import static java.util.Base64.getUrlEncoder;

public class OauthUtils {

    private final static SecureRandom secureRandom = new SecureRandom();

    public static String generateCodeVerifier() {
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        return getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
    }

    @SneakyThrows
    public static String generateCodeChallenge(String codeVerifier) {
        byte[] bytes = codeVerifier.getBytes(US_ASCII);
        MessageDigest messageDigest = getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
