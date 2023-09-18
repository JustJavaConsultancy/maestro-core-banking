package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.ApprovalGroup;
import ng.com.systemspecs.apigateway.domain.ApprovalWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Spring Data  repository for the ApprovalWorkflow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApprovalWorkflowRepository extends JpaRepository<ApprovalWorkflow, Long> {
    ApprovalWorkflow findByTransactionType_code(String transactionType_code);

    List<ApprovalWorkflow> findByApprover(@NotNull ApprovalGroup approver);

    List<ApprovalWorkflow> findByInitiator(@NotNull ApprovalGroup initiator);

    ApprovalWorkflow findByName(String name);

}
