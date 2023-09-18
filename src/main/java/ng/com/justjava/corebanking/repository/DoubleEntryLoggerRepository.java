package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.DoubleEntryLogger;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the DoubleEntryLogger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoubleEntryLoggerRepository extends JpaRepository<DoubleEntryLogger, Long> {
}
