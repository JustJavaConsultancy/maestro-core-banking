package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.Lender;
import ng.com.systemspecs.apigateway.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Lender entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LenderRepository extends JpaRepository<Lender, Long> {
    Optional<Lender> findByProfilePhoneNumber(String profile_phoneNumber);

    Optional<Lender> findByProfileUser(User user);

    Optional<Lender> findById(Long id);
}
