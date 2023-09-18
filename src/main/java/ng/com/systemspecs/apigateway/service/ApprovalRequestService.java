package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.ApprovalGroup;
import ng.com.systemspecs.apigateway.domain.ApprovalRequest;
import ng.com.systemspecs.apigateway.service.dto.ApprovalRequestDTO;
import ng.com.systemspecs.apigateway.service.dto.ApprovalRequestDetailsDTO;
import ng.com.systemspecs.apigateway.service.dto.ApproveRequestDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ApprovalRequest}.
 */
public interface ApprovalRequestService {

    /**
     * Save a bank.
     *
     * @param approvalRequestDTO the entity to save.
     * @param initiator
     * @return the persisted entity.
     */

    ApprovalRequestDTO save(ApprovalRequestDTO approvalRequestDTO, ApprovalGroup approver, ApprovalGroup initiator);


    /**
     * Get all the requests.
     *
     * @return the list of entities.
     */
    List<ApprovalRequestDTO> findAll();

    /**
     * Get all the requests.
     *
     * @return the list of entities.
     */
    Page<ApprovalRequestDTO> findAll(Pageable pageable);


    /**
     * Get the "id" bank.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApprovalRequestDTO> findOne(Long id);

    /**
     * Delete the "id" bank.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    GenericResponseDTO createApprovalRequest(ApprovalRequestDTO approvalRequestDTO);

    List<ApprovalRequestDTO> getAllApprovalRequestByPhone(String phoneNumber);

    ApprovalRequestDetailsDTO getApprovalRequestDetails(String requestRef, String requestType);

    GenericResponseDTO approveRequest(ApproveRequestDTO approveRequestDTO);

    ApprovalRequest findByRequestId(String requestId);
}
