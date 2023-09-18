package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.ApprovalGroup;
import ng.com.systemspecs.apigateway.domain.Right;
import ng.com.systemspecs.apigateway.service.dto.ApprovalGroupDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.ApprovalGroup}.
 */
public interface ApprovalGroupService {

    /**
     * Save a approvalGroup.
     *
     * @param approvalGroupDTO the entity to save.
     * @return the persisted entity.
     */
    ApprovalGroupDTO save(ApprovalGroupDTO approvalGroupDTO);

    /**
     * Get all the approvalGroups.
     *
     * @return the list of entities.
     */
    List<ApprovalGroupDTO> findAll();


    /**
     * Get the "id" approvalGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApprovalGroupDTO> findOne(Long id);

    /**
     * Delete the "id" approvalGroup.
     *
     * @param id the id of the entity.
     */
    String delete(Long id);


    ApprovalGroup removeApprovalGroupProfiles(ApprovalGroup approvalGroup);

    ApprovalGroup findByName(String name);

    List<Right> getRights(String phoneNumber);
}
