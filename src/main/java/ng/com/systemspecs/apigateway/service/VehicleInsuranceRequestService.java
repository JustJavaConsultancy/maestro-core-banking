package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.VehicleInsuranceRequestDTO;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.VehicleInsuranceRequest}.
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
