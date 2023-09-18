package ng.com.systemspecs.apigateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.google.gson.Gson;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Component
public class MigoUtils {

    @Value("${app.migo.base-url}")
    private String baseUrl;
    @Value("${app.migo.key-id}")
    private String keyId;
    @Value("${app.migo.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate;

    public MigoUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String basicAuth(){
        String auth = keyId + ":" + secretKey;
        byte[] encodedAuth = Base64.encodeBase64(
            auth.getBytes(StandardCharsets.US_ASCII));
        return new String(encodedAuth);
    }

    private HttpHeaders addHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Authorization", "Basic " + basicAuth());
        return httpHeaders;
    }

    private String getObjectAsJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    public GenericResponseDTO requestWithQuaryParams(String url, HashMap<String, String> queryParams, HttpMethod httpMethod){

        HttpHeaders headers = addHeaders();

        HttpEntity<String> httpEntity;

        try{

            httpEntity = new HttpEntity<>(headers);
            System.out.println("HTTP entity ===> " + httpEntity);

            if (queryParams == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Params not found");
            }

            System.out.println("Query Params ===> " + queryParams);

            ResponseEntity<String> responseEntity = null;

            try {

                if (HttpMethod.POST.equals(httpMethod)){
                    responseEntity = restTemplate.postForEntity(baseUrl + url, httpEntity, String.class, queryParams);
                }
                if (HttpMethod.GET.equals(httpMethod)){
                    responseEntity = restTemplate.exchange(baseUrl + url, HttpMethod.GET, httpEntity, String.class, queryParams);
                }
                if (HttpMethod.PUT.equals(httpMethod)){
                    responseEntity = restTemplate.exchange(baseUrl + url, HttpMethod.PUT, httpEntity, String.class, queryParams);
                }

            }catch (Exception ex){
                System.out.println("RestTemplate Error ===> " + ex.getLocalizedMessage());
                ex.printStackTrace();
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getStackTrace());
            }

            if (responseEntity == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response");
            }
            String responseBody = responseEntity.getBody();
            System.out.println("String representation of response body ===> " + responseBody);

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())){

                if (responseBody == null){
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No Response");
                }

//                JSONObject jsonObject = new JSONObject(responseBody);
//                System.out.println("Json representation of response body ===> " + jsonObject);
                return new GenericResponseDTO("00", HttpStatus.OK, "success", responseBody);

            }else {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Response code: " + responseEntity.getStatusCode());
            }

        }catch (Exception e){
            System.out.println("RequestWithQuaryParams Error ===> " + e.getLocalizedMessage());
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
        }

    }

    public GenericResponseDTO requestWithPayload(String url, Object payload, HttpMethod httpMethod){

        HttpHeaders headers = addHeaders();

        HttpEntity<String> httpEntity;

        try{

            String requestBody = getObjectAsJsonString(payload);
            System.out.println("Payload ===> " + requestBody);

            httpEntity = new HttpEntity<>(requestBody, headers);
            System.out.println("HTTP entity ===> " + httpEntity);

            if (payload == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Payload not found");
            }

            ResponseEntity<String> responseEntity = null;

            try {

                if (HttpMethod.POST.equals(httpMethod)){
                    responseEntity = restTemplate.exchange(baseUrl + url, httpMethod, httpEntity, String.class);
                }
                if (HttpMethod.GET.equals(httpMethod)){
                    responseEntity = restTemplate.exchange(baseUrl + url, httpMethod, httpEntity, String.class);
                }
                if (HttpMethod.PUT.equals(httpMethod)){
                    responseEntity = restTemplate.exchange(baseUrl + url, httpMethod, httpEntity, String.class);
                }

            }catch (Exception ex){
                System.out.println("RestTemplate Error ===> " + ex.getLocalizedMessage());
                ex.printStackTrace();
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getStackTrace());
            }

            if (responseEntity == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response");
            }
            String responseBody = responseEntity.getBody();
            System.out.println("String representation of response body ===> " + responseBody);

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())){

                if (responseBody == null){
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No Response");
                }

                JSONObject jsonObject = new JSONObject(responseBody);
                System.out.println("Json representation of response body ===> " + jsonObject);
                return new GenericResponseDTO("00", HttpStatus.OK, "success", responseBody);

            }else {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Response code: " + responseEntity.getStatusCode());
            }

        }catch (Exception e){
            System.out.println("RequestWithPayload Error ===> " + e.getLocalizedMessage());
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
        }

    }


    public GenericResponseDTO requestWithoutPayload(String url, HttpMethod httpMethod){

        HttpHeaders headers = addHeaders();

        HttpEntity<String> httpEntity;

        try{

            httpEntity = new HttpEntity<>(headers);
            System.out.println("HTTP entity ===> " + httpEntity);

            ResponseEntity<String> responseEntity = null;

            try {

                if (HttpMethod.POST.equals(httpMethod)){
                    responseEntity = restTemplate.postForEntity(baseUrl + url, httpEntity, String.class);
                }
                if (HttpMethod.GET.equals(httpMethod)){
                    responseEntity = restTemplate.exchange(baseUrl + url, httpMethod, httpEntity, String.class);
                }
                if (HttpMethod.PUT.equals(httpMethod)){
                    responseEntity = restTemplate.exchange(baseUrl + url, httpMethod, httpEntity, String.class);
                }

            }catch (Exception ex){
                System.out.println("RestTemplate Error ===> " + ex.getLocalizedMessage());
                ex.printStackTrace();
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getStackTrace());
            }

            System.out.println("Response Entity ===> " + responseEntity);
            if (responseEntity == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response");
            }
            String responseBody = responseEntity.getBody();
            System.out.println("String representation of response body ===> " + responseBody);

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())){

                if (responseBody != null){
//                    String dataString = new Gson().toJson(responseBody);
//                    System.out.println("Json representation of response body ===> " + dataString);
                    return new GenericResponseDTO("00", HttpStatus.OK, "success", responseBody);
//                    return new GenericResponseDTO("00", HttpStatus.OK, "success", dataString);
                }

            } else {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Response code: " + responseEntity.getStatusCode());
            }

            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "HttpEntity not found");

        }catch (Exception e){
            System.out.println("RequestWithoutPayload Error ===> " + e.getLocalizedMessage());
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
        }

    }

}
