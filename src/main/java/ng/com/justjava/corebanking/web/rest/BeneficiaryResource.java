package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.service.BeneficiaryService;
import ng.com.justjava.corebanking.service.dto.BeneficiaryDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.util.Utility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BeneficiaryResource {

    private final BeneficiaryService beneficiaryService;
    private final Utility utility;

    public BeneficiaryResource(BeneficiaryService beneficiaryService, Utility utility) {
        this.beneficiaryService = beneficiaryService;
        this.utility = utility;
    }

    @GetMapping("/beneficiary/{phoneNumber}")
    public ResponseEntity<GenericResponseDTO> getCustomerBeneficiaries(@PathVariable String phoneNumber) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        List<BeneficiaryDTO> customerBeneficiaries = beneficiaryService.findCustomerBeneficiaries(phoneNumber);
        return new ResponseEntity<>(new GenericResponseDTO("success", "success", customerBeneficiaries),
            HttpStatus.OK);
    }


    @DeleteMapping("/beneficiaries/{id}")
    public ResponseEntity<GenericResponseDTO> deleteBeneficiary(@PathVariable Long id) {
        beneficiaryService.delete(id);
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), HttpStatus.OK);
    }
}
