package ng.com.systemspecs.apigateway.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import ng.com.systemspecs.apigateway.domain.AccessItem;
import ng.com.systemspecs.apigateway.domain.AccessRight;
import ng.com.systemspecs.apigateway.domain.Right;
import ng.com.systemspecs.apigateway.service.AccessItemService;
import ng.com.systemspecs.apigateway.service.AccessRightService;
import ng.com.systemspecs.apigateway.service.RightService;
import ng.com.systemspecs.apigateway.service.dto.AccessRightDTO;
import ng.com.systemspecs.apigateway.service.dto.CreateAccessRightDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ng.com.systemspecs.apigateway.domain.Address}.
 */
@RestController
@RequestMapping("/api")
public class AccessRightResource {

    private final Logger log = LoggerFactory.getLogger(AccessRightResource.class);

    private static final String ENTITY_NAME = "AccessRight";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccessRightService accessRightService;
    private final AccessItemService accessItemService;
    private final RightService rightService;

    public AccessRightResource(AccessRightService accessRightService, AccessItemService accessItemService, RightService rightService) {
        this.accessRightService = accessRightService;
        this.accessItemService = accessItemService;
        this.rightService = rightService;
    }


    /**
     * {@code POST  /access-rights} : Create a new address.
     *
     * @param createAccessRightDTO the addressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new addressDTO, or with status {@code 400 (Bad Request)} if the address has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/access-rights")
    public ResponseEntity<GenericResponseDTO> createAccessRight(@RequestBody CreateAccessRightDTO createAccessRightDTO) throws URISyntaxException {
        log.debug("REST request to save AccessRight : {}", createAccessRightDTO);

        GenericResponseDTO genericResponseDTO = accessRightService.createAccessRight(createAccessRightDTO);
        log.info("ACCESS RIGHT +++===> " + genericResponseDTO.getData());

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    /**
     * {@code PUT  /addresses} : Updates an existing address.
     *
     * @param createAccessRightDTO the addressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated addressDTO,
     * or with status {@code 400 (Bad Request)} if the addressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the addressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/access-rights")
    public ResponseEntity<GenericResponseDTO> updateAccessRight(@RequestBody CreateAccessRightDTO createAccessRightDTO) throws URISyntaxException {
        log.debug("REST request to update AccessRight : {}", createAccessRightDTO);
        if (createAccessRightDTO.getAccessRightName() == null) {
            throw new BadRequestAlertException("Invalid name", ENTITY_NAME, "idnull");
        }
        GenericResponseDTO genericResponseDTO = accessRightService.updateAccessRight(createAccessRightDTO);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    /**
     * {@code GET  /addresses} : get all the accessRights.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of addresses in body.
     */
    @GetMapping("/access-rights")
    public ResponseEntity<GenericResponseDTO> getAllAccessRight() {
        log.debug("REST request to get all accessRights");
        List<AccessRightDTO> all = accessRightService.findAllAccessRights();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", all), HttpStatus.OK);
    }

    /**
     * {@code GET  /access-rights/:id} : get the "id" address.
     *
     * @param id the id of the addressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the addressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/access-rights/id/{id}")
    public ResponseEntity<GenericResponseDTO> getAccessRight(@PathVariable Long id) {
        log.debug("REST request to get AccessRight : {}", id);
        Optional<AccessRight> one = accessRightService.findOne(id);
        return one.map(accessRight -> new ResponseEntity<>(new GenericResponseDTO("00", "success", accessRight), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(new GenericResponseDTO("99", "failed", null),
                HttpStatus.OK));
    }

    /**
     * {@code GET  /access-rights/:id} : get the "id" address.
     *
     * @param phoneNumber the phonenumber of the accessRight to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the addressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/access-rights/{phoneNumber}")
    public ResponseEntity<GenericResponseDTO> getAccessRights(@PathVariable String phoneNumber) {
        log.debug("REST request to get AccessRight : {}", phoneNumber);
        Optional<AccessRight> one = accessRightService.findByPhoneNumber(phoneNumber);
        return one.map(accessRight -> new ResponseEntity<>(new GenericResponseDTO("00", "success", accessRight), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(new GenericResponseDTO("99", "failed", null),
                HttpStatus.OK));
    }

    /**
     * {@code DELETE  /addresses/:id} : delete the "id" address.
     *
     * @param id the id of the addressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/access-rights/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        log.debug("REST request to delete Address : {}", id);
        accessRightService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code GET  /addresses} : get all the accessRights.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of addresses in body.
     */
    @GetMapping("/access-rights/items")
    public ResponseEntity<GenericResponseDTO> getAllAccessItems() {
        log.debug("REST request to get all accessItems");
        List<AccessItem> all = accessItemService.findAll();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", all), HttpStatus.OK);
    }

    @GetMapping("/access-rights/rights")
    public ResponseEntity<GenericResponseDTO> getAllRights() {
        log.debug("REST request to get all rights");
        List<Right> all = rightService.findAll();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", all), HttpStatus.OK);
    }

    @GetMapping("/access-rights/items/right/{rightCode}")
    public ResponseEntity<GenericResponseDTO> getAccessItemsByRightCode(@PathVariable(value = "rightCode") String rightCode) {
        log.debug("REST request to get all accessItems by Right code");
        List<AccessItem> all = accessItemService.findAllByRightCode(rightCode);
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", all), HttpStatus.OK);
    }

    @GetMapping("/access-rights/items/{phoneNumber}")
    public ResponseEntity<GenericResponseDTO> getAccessItemsByPhoneNumber(@PathVariable String phoneNumber) {
        log.debug("REST request to get all accessItems by Right code");
        List<AccessItem> all = accessItemService.findAllByPhoneNumber(phoneNumber);
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", all), HttpStatus.OK);
    }

}
