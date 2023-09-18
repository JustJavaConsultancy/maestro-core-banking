package ng.com.systemspecs.apigateway.web.rest;

import ng.com.systemspecs.apigateway.service.CashConnectService;
import ng.com.systemspecs.apigateway.service.dto.AcceptLoanDTO;
import ng.com.systemspecs.apigateway.service.dto.CashConnectAccountRequestDTO;
import ng.com.systemspecs.apigateway.service.dto.CashConnectWebHookDTO;
import ng.com.systemspecs.apigateway.service.dto.FundIntraDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.InterBankCallBackDTO;
import ng.com.systemspecs.apigateway.service.dto.LoanOfferDTO;
import ng.com.systemspecs.apigateway.service.dto.LoanPaymentNotificationDTO;
import ng.com.systemspecs.apigateway.service.dto.RegisterWebHookDTO;
import ng.com.systemspecs.apigateway.service.dto.SimpleResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.UpgradeTierDTO;
import ng.com.systemspecs.apigateway.service.dto.UpgradeTierKycDTO;
import ng.com.systemspecs.apigateway.service.dto.ValidateBVNDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

/**
 * REST controller for managing {@link ng.com.systemspecs.apigateway.domain.Address}.
 */
@RestController
@RequestMapping("/api")
public class CashConnectResource {

    private final Logger log = LoggerFactory.getLogger(CashConnectResource.class);
    private final CashConnectService cashConnectService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public CashConnectResource(CashConnectService cashConnectService) {
        this.cashConnectService = cashConnectService;
    }

    @PostMapping("/cash-connect/bvn/{bvnNumber}")
    public ResponseEntity<GenericResponseDTO> getBvnDetails(@PathVariable String bvnNumber, HttpSession session) {
        log.debug("REST request to get bvn details : {}", bvnNumber);
        return cashConnectService.getBvn(bvnNumber, session);
    }

    @PostMapping("/cash-connect/cashConnect/interbank")
    public ResponseEntity<SimpleResponseDTO> interbankCallBack(@RequestBody InterBankCallBackDTO interBankCallBackDTO) {
        log.debug("Interbank transfer notification callabck : {}", interBankCallBackDTO);
        return cashConnectService.interbankCallBack(interBankCallBackDTO);
    }

    @PostMapping("/cash-connect/bvn/validate")
    public ResponseEntity<GenericResponseDTO> validateBVN(@RequestBody ValidateBVNDTO validateBVNDTO) {
        log.debug("REST request to validate bvn details : {}", validateBVNDTO);
        return cashConnectService.validateBVN(validateBVNDTO);
    }

    @PostMapping("/cash-connect/account/new")
    public ResponseEntity<GenericResponseDTO> createNewAccount(@RequestBody CashConnectAccountRequestDTO requestDTO) {
        log.debug("REST request to create new account : {}", requestDTO);
        return cashConnectService.CreateNewAccount(requestDTO);
    }

    @PostMapping("/cash-connect/account/kyc/upload")
    public ResponseEntity<GenericResponseDTO> upgradeAccountTier(@RequestBody UpgradeTierKycDTO requestDTO) {
        log.debug("REST request to upgrade an account kyc upload : {}", requestDTO);
        return cashConnectService.upgradeAccountKyc(requestDTO);
    }

    @PostMapping("/cash-connect/account/upgrade")
    public ResponseEntity<GenericResponseDTO> upgradeAccountKYC(@RequestBody UpgradeTierDTO requestDTO) {
        log.debug("REST request to upgrade an account tier : {}", requestDTO);
        return cashConnectService.upgradeAccountTier(requestDTO);
    }

    @GetMapping("/cash-connect/account/statement")
    public ResponseEntity<GenericResponseDTO> getAccountStatement(@RequestParam String accountNumber, String fromDate, String toDate) {
        log.debug("REST request to get a particular account statements: {}", accountNumber, fromDate, toDate);
        return cashConnectService.getAccountStatement(accountNumber, fromDate, toDate);
    }

    @GetMapping("/cash-connect/account/number")
    public ResponseEntity<GenericResponseDTO> retrieveAccountNumber(@RequestParam String TransactionTrackingRef) {
        log.debug("REST request to get a particular account number: {}", TransactionTrackingRef);
        return cashConnectService.retrieveAccountNumber(TransactionTrackingRef);
    }

    @GetMapping("/cash-connect/account/summary")
    public ResponseEntity<GenericResponseDTO> getAccountSummary(@RequestParam String accountNumber) {
        log.debug("REST request to get a particular account summary: {}", accountNumber);
        return cashConnectService.getAccountSummary(accountNumber);
    }

    @GetMapping("/cash-connect/account/info")
    public ResponseEntity<GenericResponseDTO> getAccountInfo(@RequestParam String accountNumber) {
        log.debug("REST request to get a particular account info: {}", accountNumber);
        return cashConnectService.getAccountInfo(accountNumber);
    }

    @GetMapping("/cash-connect/bank/list")
    public ResponseEntity<GenericResponseDTO> getBankList(@RequestParam Optional<Boolean> flag) {
        if(flag.isPresent()&&flag.get()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), headers,
                HttpStatus.OK);
        }
        return cashConnectService.getBankList();
    }

    @PostMapping("/cash-connect/fund/intra")
    public ResponseEntity<GenericResponseDTO> fundIntra(@RequestBody FundIntraDTO fundIntraDTO) {
        log.debug("REST request to Send intra fund: {}");
        return cashConnectService.fundIntra(fundIntraDTO);
    }

    @PostMapping("/cash-connect/fund/interbank")
    public ResponseEntity<GenericResponseDTO> fundInterBank(@RequestBody FundIntraDTO fundIntraDTO) {
        log.debug("REST request to Send intra fund: {}");
        return cashConnectService.fundInterBank(fundIntraDTO);
    }

    @GetMapping("/cash-connect/fund/status")
    public ResponseEntity<GenericResponseDTO> getFundTransferStatus(@RequestParam String TransactionTrackingRef) {
        log.debug("REST request to get a transaction status: {}", TransactionTrackingRef);
        return cashConnectService.getFundTransferStatus(TransactionTrackingRef);
    }

    @PostMapping("/cash-connect/loans/offer")
    public ResponseEntity<GenericResponseDTO> getLoanOffers(@RequestBody LoanOfferDTO loanOfferDTO) {
        log.debug("REST request to get list of loan offers: {}", loanOfferDTO);
        return cashConnectService.getLoanOffers(loanOfferDTO);
    }

    @PostMapping("/cash-connect/loans/accept")
    public ResponseEntity<GenericResponseDTO> acceptLoanOffer(@RequestBody AcceptLoanDTO acceptLoanDTO) {
        log.debug("REST request to accept loan offer: {}", acceptLoanDTO);
        return cashConnectService.acceptLoanOffer(acceptLoanDTO);
    }

    @PostMapping("/cash-connect/loans/payment/notification")
    public ResponseEntity<GenericResponseDTO> loanPaymentNotification(@RequestBody LoanPaymentNotificationDTO notificationDTO) {
        log.debug("REST request to receive loan payment notification: {}", notificationDTO);
        return cashConnectService.loanPaymentNotification(notificationDTO);
    }

    @GetMapping("/cash-connect/loans/credit/report")
    public ResponseEntity<GenericResponseDTO> getCreditReport(@RequestParam String bvn) {
        log.debug("REST request to get credit report: {}", bvn);
        return cashConnectService.getCreditReport(bvn);
    }

    @GetMapping("/cash-connect/loans/credit/report/phone")
    public ResponseEntity<GenericResponseDTO> getCreditReportByPhone(@RequestParam String PhoneNo) {
        log.debug("REST request to get a credit report: {}", PhoneNo);
        return cashConnectService.getCreditReportByPhone(PhoneNo);
    }

    @GetMapping("/cash-connect/loans/status")
    public ResponseEntity<GenericResponseDTO> getLoanStatus(@RequestParam String LoanId, String accountNumber, String PhoneNo) {
        log.debug("REST request to get a loan status: {}", LoanId, accountNumber, PhoneNo);
        return cashConnectService.getLoanStatus(LoanId, accountNumber, PhoneNo);
    }

    @PostMapping("/cash-connect/payment/notification")
    public ResponseEntity<GenericResponseDTO> transactionPaymentNotification(@Valid @RequestBody CashConnectWebHookDTO notificationDTO) {
        log.debug("REST request to get transaction payment notification: {}", notificationDTO);
        return cashConnectService.transactionPaymentNotification(notificationDTO);
    }

    @PostMapping("/cash-connect/register/webhook")
    public ResponseEntity<GenericResponseDTO> registerWebhook(@Valid @RequestBody RegisterWebHookDTO registerWebHook) {
        log.debug("REST request to register callback url with cash connect: {}", registerWebHook);
        return cashConnectService.registerWebhook(registerWebHook);
    }

    /*@PostMapping("/validate-bvn/{bvnNumber}")
    public ResponseEntity<GenericResponseDTO> getBvnDetails(@PathVariable String bvnNumber) {
        log.debug("REST request to get bvn details : {}", bvnNumber);
        return cashConnectService.getBvn(bvnNumber);
    }*/

}
