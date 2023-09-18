package ng.com.systemspecs.apigateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CashConnectUtils {

    final static Logger log = LoggerFactory.getLogger(CashConnectUtils.class);
    @Autowired
    RestTemplate restTemplate;
    @Value("${app.cashconnect.username}")
    private String username;
    @Value("${app.cashconnect.password}")
    private String password;
    @Value("${app.cashconnect.email}")
    private String email;
    @Value("${app.cashconnect.login-url}")
    private String loginURL;
    @Value("${app.cashconnect.base-url}")
    private String baseURL;

    private String getAuthBody() throws JsonProcessingException {
        AuthUser authUser = new AuthUser();
        authUser.setEmail(email);
        authUser.setPassword(password);

        return new ObjectMapper().writeValueAsString(authUser);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.TEXT_PLAIN_VALUE);
        headers.set("Accept", MediaType.TEXT_PLAIN_VALUE);
        return headers;
    }

   /* private HttpHeaders createHeaders() {
        return new HttpHeaders() {{
//            String auth = "remita" + ":" + "1324041821@011#4";
            String auth = serm + ":" + dough;
            byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }*/

    public HttpHeaders getAuthToken() {
        log.info("Inside AuthToken");
        HttpEntity<String> authEntity = null;
        try {
            authEntity = new HttpEntity<>(getAuthBody(), getHeaders());

            log.info("Calling AuthToken API");
            ResponseEntity<LoginResponse> responseEntity = restTemplate.exchange(baseURL + loginURL, HttpMethod.POST, authEntity, LoginResponse.class);

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
//                LoginResponse response = new Gson().fromJson(responseEntity.getBody(),LoginResponse.class);
                LoginResponse response = responseEntity.getBody();
                log.info("login Response ==> " + response);
                if (response != null) {
                    Body body = response.getBody();
                    log.info("body Response ==> " + body);

                    if (body != null) {
                        LoginResponseData data = body.getData();
                        log.info("data Response ==> " + data);
                        if (data != null) {
                            AuthenticationResult authenticationResult = data.getAuthenticationResult();
                            log.info("authenticationResult Response ==> " + authenticationResult);
                            if (authenticationResult != null) {
                                String accessToken = authenticationResult.getAccessToken();
                                String idToken = authenticationResult.getIdToken();

//                              String token = "Bearer " + accessToken;
                                String token = "Bearer " + idToken;

                                HttpHeaders headers = getHeaders();
                                headers.set("Authorization", token);
                                headers.set("AccessToken", accessToken);

                                return headers;
                            }
                        }

                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

}
