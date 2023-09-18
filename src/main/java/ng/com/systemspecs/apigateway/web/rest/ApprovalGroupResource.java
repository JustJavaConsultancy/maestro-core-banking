package ng.com.systemspecs.apigateway.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import ng.com.systemspecs.apigateway.domain.Right;
import ng.com.systemspecs.apigateway.service.ApprovalGroupService;
import ng.com.systemspecs.apigateway.service.dto.ApprovalGroupDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
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
 * REST controller for managing {@link ng.com.systemspecs.apigateway.domain.ApprovalGroup}.
 */
@RestController
@RequestMapping("/api")
public class ApprovalGroupResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalGroupResource.class);

    private static final String ENTITY_NAME = "approvalGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApprovalGroupService approvalGroupService;

    public ApprovalGroupResource(ApprovalGroupService approvalGroupService) {
        this.approvalGroupService = approvalGroupService;
    }

    /**
     * {@code PUT  /approval-groups} : Updates an existing approvalGroup.
     *
     * @param approvalGroupDTO the approvalGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalGroupDTO,
     * or with status {@code 400 (Bad Request)} if the approvalGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the approvalGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/approval-groups")
    public ResponseEntity<GenericResponseDTO> updateApprovalGroup(@Valid @RequestBody ApprovalGroupDTO approvalGroupDTO) throws URISyntaxException {
        log.debug("REST request to update ApprovalGroup : {}", approvalGroupDTO);
        if (approvalGroupDTO.getId() == null) {

            return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid null id ",  approvalGroupDTO.getId()), HttpStatus.BAD_REQUEST);

        }
        ApprovalGroupDTO result = approvalGroupService.save(approvalGroupDTO);
        if (result != null) {
            GenericResponseDTO genericResponseDTO = new GenericResponseDTO("00", HttpStatus.OK, "Success", result);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvalGroupDTO.getId().toString()))
                .body(genericResponseDTO);
        }

        return  new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null), HttpStatus.BAD_REQUEST);
    }

    /**
     * {@code GET  /approval-groups} : get all the approvalGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of approvalGroups in body.
     */
    @GetMapping("/approval-groups")
    public ResponseEntity<GenericResponseDTO> getAllApprovalGroups() {
        log.debug("REST request to get all ApprovalGroups");
        List<ApprovalGroupDTO> approvalGroups = approvalGroupService.findAll();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", approvalGroups), HttpStatus.OK);
    }

    /**
     * {@code POST  /approval-groups} : Create a new approvalGroup.
     *
     * @param approvalGroupDTO the approvalGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new approvalGroupDTO, or with status {@code 400 (Bad Request)} if the approvalGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/approval-groups")
    public ResponseEntity<GenericResponseDTO> createApprovalGroup(@Valid @RequestBody ApprovalGroupDTO approvalGroupDTO) throws URISyntaxException {
        log.debug("REST request to save ApprovalGroup : {}", approvalGroupDTO);
        if (approvalGroupDTO.getId() != null) {
            return new ResponseEntity<>(new GenericResponseDTO("99", "A new Approval group cannot already have an ID " + approvalGroupDTO.getId(), null), HttpStatus.BAD_REQUEST);
        }
        ApprovalGroupDTO result = approvalGroupService.save(approvalGroupDTO);

        if (result != null) {
            GenericResponseDTO genericResponseDTO = new GenericResponseDTO("00", "success", result);
            return ResponseEntity.created(new URI("/api/approval-groups/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(genericResponseDTO);
        }

        return new ResponseEntity<>(new GenericResponseDTO("99", "failed", null), HttpStatus.BAD_REQUEST);
    }

    /**
     * {@code GET  /approval-groups/:id} : get the "id" approvalGroup.
     *
     * @param id the id of the approvalGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the approvalGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/approval-groups/{id}")
    public ResponseEntity<GenericResponseDTO> getApprovalGroup(@PathVariable Long id) {
        log.debug("REST request to get ApprovalGroup : {}", id);
        Optional<ApprovalGroupDTO> approvalGroupDTO = approvalGroupService.findOne(id);
        return approvalGroupDTO.map(groupDTO -> new ResponseEntity<>(new GenericResponseDTO("00", "success", groupDTO), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(new GenericResponseDTO("99", "Failed to retrieve object, invalid Id", null), HttpStatus.NOT_FOUND));

    }

    /**
     * {@code DELETE  /approval-groups/:id} : delete the "id" approvalGroup.
     *
     * @param id the id of the approvalGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/approval-groups/{id}")
    public ResponseEntity<GenericResponseDTO> deleteApprovalGroup(@PathVariable Long id) {
        log.debug("REST request to delete ApprovalGroup : {}", id);
        String response = approvalGroupService.delete(id);

        log.info("RESPONSE ==> " + response);

        if (response.equalsIgnoreCase("success")) {
            return new ResponseEntity<>(new GenericResponseDTO("00", response, null), HttpStatus.OK);
        }

        return new ResponseEntity<>(new GenericResponseDTO("99", response, null), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/rights/{phoneNumber}")
    public ResponseEntity<GenericResponseDTO> getRights(@PathVariable String phoneNumber) {
        log.debug("REST request to get right : {}", phoneNumber);

        List<Right> rights = approvalGroupService.getRights(phoneNumber);
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", rights), HttpStatus.OK);

    }
}
