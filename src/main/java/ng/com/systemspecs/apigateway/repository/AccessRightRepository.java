package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.AccessRight;
import ng.com.systemspecs.apigateway.domain.Address;
import ng.com.systemspecs.apigateway.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the AccessRight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessRightRepository extends JpaRepository<AccessRight, Long> {

    Optional<AccessRight> findByProfile_PhoneNumber(String phoneNumber);
}
