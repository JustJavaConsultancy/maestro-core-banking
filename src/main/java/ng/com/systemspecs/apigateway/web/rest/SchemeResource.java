package ng.com.systemspecs.apigateway.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.systemspecs.apigateway.service.SchemeService;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.SchemeCallBackDTO;
import ng.com.systemspecs.apigateway.service.dto.SchemeDTO;
import ng.com.systemspecs.apigateway.service.dto.UpdateKeysDTO;
import ng.com.systemspecs.apigateway.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ng.com.systemspecs.apigateway.domain.Scheme}.
 */
@RestController
@RequestMapping("/api")
public class SchemeResource {

    private final Logger log = LoggerFactory.getLogger(SchemeResource.class);

    private static final String ENTITY_NAME = "scheme";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchemeService schemeService;

    public SchemeResource(SchemeService schemeService) {
        this.schemeService = schemeService;
    }

    /**
     * {@code POST  /schemes} : Create a new scheme.
     *
     * @param schemeDTO the schemeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schemeDTO, or with status {@code 400 (Bad Request)} if the scheme has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/schemes")
    public ResponseEntity<GenericResponseDTO> createScheme(@RequestBody SchemeDTO schemeDTO) throws URISyntaxException {
        log.debug("REST request to save Scheme : {}", schemeDTO);
        if (schemeDTO.getId() != null) {
            throw new BadRequestAlertException("A new scheme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchemeDTO result = schemeService.createScheme(schemeDTO);
        result.setCallBackUrl(schemeDTO.getCallBackUrl());
        if (result != null) {
            GenericResponseDTO genericResponseDTO = new GenericResponseDTO("00", "success", result);
            return ResponseEntity.created(new URI("/api/schemes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(genericResponseDTO);
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", "Scheme already exist", null), HttpStatus.BAD_REQUEST);
    }

    /**
     * {@code PUT  /schemes} : Updates an existing scheme.
     *
     * @param schemeDTO the schemeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schemeDTO,
     * or with status {@code 400 (Bad Request)} if the schemeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schemeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/schemes")
    public ResponseEntity<SchemeDTO> updateScheme(@RequestBody SchemeDTO schemeDTO) throws URISyntaxException {
        log.debug("REST request to update Scheme : {}", schemeDTO);
        if (schemeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SchemeDTO result = schemeService.save(schemeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schemeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /schemes} : get all the schemes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schemes in body.
     */
    @GetMapping("/schemes")
    public List<SchemeDTO> getAllSchemes() {
        log.debug("REST request to get all Schemes");
        return schemeService.findAll();
    }

    /**
     * {@code GET  /schemes/:id} : get the "id" scheme.
     *
     * @param id the id of the schemeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schemeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/schemes/{id}")
    public ResponseEntity<SchemeDTO> getScheme(@PathVariable Long id) {
        log.debug("REST request to get Scheme : {}", id);
        Optional<SchemeDTO> schemeDTO = schemeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schemeDTO);
    }

    /**
     * {@code DELETE  /schemes/:id} : delete the "id" scheme.
     *
     * @param id the id of the schemeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/schemes/{id}")
    public ResponseEntity<Void> deleteScheme(@PathVariable Long id) {
        log.debug("REST request to delete Scheme : {}", id);
        schemeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/schemes/admin/{phoneNumber}")
    public ResponseEntity<GenericResponseDTO> getAdminScheme(@PathVariable String phoneNumber) {
        log.debug("REST request to get Scheme : {}", phoneNumber);
        GenericResponseDTO genericResponseDTO = schemeService.getAdminScheme(phoneNumber);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PutMapping("/schemes/callback")
    public ResponseEntity<GenericResponseDTO> schemeCallback(@RequestBody SchemeCallBackDTO schemeCallBackDTO) throws URISyntaxException {
        log.debug("REST request to save Scheme : {}", schemeCallBackDTO);
        GenericResponseDTO genericResponseDTO = schemeService.updateSchemeCallBack(schemeCallBackDTO);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PutMapping("/schemes/keys")
    public ResponseEntity<GenericResponseDTO> updateSchemes(@RequestBody UpdateKeysDTO updateKeysDTO){
        log.debug("REST request to save Scheme : {}", updateKeysDTO);
        GenericResponseDTO genericResponseDTO = schemeService.updateKeys(updateKeysDTO);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

}
