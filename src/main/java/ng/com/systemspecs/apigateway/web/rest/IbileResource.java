package ng.com.systemspecs.apigateway.web.rest;

import ng.com.systemspecs.apigateway.service.IbileService;
import ng.com.systemspecs.apigateway.service.TransactionLogService;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.IbilePaymentDTO;
import ng.com.systemspecs.apigateway.service.dto.PaymentNotificationResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.ReferenceVerificationResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IbileResource controller
 */
@RestController
@RequestMapping("/api")
public class IbileResource {

    private final Logger log = LoggerFactory.getLogger(IbileResource.class);

    private final IbileService ibileService;

    private final TransactionLogService transactionLogService;

    public IbileResource(IbileService ibileService, TransactionLogService transactionLogService) {
        this.ibileService = ibileService;
        this.transactionLogService = transactionLogService;
    }

    /**
     * POST referenceVerification
     *
     * @return
     */
    @PostMapping("/ibile/reference-verification")
    public ReferenceVerificationResponseDTO referenceVerification() {
        return ibileService.referenceVerification();
    }

    /**
     * POST paymentVerification
     *
     * @return
     */
    @PostMapping("/ibile/payment-verification")
    public PaymentNotificationResponseDTO paymentVerification() {
        return ibileService.paymentVerification();
    }

    /**
     * POST paymentVerification
     */
    @GetMapping("/ibile/payment-details/{billType}/{billReferenceNo}")
    public ResponseEntity<GenericResponseDTO> getPaymentDetails(@PathVariable String billType,
                                                                @PathVariable String billReferenceNo,
                                                                HttpSession session) {


        log.info("Ibile payment details request billReferenceNo ===> " + billReferenceNo);

        GenericResponseDTO genericResponseDTO = ibileService.getPaymentDetails(billType, billReferenceNo, session);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());

    }

    /**
     * POST paymentVerification
     */
    @PostMapping("/ibile/pay")
    public ResponseEntity<GenericResponseDTO> pay(@RequestBody List<IbilePaymentDTO> ibilePaymentDTOs) {

        log.info("Ibile pay request ===> " + ibilePaymentDTOs);
        List<IbilePaymentDTO> vettedList = checkPaymentItemList(ibilePaymentDTOs);
        log.info("Ibile pay request after vet ===> " + ibilePaymentDTOs);
        GenericResponseDTO genericResponseDTO = ibileService.pay(vettedList);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    List <IbilePaymentDTO> checkPaymentItemList(List<IbilePaymentDTO> list){
        return list.stream().filter(
            p -> !transactionLogService.existsByRrr(p.getWebGuid())
        ).collect(Collectors.toList());
    }

    @GetMapping("/ibile/generate-receipt/{externalRef}")
    public ResponseEntity<GenericResponseDTO> generateReceipt(@PathVariable String externalRef) {
        GenericResponseDTO genericResponseDTO = ibileService.generateAgentReceipt(externalRef);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }


    @GetMapping("/ibile/get-webguid/{pId}")
    public ResponseEntity<GenericResponseDTO> getWebguid(@PathVariable String pId) {
        GenericResponseDTO genericResponseDTO = ibileService.getWebguids(pId);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @GetMapping("/ibile/get-receipts/{refId}")
    public ResponseEntity<GenericResponseDTO> generateReceiptPidWebguid(@PathVariable String refId) {
        GenericResponseDTO genericResponseDTO = ibileService.getReceiptsByRef(refId);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

}

