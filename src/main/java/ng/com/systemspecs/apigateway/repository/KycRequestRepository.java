package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.KycRequest;
import ng.com.systemspecs.apigateway.domain.enumeration.KycRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the KycRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KycRequestRepository extends JpaRepository<KycRequest, Long> {
    List<KycRequest> findByStatus(KycRequestStatus status);

    Boolean existsByProfileIdAndStatus(Long profile_id, KycRequestStatus status);
}
