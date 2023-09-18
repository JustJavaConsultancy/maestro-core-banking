package ng.com.systemspecs.apigateway.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import ng.com.systemspecs.apigateway.service.AgentService;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.systemspecs.apigateway.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing {@link ng.com.systemspecs.apigateway.domain.Agent}.
 */
@RestController
@RequestMapping("/api")
public class AgentResource {

    private final Logger log = LoggerFactory.getLogger(AgentResource.class);

    private static final String ENTITY_NAME = "Agent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgentService agentService;

    public AgentResource(AgentService agentService) {
        this.agentService = agentService;
    }

    /**
     * {@code POST  /agents} : Create a new Agent.
     *
     * @param agentDTO the agentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentDTO, or with status {@code 400 (Bad Request)} if the Agent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agents")
    public ResponseEntity<GenericResponseDTO> updateAgent(@RequestBody AgentDTO agentDTO) throws URISyntaxException {
        log.debug("REST request to save Agent : {}", agentDTO);
        if (agentDTO.getId() != null) {
            throw new BadRequestAlertException("A new Agent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgentDTO result = agentService.save(agentDTO);
        return ResponseEntity.created(new URI("/api/agents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getFullName()))
            .body(new GenericResponseDTO("00", "success", result));
    }

    @PostMapping("/agents/{phoneNumber}/{location}")
    public ResponseEntity<GenericResponseDTO> upgradeToAnAgent(@PathVariable String phoneNumber, @PathVariable String location) throws URISyntaxException {
        log.debug("REST request to upgrade A Profile to Agent : {}", phoneNumber);
        AgentDTO result = agentService.upgradeToAnAgent(phoneNumber, location);
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), HttpStatus.OK);
    }

    @PostMapping("/agents/{phoneNumber}/{location}/{latitude}/{longitude}")
    public ResponseEntity<GenericResponseDTO> upgradeToAnAgentWithGeoLocation(@PathVariable String phoneNumber,
                                                                              @PathVariable String location,
                                                                              @PathVariable double latitude,
                                                                              @PathVariable double longitude
    ) throws URISyntaxException {
        log.debug("REST request to upgrade A Profile to Agent : {}", phoneNumber);
        AgentDTO result = agentService.upgradeToAnAgentGeo(phoneNumber, location, latitude, longitude);
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), HttpStatus.OK);
    }

    @PostMapping("/agents")
    public ResponseEntity<GenericResponseDTO> createAgent(@RequestBody CreateAgentDTO createAgentDTO, HttpSession session) throws URISyntaxException {
        log.debug("REST request to create Agent : {}", createAgentDTO);
        GenericResponseDTO genericResponseDTO = agentService.createAgent(createAgentDTO, session);

        if (genericResponseDTO != null && HttpStatus.CREATED.equals(genericResponseDTO.getStatus())) {
            CreateAgentResponseDTO createAgentResponseDTO = (CreateAgentResponseDTO) genericResponseDTO.getData();
            return ResponseEntity.created(new URI("/api/agents/" + createAgentResponseDTO.getAgentId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, createAgentResponseDTO.getAccountName()))
                .body(new GenericResponseDTO("00", "success", createAgentResponseDTO));
        }
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/agents/list")
    public ResponseEntity<GenericResponseDTO> createAgent(@RequestBody List<CreateAgentDTO> createAgentDTOS, HttpSession session) throws URISyntaxException {
        log.debug("REST request to create Agent : {}", createAgentDTOS);
        GenericResponseDTO genericResponseDTO = agentService.createAgents(createAgentDTOS, session);

        if (genericResponseDTO != null && HttpStatus.CREATED.equals(genericResponseDTO.getStatus())) {
            CreateAgentResponseDTO createAgentResponseDTO = (CreateAgentResponseDTO) genericResponseDTO.getData();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", createAgentResponseDTO), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/agents/become")
    public ResponseEntity<GenericResponseDTO> becomeAnAgent(@RequestBody CreateAgentDTO createAgentDTO, HttpSession session) throws URISyntaxException {
        log.debug("REST request to create Agent : {}", createAgentDTO);
        GenericResponseDTO genericResponseDTO = agentService.becomeAnAgent(createAgentDTO, session);

        if (genericResponseDTO != null && HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
            CreateAgentResponseDTO createAgentResponseDTO = (CreateAgentResponseDTO) genericResponseDTO.getData();
            return ResponseEntity.created(new URI("/api/agents/" + createAgentResponseDTO.getAgentId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, createAgentResponseDTO.getAccountName()))
                .body(new GenericResponseDTO("00", "success", createAgentResponseDTO));
        }
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/agents/add-agent-to-super-agent/{superAgentPhoneNo}/{agentPhoneNo}")
    public ResponseEntity<GenericResponseDTO> addAgentToSuperAgent(@PathVariable String superAgentPhoneNo, @PathVariable String agentPhoneNo) throws URISyntaxException {
        log.debug("REST request to save Agent : {}", superAgentPhoneNo);
        GenericResponseDTO genericResponseDTO = agentService.addAgentToSuperAgent(superAgentPhoneNo, agentPhoneNo);

        if (genericResponseDTO != null && HttpStatus.OK.equals(genericResponseDTO.getStatus())){
            return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
        }
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/agents/add-teller-to-agent/{agentPhoneNo}/{tellerPhoneNo}")
    public ResponseEntity<GenericResponseDTO> addTellerToAgent(@PathVariable String agentPhoneNo, @PathVariable String tellerPhoneNo) {
        log.debug("REST request to save Agent : {}", tellerPhoneNo);
        GenericResponseDTO genericResponseDTO = agentService.addTellerToAgent(agentPhoneNo, tellerPhoneNo);

        if (genericResponseDTO != null && HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
            return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
        }
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/agents/fund-to-agent/{superAgentPhoneNumber}/{agentPhoneNumber}/{amount}/{pin}")
    public ResponseEntity<GenericResponseDTO> allocateFundToAgent(@PathVariable String superAgentPhoneNumber,
                                                                  @PathVariable String agentPhoneNumber,
                                                                  @PathVariable String pin,
                                                                  @PathVariable double amount){

        log.debug("REST request to allocate fund to an agent : {}", amount);

        GenericResponseDTO genericResponseDTO = agentService.allocateFund(superAgentPhoneNumber, agentPhoneNumber, pin, amount, 2L);

        if (genericResponseDTO != null && HttpStatus.OK.equals(genericResponseDTO.getStatus())){
            return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
        }
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/agents/fund-to-teller/{agentPhoneNumber}/{tellerPhoneNumber}/{amount}/{pin}")
    public ResponseEntity<GenericResponseDTO> allocateFundToTeller(@PathVariable String agentPhoneNumber,
                                                                  @PathVariable String tellerPhoneNumber,
                                                                  @PathVariable String pin,
                                                                  @PathVariable double amount){

        log.debug("REST request to allocate fund to a teller : {}", amount);

        GenericResponseDTO genericResponseDTO = agentService.allocateFund(agentPhoneNumber, tellerPhoneNumber, pin, amount, 5L);

        if (genericResponseDTO != null && HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
            return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
        }
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/agents")
    public ResponseEntity<GenericResponseDTO> getAllAgents(Pageable pageable) {
        log.debug("REST request to get all agents : {}", pageable);

        Page<AgentDTO> agents = agentService.getAllAgents(pageable);

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), agents);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", agents.getSize());
        metaMap.put("totalNumberOfRecords", agents.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", agents.getContent(), metaMap), headers, HttpStatus.OK);

    }

    @GetMapping("/agents/{phoneNumber}")
    public ResponseEntity<GenericResponseDTO> getAgentDetails(@PathVariable String phoneNumber) {
        log.debug("REST request to get all agents : {}", phoneNumber);

        AgentDTO agent = agentService.getAgentDetails(phoneNumber);

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", agent), HttpStatus.OK);

    }


    @GetMapping("/agents/{phoneNumber}/agent")
    public ResponseEntity<GenericResponseDTO> getAllASuperAgentAgents(@PathVariable String phoneNumber) {
        log.debug("REST request to get all SuperAgent agents : {}", phoneNumber);

        List<AgentDTO> agents = agentService.getAllASuperAgentAgents(phoneNumber);

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", agents), HttpStatus.OK);
    }

    @PostMapping("/agents/invite")
    public ResponseEntity<GenericResponseDTO> sendInvite(@RequestBody AgentInviteDTO agentInviteDTO) {
        log.debug("REST request to send Invite to agent(s) : {}", agentInviteDTO);
        GenericResponseDTO genericResponseDTO = agentService.sendInvite(agentInviteDTO);

        if (genericResponseDTO != null && HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
            return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
        }
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/agents/dashboard")
    public ResponseEntity<SuperAgentDataDTO> getSuperAgentsMetrics(){
    	SuperAgentDataDTO theAgents = agentService.getSuperAgentsMetrics();
    	if(!theAgents.equals(null)) {
        	return new ResponseEntity<>(theAgents,HttpStatus.ACCEPTED);

    	}
    	return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }


}
