package ng.com.systemspecs.apigateway.web.rest;

import ng.com.systemspecs.apigateway.service.CoralPayService;
import ng.com.systemspecs.apigateway.service.dto.CgateDetailsRequestDTO;
import ng.com.systemspecs.apigateway.service.dto.CgateDetailsResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.CgatePaymentNotificationRequestDTO;
import ng.com.systemspecs.apigateway.service.dto.CgatePaymentNotificationResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api")
public class CoralPayResource {

    final static Logger log = LoggerFactory.getLogger(CoralPayResource.class);

    private final CoralPayService coralPayService;

    public CoralPayResource(CoralPayService coralPayService) {
        this.coralPayService = coralPayService;
    }


    @PostMapping(value = "/cgate/notification", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<CgatePaymentNotificationResponseDTO> processPaymentNotification(@RequestBody CgatePaymentNotificationRequestDTO notificationRequest) {

        log.info(">>>>> Notification request {}", notificationRequest);
        CgatePaymentNotificationResponseDTO response = coralPayService.processPaymentNotification(notificationRequest);

        log.info(">>>>> Notification response {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/cgate/details", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<CgateDetailsResponseDTO> getPaymentDetails(@RequestBody CgateDetailsRequestDTO request) {
        log.info(">>>>> Notification request {}", request);

        CgateDetailsResponseDTO response = coralPayService.getPaymentDetails(request);

        log.info(">>>>> Details response {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/cgate/payment/notification", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> paymentNotification(@RequestBody String request) {
        log.info(">>>>> Payment Notification request {}", request);

        String response = coralPayService.coralPayPaymentNotification(request);

        log.info(">>>>> Payment Notification response {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/cgate/statusquery", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> statusQuery(@RequestBody String request) {
        log.info(">>>>> Status Query request {}", request);

        String response = coralPayService.statusQuery(request);

        log.info(">>>>> Status Query response {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping(value = "/cgate/refund", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> refund(@RequestBody String request) {
        log.info(">>>>> Status Query request {}", request);

        String response = coralPayService.refund(request);

        log.info(">>>>> Status Query response {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/cgate/reference/{walletNumber}/{amount}")
    public ResponseEntity<String> getTransactionReference(@PathVariable String walletNumber,
                                                          @PathVariable Double amount) {
        log.info(">>>>> Get Reference request {}", walletNumber, amount);

        String reference = coralPayService.getTransactionReference(walletNumber, amount);

        if (reference != null) {
            log.info(">>>>> Status Query response {}", reference);
            return new ResponseEntity<>(reference, HttpStatus.OK);
        }

        return new ResponseEntity<>("failed to retrieve reference", HttpStatus.BAD_REQUEST);
    }

//    @PostMapping(value = "/cgate/refund", consumes = MediaType.ALL_VALUE)
//    public ResponseEntity<String> invokeReference(@RequestBody String request) {


}
