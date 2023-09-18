package ng.com.systemspecs.apigateway.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnqiryRequest;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnquiry;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnquiryResponse;
import org.apache.commons.collections.MapUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class FlutterWaveUtil {

    private static final String URL = "https://fundacause.net/api/flutterwave/accounts/resolve";
    private final RestTemplate msRestTemplate;
    protected ObjectMapper mapper = new ObjectMapper();

    public AccountEnquiryResponse resolveAccounts(AccountEnqiryRequest accountEnqiryRequest) {
        log.debug("Account Enquiry Request  ===> {} ", accountEnqiryRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> request = new HashMap<>();
        request.put("accountNumber", accountEnqiryRequest.getAccountNo());
        request.put("bankCode", accountEnqiryRequest.getBankCode());
        log.debug("Request Payload  ===> {} ", request);

        URI uri = UriComponentsBuilder.fromUriString(URL).build().toUri();
        RequestEntity<?> requestEntity = new RequestEntity<>(request, headers, HttpMethod.POST,
            uri);
        log.debug("RequestEntity Payload  ===> {} ", requestEntity.getBody());
        FlutterWaveResponse response;
        try {
            ResponseEntity<String> responseEntity = msRestTemplate
                .exchange(requestEntity, String.class);
            String jsonResponse = responseEntity.getBody();
            log.debug("Json Response  ===> {} ",jsonResponse);
            response = mapper.readValue(jsonResponse, FlutterWaveResponse.class);
            log.debug("FlutterWave Account Lookup===> {}", response);
            return convertMapToEnquiryResponse(response);
        } catch (Exception ex) {
            log.error("ERROR {}", ex.getMessage());
            AccountEnquiryResponse accountEnquiryResponse = new AccountEnquiryResponse();
            accountEnquiryResponse.setStatus("failed");
            return accountEnquiryResponse;
        }
    }

    private AccountEnquiryResponse convertMapToEnquiryResponse(FlutterWaveResponse response) {
        log.debug("FlutterWave Response ===> {} ", response);
        String status = response.getStatus();
        String accountNumber = MapUtils.getString(response.getData(), "account_number");
        String accountName = MapUtils.getString(response.getData(), "account_name");
        AccountEnquiry accountEnquiry = new AccountEnquiry();
        accountEnquiry.setAccountName(accountName);
        accountEnquiry.setAccountNo(accountNumber);
        AccountEnquiryResponse accountEnquiryResponse = new AccountEnquiryResponse();
        accountEnquiryResponse.setData(accountEnquiry);
        accountEnquiryResponse.setStatus(status);
        log.debug("AccountEnquiry Response ===> {} ", accountEnquiryResponse);
        return accountEnquiryResponse;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class FlutterWaveResponse {

        private String status;
        private String message;
        private Map<String, Object> data;
    }

}
