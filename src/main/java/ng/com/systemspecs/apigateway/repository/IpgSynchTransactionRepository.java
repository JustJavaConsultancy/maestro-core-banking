package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.IpgSynchTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IpgSynchTransactionRepository extends JpaRepository<IpgSynchTransaction, Long> {

    List<IpgSynchTransaction> findByTransactionRef(String transactionRef);

    List<IpgSynchTransaction> findByStatus(String status);



}
