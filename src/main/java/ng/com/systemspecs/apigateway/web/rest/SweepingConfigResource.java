package ng.com.systemspecs.apigateway.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.systemspecs.apigateway.service.SweepingConfigService;
import ng.com.systemspecs.apigateway.service.dto.SweepingConfigDTO;
import ng.com.systemspecs.apigateway.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link ng.com.systemspecs.apigateway.domain.SweepingConfig}.
 */
@RestController
@RequestMapping("/api")
public class SweepingConfigResource {

    private static final String ENTITY_NAME = "sweepingConfig";
    private final Logger log = LoggerFactory.getLogger(SweepingConfigResource.class);
    private final SweepingConfigService sweepingConfigService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public SweepingConfigResource(SweepingConfigService sweepingConfigService) {
        this.sweepingConfigService = sweepingConfigService;
    }

    /**
     * {@code POST  /sweeping-configs} : Create a new sweepingConfig.
     *
     * @param sweepingConfigDTO the sweepingConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sweepingConfigDTO, or with status {@code 400 (Bad Request)} if the sweepingConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sweeping-configs")
    public ResponseEntity<SweepingConfigDTO> createSweepingConfig(@Valid @RequestBody SweepingConfigDTO sweepingConfigDTO) throws URISyntaxException {
        log.debug("REST request to save SweepingConfig : {}", sweepingConfigDTO);
        if (sweepingConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new sweepingConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SweepingConfigDTO result = sweepingConfigService.save(sweepingConfigDTO);
        return ResponseEntity.created(new URI("/api/sweeping-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sweeping-configs} : Updates an existing sweepingConfig.
     *
     * @param sweepingConfigDTO the sweepingConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sweepingConfigDTO,
     * or with status {@code 400 (Bad Request)} if the sweepingConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sweepingConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sweeping-configs")
    public ResponseEntity<SweepingConfigDTO> updateSweepingConfig(@Valid @RequestBody SweepingConfigDTO sweepingConfigDTO) throws URISyntaxException {
        log.debug("REST request to update SweepingConfig : {}", sweepingConfigDTO);
        if (sweepingConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SweepingConfigDTO result = sweepingConfigService.save(sweepingConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sweepingConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sweeping-configs} : get all the sweepingConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sweepingConfigs in body.
     */
    @GetMapping("/sweeping-configs")
    public List<SweepingConfigDTO> getAllSweepingConfigs() {
        log.debug("REST request to get all SweepingConfigs");
        return sweepingConfigService.findAll();
    }

    /**
     * {@code GET  /sweeping-configs/:id} : get the "id" sweepingConfig.
     *
     * @param id the id of the sweepingConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sweepingConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sweeping-configs/{id}")
    public ResponseEntity<SweepingConfigDTO> getSweepingConfig(@PathVariable Long id) {
        log.debug("REST request to get SweepingConfig : {}", id);
        Optional<SweepingConfigDTO> sweepingConfigDTO = sweepingConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sweepingConfigDTO);
    }

    /**
     * {@code DELETE  /sweeping-configs/:id} : delete the "id" sweepingConfig.
     *
     * @param id the id of the sweepingConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sweeping-configs/{id}")
    public ResponseEntity<Void> deleteSweepingConfig(@PathVariable Long id) {
        log.debug("REST request to delete SweepingConfig : {}", id);
        sweepingConfigService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
