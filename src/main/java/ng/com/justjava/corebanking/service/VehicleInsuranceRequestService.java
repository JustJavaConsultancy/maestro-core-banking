package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.VehicleInsuranceRequest;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.VehicleInsuranceRequestDTO;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

/**
 * Service Interface for managing {@link VehicleInsuranceRequest}.
 */
public interface VehicleInsuranceRequestService {

    /**
     * Save a vehicleInsuranceRequest.
     *
     * @param vehicleInsuranceRequestDTO the entity to save.
     * @return the persisted entity.
     */
    VehicleInsuranceRequestDTO save(VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO);

    /**
     * Create a new vehicle insurance.
     *
     * @param vehicleInsuranceRequestDTO the entity to save.
     * @return the persisted entity.
     */
    GenericResponseDTO createNewVehicleInsurance(VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO, HttpSession session);

    /**
     * Get all the vehicleInsuranceRequests.
     *
     * @return the list of entities.
     */
    List<VehicleInsuranceRequestDTO> findAll();


    /**
     * Get the "id" vehicleInsuranceRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehicleInsuranceRequestDTO> findOne(Long id);

    /**
     * Delete the "id" vehicleInsuranceRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

	GenericResponseDTO findByLoggedInUser();
}
