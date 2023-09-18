package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.InitiateBillerTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InitiateBillerTransactionRepository extends JpaRepository<InitiateBillerTransactionEntity, Long> {

    InitiateBillerTransactionEntity findByUserIdAndRecurringId(Long userId, Long id);
}
