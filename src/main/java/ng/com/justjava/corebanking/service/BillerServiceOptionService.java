package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.BillerPlatform;
import ng.com.justjava.corebanking.domain.BillerServiceOption;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link BillerPlatform}.
 */
public interface BillerServiceOptionService {

    /**
     * Save a billerPlatform.
     *
     * @param billerPlatformDTO the entity to save.
     * @return the persisted entity.
     */
    BillerServiceOption save(BillerServiceOption BillerServiceOption);

    /**
     * Get all the billerPlatforms.
     *
     * @return the list of entities.
     */
    List<BillerServiceOption> findAll();


    /**
     * Get the "id" billerPlatform.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillerServiceOption> findOne(Long id);

    /**
     * Delete the "id" billerPlatform.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<BillerServiceOption> findAllByBillerCustomFieldOptionId(long billerPlatformId);

    List<BillerServiceOption> findByServiceOptionId(Long serviceOptionId);


}
