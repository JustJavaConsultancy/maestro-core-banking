package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.Loan;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.enumeration.LoanStatus;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.LoanDTO;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.Loan}.
 */
public interface LoanService {

    /**
     * Save a loan.
     *
     * @param loanDTO the entity to save.
     * @return the persisted entity.
     */
    LoanDTO save(LoanDTO loanDTO);

    Loan save(Loan loan);

    /**
     * Get all the loans.
     *
     * @return the list of entities.
     */
    List<LoanDTO> findAll();

    GenericResponseDTO findAllByProfileId();

    List<LoanDTO> findAllByProfile(Profile profile);


    /**
     * Get the "id" loan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoanDTO> findOne(Long id);

    /**
     * Get the "id" loan.
     *
     * @param loanId the id of the entity.
     * @return the entity.
     */
    Optional<LoanDTO> findByLoanId(String loanId);

    /**
     * Get the "id" loan.
     *
     * @param loanId the id of the entity.
     * @return the entity.
     */
    Optional<Loan> findLoanByLoanId(String loanId);

    List<Loan> findLoanByCustomerIdAndStatus(String customerId, LoanStatus status);

    /**
     * Delete the "id" loan.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    GenericResponseDTO createLoan(LoanDTO loanDTO, HttpSession session);

    Optional<Loan> findByStatusAndBorrowerIdAndCustomerId(LoanStatus status, String borrowerId, String customerId);

}
