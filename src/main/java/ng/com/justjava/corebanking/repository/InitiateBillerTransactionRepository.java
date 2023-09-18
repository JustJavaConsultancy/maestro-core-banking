package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.InitiateBillerTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InitiateBillerTransactionRepository extends JpaRepository<InitiateBillerTransactionEntity, Long> {

    InitiateBillerTransactionEntity findByUserIdAndRecurringId(Long userId, Long id);
}
