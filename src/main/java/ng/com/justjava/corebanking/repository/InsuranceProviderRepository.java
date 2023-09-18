package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.InsuranceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InsuranceProvider entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceProviderRepository extends JpaRepository<InsuranceProvider, Long> {
}
