package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.service.dto.ContactUsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.ContactUs}.
 */
public interface ContactUsService {

    /**
     * Save a contactUs.
     *
     * @param contactUsDTO      the entity to save.
     * @param senderProfile
     * @param assignedToProfile
     * @return the persisted entity.
     */
    ContactUsDTO save(ContactUsDTO contactUsDTO, Profile senderProfile, Profile assignedToProfile);

    /**
     * Get all the contactuses.
     *
     * @return the list of entities.
     */
    List<ContactUsDTO> findAll();


    /**
     * Get the "id" contactUs.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContactUsDTO> findOne(Long id);

    /**
     * Delete the "id" contactUs.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    ContactUsDTO createContactUs(ContactUsDTO contactUsDTO);

    ContactUsDTO updateContactUs(ContactUsDTO contactUsDTO);
}
