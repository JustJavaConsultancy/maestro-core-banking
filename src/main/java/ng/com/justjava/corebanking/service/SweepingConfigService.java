package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.SweepingConfig;
import ng.com.justjava.corebanking.service.dto.SweepingConfigDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link SweepingConfig}.
 */
public interface SweepingConfigService {

    /**
     * Save a sweepingConfig.
     *
     * @param sweepingConfigDTO the entity to save.
     * @return the persisted entity.
     */
    SweepingConfigDTO save(SweepingConfigDTO sweepingConfigDTO);

    /**
     * Get all the sweepingConfigs.
     *
     * @return the list of entities.
     */
    List<SweepingConfigDTO> findAll();


    /**
     * Get the "id" sweepingConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SweepingConfigDTO> findOne(Long id);

    /**
     * Delete the "id" sweepingConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
