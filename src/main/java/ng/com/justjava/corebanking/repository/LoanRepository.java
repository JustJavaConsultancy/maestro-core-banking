package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.Loan;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.enumeration.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Loan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAllByProfileIdOrderByIdDesc(Long profileId);

    List<Loan> findAllByProfileOrderByIdDesc(Profile profile);

    Optional<Loan> findByLoanId(String loanId);

    List<Loan> findLoanByCustomerIdAndStatusOrderByCreatedDateDesc(String customerId, LoanStatus status);

    Optional<Loan> findByStatusAndBorrowerIdAndCustomerId(LoanStatus status, String borrowerId, String customerId);

}
