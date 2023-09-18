package ng.com.justjava.corebanking.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RemitaCarmelUtils {

//    @Value("${app.pay-biller.baseUrl}")
    private String baseUrl = "http://154.113.17.252:8484/integration-service";

    private final RestTemplate restTemplate;

    public RemitaCarmelUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getObjectAsJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    public GenericResponseDTO requestWithPayload(String url, Object payload, HttpMethod httpMethod){

        HttpEntity<String> httpEntity;

        try{

            String requestBody = getObjectAsJsonString(payload);
            System.out.println("Payload ===> " + requestBody);

            httpEntity = new HttpEntity<>(requestBody);
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

    public RemitaBVNResponse bvnResponse(RemitaBVNRequest remitaBVNRequest){

        GenericResponseDTO bvnResponse = requestWithPayload("/api/remita/validation/bvn", remitaBVNRequest, HttpMethod.POST);

        if (bvnResponse.getStatus() != HttpStatus.OK){
            return null;
        }

        String genericResponseDTOData = (String) bvnResponse.getData();

        RemitaBVNResponse response = new Gson().fromJson(genericResponseDTOData, RemitaBVNResponse.class);

        return response;

    }

    public RemitaNINResponse ninResponse(RemitaNINRequest remitaNINRequest){

        GenericResponseDTO bvnResponse = requestWithPayload("/api/remita/validation/nin", remitaNINRequest, HttpMethod.POST);

        if (bvnResponse.getStatus() != HttpStatus.OK){
            return null;
        }

        String genericResponseDTOData = (String) bvnResponse.getData();

        RemitaNINResponse response = new Gson().fromJson(genericResponseDTOData, RemitaNINResponse.class);

        return response;

    }


}
