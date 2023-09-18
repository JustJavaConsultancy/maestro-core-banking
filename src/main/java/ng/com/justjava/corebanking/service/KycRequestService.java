package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.KycRequestDTO;
import ng.com.justjava.corebanking.service.dto.KycRequestDecisionDTO;
import ng.com.justjava.corebanking.domain.KycRequest;
import ng.com.justjava.corebanking.domain.enumeration.KycRequestStatus;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link KycRequest}.
 */
public interface KycRequestService {

    /**
     * Save a kycRequest.
     *
     * @param kycRequest the entity to save.
     * @return the persisted entity.
     */
    KycRequest save(KycRequest kycRequest);

    /**
     * Save a kycRequest.
     *
     * @param kycRequestDTO the entity to save.
     * @return the persisted entity.
     */
    KycRequestDTO save(KycRequestDTO kycRequestDTO);

    /**
     * Get all the kycRequests.
     *
     * @return the list of entities.
     */
    List<KycRequestDTO> findAll();


    /**
     * Get the "id" kycRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<KycRequestDTO> findOne(Long id);

    /**
     * Delete the "id" kycRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    GenericResponseDTO approveKycRequest(KycRequestDecisionDTO reason);

    List<KycRequestDTO> findByStatus(KycRequestStatus status);
}
