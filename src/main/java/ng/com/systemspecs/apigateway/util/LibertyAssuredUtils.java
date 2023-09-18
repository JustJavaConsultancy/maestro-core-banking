package ng.com.systemspecs.apigateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class LibertyAssuredUtils {

    final static Logger log = LoggerFactory.getLogger(LibertyAssuredUtils.class);
    @Autowired
    RestTemplate restTemplate;
    @Value("${app.liberty-assured.auth_code}")
    private String authCode;
    @Value("${app.liberty-assured.branch_code}")
    private String branchCode;
    @Value("${app.liberty-assured.public_key}")
    private String publicKey;
    @Value("${app.liberty-assured.base_url}")
    private String baseUrl;


    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", authCode);
        return headers;
    }

    private String getURL(String endpoint) {
        return baseUrl + "/" + publicKey + "/" + branchCode + "/" + endpoint;
    }

    private String getEligibilityURL(String endpoint) {
        return "http://157.230.230.51/" + endpoint;
    }

    public ResponseEntity<GenericResponseDTO> callLibertyAssuredAPI(String URL, Object obj, HttpMethod type) throws JSONException, org.springframework.boot.configurationprocessor.json.JSONException {
        System.out.println("obj initial entity ===> " + obj);

        HttpHeaders headers = getHeaders();

        HttpEntity<String> httpEntity = null;

        try {
            if (obj != null) {
                String requestBody = getObjectAsJsonString(obj);
                System.out.println("Get obj requestBody ===> " + requestBody);

                httpEntity = new HttpEntity<>(requestBody, headers);
            } else {
                httpEntity = new HttpEntity<>(headers);
            }

            System.out.println("Get obj HTTP entity ===> " + httpEntity);
            ResponseEntity<String> responseEntity = null;

            try {
                if (HttpMethod.POST.equals(type)) {
                    responseEntity = restTemplate.postForEntity(getURL(URL), httpEntity, String.class);
                } else if (HttpMethod.PUT.equals(type)) {
                    responseEntity = restTemplate.exchange(getURL(URL), HttpMethod.PUT, httpEntity, String.class);
                } else if (HttpMethod.GET.equals(type)) {
                    responseEntity = restTemplate.exchange(getURL(URL), HttpMethod.GET, httpEntity, String.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace()), HttpStatus.BAD_REQUEST);
            }

            System.out.println("ResponseEntity ====> " + responseEntity);
            String body = responseEntity.getBody();
            System.out.println("ResponseEntity Body ====> " + body);

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                System.out.println("Generic Response Entity ===> " + body);
                System.out.println("Body ====> " + body);
                if (body != null) {

                    JSONObject jsonObject = new JSONObject(body);

                    System.out.println("JSONOBJECT ==>" + jsonObject);
                    if (jsonObject.has("response")) {
                        String response = jsonObject.getString("response");

                        if (response.contains("Errors")) {
                            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "fail", response), HttpStatus.BAD_REQUEST);
                        }

                        System.out.println("response ====> " + response);
                        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", response), HttpStatus.OK);
                    }

                    if (jsonObject.has("error")) {
                        JSONObject error = jsonObject.getJSONObject("error");
                        if (error.has("message")) {
                            String message = error.getString("message");
                            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, message, body), HttpStatus.BAD_REQUEST);
                        }
                    }

                    return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", body), HttpStatus.OK);
                }
            }

            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", body), HttpStatus.BAD_REQUEST);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace()), HttpStatus.BAD_REQUEST);
        }


    }

    private String getObjectAsJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    public GenericResponseDTO callEligibilityAPI(String URL, Object obj, HttpMethod type) throws JSONException {
        System.out.println("obj initial entity ===> " + obj);

        HttpHeaders headers = getHeaders();

        HttpEntity<String> httpEntity = null;

        try {
            if (obj != null) {
                String requestBody = getObjectAsJsonString(obj);
                System.out.println("Get obj requestBody ===> " + requestBody);

                httpEntity = new HttpEntity<>(requestBody, headers);
            } else {
                httpEntity = new HttpEntity<>(headers);
            }

            System.out.println("Get obj HTTP entity ===> " + httpEntity);
            ResponseEntity<String> responseEntity = null;

            try {
                if (HttpMethod.POST.equals(type)) {
                    responseEntity = restTemplate.postForEntity(getEligibilityURL(URL), httpEntity, String.class);
                } else if (HttpMethod.PUT.equals(type)) {
                    responseEntity = restTemplate.exchange(getEligibilityURL(URL), HttpMethod.PUT, httpEntity, String.class);
                } else if (HttpMethod.GET.equals(type)) {
                    responseEntity = restTemplate.exchange(getEligibilityURL(URL), HttpMethod.GET, httpEntity, String.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
            }

            System.out.println("ResponseEntity ====> " + responseEntity);
            String body = responseEntity.getBody();
            System.out.println("ResponseEntity Body ====> " + body);

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                System.out.println("Generic Response Entity ===> " + body);
                System.out.println("Body ====> " + body);

                return new GenericResponseDTO("00", HttpStatus.OK, "success", body);
            }

            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", body);

        } catch (IOException e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
        }


    }


}
