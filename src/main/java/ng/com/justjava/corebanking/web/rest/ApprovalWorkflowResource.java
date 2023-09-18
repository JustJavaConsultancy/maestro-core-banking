package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import ng.com.justjava.corebanking.domain.ApprovalWorkflow;
import ng.com.justjava.corebanking.service.ApprovalWorkflowService;
import ng.com.justjava.corebanking.service.dto.ApprovalWorkflowDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ApprovalWorkflow}.
 */
@RestController
@RequestMapping("/api")
public class ApprovalWorkflowResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalWorkflowResource.class);

    private static final String ENTITY_NAME = "approvalWorkflow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApprovalWorkflowService approvalWorkflowService;

    public ApprovalWorkflowResource(ApprovalWorkflowService approvalWorkflowService) {
        this.approvalWorkflowService = approvalWorkflowService;
    }

    /**
     * {@code POST  /approval-workflows} : Create a new approvalWorkflow.
     *
     * @param approvalWorkflowDTO the approvalWorkflowDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new approvalWorkflowDTO, or with status {@code 400 (Bad Request)} if the approvalWorkflow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/approval-workflows")
    public ResponseEntity<GenericResponseDTO> createApprovalWorkflow(@Valid @RequestBody ApprovalWorkflowDTO approvalWorkflowDTO) throws URISyntaxException {
        log.debug("REST request to save ApprovalWorkflow : {}", approvalWorkflowDTO);
        if (approvalWorkflowDTO.getId() != null) {
            return new ResponseEntity<>(new GenericResponseDTO("99", "A new Approval Workflow cannot already have an ID " + approvalWorkflowDTO.getId(), null), HttpStatus.BAD_REQUEST);
        }
        ApprovalWorkflowDTO result = approvalWorkflowService.save(approvalWorkflowDTO);

        if (result != null) {
            GenericResponseDTO genericResponseDTO = new GenericResponseDTO("00", "success", result);

            return ResponseEntity.created(new URI("/api/approval-workflows/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(genericResponseDTO);
        }

        return new ResponseEntity<>(new GenericResponseDTO("99", "failed", null), HttpStatus.BAD_REQUEST);
    }

    /**
     * {@code PUT  /approval-workflows} : Updates an existing approvalWorkflow.
     *
     * @param approvalWorkflowDTO the approvalWorkflowDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalWorkflowDTO,
     * or with status {@code 400 (Bad Request)} if the approvalWorkflowDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the approvalWorkflowDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/approval-workflows")
    public ResponseEntity<GenericResponseDTO> updateApprovalWorkflow(@Valid @RequestBody ApprovalWorkflowDTO approvalWorkflowDTO) throws URISyntaxException {
        log.debug("REST request to update ApprovalWorkflow : {}", approvalWorkflowDTO);
        if (approvalWorkflowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApprovalWorkflowDTO result = approvalWorkflowService.save(approvalWorkflowDTO);

        if (result != null) {
            GenericResponseDTO genericResponseDTO = new GenericResponseDTO("00", "success", result);

            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvalWorkflowDTO.getId().toString()))
                .body(genericResponseDTO);
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", "failed", null), HttpStatus.BAD_REQUEST);

    }

    /**
     * {@code GET  /approval-workflows} : get all the approvalWorkflows.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of approvalWorkflows in body.
     */
    @GetMapping("/approval-workflows")
    public ResponseEntity<GenericResponseDTO> getAllApprovalWorkflows() {
        log.debug("REST request to get all ApprovalWorkflows");
        List<ApprovalWorkflowDTO> all = approvalWorkflowService.findAll();

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", all), HttpStatus.OK);
    }

    /**
     * {@code GET  /approval-workflows/:id} : get the "id" approvalWorkflow.
     *
     * @param id the id of the approvalWorkflowDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the approvalWorkflowDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/approval-workflows/{id}")
    public ResponseEntity<GenericResponseDTO> getApprovalWorkflow(@PathVariable Long id) {
        log.debug("REST request to get ApprovalWorkflow : {}", id);
        Optional<ApprovalWorkflowDTO> approvalWorkflowDTO = approvalWorkflowService.findOne(id);

        return approvalWorkflowDTO.map(workflowDTO -> new ResponseEntity<>(new GenericResponseDTO("00", "success", workflowDTO), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(new GenericResponseDTO("99", "Invalid Id", null), HttpStatus.NOT_FOUND));
    }

    /**
     * {@code DELETE  /approval-workflows/:id} : delete the "id" approvalWorkflow.
     *
     * @param id the id of the approvalWorkflowDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/approval-workflows/{id}")
    public ResponseEntity<GenericResponseDTO> deleteApprovalWorkflow(@PathVariable Long id) {
        log.debug("REST request to delete ApprovalWorkflow : {}", id);
        String response = approvalWorkflowService.delete(id);
        log.info("RESPONSE ==> " + response);

        if (response.equalsIgnoreCase("success")) {
            return new ResponseEntity<>(new GenericResponseDTO("00", response, null), HttpStatus.OK);
        }

        return new ResponseEntity<>(new GenericResponseDTO("99", response, null), HttpStatus.BAD_REQUEST);
    }
}
