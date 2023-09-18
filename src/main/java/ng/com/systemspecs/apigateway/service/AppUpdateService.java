package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.service.dto.AppUpdateDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.AppUpdate}.
 */
public interface AppUpdateService {

    /**
     * Save a appUpdate.
     *
     * @param appUpdateDTO the entity to save.
     * @return the persisted entity.
     */
    AppUpdateDTO save(AppUpdateDTO appUpdateDTO);

    /**
     * Get all the appUpdates.
     *
     * @return the list of entities.
     */
    List<AppUpdateDTO> findAll();
    
    AppUpdateDTO getLatestUpdate();


    /**
     * Get the "id" appUpdate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppUpdateDTO> findOne(Long id);

    /**
     * Delete the "id" appUpdate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
