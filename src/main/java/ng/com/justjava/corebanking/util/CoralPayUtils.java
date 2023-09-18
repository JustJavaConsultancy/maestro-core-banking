package ng.com.justjava.corebanking.util;

import com.coralpay.pgp.Helper;
import com.coralpay.pgp.PGPEncryption;
import com.google.api.client.util.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class CoralPayUtils {

    final static Logger log = LoggerFactory.getLogger(CoralPayUtils.class);

    @Value("${app.publicKeyFile}")
    private String publicKeyPath;

    @Value("${app.secretKeyFile}")
    private String privateKeyPath;

    @Value("${app.passphrase}")
    private String privateKeyPassword;

    @Value("${app.serm}")
    private String serm;

    @Value("${app.dough}")
    private String dough;

    private final ResourceLoader resourceLoader;

    public CoralPayUtils(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    // Load the private and public keys here
    private byte[] getPrivateKeyFileContent() {

        Resource resource = resourceLoader.getResource(privateKeyPath);
        try {
            InputStream inputStream = resource.getInputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            log.info("Private Key ===> " + new String(bytes));
            return bytes;

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();

            String filePath = "src/main/resources/static/privateKey.txt";
            Path path = Paths.get(filePath);
            byte[] keyFileContentAsByteArray = Helper.getKeyFileContentAsByteArray(path.toString());
            log.info("Private Key ===> " + new String(keyFileContentAsByteArray));
            return keyFileContentAsByteArray;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getPublicKeyFileContent() {

        Resource resource = resourceLoader.getResource(publicKeyPath);
        try {
            InputStream inputStream = resource.getInputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            log.info("Public Key ===> " + new String(bytes));
            return bytes;

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();

            String filePath = "src/main/resources/static/publicKey.txt";
            Path path = Paths.get(filePath);

            byte[] keyFileContentAsByteArray = Helper.getKeyFileContentAsByteArray(path.toString());
            log.info("Private Key ===> " + new String(keyFileContentAsByteArray));
            return keyFileContentAsByteArray;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String encrypt(String data) {
        String encryptedRequestString = PGPEncryption.encryptRequest(data, getPublicKeyFileContent());
        System.out.println("Encrypt ===> " + encryptedRequestString);
        return encryptedRequestString;
    }

    public String decrypt(String data) {
        String decryptedJSONOutPut = PGPEncryption.decryptResponseString(data, getPrivateKeyFileContent(), privateKeyPassword);
        System.out.println("Decrypt ===> " + decryptedJSONOutPut);
        return decryptedJSONOutPut;
    }

    private HttpHeaders createHeaders() {
        return new HttpHeaders() {{
//            String auth = "remita" + ":" + "1324041821@011#4";
            String auth = serm + ":" + dough;
            byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }


    public String invokeCoralPay(String url, String payload) {

        System.out.println("Calling Coral pay Invoke Reference");
        RestTemplate restTemplate = new RestTemplate();

//        HttpHeaders httpHeaders = createHeaders("Remita", "0841011021@006#1");
        HttpHeaders httpHeaders = createHeaders();
        httpHeaders.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> resquestEntity = new HttpEntity<>(payload, httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url,
            HttpMethod.POST, resquestEntity, String.class);

        if (responseEntity.hasBody()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Error ==== ");
        }
        return null;

    }
}
