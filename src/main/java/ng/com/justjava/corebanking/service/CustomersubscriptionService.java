package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.Customersubscription;
import ng.com.justjava.corebanking.service.dto.CustomersubscriptionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Customersubscription}.
 */
public interface CustomersubscriptionService {

    /**
     * Save a customersubscription.
     *
     * @param customersubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    CustomersubscriptionDTO save(CustomersubscriptionDTO customersubscriptionDTO);

    /**
     * Get all the customersubscriptions.
     *
     * @return the list of entities.
     */
    List<CustomersubscriptionDTO> findAll();


    /**
     * Get the "id" customersubscription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomersubscriptionDTO> findOne(Long id);

    /**
     * Delete the "id" customersubscription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
