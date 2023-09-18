package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.CountrolAccount;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CountrolAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountrolAccountRepository extends JpaRepository<CountrolAccount, Long> {
}
