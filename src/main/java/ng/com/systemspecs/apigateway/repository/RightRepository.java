package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.AccessRight;
import ng.com.systemspecs.apigateway.domain.Right;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Right entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RightRepository extends JpaRepository<Right, Long> {

    Optional<Right> findByCode(String rightCode);
}
