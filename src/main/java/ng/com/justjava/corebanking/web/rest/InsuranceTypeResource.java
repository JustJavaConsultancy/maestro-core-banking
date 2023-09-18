package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.InsuranceType;
import ng.com.justjava.corebanking.service.InsuranceTypeService;
import ng.com.justjava.corebanking.service.dto.InsuranceTypeDTO;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link InsuranceType}.
 */
@RestController
@RequestMapping("/api")
public class InsuranceTypeResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceTypeResource.class);

    private static final String ENTITY_NAME = "insuranceType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceTypeService insuranceTypeService;

    public InsuranceTypeResource(InsuranceTypeService insuranceTypeService) {
        this.insuranceTypeService = insuranceTypeService;
    }

    /**
     * {@code POST  /insurance-types} : Create a new insuranceType.
     *
     * @param insuranceTypeDTO the insuranceTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuranceTypeDTO, or with status {@code 400 (Bad Request)} if the insuranceType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/insurance-types")
    public ResponseEntity<InsuranceTypeDTO> createInsuranceType(@Valid @RequestBody InsuranceTypeDTO insuranceTypeDTO) throws URISyntaxException {
        log.debug("REST request to save InsuranceType : {}", insuranceTypeDTO);
        if (insuranceTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new insuranceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InsuranceTypeDTO result = insuranceTypeService.save(insuranceTypeDTO);
        return ResponseEntity.created(new URI("/api/insurance-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /insurance-types} : Updates an existing insuranceType.
     *
     * @param insuranceTypeDTO the insuranceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the insuranceTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuranceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/insurance-types")
    public ResponseEntity<InsuranceTypeDTO> updateInsuranceType(@Valid @RequestBody InsuranceTypeDTO insuranceTypeDTO) throws URISyntaxException {
        log.debug("REST request to update InsuranceType : {}", insuranceTypeDTO);
        if (insuranceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InsuranceTypeDTO result = insuranceTypeService.save(insuranceTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insuranceTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /insurance-types} : get all the insuranceTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insuranceTypes in body.
     */
    @GetMapping("/insurance-types")
    public List<InsuranceTypeDTO> getAllInsuranceTypes() {
        log.debug("REST request to get all InsuranceTypes");
        return insuranceTypeService.findAll();
    }

    /**
     * {@code GET  /insurance-types/:id} : get the "id" insuranceType.
     *
     * @param id the id of the insuranceTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuranceTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/insurance-types/{id}")
    public ResponseEntity<InsuranceTypeDTO> getInsuranceType(@PathVariable Long id) {
        log.debug("REST request to get InsuranceType : {}", id);
        Optional<InsuranceTypeDTO> insuranceTypeDTO = insuranceTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insuranceTypeDTO);
    }

    /**
     * {@code DELETE  /insurance-types/:id} : delete the "id" insuranceType.
     *
     * @param id the id of the insuranceTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/insurance-types/{id}")
    public ResponseEntity<Void> deleteInsuranceType(@PathVariable Long id) {
        log.debug("REST request to delete InsuranceType : {}", id);
        insuranceTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
