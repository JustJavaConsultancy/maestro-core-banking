package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.ApprovalGroup;
import ng.com.justjava.corebanking.domain.ApprovalWorkflow;
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
