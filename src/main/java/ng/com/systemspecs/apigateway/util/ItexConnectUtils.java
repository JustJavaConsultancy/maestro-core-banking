package ng.com.systemspecs.apigateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Optional;

import static java.lang.System.out;

@Component
public class ItexConnectUtils {

    final static Logger log = LoggerFactory.getLogger(ItexConnectUtils.class);

    @Autowired
    RestTemplate restTemplate;

    @Value("${app.itex.base-url}")
    private String baseUrl;
    @Value("${app.itex.base-url2}")
    private String baseUrl2;
    @Value("${app.itex.organisation-code}")
    private String organisationCode;
    @Value("${app.itex.pin}")
    private String pin;
    @Value("${app.itex.username}")
    private String username;
    @Value("${app.itex.wallet}")
    private String wallet;
    @Value("${app.itex.name}")
    private String name;
    @Value("${app.itex.identifier}")
    private String identifier;
    @Value("${app.itex.password}")
    private String password;
    @Value("${app.itex.authenticate-url}")
    private String authenticateUrl;
    private final Utility utility;
    @Value("${app.itex.channel}")
    private String channel;
    @Value("${app.itex.key}")
    private String key;

    public ItexConnectUtils(Utility utility) {
        this.utility = utility;
    }


    private String getURL(int version, String endpoint) {
        if (version == 1) {
            return baseUrl + endpoint;
        }
        return baseUrl2 + endpoint;
    }

    static private String calcHmacSha256(byte[] secretKey, byte[] message) {
        byte[] hmacSha256 = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }

        return Hex.encodeHexString(hmacSha256);

    }

    private HttpHeaders getHeaders(String signature, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("signature", signature);
        headers.set("token", token);
        return headers;
    }

    private String getAuthBody() throws JsonProcessingException {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setIdentifier(identifier);
        authenticationDTO.setPassword(password);
        authenticationDTO.setUsername(username);
        authenticationDTO.setWallet(wallet);

        return getObjectAsJsonString(authenticationDTO);
    }

    public Optional<HttpHeaders> authorize(String requestBody) {

        HttpEntity<String> authEntity = null;
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

            authEntity = new HttpEntity<>(getAuthBody(), headers);

            log.info("AuthEntity ===> " + authEntity);

            String url = authenticateUrl;

            log.info("Authorize URL  ===> " + url);

            ResponseEntity<ItexLoginResponse> responseEntity = restTemplate.postForEntity(url, authEntity, ItexLoginResponse.class);

            log.info(String.format("Authorize Response  ===> %s", responseEntity));

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {

                ItexLoginResponse itexLoginResponse = responseEntity.getBody();
                log.info(String.format("ItexLoginResponse  ===> %s", itexLoginResponse));

                if (itexLoginResponse != null && "00".equalsIgnoreCase(itexLoginResponse.getResponseCode())) {

                    ItexLoginResponseData itexLoginResponseData = itexLoginResponse.getData();
                    if (itexLoginResponseData != null) {
//                        String signature = new DigestUtils("SHA-512").digestAsHex(requestBody + key);
                        String signature = calcHmacSha256(key.getBytes(), requestBody.getBytes());
                        log.info("SIGNATURE ===> " + signature);
                        return Optional.of(getHeaders(signature, itexLoginResponseData.getApiToken()));
                    }
                }
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private String getObjectAsJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    public GenericResponseDTO callItexApi(int version, String URL, Object obj, HttpMethod type) throws JSONException {
        out.println("obj initial entity ===> " + obj);

        String requestBody = "";
        try {
            requestBody = getObjectAsJsonString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Error occurred while parsing request", obj);
        }

        if (utility.checkStringIsNotValid(requestBody)) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Request body cannot be empty", obj);
        }

        Optional<HttpHeaders> authorizeOptional;

        try {
            authorizeOptional = authorize(requestBody);
        } catch (Exception e) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Connection error", null);
        }

        HttpEntity<String> httpEntity = null;

        if (authorizeOptional.isPresent()) {
            HttpHeaders headers = authorizeOptional.get();
            httpEntity = new HttpEntity<>(requestBody, headers);
        }

        out.println("Get obj HTTP entity ===> " + httpEntity);
        ResponseEntity<ItexResponseDTO> responseEntity = null;

        try {
            String url = getURL(version, URL);
            out.println("ITEX URL ===> " + url);

            if (HttpMethod.POST.equals(type)) {
                responseEntity = restTemplate.postForEntity(url, httpEntity, ItexResponseDTO.class);
            } else if (HttpMethod.PUT.equals(type)) {
                responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, ItexResponseDTO.class);
            } else if (HttpMethod.GET.equals(type)) {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ItexResponseDTO.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), null);
        }

        out.println("ResponseEntity ====> " + responseEntity);
        ItexResponseDTO body = null;
        if (responseEntity != null) {
            body = responseEntity.getBody();
        }
        out.println("ResponseEntity Body ====> " + body);

        if (body != null) {

            if ("00".equalsIgnoreCase(body.getResponseCode())) {
                out.println("Generic Response Entity ===> " + body);

                out.println("response ====> " + body);
                Object data = body.getData();
                String dataString = new Gson().toJson(data);
                return new GenericResponseDTO(body.getResponseCode(), responseEntity.getStatusCode(), body.getMessage(), dataString);
            } else {
                return new GenericResponseDTO(body.getResponseCode(), HttpStatus.BAD_REQUEST, body.getMessage(), body);
            }

        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null);
    }

    public PurchaseElectricityResponseData toPurchaseElectricityResponseData(PurchaseElectricityResponseDataData purchaseElectricityResponseDataData){
        if (purchaseElectricityResponseDataData == null){
            return null;
        }

        PurchaseElectricityResponseData purchaseElectricityResponseData = new PurchaseElectricityResponseData();
        purchaseElectricityResponseData.setStatus(purchaseElectricityResponseDataData.getStatus());
        purchaseElectricityResponseData.setError(purchaseElectricityResponseDataData.getError());
        purchaseElectricityResponseData.setMessage(purchaseElectricityResponseDataData.getMessage());
        purchaseElectricityResponseData.setDate(purchaseElectricityResponseDataData.getDate());
        purchaseElectricityResponseData.setRef(purchaseElectricityResponseDataData.getRef());
        purchaseElectricityResponseData.setToken(purchaseElectricityResponseDataData.getToken());
        purchaseElectricityResponseData.setAddress(purchaseElectricityResponseDataData.getAddress());
        purchaseElectricityResponseData.setPayer(purchaseElectricityResponseDataData.getPayer());
        purchaseElectricityResponseData.setAmount(purchaseElectricityResponseDataData.getAmount());
        purchaseElectricityResponseData.setAccount_type(purchaseElectricityResponseDataData.getAccount_type());
        purchaseElectricityResponseData.setKct(purchaseElectricityResponseDataData.getKct());
        purchaseElectricityResponseData.setClient_id(purchaseElectricityResponseDataData.getClient_id());
        purchaseElectricityResponseData.setSgc(purchaseElectricityResponseDataData.getSgc());
        purchaseElectricityResponseData.setMsno(purchaseElectricityResponseDataData.getMsno());
        purchaseElectricityResponseData.setUnit_cost(purchaseElectricityResponseDataData.getUnit_cost());
        purchaseElectricityResponseData.setTran_id(purchaseElectricityResponseDataData.getTran_id());
        purchaseElectricityResponseData.setKrn(purchaseElectricityResponseDataData.getKrn());
        purchaseElectricityResponseData.setTi(purchaseElectricityResponseDataData.getTi());
        purchaseElectricityResponseData.setTt(purchaseElectricityResponseDataData.getTt());
        purchaseElectricityResponseData.setUnit(purchaseElectricityResponseDataData.getUnit());
        purchaseElectricityResponseData.setAdjust_unit(purchaseElectricityResponseDataData.getAdjust_unit());
        purchaseElectricityResponseData.setPreset_unit(purchaseElectricityResponseDataData.getPreset_unit());
        purchaseElectricityResponseData.setTotal_unit(purchaseElectricityResponseDataData.getTotal_unit());
        purchaseElectricityResponseData.setUnit_value(purchaseElectricityResponseDataData.getUnit_value());
        purchaseElectricityResponseData.setManuf(purchaseElectricityResponseDataData.getManuf());
        purchaseElectricityResponseData.setModel(purchaseElectricityResponseDataData.getModel());
        purchaseElectricityResponseData.setFeederBand(purchaseElectricityResponseDataData.getFeederBand());
        purchaseElectricityResponseData.setFeederName(purchaseElectricityResponseDataData.getFeederName());
        purchaseElectricityResponseData.setArrears(purchaseElectricityResponseDataData.getArrears());
        purchaseElectricityResponseData.setBalance(purchaseElectricityResponseDataData.getBalance());
        purchaseElectricityResponseData.setRefund(purchaseElectricityResponseDataData.getRefund());
        purchaseElectricityResponseData.setWalletBalance(purchaseElectricityResponseDataData.getWalletBalance());
        purchaseElectricityResponseData.setTariff(purchaseElectricityResponseDataData.getTariff());
        purchaseElectricityResponseData.setTariff_class(purchaseElectricityResponseDataData.getTariff_class());
        purchaseElectricityResponseData.setVatRate(purchaseElectricityResponseDataData.getVatRate());
        purchaseElectricityResponseData.setResponse_code(purchaseElectricityResponseDataData.getResponse_code());
        purchaseElectricityResponseData.setTransactionUniqueNumber(purchaseElectricityResponseDataData.getTransactionUniqueNumber());
        purchaseElectricityResponseData.setTransactionDateTime(purchaseElectricityResponseDataData.getTransactionDateTime());
        purchaseElectricityResponseData.setTransactionDebitTransactionId(purchaseElectricityResponseDataData.getTransactionDebitTransactionId());
        purchaseElectricityResponseData.setDealer_name(purchaseElectricityResponseDataData.getDealer_name());
        purchaseElectricityResponseData.setAgent_name(purchaseElectricityResponseDataData.getAgent_name());
        purchaseElectricityResponseData.setAgent_code(purchaseElectricityResponseDataData.getAgent_code());
        purchaseElectricityResponseData.setRate(purchaseElectricityResponseDataData.getRate());
        purchaseElectricityResponseData.setType(purchaseElectricityResponseDataData.getType());
        purchaseElectricityResponseData.setOutstandingDebt(purchaseElectricityResponseDataData.getOutstandingDebt());
        purchaseElectricityResponseData.setCostOfUnit(purchaseElectricityResponseDataData.getCostOfUnit());
        purchaseElectricityResponseData.setVat(purchaseElectricityResponseDataData.getVat());
        purchaseElectricityResponseData.setFixedCharge(purchaseElectricityResponseDataData.getFixedCharge());
        purchaseElectricityResponseData.setReconnectionFee(purchaseElectricityResponseDataData.getReconnectionFee());
        purchaseElectricityResponseData.setLor(purchaseElectricityResponseDataData.getLor());
        purchaseElectricityResponseData.setAdministrativeCharge(purchaseElectricityResponseDataData.getAdministrativeCharge());
        purchaseElectricityResponseData.setInstallationFee(purchaseElectricityResponseDataData.getInstallationFee());
        purchaseElectricityResponseData.setPenalty(purchaseElectricityResponseDataData.getPenalty());
        purchaseElectricityResponseData.setMeterCost(purchaseElectricityResponseDataData.getMeterCost());
        purchaseElectricityResponseData.setCurrentCharge(purchaseElectricityResponseDataData.getCurrentCharge());
        purchaseElectricityResponseData.setMsc(purchaseElectricityResponseDataData.getMsc());
        purchaseElectricityResponseData.setResponseCode(purchaseElectricityResponseDataData.getResponseCode());
        purchaseElectricityResponseData.setReference(purchaseElectricityResponseDataData.getReference());
        purchaseElectricityResponseData.setSequence(purchaseElectricityResponseDataData.getSequence());
        purchaseElectricityResponseData.setClientReference(purchaseElectricityResponseDataData.getClientReference());

        return purchaseElectricityResponseData;
    }


}
