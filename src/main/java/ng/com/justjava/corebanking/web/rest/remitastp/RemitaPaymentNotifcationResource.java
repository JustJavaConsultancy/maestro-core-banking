package ng.com.justjava.corebanking.web.rest.remitastp;

import com.google.gson.Gson;
import ng.com.justjava.corebanking.service.BillerTransactionService;
import ng.com.justjava.corebanking.service.dto.stp.RecieptVO;
import ng.com.justjava.corebanking.service.dto.stp.ResponseVm;
import ng.com.justjava.corebanking.service.dto.stp.TopPaymentNotificationRequest;
import ng.com.systemspecs.remitabillinggateway.rrrdetails.GetRRRDetailsResponse;
import ng.com.systemspecs.remitabillinggateway.service.RemitaBillingGatewayService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/notification")
public class RemitaPaymentNotifcationResource {

    private final Logger log = LoggerFactory.getLogger(RemitaPaymentNotifcationResource.class);

    String mirrorAccount = "0039123456";

    String defaultBankCode = "011";

    @Value("${app.rrr.lookup.url:}")
    private String getRRRDetailsUrl;

    @Autowired
    private TopUpPaymentNotifcationResource topUpPaymentNotifcationResource;

    @Autowired
    private BillerTransactionService billerTransactionService;


    @PostMapping(value = "/remita/notification", consumes = MediaType.ALL_VALUE)
    public ResponseEntity processPaymentNotification(@RequestBody RecieptVO notification) {
        log.info("Remita Payment notification received for ", notification.toString());
        AtomicInteger responseCode = new AtomicInteger(422);
        ResponseVm param = new ResponseVm();
        try {
            String rrrNumber = notification.getRrrNumber();
            log.info("Calling remitaBilingGateway with RRR Number {}", rrrNumber);

            RemitaBillingGatewayService remitaBilingGateway = billerTransactionService.getRemitaBillingGatewayService(String.valueOf(System.currentTimeMillis()));

            GetRRRDetailsResponse rrrdetails = remitaBilingGateway.getRRRDetails(rrrNumber);
            log.info("Response from remitaBilingGateway {}", rrrdetails.getResponseCode());
            if (rrrdetails != null && getRRRStatus(rrrNumber).equalsIgnoreCase("APPROVED")) {
                if (rrrdetails.getResponseData().get(0).getRrrAmount().doubleValue() >= notification.getAmount().doubleValue()) {
                    doProcessPaymentNotification(notification, responseCode, param);
                } else {
                    param.setMessage("Invalid amount for ref :" + rrrNumber);
                    param.setAmount(BigDecimal.valueOf(notification.getAmount()));
                    param.setStatus("401");
                    responseCode.set(200);
                }
            } else {
                //Error getting RRR details
                param.setMessage("Error getting RRR details or RRR Status Not Approved");
                param.setAmount(BigDecimal.valueOf(notification.getAmount()));
                param.setStatus("400");
                responseCode.set(200);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Final Response from processPaymentNotification {}", param.toString());
        return ResponseEntity.status(responseCode.get()).body(param);
    }

    private boolean canProcess(String rrrNumber) {

        RemitaBillingGatewayService remitaBilingGateway = billerTransactionService.getRemitaBillingGatewayService(String.valueOf(System.currentTimeMillis()));
        log.info("Calling remitaBilingGateway with RRR Number {}", rrrNumber);
        GetRRRDetailsResponse rrrdetails = remitaBilingGateway.getRRRDetails(rrrNumber);
        log.info("Response from remitaBilingGateway {}", rrrdetails.getResponseCode());
        return rrrdetails != null;
    }

    public String getRRRStatus(String rrrNumber) {
        log.info("Calling getRRRStatus Via Rest Template: {}", rrrNumber);
        String status = StringUtils.EMPTY;
        ng.com.justjava.corebanking.service.dto.stp.GetRRRDetailsResponse rrrDetailsResponse = null;
        String url = String.format(getRRRDetailsUrl, rrrNumber);

        try {
            log.info("Calling getRRRStatus with url: {}", url);
            /*ClientHttpRequestInterceptor requestInterceptor = new LoggingRequestInterceptor();
            List<ClientHttpRequestInterceptor> requestInterceptors = new ArrayList<>();
            requestInterceptors.add(requestInterceptor);
            restTemplate.setInterceptors(requestInterceptors);*/
            RestTemplate restTemplate = new RestTemplate();
            String responsePayload = restTemplate.getForEntity(url, String.class).getBody();
            String responseBody = new String(Base64.decodeBase64(responsePayload));
            log.info("getRRRStatus responseBody: {} ", responseBody);
            rrrDetailsResponse = new Gson().fromJson(responseBody, ng.com.justjava.corebanking.service.dto.stp.GetRRRDetailsResponse.class);
            status = rrrDetailsResponse.getResponseData().isEmpty() ? StringUtils.EMPTY : rrrDetailsResponse.getResponseData().get(0).getStatus();
        } catch (Exception e) {
            log.error("Error Reaching RRR Details Service ", e);
            e.printStackTrace();
        }
        return status;
    }


    private void doProcessPaymentNotification(@RequestBody RecieptVO notification, AtomicInteger responseCode, ResponseVm param) {
        try {
            TopPaymentNotificationRequest topupRequest = new TopPaymentNotificationRequest();
            topupRequest.setAmount(BigDecimal.valueOf(notification.getAmount()));
            topupRequest.setAmountDebited(BigDecimal.valueOf(notification.getAmount()));
            topupRequest.setPaymentReference(notification.getRrrNumber());
            topupRequest.setChannel(notification.getPaymentChannel());
            topupRequest.setNarration(notification.getDescriptionOfPayment());
            String accountNumber = getPropertiesFromCustomField("Walletid", notification.getCustomFieldData());
            if (StringUtils.isNotBlank(accountNumber)) {
                topupRequest.setWalletId(accountNumber);
                topupRequest.setBankCode(defaultBankCode);
                ResponseEntity presponse = topUpPaymentNotifcationResource.processPaymentNotification(topupRequest);
                if (presponse.getStatusCode() == HttpStatus.OK) {
                    responseCode.set(200);
                    param.setMessage("Wallet account top up received");
                    param.setAmount(BigDecimal.valueOf(notification.getAmount()));
                    param.setStatus("00");
                } else {
                    responseCode.set(400);
                }
            } else {
                param.setMessage("Invalid Wallet account");
                param.setAmount(BigDecimal.valueOf(notification.getAmount()));
                param.setStatus("400");
                responseCode.set(400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            param.setMessage("Error processing request");
            param.setAmount(BigDecimal.valueOf(notification.getAmount()));
            param.setStatus("500");
            responseCode.set(500);
        }
    }

    private String getPropertiesFromCustomField(final String requireCustomFiledId, List<Map<String, Object>> customFields) {
        Map<String, Object> mapped = customFields.stream().filter(customField -> requireCustomFiledId.equalsIgnoreCase((customField).get("DESCRIPTION").toString().trim())).findFirst().orElse(null);
        if (mapped != null) {
            return mapped.get("COLVAL") + StringUtils.EMPTY;
        }
        return null;
    }

}
