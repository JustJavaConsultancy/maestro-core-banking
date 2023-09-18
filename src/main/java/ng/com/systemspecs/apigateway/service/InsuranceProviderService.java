package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.service.dto.InsuranceProviderDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.InsuranceProvider}.
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
