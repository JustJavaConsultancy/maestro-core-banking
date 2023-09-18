package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.Customersubscription;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Customersubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomersubscriptionRepository extends JpaRepository<Customersubscription, Long> {
}
