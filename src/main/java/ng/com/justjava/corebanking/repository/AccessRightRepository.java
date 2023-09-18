package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.AccessRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the AccessRight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessRightRepository extends JpaRepository<AccessRight, Long> {

    Optional<AccessRight> findByProfile_PhoneNumber(String phoneNumber);
}
