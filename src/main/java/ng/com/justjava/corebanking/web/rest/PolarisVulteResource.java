package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.JournalLine;
import ng.com.justjava.corebanking.service.PolarisVulteService;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.PolarisVulteFundTransferDTO;
import ng.com.justjava.corebanking.service.dto.PolarisVulteOpenAccountDTO;
import ng.com.justjava.corebanking.service.dto.PolarisVulteWebHookRequest;
import ng.com.justjava.corebanking.service.dto.ValidateBVNDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * REST controller for managing {@link JournalLine Functions of the Application}.
 */
@RestController
@RequestMapping("/api")
public class PolarisVulteResource {

    private final Logger log = LoggerFactory.getLogger(PolarisVulteResource.class);
    private final PolarisVulteService polarisVulteService;

    public PolarisVulteResource(PolarisVulteService polarisVulteService) {
        this.polarisVulteService = polarisVulteService;
    }

    @GetMapping("/vulte/{accountNumber}/{schemeId}")
    public ResponseEntity<GenericResponseDTO> getBalance(@PathVariable String accountNumber,
                                                         @PathVariable String schemeId) throws Exception {
        log.debug("REST request to get account balance : {}", accountNumber);
        GenericResponseDTO response = polarisVulteService.getBalance(accountNumber, schemeId);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/vulte/statement/{accountNumber}/{startDate}/{endDate}/{schemeId}")
    public ResponseEntity<GenericResponseDTO> getStatements(@PathVariable String accountNumber,
                                                            @PathVariable String startDate,
                                                            @PathVariable String endDate,
                                                            @PathVariable String schemeId) throws Exception {

        log.debug("REST request to get account statement {}", accountNumber);
        GenericResponseDTO response = polarisVulteService.getStatement(accountNumber, startDate, endDate, schemeId);
        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/transfer")
    public ResponseEntity<GenericResponseDTO> fundTransfer(@RequestBody PolarisVulteFundTransferDTO request) throws Exception {
        log.debug("REST request perform fund transfer :" + request);
        GenericResponseDTO response = polarisVulteService.fundTransfer(request);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/disburse")
    public ResponseEntity<GenericResponseDTO>
    disburse(@RequestBody PolarisVulteFundTransferDTO request) throws Exception {
        log.debug("REST request to disburse fund : {}", "");
        GenericResponseDTO response = polarisVulteService.disburse(request);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/collect")
    public ResponseEntity<GenericResponseDTO> collect(@RequestBody PolarisVulteFundTransferDTO request) throws Exception {
        log.debug("REST request to get PaymentTransaction : {}", request);

        GenericResponseDTO response = polarisVulteService.collect(request);
        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/bvn/lookup/min/{schemeId}")
    public ResponseEntity<GenericResponseDTO> bvnLookupMin(@PathVariable String schemeId,
                                                           @RequestBody ValidateBVNDTO request) throws Exception {
        log.debug("REST request to lookup bvn min : {}, {}", request, schemeId);

        GenericResponseDTO response = polarisVulteService.bvnLookupMin(request, schemeId);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/bvn/lookup/mid/{schemeId}")
    public ResponseEntity<GenericResponseDTO> bvnLookupMid(@PathVariable String schemeId,
                                                           @RequestBody ValidateBVNDTO request) throws Exception {
        log.debug("REST request to lookup bvn mid : {}", request);

        GenericResponseDTO response = polarisVulteService.bvnLookupMid(request, schemeId);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/bvn/lookup/max/{schemeId}")
    public ResponseEntity<GenericResponseDTO> bvnLookupMax(@PathVariable String schemeId,
                                                           @RequestBody ValidateBVNDTO request) throws Exception {
        log.debug("REST request to lookup bvn max : {}", request);

        GenericResponseDTO response = polarisVulteService.bvnLookupMax(request, schemeId);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/account/lookup/max")
    public ResponseEntity<GenericResponseDTO> lookupAccountMax(@RequestBody PolarisVulteFundTransferDTO request) throws Exception {
        log.debug("REST request to lookup account max : {}", request);

        GenericResponseDTO response = polarisVulteService.lookupAccountMax(request);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/account/lookup/mid")
    public ResponseEntity<GenericResponseDTO> lookupAccountMid(@RequestBody PolarisVulteFundTransferDTO request) throws Exception {
        log.debug("REST request to lookup account mid : {}", request);

        GenericResponseDTO response = polarisVulteService.lookupAccountMid(request);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/account/lookup/min")
    public ResponseEntity<GenericResponseDTO> lookupAccountMin(@RequestBody PolarisVulteFundTransferDTO request) throws Exception {
        log.debug("REST request to lookup account min : {}", request);

        GenericResponseDTO response = polarisVulteService.lookupAccountMin(request);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/accounts/min")
    public ResponseEntity<GenericResponseDTO> getAccountsMin(@RequestBody PolarisVulteFundTransferDTO request) throws Exception {
        log.debug("REST request to get accounts min : {}", request);

        GenericResponseDTO response = polarisVulteService.getAccountsMin(request);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/accounts/mid")
    public ResponseEntity<GenericResponseDTO> getAccountsMid(@RequestBody PolarisVulteFundTransferDTO request) throws Exception {
        log.debug("REST request to get accounts mid : {}", request);

        GenericResponseDTO response = polarisVulteService.getAccountsMid(request);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/accounts/max")
    public ResponseEntity<GenericResponseDTO> getAccountsMax(@RequestBody PolarisVulteFundTransferDTO request) throws Exception {
        log.debug("REST request to get accounts max : {}", request);

        GenericResponseDTO response = polarisVulteService.getAccountsMax(request);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/nuban/lookup")
    public ResponseEntity<GenericResponseDTO> lookUpNuban(@RequestBody PolarisVulteFundTransferDTO request) throws Exception {
        log.debug("REST request to lookup nuban : {}", request);

        GenericResponseDTO response = polarisVulteService.lookUpNuban(request);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/vulte/banks/{schemeId}")
    public ResponseEntity<GenericResponseDTO> getBanks(@PathVariable String schemeId, HttpSession session) throws Exception {
        log.debug("REST request to get list of banks : {}", schemeId);

        GenericResponseDTO response = polarisVulteService.getBanks(schemeId, session);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/vulte/branch/{schemeId}")
    public ResponseEntity<GenericResponseDTO> getBranch(@PathVariable String schemeId, HttpSession session) throws Exception {
        log.debug("REST request to get list of banks : {}", schemeId);

        GenericResponseDTO response = polarisVulteService.getBranch(schemeId, session);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/account/open")
    public ResponseEntity<GenericResponseDTO> openAccount(@RequestBody PolarisVulteOpenAccountDTO request) throws Exception {
        log.debug("REST request to open an account  : {}", request);

        GenericResponseDTO response = polarisVulteService.openAccount(request);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/account/virtual/open")
    public ResponseEntity<GenericResponseDTO> openVirtualAccount(@RequestBody PolarisVulteOpenAccountDTO request) throws Exception {
        log.debug("REST request to open virtual account  : {}", request);

        GenericResponseDTO response = polarisVulteService.openVirtualAccount(request);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/wallet/open")
    public ResponseEntity<GenericResponseDTO> openWallet(@RequestBody PolarisVulteOpenAccountDTO request) throws Exception {
        log.debug("REST request to open virtual account : {}", request);

        GenericResponseDTO response = polarisVulteService.openWallet(request);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/webhook")
    public ResponseEntity<GenericResponseDTO> webHook(@RequestBody PolarisVulteWebHookRequest request) throws Exception {
        log.debug("REST request to process notification : {}", request);

        GenericResponseDTO response = polarisVulteService.webHook(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/vulte/validate/{otp}/{requestType}/{transactionRef}/{schemeId}")
    public ResponseEntity<GenericResponseDTO> validateTransaction(
        @PathVariable String otp,
        @PathVariable String requestType,
        @PathVariable String transactionRef,
        @PathVariable String schemeId
    ) throws Exception {
        log.debug("REST request to validate otp : {}", otp, requestType, transactionRef);

        GenericResponseDTO response = polarisVulteService.validateTransaction(otp, requestType, transactionRef, schemeId);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/vulte/validate/{requestType}/{transactionRef}/{schemeId}")
    public ResponseEntity<GenericResponseDTO> queryTransaction(
        @PathVariable String requestType,
        @PathVariable String transactionRef,
        @PathVariable String schemeId
    ) throws Exception {
        log.debug("REST request to query transaction : {}", requestType, transactionRef);

        GenericResponseDTO response = polarisVulteService.queryTransaction(requestType, transactionRef, schemeId);

        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }

        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }
}
