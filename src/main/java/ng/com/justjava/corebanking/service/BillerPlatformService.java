package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.BillerPlatformDTO;
import ng.com.justjava.corebanking.domain.Biller;
import ng.com.justjava.corebanking.domain.BillerPlatform;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link BillerPlatform}.
 */
public interface BillerPlatformService {

    /**
     * Save a billerPlatform.
     *
     * @param billerPlatformDTO the entity to save.
     * @return the persisted entity.
     */
    BillerPlatformDTO save(BillerPlatformDTO billerPlatformDTO);

    /**
     * Get all the billerPlatforms.
     *
     * @return the list of entities.
     */
    List<BillerPlatformDTO> findAll();

    /**
     * Get the "id" billerPlatform.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillerPlatformDTO> findOne(Long id);

    /**
     * Delete the "id" billerPlatform.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<BillerPlatform> findAllByBiller(Biller biller);

    Optional<BillerPlatform> findByBillerplatformID(Long billerPlatformID);

    List<BillerPlatform> findBillerPlatformsByBiller(Biller biller);
}
