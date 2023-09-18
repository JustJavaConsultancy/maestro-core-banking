package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.IbilePaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the IbilePaymentDetails entity.
 */
@Repository
public interface IbileRepository extends JpaRepository<IbilePaymentDetails, Long> {

    @Override
    Optional<IbilePaymentDetails> findById(Long aLong);

    Optional<IbilePaymentDetails> findOneByBillReferenceNumber(String billReferenceNumber);
}
