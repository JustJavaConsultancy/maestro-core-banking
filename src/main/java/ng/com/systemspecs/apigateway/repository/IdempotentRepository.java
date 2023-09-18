package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.Idempotent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Kyclevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdempotentRepository extends JpaRepository<Idempotent, Long> {
    Optional<Idempotent> findByIdempotentKey(String idempotentKey);
}
