package ro.apisec.ms.demo.hmac.exception;


public class HMacEncodingException extends Exception {

    public HMacEncodingException(final String message, final Exception exception) {
        super(message, exception);
    }
}
