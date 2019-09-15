package ro.apisec.ms.demo.hmac;


import ro.apisec.ms.demo.hmac.exception.EncryptionException;
import ro.apisec.ms.demo.hmac.exception.HMacEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

public class ParameterComputing {
    private final Settings parameterBean;
    private Cart cart;
    private Cipher cipher = null;
    private SecretKeySpec KS = null;

    public ParameterComputing(final Settings parameterBean, final Cart cart) {
        this.parameterBean = Objects.requireNonNull(parameterBean, "Settings must not be null.");
        this.cart = Objects.requireNonNull(cart, "cart must not be null.");

        try {
            cipher = Cipher.getInstance("Blowfish");
            KS = new SecretKeySpec(parameterBean.getEncryptionKey().getBytes(), "Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, KS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] getEncryptedDataForCart() {
        try {
            final String uriQueryString = getMacForCart();
            return cipher.doFinal(uriQueryString.getBytes());
        } catch (final Exception e) {
            throw new EncryptionException("data", e);
        }
    }

    public String getDecryptedData(final byte[] encr) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, KS);
            return new String(cipher.doFinal(encr));
        } catch (Exception e) {
            return "";
        }
    }


    private String getMacForCart() throws HMacEncodingException {
        final StringBuilder sb = new StringBuilder("*");
        sb.append(cart.getId()).append('*');
        sb.append(parameterBean.getMerchantID()).append('*');
        sb.append(cart.getTotalPrice()).append('*');
        sb.append(cart.getCurrency());
        try {
            return HashComputing.shaEncoding(parameterBean.getHMacEncodeKey(), sb.toString());
        } catch (final Exception ex) {
            throw new HMacEncodingException(String.format("Something went wrong when trying create hmac"), ex);
        }
    }

}
