package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.Address;
import ng.com.justjava.corebanking.service.dto.AddressDTO;
import ng.com.justjava.corebanking.domain.Profile;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Address}.
 */
public interface AddressService {

    /**
     * Save a address.
     *
     * @param addressDTO the entity to save.
     * @return the persisted entity.
     */
    AddressDTO save(AddressDTO addressDTO, Profile addressOwner);

    AddressDTO save(AddressDTO addressDTO);

    /**
     * Get all the addresses.
     *
     * @return the list of entities.
     */
    List<AddressDTO> findAll();


    /**
     * Get the "id" address.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AddressDTO> findOne(Long id);

    /**
     * Delete the "id" address.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<AddressDTO> findByAddressOwner(String phoneNumber);
}
