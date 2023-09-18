package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.KyclevelDTO;
import ng.com.justjava.corebanking.domain.Kyclevel;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Kyclevel}.
 */
public interface KyclevelService {

    /**
     * Save a kyclevel.
     *
     * @param kyclevelDTO the entity to save.
     * @return the persisted entity.
     */
    KyclevelDTO save(KyclevelDTO kyclevelDTO);

    /**
     * Get all the kyclevels.
     *
     * @return the list of entities.
     */
    List<KyclevelDTO> findAll();


    /**
     * Get the "id" kyclevel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<KyclevelDTO> findOne(Long id);
    Kyclevel findByKycLevel(Integer kycLevel);

    Kyclevel findByKyc(String kyc);

    /**
     * Delete the "id" kyclevel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
