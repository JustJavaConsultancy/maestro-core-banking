package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.service.LibertyAssuredService;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class LibertyAssuredResource {

    private final Logger log = LoggerFactory.getLogger(LenderResource.class);
    private final LibertyAssuredService libertyAssuredService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;


    public LibertyAssuredResource(LibertyAssuredService libertyAssuredService) {
        this.libertyAssuredService = libertyAssuredService;
    }

    @PostMapping("/borrowers")
    public ResponseEntity<GenericResponseDTO> createBorrower(@Valid @RequestBody CreateBorrowerDTO createBorrowerDTO) throws URISyntaxException {
        log.debug("REST request to create a borrower : {}", createBorrowerDTO);
        if (createBorrowerDTO.getBorrowerId() != null) {
            throw new BadRequestAlertException("A new borrower cannot already have an ID", "Borrower", "idexists");
        }
        return libertyAssuredService.createBorrower(createBorrowerDTO);
    }

    @PutMapping("/borrowers")
    public ResponseEntity<GenericResponseDTO> editBorrower(@Valid @RequestBody CreateBorrowerDTO createBorrowerDTO) throws URISyntaxException {
        log.debug("REST request to edit a borrower : {}", createBorrowerDTO);
        if (createBorrowerDTO.getBorrowerId() == null) {
            throw new BadRequestAlertException("Invalid id", "Borrower", "idnull");
        }
        return libertyAssuredService.editBorrower(createBorrowerDTO);
    }

    @GetMapping("/borrowers")
    public ResponseEntity<GenericResponseDTO> getBorrower(@Valid @RequestBody BorrowerDetailsDTO borrowerDetailsDTO) throws URISyntaxException {
        log.debug("REST request to get a borrower details : {}", borrowerDetailsDTO);
        if (borrowerDetailsDTO.getBorrowerId() == null) {
            throw new BadRequestAlertException("Invalid id", "Borrower", "idnull");
        }
        return libertyAssuredService.getBorrowerDetails(borrowerDetailsDTO);
    }

    @PostMapping("/borrowers/notifications")
    public ResponseEntity<GenericResponseDTO> borrowerNotifications(@RequestParam("branch_id") String branchId,
                                                                    @RequestParam("type") String type,
                                                                    @RequestParam("records") String... records) throws URISyntaxException {
        log.debug("REST request to notify about a borrower : {}", branchId, type, records);

        return libertyAssuredService.borrowerNotifications(branchId, type, records);
    }

    @PostMapping("/loans/create")
    public ResponseEntity<GenericResponseDTO> createLoan(@Valid @RequestBody CreateLoanDTO createLoanDTO) throws URISyntaxException {
        log.debug("REST request to create a loan : {}", createLoanDTO);
        if (createLoanDTO.getLoanId() != null) {
            throw new BadRequestAlertException("A new loan cannot already have an ID", "Loan", "idexists");
        }

        return libertyAssuredService.createLoan(createLoanDTO);
    }

    @PutMapping("/loans/edit")
    public ResponseEntity<GenericResponseDTO> editLoans(@Valid @RequestBody CreateLoanDTO createLoanDTO) throws URISyntaxException {
        log.debug("REST request to edit a loan : {}", createLoanDTO);
        if (createLoanDTO.getBorrowerId() == null) {
            throw new BadRequestAlertException("Invalid id", "Borrower", "idnull");
        }
        return libertyAssuredService.editLoan(createLoanDTO);
    }

    @GetMapping("/loans/get")
    public ResponseEntity<GenericResponseDTO> getLoan(@Valid @RequestBody LoanDetailsDTO loanDetailsDTO) throws URISyntaxException {
        log.debug("REST request to get a loan details : {}", loanDetailsDTO);
        if (loanDetailsDTO.getLoanId() == null) {
            throw new BadRequestAlertException("Invalid id", "Borrower", "idnull");
        }
        return libertyAssuredService.getLoanDetails(loanDetailsDTO);
    }

    @PostMapping("/loans/notifications")
    public ResponseEntity<GenericResponseDTO> loanNotifications(@RequestParam("branch_id") String branchId,
                                                                @RequestParam("type") String type,
                                                                @RequestParam("records") String... records) throws URISyntaxException {
        log.debug("REST request to notify about a loan : {}", branchId, type, records);

        return libertyAssuredService.loanNotifications(branchId, type, records);
    }

    @PostMapping("liberty/eligible")
    public ResponseEntity<GenericResponseDTO> eligibility(@RequestBody EligibilityRequestDTO eligibilityRequestDTO) {
        log.debug("REST request to get loan eligibility: {}", eligibilityRequestDTO);

        return libertyAssuredService.eligibility(eligibilityRequestDTO);
    }

    @PostMapping("liberty/request")
    public ResponseEntity<GenericResponseDTO> requestLoan(@RequestBody RequestLoanOfferRequestDTO requestLoanOfferRequestDTO) {
        log.debug("REST request to get loan eligibility: {}", requestLoanOfferRequestDTO);

        return libertyAssuredService.requestLoanOffer(requestLoanOfferRequestDTO);
    }

    @PostMapping("liberty/calculate")
    public ResponseEntity<GenericResponseDTO> calculateLoan(@RequestBody CalculateRepaymentRequestDTO calculateRepaymentRequestDTO) {
        log.debug("REST request to get loan eligibility: {}", calculateRepaymentRequestDTO);

        return libertyAssuredService.loanCalculation(calculateRepaymentRequestDTO);
    }

    @PostMapping("liberty/approval")
    public ResponseEntity<GenericResponseDTO> approveLoan(@RequestBody ApproveLoanRequestDTO approveLoanRequestDTO) {
        log.debug("REST request to get loan approval: {}", approveLoanRequestDTO);

        return libertyAssuredService.approveLoan(approveLoanRequestDTO);
    }

    @PostMapping("liberty/disbursement")
    public ResponseEntity<GenericResponseDTO> disburseLoan(@RequestBody LoanDisbursementRequestDTO loanDisbursementRequestDTO) {
        log.debug("REST request for loan disbursement: {}", loanDisbursementRequestDTO);
        return libertyAssuredService.disburseLoan(loanDisbursementRequestDTO);
    }

    @PostMapping("liberty/repayment")
    public ResponseEntity<GenericResponseDTO> repayment(@RequestBody LoanRepaymentDTO loanRepaymentDTO) {
        log.debug("REST request for loan repayment: {}", loanRepaymentDTO);
        return libertyAssuredService.loanRepayment(loanRepaymentDTO);
    }

}
