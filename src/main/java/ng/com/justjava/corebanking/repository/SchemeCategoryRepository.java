package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.SchemeCategory;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SchemeCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchemeCategoryRepository extends JpaRepository<SchemeCategory, Long> {
}
