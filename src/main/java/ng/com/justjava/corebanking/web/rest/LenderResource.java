package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.justjava.corebanking.domain.Lender;
import ng.com.justjava.corebanking.service.LenderService;
import ng.com.justjava.corebanking.service.dto.CreateLenderDTO;
import ng.com.justjava.corebanking.service.dto.CreateLenderResponseDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.LenderDTO;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link Lender}.
 */
@RestController
@RequestMapping("/api")
public class LenderResource {

    private static final String ENTITY_NAME = "lender";
    private final Logger log = LoggerFactory.getLogger(LenderResource.class);
    private final LenderService lenderService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public LenderResource(LenderService lenderService) {
        this.lenderService = lenderService;
    }

    /**
     * {@code POST  /lenders} : Create a new lender.
     *
     * @param createLenderDTO the lenderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lenderDTO, or with status {@code 400 (Bad Request)} if the lender has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lenders")
    public ResponseEntity<GenericResponseDTO> createLender(@Valid @RequestBody CreateLenderDTO createLenderDTO, HttpSession session) throws URISyntaxException {
        log.debug("REST request to save Lender : {}", createLenderDTO);
        if (createLenderDTO.getId() != null) {
            throw new BadRequestAlertException("A new lender cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GenericResponseDTO genericResponseDTO = lenderService.createLender(createLenderDTO, session);

        CreateLenderResponseDTO data = (CreateLenderResponseDTO) genericResponseDTO.getData();
        if (data != null) {
            return ResponseEntity.created(new URI("/api/lenders/" + data.getLenderId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, data.getLenderId().toString()))
                .body(genericResponseDTO);
        }

        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
    }


    /**
     * {@code PUT  /lenders} : Updates an existing lender.
     *
     * @param lenderDTO the lenderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lenderDTO,
     * or with status {@code 400 (Bad Request)} if the lenderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lenderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lenders")
    public ResponseEntity<LenderDTO> updateLender(@Valid @RequestBody LenderDTO lenderDTO) throws URISyntaxException {
        log.debug("REST request to update Lender : {}", lenderDTO);
        if (lenderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LenderDTO result = lenderService.updateLender(lenderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lenderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lenders} : get all the lenders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lenders in body.
     */
    @GetMapping("/lenders")
    public List<LenderDTO> getAllLenders() {
        log.debug("REST request to get all Lenders");
        return lenderService.findAll();
    }

    /**
     * {@code GET  /lenders/:id} : get the "id" lender.
     *
     * @param id the id of the lenderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lenderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lenders/{id}")
    public ResponseEntity<LenderDTO> getLender(@PathVariable Long id) {
        log.debug("REST request to get Lender : {}", id);
        Optional<LenderDTO> lenderDTO = lenderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lenderDTO);
    }

    /**
     * {@code GET  /lenders/:id} : get the "id" lender.
     *
     * @param phoneNumber the id of the lenderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lenderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lenders/phone/{phoneNumber}")
    public ResponseEntity<LenderDTO> getLenderByPhoneNumber(@PathVariable String phoneNumber) {
        log.debug("REST request to get Lender by phoneNumber : {}", phoneNumber);
        Optional<LenderDTO> lenderDTO = lenderService.getLenderByPhoneNumber(phoneNumber);
        return ResponseUtil.wrapOrNotFound(lenderDTO);
    }

    /**
     * {@code DELETE  /lenders/:id} : delete the "id" lender.
     *
     * @param id the id of the lenderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lenders/{id}")
    public ResponseEntity<Void> deleteLender(@PathVariable Long id) {
        log.debug("REST request to delete Lender : {}", id);
        lenderService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
