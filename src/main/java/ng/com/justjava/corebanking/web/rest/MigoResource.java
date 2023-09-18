package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.service.MigoService;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class MigoResource {

    private final Logger log = LoggerFactory.getLogger(MigoResource.class);
    private final MigoService migoService;

    public MigoResource(MigoService migoService) {
        this.migoService = migoService;
    }

    @PostMapping("/migo/callback")
    public ResponseEntity<MigoCallbackResponseDTO> migoCallback(@Valid @RequestBody MigoCallbackRequestDTO migoCallbackRequestDTO){
        log.info("migo call back request payload ====> " + migoCallbackRequestDTO);
        return new ResponseEntity<>(new MigoCallbackResponseDTO("successful"), HttpStatus.OK);
    }

    @GetMapping("/migo/loan")
    public ResponseEntity<GenericResponseDTO> getLoan(@Valid @RequestParam String clientNo){
        log.info("migo call back request payload ====> " + clientNo);
        HashMap<String, String> queryParam = new HashMap<>();
        queryParam.put("clientNo", clientNo);
        GenericResponseDTO response = migoService.getLoan(queryParam);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/migo/real-time-loan")
    public ResponseEntity<GenericResponseDTO> getLoanRealTimeOffers(@Valid @RequestBody MigoLoanRealTimeOffersRequestDTO migoLoanRealTimeOffersRequestDTO){
        log.info("Migo Loan Real Time Offers Request Payload ====> " + migoLoanRealTimeOffersRequestDTO);
        GenericResponseDTO response = migoService.getLoanRealTimeOffers(migoLoanRealTimeOffersRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/migo/select-offer")
    public ResponseEntity<GenericResponseDTO> selectOffer(@Valid @RequestBody MigoSelectOfferRequestDTO migoSelectOfferRequestDTO){
        log.info("Migo Select Offer Request Payload ====> " + migoSelectOfferRequestDTO);
        GenericResponseDTO response = migoService.selectOffer(migoSelectOfferRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/migo/account")
    public ResponseEntity<GenericResponseDTO> getAccount(@Valid @RequestParam String clientNo, String lastUsed){
        HashMap<String, String> queryParam = new HashMap<>();
        queryParam.put("clientNo", clientNo);
        queryParam.put("lastUsed", lastUsed);
        GenericResponseDTO response = migoService.getAccount(queryParam);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/migo/bank-list")
    public ResponseEntity<GenericResponseDTO> getBankList(){
        GenericResponseDTO response = migoService.getBankList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/migo/submit-application")
    public ResponseEntity<GenericResponseDTO> submitApplication(@Valid @RequestBody MigoSubmitApplicationRequestDTO migoSubmitApplicationRequestDTO){
        log.info("Migo Submit Application Request Payload ====> " + migoSubmitApplicationRequestDTO);
        GenericResponseDTO response = migoService.submitApplication(migoSubmitApplicationRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/migo/terms-and-conditions")
    public ResponseEntity<GenericResponseDTO> termsAndConditions(){
        GenericResponseDTO response = migoService.termsAndConditions();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/migo/application-checkout")
    public ResponseEntity<GenericResponseDTO> applicationCheckout(@Valid @RequestBody MigoSubmitApplicationRequestDTO migoSubmitApplicationRequestDTO){
        GenericResponseDTO response = migoService.applicationCheckout(migoSubmitApplicationRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/migo/active-loan")
    public ResponseEntity<GenericResponseDTO> getActiveLoan(@Valid @RequestParam String clientNo){
        log.info("migo call back request payload ====> " + clientNo);
        HashMap<String, String> queryParam = new HashMap<>();
        queryParam.put("clientNo", clientNo);
        GenericResponseDTO response = migoService.getActiveLoan(queryParam);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/migo/post-payment")
    public ResponseEntity<GenericResponseDTO> postPayment(@Valid @RequestBody MigoPostPaymentsRequestDTO migoPostPaymentsRequestDTO){
        log.info("Migo Post Payments Request Payload ====> " + migoPostPaymentsRequestDTO);
        GenericResponseDTO response = migoService.postPayment(migoPostPaymentsRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
