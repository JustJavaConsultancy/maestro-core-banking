package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import ng.com.justjava.corebanking.domain.Agent;
import ng.com.justjava.corebanking.service.TellerService;
import ng.com.justjava.corebanking.service.dto.CreateAgentDTO;
import ng.com.justjava.corebanking.service.dto.CreateAgentResponseDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.TellerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link Agent}.
 */
@RestController
@RequestMapping("/api")
public class TellerResource {

    private final Logger log = LoggerFactory.getLogger(TellerResource.class);

    private static final String ENTITY_NAME = "Agent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TellerService tellerService;

    public TellerResource(TellerService TellerService) {
        this.tellerService = TellerService;
    }

    /**
     * {@code POST  /tellers} : Create a new Agent.
     *
     * @param createAgentDTO the createAgentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tellerDTO, or with status {@code 400 (Bad Request)} if the Agent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tellers")
    public ResponseEntity<GenericResponseDTO> createTeller(@RequestBody CreateAgentDTO createAgentDTO, HttpSession session) throws URISyntaxException {
        log.debug("REST request to create Teller : {}", createAgentDTO);

        GenericResponseDTO genericResponseDTO = tellerService.createTeller(createAgentDTO, session);

        if (genericResponseDTO != null && HttpStatus.CREATED.equals(genericResponseDTO.getStatus())) {
            CreateAgentResponseDTO createAgentResponseDTO = (CreateAgentResponseDTO) genericResponseDTO.getData();
            return ResponseEntity.created(new URI("/api/tellers/" + createAgentResponseDTO.getAgentId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, createAgentResponseDTO.getAccountName()))
                .body(new GenericResponseDTO("00", "success", createAgentResponseDTO));
        }
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/tellers/agent/{phoneNumber}")
    public ResponseEntity<GenericResponseDTO> getAllASuperAgentTellers(@PathVariable String phoneNumber) {
        log.debug("REST request to get all SuperAgent tellers : {}", phoneNumber);

        List<TellerDTO> tellers = tellerService.getAllASuperAgentTellers(phoneNumber);

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", tellers), HttpStatus.OK);
    }

    @PostMapping("/tellers/{phoneNumber}/{location}/{latitude}/{longitude}")
    public ResponseEntity<GenericResponseDTO> upgradeToTeller(@PathVariable String phoneNumber,
                                                              @PathVariable String location,
                                                              @PathVariable Double latitude,
                                                              @PathVariable Double longitude
    ) throws URISyntaxException {
        log.debug("REST request to upgrade A Profile to Agent : {}", phoneNumber);
        TellerDTO result = tellerService.upgradeToTeller(phoneNumber, location, latitude, longitude);
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), HttpStatus.OK);
    }

    @PostMapping("/tellers/limit/{accountNumber}/{amount}")
    public ResponseEntity<GenericResponseDTO> setTellerLimit(@PathVariable String accountNumber,
                                                             @PathVariable Double amount
    ) throws URISyntaxException {
        log.debug("REST request to set teller Wallet Limit : {} {}", accountNumber, amount);
        GenericResponseDTO result = tellerService.setTellerLimit(accountNumber, amount);

        return new ResponseEntity<>(result, result.getStatus());

    }

}
