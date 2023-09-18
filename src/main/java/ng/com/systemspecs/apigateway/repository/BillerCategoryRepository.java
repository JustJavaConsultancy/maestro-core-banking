package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.BillerCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the BillerCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillerCategoryRepository extends JpaRepository<BillerCategory, Long> {
    Optional<BillerCategory> findByBillerCategory(String billerCategory);
}
