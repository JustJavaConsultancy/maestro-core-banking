package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.SweepingConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SweepingConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SweepingConfigRepository extends JpaRepository<SweepingConfig, Long> {
}
