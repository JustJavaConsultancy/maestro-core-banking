package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.BillerTransaction;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the BillerTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillerTransactionRepository extends JpaRepository<BillerTransaction, Long> {
}
