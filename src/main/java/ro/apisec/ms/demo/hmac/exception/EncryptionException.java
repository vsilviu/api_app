package ro.apisec.ms.demo.hmac.exception;


public class EncryptionException extends RuntimeException {

    public EncryptionException(final String message, final Exception exception) {
        super(message, exception);
    }
}
