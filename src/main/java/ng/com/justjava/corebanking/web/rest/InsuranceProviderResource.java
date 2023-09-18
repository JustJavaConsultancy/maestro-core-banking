package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.justjava.corebanking.domain.InsuranceProvider;
import ng.com.justjava.corebanking.service.InsuranceProviderService;
import ng.com.justjava.corebanking.service.dto.InsuranceProviderDTO;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link InsuranceProvider}.
 */
@RestController
@RequestMapping("/api")
public class InsuranceProviderResource {

    private static final String ENTITY_NAME = "insuranceProvider";
    private final Logger log = LoggerFactory.getLogger(InsuranceProviderResource.class);
    private final InsuranceProviderService insuranceProviderService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public InsuranceProviderResource(InsuranceProviderService insuranceProviderService) {
        this.insuranceProviderService = insuranceProviderService;
    }

    /**
     * {@code POST  /insurance-providers} : Create a new insuranceProvider.
     *
     * @param insuranceProviderDTO the insuranceProviderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuranceProviderDTO, or with status {@code 400 (Bad Request)} if the insuranceProvider has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/insurance-providers")
    public ResponseEntity<InsuranceProviderDTO> createInsuranceProvider(@Valid @RequestBody InsuranceProviderDTO insuranceProviderDTO) throws URISyntaxException {
        log.debug("REST request to save InsuranceProvider : {}", insuranceProviderDTO);
        if (insuranceProviderDTO.getId() != null) {
            throw new BadRequestAlertException("A new insuranceProvider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InsuranceProviderDTO result = insuranceProviderService.save(insuranceProviderDTO);
        return ResponseEntity.created(new URI("/api/insurance-providers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /insurance-providers} : Updates an existing insuranceProvider.
     *
     * @param insuranceProviderDTO the insuranceProviderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceProviderDTO,
     * or with status {@code 400 (Bad Request)} if the insuranceProviderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuranceProviderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/insurance-providers")
    public ResponseEntity<InsuranceProviderDTO> updateInsuranceProvider(@Valid @RequestBody InsuranceProviderDTO insuranceProviderDTO) throws URISyntaxException {
        log.debug("REST request to update InsuranceProvider : {}", insuranceProviderDTO);
        if (insuranceProviderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InsuranceProviderDTO result = insuranceProviderService.save(insuranceProviderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insuranceProviderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /insurance-providers} : get all the insuranceProviders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insuranceProviders in body.
     */
    @GetMapping("/insurance-providers")
    public List<InsuranceProviderDTO> getAllInsuranceProviders() {
        log.debug("REST request to get all InsuranceProviders");
        return insuranceProviderService.findAll();
    }

    /**
     * {@code GET  /insurance-providers/:id} : get the "id" insuranceProvider.
     *
     * @param id the id of the insuranceProviderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuranceProviderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/insurance-providers/{id}")
    public ResponseEntity<InsuranceProviderDTO> getInsuranceProvider(@PathVariable Long id) {
        log.debug("REST request to get InsuranceProvider : {}", id);
        Optional<InsuranceProviderDTO> insuranceProviderDTO = insuranceProviderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insuranceProviderDTO);
    }

    /**
     * {@code DELETE  /insurance-providers/:id} : delete the "id" insuranceProvider.
     *
     * @param id the id of the insuranceProviderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/insurance-providers/{id}")
    public ResponseEntity<Void> deleteInsuranceProvider(@PathVariable Long id) {
        log.debug("REST request to delete InsuranceProvider : {}", id);
        insuranceProviderService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
