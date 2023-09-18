package ng.com.systemspecs.apigateway.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.systemspecs.apigateway.service.BonusPointService;
import ng.com.systemspecs.apigateway.service.dto.BonusPointDTO;
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
 * REST controller for managing {@link ng.com.systemspecs.apigateway.domain.BonusPoint}.
 */
@RestController
@RequestMapping("/api")
public class BonusPointResource {

    private final Logger log = LoggerFactory.getLogger(BonusPointResource.class);

    private static final String ENTITY_NAME = "bonusPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BonusPointService bonusPointService;

    public BonusPointResource(BonusPointService bonusPointService) {
        this.bonusPointService = bonusPointService;
    }

    /**
     * {@code POST  /bonus-points} : Create a new bonusPoint.
     *
     * @param bonusPointDTO the bonusPointDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bonusPointDTO, or with status {@code 400 (Bad Request)} if the bonusPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bonus-points")
    public ResponseEntity<BonusPointDTO> createBonusPoint(@Valid @RequestBody BonusPointDTO bonusPointDTO) throws URISyntaxException {
        log.debug("REST request to save BonusPoint : {}", bonusPointDTO);
        if (bonusPointDTO.getId() != null) {
            throw new BadRequestAlertException("A new bonusPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BonusPointDTO result = bonusPointService.save(bonusPointDTO);
        return ResponseEntity.created(new URI("/api/bonus-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bonus-points} : Updates an existing bonusPoint.
     *
     * @param bonusPointDTO the bonusPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bonusPointDTO,
     * or with status {@code 400 (Bad Request)} if the bonusPointDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bonusPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bonus-points")
    public ResponseEntity<BonusPointDTO> updateBonusPoint(@Valid @RequestBody BonusPointDTO bonusPointDTO) throws URISyntaxException {
        log.debug("REST request to update BonusPoint : {}", bonusPointDTO);
        if (bonusPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BonusPointDTO result = bonusPointService.save(bonusPointDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bonusPointDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bonus-points} : get all the bonusPoints.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bonusPoints in body.
     */
    @GetMapping("/bonus-points")
    public List<BonusPointDTO> getAllBonusPoints() {
        log.debug("REST request to get all BonusPoints");
        return bonusPointService.findAll();
    }


    /**
     * {@code GET  /bonus-points/:id} : get the "id" bonusPoint.
     *
     * @param id the id of the bonusPointDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bonusPointDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bonus-points/{id}")
    public ResponseEntity<BonusPointDTO> getBonusPoint(@PathVariable Long id) {
        log.debug("REST request to get BonusPoint : {}", id);
        Optional<BonusPointDTO> bonusPointDTO = bonusPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bonusPointDTO);
    }

    /**
     * {@code DELETE  /bonus-points/:id} : delete the "id" bonusPoint.
     *
     * @param id the id of the bonusPointDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bonus-points/{id}")
    public ResponseEntity<Void> deleteBonusPoint(@PathVariable Long id) {
        log.debug("REST request to delete BonusPoint : {}", id);
        bonusPointService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
