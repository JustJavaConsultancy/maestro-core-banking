package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the PaymentTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
	List<PaymentTransaction> findBySourceAccount(String sourceAccount);
	List<PaymentTransaction> findByDestinationAccount(String destinationAccount);
	List<PaymentTransaction> findBySourceAccountName(String sourceAccountName);

    @Query("select p from PaymentTransaction p where p.transactionRef = ?1")
    PaymentTransaction findByTransactionRef(String transactionRef);
}
