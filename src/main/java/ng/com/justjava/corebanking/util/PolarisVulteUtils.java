package ng.com.justjava.corebanking.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.PolarisVulteRequestDTO;
import ng.com.justjava.corebanking.service.dto.PolarisVulteResponseDTO;
import ng.com.justjava.corebanking.service.exception.GenericException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

import static java.lang.System.out;

@Component
public class PolarisVulteUtils {

    final static Logger log = LoggerFactory.getLogger(PolarisVulteUtils.class);
    //    private static final String secretKey = "8da7P7Qu0JgLqDJx";

    @Value("${app.vulte.base_url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final Utility utility;

    public PolarisVulteUtils(RestTemplate restTemplate, Utility utility) {
        this.restTemplate = restTemplate;
        this.utility = utility;
    }

    public String generateSignature(String requestRef, String secretKey) {
        System.out.println("Secret key ==> " + secretKey);

        if (utility.checkStringIsNotValid(secretKey)) {
            throw new GenericException("Invalid Api keys");
        }
        return DigestUtils.md5Hex(requestRef.trim() + ";" + secretKey);
    }

    public String generateAuthSecure(String toBeEncrypted, String secretKey) throws Exception {

        System.out.println("Secret key ==> " + secretKey);

        if (utility.checkStringIsNotValid(secretKey)) {
            throw new GenericException("Invalid Api keys");
        }

        toBeEncrypted = toBeEncrypted.trim();
        MessageDigest md = MessageDigest.getInstance("md5");

        System.out.println("Secret key ===> " + secretKey);
        byte[] digestOfPassword = md.digest(secretKey.getBytes(StandardCharsets.UTF_16LE));
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

        for (int j = 0, k = 16; j < 8; ) {
            keyBytes[k++] = keyBytes[j++];
        }

        SecretKey secretKeySpec = new SecretKeySpec(keyBytes, 0, 24, "DESede");

        IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);

        byte[] plainTextBytes = toBeEncrypted.getBytes(StandardCharsets.UTF_16LE);
        byte[] cipherText = cipher.doFinal(plainTextBytes);

        return new String(Base64.encodeBase64(cipherText));
    }

    public HttpHeaders getHeaders(String requestRef, String apiKey, String secretKey) {
        System.out.println("APi key ==> " + apiKey);
        System.out.println("Secret key ==> " + secretKey);
        if (utility.checkStringIsNotValid(apiKey, secretKey)) {
            throw new GenericException("Invalid Api keys");
        }

        System.out.println("Api key ===> " + apiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Signature", generateSignature(requestRef, secretKey));

        out.println("Polaris headers ===> " + headers);

        return headers;
    }

    public GenericResponseDTO invokeVulteApi(PolarisVulteRequestDTO request, HttpHeaders headers, String url) {
        out.println("invokeVulteApi initial request object ===> " + request);
        String requestBody;

        try {
            requestBody = getObjectAsJsonString(request);
            out.println("invokeVulteApi request body ===> " + requestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            out.println("invokeVulteApi request body error ===> " + e.getLocalizedMessage());
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Unable to parse request", e.getLocalizedMessage());
        }

        if (utility.checkStringIsNotValid(requestBody)) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Request body cannot be empty", request);
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        out.println("invokeVulteApi Get obj HTTP entity ===> " + httpEntity);
        ResponseEntity<PolarisVulteResponseDTO> responseEntity = null;

        out.println("invokeVulteApi URL ====> " + url);
        try {
            responseEntity = restTemplate.postForEntity(buildUrl(url), httpEntity, PolarisVulteResponseDTO.class);
            out.println("invokeVulteApi ResponseEntity ====> " + responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("invokeVulteApi ResponseEntity error ====> " + e.getMessage() + " class ==> " + e.getClass());
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), null);
        }

        PolarisVulteResponseDTO body = responseEntity.getBody();
        out.println("invokeVulteApi ResponseEntity body ====> " + body);

        return new GenericResponseDTO("00", HttpStatus.OK, "success", body);

    }

    private String buildUrl(String url) {
        return baseUrl + url;
    }

    private String getObjectAsJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
