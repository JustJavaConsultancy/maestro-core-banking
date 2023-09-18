package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.ApprovalGroup;
import ng.com.systemspecs.apigateway.domain.ApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {

    List<ApprovalRequest> findAllByApprover(ApprovalGroup approver);

    Optional<ApprovalRequest> findAllByRequestRefAndRequestTypeCode(String requestRef, String requestTypeCode);

    Optional<ApprovalRequest> findByRequestRef(String requestRef);

    ApprovalRequest findAllByRequestId(String requestId);
}
