package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.SchemeCallBackDTO;
import ng.com.justjava.corebanking.service.dto.SchemeDTO;
import ng.com.justjava.corebanking.service.dto.UpdateKeysDTO;
import ng.com.justjava.corebanking.domain.Scheme;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Scheme}.
 */
public interface SchemeService {

    /**
     * Save a scheme.
     *
     * @param schemeDTO the entity to save.
     * @return the persisted entity.
     */
    SchemeDTO save(SchemeDTO schemeDTO);

    /**
     * Get all the schemes.
     *
     * @return the list of entities.
     */
    List<SchemeDTO> findAll();

    List<Scheme> findAllSchemes();


    /**
     * Get the "id" scheme.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SchemeDTO> findOne(Long id);

    Optional<Scheme> findSchemeId(Long id);

    /**
     * Delete the "id" scheme.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Scheme findBySchemeID(String schemeID);

    SchemeDTO createScheme(SchemeDTO schemeDTO);

    GenericResponseDTO getAdminScheme(String phoneNumber);

    GenericResponseDTO updateSchemeCallBack(SchemeCallBackDTO schemeCallBackDTO);

    GenericResponseDTO updateKeys(UpdateKeysDTO updateKeysDTO);

}
