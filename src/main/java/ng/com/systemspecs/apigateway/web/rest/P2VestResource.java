package ng.com.systemspecs.apigateway.web.rest;

import ng.com.systemspecs.apigateway.service.P2VestService;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class P2VestResource {

    private final Logger log = LoggerFactory.getLogger(P2VestResource.class);
    private final P2VestService p2VestService;

    public P2VestResource(P2VestService p2VestService) {
        this.p2VestService = p2VestService;
    }

    @PostMapping("/p2vest/validate-offer")
    public ResponseEntity<GenericResponseDTO> validateOffer(@Valid @RequestBody P2VestValidateOfferRequestDTO p2VestValidateOfferRequestDTO){
        log.info("P2Vest Loan Validate Offer Request Payload ====> " + p2VestValidateOfferRequestDTO);
        GenericResponseDTO response = p2VestService.validateOffer(p2VestValidateOfferRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/p2vest/offer")
    public ResponseEntity<GenericResponseDTO> offer(@Valid @RequestBody P2VestOfferRequestDTO p2VestOfferRequestDTO){
        log.info("P2Vest Loan Offer Request Payload ====> " + p2VestOfferRequestDTO);
        GenericResponseDTO response = p2VestService.offer(p2VestOfferRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/p2vest/create-borrower")
    public ResponseEntity<GenericResponseDTO> createBorrower(@Valid @RequestBody P2VestCreateBorrowerRequestDTO p2VestCreateBorrowerRequestDTO){
        log.info("P2Vest Loan Create Borrower Request Payload ====> " + p2VestCreateBorrowerRequestDTO);
        GenericResponseDTO response = p2VestService.createBorrower(p2VestCreateBorrowerRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/p2vest/create-loan")
    public ResponseEntity<GenericResponseDTO> createLoan(@Valid @RequestBody P2VestCreateLoanDTO p2VestCreateLoanDTO){
        log.info("P2Vest Loan Create Loan Request Payload ====> " + p2VestCreateLoanDTO);
        GenericResponseDTO response = p2VestService.createLoan(p2VestCreateLoanDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
