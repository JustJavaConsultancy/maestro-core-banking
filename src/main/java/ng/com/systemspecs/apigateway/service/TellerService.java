package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.Teller;
import ng.com.systemspecs.apigateway.service.dto.CreateAgentDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.TellerDTO;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Teller}.
 */
public interface TellerService {

    /**
     * Save a Teller.
     *
     * @param TellerDTO the entity to save.
     * @return the persisted entity.
     */

    TellerDTO save(TellerDTO TellerDTO);

    /**
     * Get all the addresses.
     *
     * @return the list of entities.
     */
    List<TellerDTO> findAll();


    /**
     * Get the "id" Teller.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TellerDTO> findOne(Long id);

    /**
     * Delete the "id" Teller.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<TellerDTO> findAllByAgentProfilePhoneNumber(String phoneNumber);

    List<TellerDTO> findByLocationLike(String location);

    Optional<TellerDTO> findByProfilePhoneNumber(String phoneNumber);

    GenericResponseDTO createTeller(CreateAgentDTO createAgentDTO, HttpSession session);

    List<TellerDTO> getAllASuperAgentTellers(String phoneNumber);

    TellerDTO upgradeToTeller(String phoneNumber, String location, double latitude, double longitude);

    GenericResponseDTO setTellerLimit(String accountNumber, Double amount);

}
