package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.Journal;
import ng.com.justjava.corebanking.domain.enumeration.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JournalRepository extends JpaRepository<Journal, Long> {
    Optional<Journal> findByReferenceIgnoreCase(String reference);
	Journal findByReferenceIgnoreCase_(String reference);
    List<Journal> findAllByPaymentTypeOrderByTransDateDesc(PaymentType paymentType);
    List<Journal> findByExternalRef(String externalRef);


//    @Query(
//        value = "select  SUM(debit) as debit from Journal journal where journal.")
//    double findActualBalance(String accountNumber);

//    double findByTransactionStatus

}
