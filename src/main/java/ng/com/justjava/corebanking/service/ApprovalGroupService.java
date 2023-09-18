package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.ApprovalGroupDTO;
import ng.com.justjava.corebanking.domain.ApprovalGroup;
import ng.com.justjava.corebanking.domain.Right;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ApprovalGroup}.
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
