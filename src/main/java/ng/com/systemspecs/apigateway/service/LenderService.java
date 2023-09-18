package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.Lender;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.service.dto.CreateLenderDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.LenderDTO;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.Lender}.
 */
public interface LenderService {

    /**
     * Save a lender.
     *
     * @param lenderDTO the entity to save.
     * @return the persisted entity.
     */
    LenderDTO save(LenderDTO lenderDTO);

    /**
     * Save a lender.
     *
     * @param lenderDTO the entity to save.
     * @param profile   the entity to save.
     * @return the persisted entity.
     */
    LenderDTO save(LenderDTO lenderDTO, Profile profile);

    /**
     * Save a lender.
     *
     * @param lender the entity to save.
     * @return the persisted entity.
     */
    Lender save(Lender lender);

    /**
     * Get all the lenders.
     *
     * @return the list of entities.
     */
    List<LenderDTO> findAll();


    /**
     * Get the "id" lender.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LenderDTO> findOne(Long id);

    /**
     * Delete the "id" lender.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<Lender> findByProfilePhoneNumber(String profile_phoneNumber);

    GenericResponseDTO createLender(@Valid CreateLenderDTO lenderDTO, HttpSession session);

    LenderDTO updateLender(LenderDTO lenderDTO);

    Optional<LenderDTO> getLenderByPhoneNumber(String phoneNumber);

    Optional<LenderDTO> getLenderById(Long LenderId);
}
