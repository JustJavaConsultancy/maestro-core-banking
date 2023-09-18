package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.BonusPointDTO;
import ng.com.justjava.corebanking.service.dto.FundDTO;
import ng.com.justjava.corebanking.domain.BonusPoint;
import ng.com.justjava.corebanking.domain.Journal;
import ng.com.justjava.corebanking.domain.Profile;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link BonusPoint}.
 */
public interface BonusPointService {

    /**
     * Save a bonusPoint.
     *
     * @param bonusPointDTO the entity to save.
     * @return the persisted entity.
     */
    BonusPointDTO save(BonusPointDTO bonusPointDTO);

    BonusPointDTO save(BonusPoint bonusPoint);

    /**
     * Get all the bonusPoints.
     *
     * @return the list of entities.
     */
    List<BonusPointDTO> findAll();


    /**
     * Get the "id" bonusPoint.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BonusPointDTO> findOne(Long id);

    /**
     * Delete the "id" bonusPoint.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<BonusPointDTO> getCustomerBonusPoints(String phoneNumber);

    void calculateBonusPoint(Journal journal, Profile profile, FundDTO fundDTO);
}
