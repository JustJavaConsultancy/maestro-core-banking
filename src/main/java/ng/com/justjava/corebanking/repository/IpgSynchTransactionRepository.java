package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.IpgSynchTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IpgSynchTransactionRepository extends JpaRepository<IpgSynchTransaction, Long> {

    List<IpgSynchTransaction> findByTransactionRef(String transactionRef);

    List<IpgSynchTransaction> findByStatus(String status);



}
