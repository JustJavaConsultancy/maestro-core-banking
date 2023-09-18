package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.WalletAccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the WalletAccountType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalletAccountTypeRepository extends JpaRepository<WalletAccountType, Long> {

    Optional<WalletAccountType> findByAccountypeID(Long accountypeID);
}
