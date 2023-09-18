package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.VehicleInsuranceRequest;
import ng.com.justjava.corebanking.service.VehicleInsuranceRequestService;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.VehicleInsuranceRequestDTO;
import ng.com.justjava.corebanking.util.Utility;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link VehicleInsuranceRequest}.
 */
@RestController
@RequestMapping("/api")
public class VehicleInsuranceRequestResource {

    private final Logger log = LoggerFactory.getLogger(VehicleInsuranceRequestResource.class);

    private static final String ENTITY_NAME = "vehicleInsuranceRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleInsuranceRequestService vehicleInsuranceRequestService;
    private final Utility utility;


    public VehicleInsuranceRequestResource(VehicleInsuranceRequestService vehicleInsuranceRequestService, Utility utility) {
        this.vehicleInsuranceRequestService = vehicleInsuranceRequestService;
        this.utility = utility;
    }

    /**
     * {@code POST  /vehicle-insurance-requests} : Create a new vehicleInsuranceRequest.
     *
     * @param vehicleInsuranceRequestDTO the vehicleInsuranceRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleInsuranceRequestDTO, or with status {@code 400 (Bad Request)} if the vehicleInsuranceRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vehicle-insurance-requests")
    public ResponseEntity<VehicleInsuranceRequestDTO> createVehicleInsuranceRequest(@Valid @RequestBody VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO) throws URISyntaxException {
        log.debug("REST request to save VehicleInsuranceRequest : {}", vehicleInsuranceRequestDTO);


        if (vehicleInsuranceRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleInsuranceRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VehicleInsuranceRequestDTO result = vehicleInsuranceRequestService.save(vehicleInsuranceRequestDTO);
        return ResponseEntity.created(new URI("/api/vehicle-insurance-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
//    int pinFailureCount = getPinFailureCount(session);
//
//    if (pinFailureCount >= 3) {
//        return buildPinAttemptExceededResponse(response, session);
//    }
//
//    Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());
//
//    String currentEncryptedPin = profile.getPin();
//    if (!passwordEncoder.matches(buyAirtimeDTO.getPin(), currentEncryptedPin) || StringUtils.isEmpty(buyAirtimeDTO.getPin())) {
//
//        return buildPinErrorResponse(session, pinFailureCount);
//    }
    @PostMapping("/create-vehicle-insurance-request")
    public  ResponseEntity<GenericResponseDTO> CreateNewVehicleInsurance(@Valid @RequestBody VehicleInsuranceRequestDTO vehicle, HttpSession session) {
        ResponseEntity<GenericResponseDTO> responseEntity = utility.checkTransactionPin(session, vehicle.getPin());
        System.out.println(responseEntity+"=============i am the response from the pin check");
        if (responseEntity.getStatusCode().isError()) {
            System.out.println(responseEntity+"=============The error returned");
            return responseEntity;
        }
    	GenericResponseDTO genericResponseDTO = vehicleInsuranceRequestService.createNewVehicleInsurance(vehicle, session);
    	//return  ResponseEntity.ok().body(vehicle);
    	 if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
    		 return  ResponseEntity.ok(genericResponseDTO);
    				// (genericResponseDTO, HttpStatus.OK);
    	 }
    	 return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * {@code PUT  /vehicle-insurance-requests} : Updates an existing vehicleInsuranceRequest.
     *
     * @param vehicleInsuranceRequestDTO the vehicleInsuranceRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleInsuranceRequestDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleInsuranceRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleInsuranceRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vehicle-insurance-requests")
    public ResponseEntity<VehicleInsuranceRequestDTO> updateVehicleInsuranceRequest(@Valid @RequestBody VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO) throws URISyntaxException {
        log.debug("REST request to update VehicleInsuranceRequest : {}", vehicleInsuranceRequestDTO);
        if (vehicleInsuranceRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VehicleInsuranceRequestDTO result = vehicleInsuranceRequestService.save(vehicleInsuranceRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleInsuranceRequestDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/users-vehicle-insurance-requests")
    public ResponseEntity<GenericResponseDTO> getVehicleInsuranceRequestsByLoggedInUser() {
        log.debug("REST request to get all VehicleInsuranceRequests");
        GenericResponseDTO genericResponseDTO =  vehicleInsuranceRequestService.findByLoggedInUser();
        if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
   		 return  ResponseEntity.ok(genericResponseDTO);
   	 }
   	 return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * {@code GET  /vehicle-insurance-requests} : get all the vehicleInsuranceRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleInsuranceRequests in body.
     */
    @GetMapping("/vehicle-insurance-requests")
    public List<VehicleInsuranceRequestDTO> getAllVehicleInsuranceRequests() {
        log.debug("REST request to get all VehicleInsuranceRequests");
        return vehicleInsuranceRequestService.findAll();
    }

    /**
     * {@code GET  /vehicle-insurance-requests/:id} : get the "id" vehicleInsuranceRequest.
     *
     * @param id the id of the vehicleInsuranceRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleInsuranceRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vehicle-insurance-requests/{id}")
    public ResponseEntity<VehicleInsuranceRequestDTO> getVehicleInsuranceRequest(@PathVariable Long id) {
        log.debug("REST request to get VehicleInsuranceRequest : {}", id);
        Optional<VehicleInsuranceRequestDTO> vehicleInsuranceRequestDTO = vehicleInsuranceRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleInsuranceRequestDTO);
    }

    /**
     * {@code DELETE  /vehicle-insurance-requests/:id} : delete the "id" vehicleInsuranceRequest.
     *
     * @param id the id of the vehicleInsuranceRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vehicle-insurance-requests/{id}")
    public ResponseEntity<Void> deleteVehicleInsuranceRequest(@PathVariable Long id) {
        log.debug("REST request to delete VehicleInsuranceRequest : {}", id);
        vehicleInsuranceRequestService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
