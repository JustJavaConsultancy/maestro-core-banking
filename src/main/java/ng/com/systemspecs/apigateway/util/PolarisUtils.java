package ng.com.systemspecs.apigateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ng.com.systemspecs.apigateway.domain.Kyclevel;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.service.ProfileService;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.PolarisAuthRequestDTO;
import ng.com.systemspecs.apigateway.service.dto.PolarisAuthResponseDTO;
import ng.com.systemspecs.apigateway.service.exception.GenericException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class PolarisUtils {


    private final Logger log = LoggerFactory.getLogger(PolarisUtils.class);

    private final RestTemplate restTemplate;
    private final Utility utility;
    private final ProfileService profileService;
    @Value("${app.polaris-card.token-url}")
    private String baseUrl;
    @Value("${app.polaris-card.oauth}")
    private String OAuthurl;
    @Value("${app.polaris-card.client_id}")
    private String client_id;
    @Value("${app.polaris-card.client_secret}")
    private String client_secret;
    @Value("${app.polaris-card.grant_type}")
    private String grant_type;

    public PolarisUtils(RestTemplate restTemplate, Utility utility, ProfileService profileService) {
        this.restTemplate = restTemplate;
        this.utility = utility;
        this.profileService = profileService;
    }

    public GenericResponseDTO getAuth() {
        PolarisAuthRequestDTO polarisAuthRequestDTO = new PolarisAuthRequestDTO();
        polarisAuthRequestDTO.setClientId(client_id);
        polarisAuthRequestDTO.setClientSecret(client_secret);
        polarisAuthRequestDTO.setGrantType(grant_type);

        GenericResponseDTO genericResponseDTO = callPolarisApiAuth(OAuthurl, HttpMethod.POST);
        return genericResponseDTO;
    }

    private HttpHeaders getOAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private HttpHeaders getHeaders() {

        GenericResponseDTO genericResponseDTO = getAuth();
        PolarisAuthResponseDTO polarisAuthResponseDTO = (PolarisAuthResponseDTO) genericResponseDTO.getData();

        String token = polarisAuthResponseDTO.getAccessToken();
        String tokenString = "Bearer " + token;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", tokenString);
        return headers;
    }


    private String getObjectAsJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }


    public GenericResponseDTO callPolarisApiAuth(String url, HttpMethod type) {

        HttpHeaders headers = getOAuthHeaders();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", client_id);
        map.add("client_secret", client_secret);
        map.add("grant_type", grant_type);

        HttpEntity<MultiValueMap<String, String>> httpEntity;

        try {

            httpEntity = new HttpEntity<>(map, headers);

            System.out.println("Get obj HTTP entity ===> " + httpEntity);
            ResponseEntity<PolarisAuthResponseDTO> responseEntity = null;
            try {
                if (HttpMethod.POST.equals(type)) {
                    responseEntity = restTemplate.postForEntity(baseUrl + url, httpEntity, PolarisAuthResponseDTO.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
            }

            System.out.println("ResponseEntity ====> " + responseEntity);
            PolarisAuthResponseDTO responsebody = responseEntity.getBody();
            System.out.println("ResponseEntity Body ====> " + responsebody);

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {

                if (responsebody != null) {
                    return new GenericResponseDTO("00", HttpStatus.OK, "success", responsebody);
                }
            }

            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", responsebody);

        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
        }
    }


    public GenericResponseDTO callPolarisApi(String url, Object body, HttpMethod type) {

        HttpHeaders headers = getHeaders();

        HttpEntity<String> httpEntity;

        try {
            if (body != null) {
                String requestbody = getObjectAsJsonString(body);
                System.out.println("Get obj requestbody ==>" + requestbody);

                httpEntity = new HttpEntity<>(requestbody, headers);
            } else {
                httpEntity = new HttpEntity<>(headers);
            }

            System.out.println("Get obj HTTP entity ===> " + httpEntity);
            ResponseEntity<String> responseEntity = null;
            try {
                if (HttpMethod.POST.equals(type)) {
                    responseEntity = restTemplate.postForEntity(baseUrl + url, httpEntity, String.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
            }

            System.out.println("ResponseEntity ====> " + responseEntity);
            String responsebody = responseEntity.getBody();
            System.out.println("ResponseEntity Body ====> " + responsebody);

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {

                System.out.println("Generic Response Entity ===> " + body);
                System.out.println("Body ====> " + body);

                if (body != null) {
                    JSONObject jsonObject = new JSONObject(responsebody);
                    System.out.println("JSONOBJECT ==>" + jsonObject);
                    return new GenericResponseDTO("00", HttpStatus.OK, "success", jsonObject);
                }
            }

            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", responsebody);

        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
        }

    }


    public void updatePolarisKYCLevel(String phoneNumber) {
        //TODO consume Polaris KYC update API
        log.debug("Updating User KYC ==<> {} ", phoneNumber);
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        Profile user =  profileService.findByPhoneNumber(phoneNumber);
        if(user==null){
            throw new GenericException("Profile not found for this phone number {} ==> " + phoneNumber);
        }

        Kyclevel userKYC = user.getKyc();
        log.debug("User Current KYC Level ===> {}", userKYC);
        if(userKYC.getKycLevel() == 2){

        }
        else if(userKYC.getKycLevel() == 3){

        }

    }


}
