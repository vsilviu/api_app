package ro.apisec.ms.demo.hmac;


import org.springframework.stereotype.Component;

@Component
public class Settings {

    public String getEncryptionKey() {
        return "encryptionKey";
    }

    public String getHMacEncodeKey() {
        return "HMacEncodeKey";
    }

    public String getMerchantID() {
        return "merchantID";
    }
}
