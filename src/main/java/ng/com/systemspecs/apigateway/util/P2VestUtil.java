package ng.com.systemspecs.apigateway.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Component
public class P2VestUtil {

    @Value("${app.p2vest.base-url}")
    private String baseUrl;
    @Value("${app.p2vest.bearer-token}")
    private String bearerToken;

    private final RestTemplate restTemplate;

    public P2VestUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders addHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Authorization", "Bearer " + bearerToken);
        return httpHeaders;
    }

    public GenericResponseDTO requestWithPayload(String url, Object payload, HttpMethod httpMethod){

        HttpHeaders headers = addHeaders();

        HttpEntity<String> httpEntity;

        try{

            String requestBody = new ObjectMapper().writeValueAsString(payload);
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

}
