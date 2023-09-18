package ng.com.systemspecs.apigateway.web.rest;

import ng.com.systemspecs.apigateway.service.PolarisCardService;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.systemspecs.apigateway.service.impl.PolarisVulteServiceImpl;
import ng.com.systemspecs.apigateway.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Polaris Card Requests.
 */
@RestController
@RequestMapping("/api")
public class PolarisCardRequestResource {

    private final Logger log = LoggerFactory.getLogger(PolarisCardRequestResource.class);
    private final PolarisCardService polarisCardService;
    private final Utility utility;
    private final PolarisVulteServiceImpl polarisVulteServiceImpl;

    private final Environment environment;

    public PolarisCardRequestResource(PolarisCardService polarisCardService, Utility utility, PolarisVulteServiceImpl polarisVulteServiceImpl, Environment environment) {
        this.polarisCardService = polarisCardService;
        this.utility = utility;
        this.polarisVulteServiceImpl = polarisVulteServiceImpl;
        this.environment = environment;
    }


    @PostMapping("/polaris/cards/request/{phoneNumber}/{scheme}")
    public ResponseEntity<GenericResponseDTO> requestCard(
        @RequestBody PolarisCardRequestDTO polarisCardRequestDTO,
        @PathVariable String phoneNumber,
        @PathVariable String scheme
    ) throws Exception {
        log.debug("REST request to request a card  : {}", polarisCardRequestDTO);
        try {
            String phone = utility.returnPhoneNumberFormat(phoneNumber);
            GenericResponseDTO response = polarisCardService.requestCard(polarisCardRequestDTO, phone, scheme);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception ex) {
            log.debug("CARD REQUEST ERROR  ====>  " + ex.getMessage());
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
                HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/polaris/cards/requestconnect")
    public ResponseEntity<GenericResponseDTO> requestCardConnect(
        @RequestBody PolarisCollectionAccountRequestDTO polarisCollectionAccountRequestDTO) throws Exception {
        log.debug("REST request to request a card  : {}", polarisCollectionAccountRequestDTO);
        try {
            String phone = utility.returnPhoneNumberFormat(polarisCollectionAccountRequestDTO.getMobileNumber());
            polarisCollectionAccountRequestDTO.setMobileNumber(phone);
            GenericResponseDTO response = polarisVulteServiceImpl.openCollectionAccount(polarisCollectionAccountRequestDTO);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception ex) {
            log.debug("CARD REQUEST ERROR  ====>  " + ex.getMessage());
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
                HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/polaris/cards/requestauth")
    public ResponseEntity<GenericResponseDTO> requestCardauth() throws Exception {
        try {
            GenericResponseDTO response = polarisVulteServiceImpl.getAuth();
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception ex) {
            log.debug("CARD REQUEST ERROR  ====>  " + ex.getMessage());
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
                HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/polaris/cards/{phoneNumber}/{scheme}")
    public ResponseEntity<GenericResponseDTO> getVendorCards(@PathVariable String phoneNumber, @PathVariable String scheme) throws Exception {
        log.debug("REST request to get customer cards : {}", phoneNumber);
        List<PolarisCard> cards = polarisCardService.getVendorCards(scheme, phoneNumber);
        System.out.println("Cards Output ===>  "+ cards);
        if (cards != null) {
            if(!cards.isEmpty() || cards.size()!=0) {
                return ResponseEntity.ok(new GenericResponseDTO("00", HttpStatus.OK, "success", cards));
            }
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No Cards found for user!", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/polaris/cards/activate")
    public ResponseEntity<GenericResponseDTO> activateCard(@RequestBody PolarisCardOperationsDTO polarisCardOperationsDTO) {
        log.debug("REST request to get activate customer card : {}", polarisCardOperationsDTO);
        GenericResponseDTO response = polarisCardService.activateCard(polarisCardOperationsDTO);
        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/polaris/cards/change-pin")
    public ResponseEntity<GenericResponseDTO> changeCardPin(@RequestBody PolarisCardOperationsDTO polarisCardOperationsDTO) {
        log.debug("REST request to get activate customer card : {}", polarisCardOperationsDTO);
        GenericResponseDTO response = polarisCardService.changeCardPin(polarisCardOperationsDTO);
        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/polaris/cards/vendor-card-profile/{scheme}")
    public ResponseEntity<GenericResponseDTO> getVendorCardProfile(@Param("scheme") String scheme) {
        log.debug("REST request to get vendor card profile: {}", scheme);
        GenericResponseDTO response = polarisCardService.getVendorCardProfile(scheme);
        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/polaris/cards/vendor-card-profiles/{scheme}")
    public ResponseEntity<GenericResponseDTO> getPolarisVendorCardProfile(@Param("scheme") String scheme) {
        log.debug("REST request to get vendor card profile: {}", scheme);
        GenericResponseDTO response = polarisCardService.getPolarisVendorCardProfile(scheme);
        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/polaris/cards/vendor-card-branches")
    public ResponseEntity<GenericResponseDTO> getDeliveryBranches() {
        log.debug("REST request to get branches fro card delivery {}");
        GenericResponseDTO response = polarisCardService.getBranches();
        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/polaris/cards/open-collection-account/{phoneNumber}/{scheme}")
    public ResponseEntity<GenericResponseDTO> openCollectionAccount(
        @PathVariable String phoneNumber,
        @PathVariable String scheme
    ) {
        log.debug("REST request to get branches for card delivery {}");
        GenericResponseDTO response = polarisCardService.openCollectionAccount(phoneNumber, scheme);
        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/application/env/profile/{env}")
    public ResponseEntity<Boolean> getEnvironmentProfile(@PathVariable String env) {
        boolean yes = false;
        if (environment.acceptsProfiles(Profiles.of(env))) {
            yes = true;
            return new ResponseEntity<>(yes,HttpStatus.OK);
        }
        return new ResponseEntity<>(yes,HttpStatus.EXPECTATION_FAILED);
    }
}
