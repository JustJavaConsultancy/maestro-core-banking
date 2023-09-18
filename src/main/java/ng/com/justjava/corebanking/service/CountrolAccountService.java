package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.CountrolAccount;
import ng.com.justjava.corebanking.service.dto.CountrolAccountDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link CountrolAccount}.
 */
public interface CountrolAccountService {

    /**
     * Save a countrolAccount.
     *
     * @param countrolAccountDTO the entity to save.
     * @return the persisted entity.
     */
    CountrolAccountDTO save(CountrolAccountDTO countrolAccountDTO);

    /**
     * Get all the countrolAccounts.
     *
     * @return the list of entities.
     */
    List<CountrolAccountDTO> findAll();


    /**
     * Get the "id" countrolAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CountrolAccountDTO> findOne(Long id);

    /**
     * Delete the "id" countrolAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
