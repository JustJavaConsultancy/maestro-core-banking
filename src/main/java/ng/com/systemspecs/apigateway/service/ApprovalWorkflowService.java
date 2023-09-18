package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.ApprovalGroup;
import ng.com.systemspecs.apigateway.domain.ApprovalWorkflow;
import ng.com.systemspecs.apigateway.service.dto.ApprovalWorkflowDTO;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.ApprovalWorkflow}.
 */
public interface ApprovalWorkflowService {

    /**
     * Save a approvalWorkflow.
     *
     * @param approvalWorkflowDTO the entity to save.
     * @return the persisted entity.
     */
    ApprovalWorkflowDTO save(ApprovalWorkflowDTO approvalWorkflowDTO);

    /**
     * Get all the approvalWorkflows.
     *
     * @return the list of entities.
     */
    List<ApprovalWorkflowDTO> findAll();


    /**
     * Get the "id" approvalWorkflow.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApprovalWorkflowDTO> findOne(Long id);

    /**
     * Delete the "id" approvalWorkflow.
     *
     * @param id the id of the entity.
     * @return
     */
    String delete(Long id);

    ApprovalWorkflow findByTransactionType_code(String requestTypeCode);

    List<ApprovalWorkflow> findByApprover(@NotNull ApprovalGroup approver);

    List<ApprovalWorkflow> findByInitiator(@NotNull ApprovalGroup initiator);
}
