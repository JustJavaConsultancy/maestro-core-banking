package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.ContactUs;
import ng.com.justjava.corebanking.service.dto.ContactUsDTO;
import ng.com.justjava.corebanking.domain.Profile;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ContactUs}.
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
