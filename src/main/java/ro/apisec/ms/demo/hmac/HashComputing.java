package ro.apisec.ms.demo.hmac;


import org.springframework.security.crypto.codec.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class HashComputing {

    private HashComputing() {
        throw  new AssertionError("Instantiation not permitted.");
    }

    /**
     * Get hash for string using SHA-256 encoding algorithm
     */
    public static String shaEncoding(final String key, final String data) throws Exception {
        final Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        final SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        hmacSHA256.init(secretKey);

        return new String(Hex.encode(hmacSHA256.doFinal(data.getBytes("UTF-8"))));
    }
}
