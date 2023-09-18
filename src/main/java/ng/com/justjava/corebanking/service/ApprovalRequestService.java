package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.ApprovalRequestDTO;
import ng.com.justjava.corebanking.service.dto.ApprovalRequestDetailsDTO;
import ng.com.justjava.corebanking.service.dto.ApproveRequestDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.domain.ApprovalGroup;
import ng.com.justjava.corebanking.domain.ApprovalRequest;
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
