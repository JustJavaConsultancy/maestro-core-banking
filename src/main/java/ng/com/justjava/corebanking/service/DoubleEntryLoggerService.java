package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.DoubleEntryLogger;
import ng.com.justjava.corebanking.service.dto.DoubleEntryLoggerDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link DoubleEntryLogger}.
 */
public interface DoubleEntryLoggerService {

    /**
     * Save a doubleEntryLogger.
     *
     * @param doubleEntryLoggerDTO the entity to save.
     * @return the persisted entity.
     */
    DoubleEntryLoggerDTO save(DoubleEntryLoggerDTO doubleEntryLoggerDTO);

    /**
     * Get all the doubleEntryLoggers.
     *
     * @return the list of entities.
     */
    List<DoubleEntryLoggerDTO> findAll();


    /**
     * Get the "id" doubleEntryLogger.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DoubleEntryLoggerDTO> findOne(Long id);

    /**
     * Delete the "id" doubleEntryLogger.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
