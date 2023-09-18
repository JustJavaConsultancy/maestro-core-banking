package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.InsuranceProvider;
import ng.com.justjava.corebanking.service.dto.InsuranceProviderDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link InsuranceProvider}.
 */
public interface InsuranceProviderService {

    /**
     * Save a insuranceProvider.
     *
     * @param insuranceProviderDTO the entity to save.
     * @return the persisted entity.
     */
    InsuranceProviderDTO save(InsuranceProviderDTO insuranceProviderDTO);

    /**
     * Get all the insuranceProviders.
     *
     * @return the list of entities.
     */
    List<InsuranceProviderDTO> findAll();


    /**
     * Get the "id" insuranceProvider.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InsuranceProviderDTO> findOne(Long id);

    /**
     * Delete the "id" insuranceProvider.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
