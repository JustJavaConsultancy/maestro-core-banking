package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.justjava.corebanking.domain.SuperAgent;
import ng.com.justjava.corebanking.domain.enumeration.AgentStatus;
import ng.com.justjava.corebanking.service.SuperAgentService;
import ng.com.justjava.corebanking.service.dto.CreateAgentDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.SuperAgentDTO;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link SuperAgent}.
 */
@RestController
@RequestMapping("/api")
public class SuperAgentResource {

    private static final String ENTITY_NAME = "superAgent";
    private final Logger log = LoggerFactory.getLogger(SuperAgentResource.class);
    private final SuperAgentService superAgentService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public SuperAgentResource(SuperAgentService superAgentService) {
        this.superAgentService = superAgentService;
    }

    /**
     * x
     * {@code POST  /super-agents} : Create a new superAgent.
     *
     * @param createAgentDTO the createAgentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new createAgentDTO, or with status {@code 400 (Bad Request)} if the superAgent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/super-agents")
    public ResponseEntity<GenericResponseDTO> createSuperAgent(@Valid @RequestBody CreateAgentDTO createAgentDTO, HttpSession session) throws URISyntaxException {
        log.debug("REST request to save SuperAgent : {}", createAgentDTO);
        GenericResponseDTO result = superAgentService.createSuperAgent(createAgentDTO, session);

        if (HttpStatus.BAD_REQUEST.equals(result.getStatus())) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * x
     * {@code POST  /super-agents} : Create a new superAgent.
     *
     * @param createAgentDTOs the createAgentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new createAgentDTO, or with status {@code 400 (Bad Request)} if the superAgent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/super-agents/list")
    public ResponseEntity<GenericResponseDTO> createSuperAgent(@Valid @RequestBody List<CreateAgentDTO> createAgentDTOs, HttpSession session) throws URISyntaxException {
        log.debug("REST request to save List of SuperAgent : {}", createAgentDTOs);
        GenericResponseDTO result = superAgentService.createSuperAgents(createAgentDTOs, session);

        if (HttpStatus.BAD_REQUEST.equals(result.getStatus())) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /super-agents} : Updates an existing superAgent.
     *
     * @param superAgentDTO the superAgentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated superAgentDTO,
     * or with status {@code 400 (Bad Request)} if the superAgentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the superAgentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/super-agents")
    public ResponseEntity<SuperAgentDTO> updateSuperAgent(@Valid @RequestBody SuperAgentDTO superAgentDTO) throws URISyntaxException {
        log.debug("REST request to update SuperAgent : {}", superAgentDTO);
        if (superAgentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SuperAgentDTO result = superAgentService.save(superAgentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, superAgentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /super-agents} : get all the superAgents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of superAgents in body.
     */
    @GetMapping("/super-agents")
    public List<SuperAgentDTO> getAllSuperAgents() {
        log.debug("REST request to get all SuperAgents");
        return superAgentService.findAll();
    }

    /**
     * {@code GET  /super-agents/:id} : get the "id" superAgent.
     *
     * @param id the id of the superAgentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the superAgentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/super-agents/{id}")
    public ResponseEntity<SuperAgentDTO> getSuperAgent(@PathVariable Long id) {
        log.debug("REST request to get SuperAgent : {}", id);
        Optional<SuperAgentDTO> superAgentDTO = superAgentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(superAgentDTO);
    }

    /**
     * {@code DELETE  /super-agents/:id} : delete the "id" superAgent.
     *
     * @param id the id of the superAgentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/super-agents/{id}")
    public ResponseEntity<Void> deleteSuperAgent(@PathVariable Long id) {
        log.debug("REST request to delete SuperAgent : {}", id);
        superAgentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/super-agents/status/{phoneNumber}/{agentStatus}")
    public ResponseEntity<GenericResponseDTO> changeSuperAgentStatus(@PathVariable AgentStatus agentStatus, @PathVariable String phoneNumber) {
        log.debug("REST request to change SuperAgent status : {}", agentStatus, phoneNumber);
        GenericResponseDTO result = superAgentService.changeSuperAgentStatus(phoneNumber, agentStatus);

        return new ResponseEntity<>(result, result.getStatus());
    }

    @GetMapping("/super-agents/status/{agentStatus}")
    public ResponseEntity<GenericResponseDTO> getSuperAgentsByStatus(@PathVariable AgentStatus agentStatus) {
        log.debug("REST request to get SuperAgent not approved");
        GenericResponseDTO result = superAgentService.getSuperAgentsByStatus(agentStatus);

        return new ResponseEntity<>(result, result.getStatus());

    }
}
