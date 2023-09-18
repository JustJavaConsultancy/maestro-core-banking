package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.justjava.corebanking.service.dto.RequestLoanOfferRequestDTO;
import org.springframework.http.ResponseEntity;

/**
 * Service Interface for managing {@link    }.
 */
public interface LibertyAssuredService {

    ResponseEntity<GenericResponseDTO> createBorrower(CreateBorrowerDTO createBorrowerDTO);

    ResponseEntity<GenericResponseDTO> editBorrower(CreateBorrowerDTO createBorrowerDTO);

    ResponseEntity<GenericResponseDTO> getBorrowerDetails(BorrowerDetailsDTO borrowerDetailsDTO);

    ResponseEntity<GenericResponseDTO> borrowerNotifications(String branchId, String type, String[] records);

    ResponseEntity<GenericResponseDTO> loanNotifications(String branchId, String type, String[] records);

    ResponseEntity<GenericResponseDTO> getLoanDetails(LoanDetailsDTO loanDetailsDTO);

    ResponseEntity<GenericResponseDTO> createLoan(CreateLoanDTO createLoanDTO);

    ResponseEntity<GenericResponseDTO> editLoan(CreateLoanDTO createLoanDTO);

    ResponseEntity<GenericResponseDTO> eligibility(EligibilityRequestDTO eligibilityRequestDTO);

    ResponseEntity<GenericResponseDTO> approveLoan(ApproveLoanRequestDTO approveLoanRequestDTO);

    ResponseEntity<GenericResponseDTO> disburseLoan(LoanDisbursementRequestDTO loanDisbursementRequestDTO);

    ResponseEntity<GenericResponseDTO> loanRepayment(LoanRepaymentDTO loanRepaymentDTO);

    ResponseEntity<GenericResponseDTO> requestLoanOffer(RequestLoanOfferRequestDTO requestLoanOfferRequestDTO);

    ResponseEntity<GenericResponseDTO> loanCalculation(CalculateRepaymentRequestDTO calculateRepaymentRequestDTO);
}
