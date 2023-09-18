package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.CustomFieldDropdown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Address entity.
 */

@SuppressWarnings("unused")
@Repository
public interface CustomFieldDropdownRepository extends JpaRepository<CustomFieldDropdown, Long> {
    Optional<CustomFieldDropdown> findByFieldDropdownId(String fieldId);
}
