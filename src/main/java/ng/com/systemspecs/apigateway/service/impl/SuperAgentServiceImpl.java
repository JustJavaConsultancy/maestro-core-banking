package ng.com.systemspecs.apigateway.service.impl;

import ng.com.systemspecs.apigateway.domain.Agent;
import ng.com.systemspecs.apigateway.domain.Scheme;
import ng.com.systemspecs.apigateway.domain.SuperAgent;
import ng.com.systemspecs.apigateway.domain.enumeration.AgentStatus;
import ng.com.systemspecs.apigateway.repository.SuperAgentRepository;
import ng.com.systemspecs.apigateway.service.AgentService;
import ng.com.systemspecs.apigateway.service.SchemeService;
import ng.com.systemspecs.apigateway.service.SuperAgentService;
import ng.com.systemspecs.apigateway.service.dto.CreateAgentDTO;
import ng.com.systemspecs.apigateway.service.dto.CreateAgentResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.SuperAgentDTO;
import ng.com.systemspecs.apigateway.service.mapper.SuperAgentMapper;
import ng.com.systemspecs.apigateway.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SuperAgent}.
 */
@Service
@Transactional
public class SuperAgentServiceImpl implements SuperAgentService {

    private final Logger log = LoggerFactory.getLogger(SuperAgentServiceImpl.class);

    private final SuperAgentRepository superAgentRepository;
    private final AgentService agentService;
    private final Utility utility;
    private final SchemeService schemeService;

    private final SuperAgentMapper superAgentMapper;

    public SuperAgentServiceImpl(SuperAgentRepository superAgentRepository, AgentService agentService, Utility utility, SchemeService schemeService, SuperAgentMapper superAgentMapper) {
        this.superAgentRepository = superAgentRepository;
        this.agentService = agentService;
        this.utility = utility;
        this.schemeService = schemeService;
        this.superAgentMapper = superAgentMapper;
    }

    @Override
    public SuperAgentDTO save(SuperAgentDTO superAgentDTO) {
        log.debug("Request to save SuperAgent : {}", superAgentDTO);
        SuperAgent superAgent = superAgentMapper.toEntity(superAgentDTO);
        superAgent = superAgentRepository.save(superAgent);
        return superAgentMapper.toDto(superAgent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuperAgentDTO> findAll() {
        log.debug("Request to get all SuperAgents");
        return superAgentRepository.findAll().stream()
            .map(superAgentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SuperAgentDTO> findOne(Long id) {
        log.debug("Request to get SuperAgent : {}", id);
        return superAgentRepository.findById(id)
            .map(superAgentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SuperAgent : {}", id);
        superAgentRepository.deleteById(id);
    }

    @Override
    public GenericResponseDTO createSuperAgent(CreateAgentDTO createAgentDTO, HttpSession session) {
        createAgentDTO.setSuperAgent(true);

        if (superAgentRepository.existsByAgentProfilePhoneNumber(utility.formatPhoneNumber(createAgentDTO.getPhoneNumber()))) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Super Agent with phone number already exist"
                , null);
        }

        GenericResponseDTO genericResponseDTO = agentService.createAgent(createAgentDTO, session);

        if (!HttpStatus.CREATED.equals(genericResponseDTO.getStatus())) {
            return genericResponseDTO;
        }

        Scheme scheme = schemeService.findBySchemeID(createAgentDTO.getScheme());
        if (scheme == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid scheme");
        }

        CreateAgentResponseDTO data = (CreateAgentResponseDTO) genericResponseDTO.getData();
        SuperAgentDTO superAgentDTO = new SuperAgentDTO();
        superAgentDTO.setAgentId(data.getAgentId());
        superAgentDTO.setSchemeId(scheme.getId());
        superAgentDTO.setSchemeSchemeID(scheme.getSchemeID());
        superAgentDTO.setStatus(AgentStatus.UNAPPROVED);

        SuperAgentDTO save = save(superAgentDTO);
        log.info(String.format("Saved Super agent ===> %s", save));

        return new GenericResponseDTO("00", HttpStatus.OK, "success", data);

    }

    @Override
    public GenericResponseDTO changeSuperAgentStatus(String phoneNumber, AgentStatus agentStatus) {
        Optional<SuperAgent> superAgentOptional = superAgentRepository.findByAgentProfilePhoneNumber(utility.formatPhoneNumber(phoneNumber));

        if (!superAgentOptional.isPresent()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid phone number");
        }
        SuperAgent superAgent = superAgentOptional.get();

        superAgent.setStatus(agentStatus);
        SuperAgentDTO save = save(superAgentMapper.toDto(superAgent));

        log.info("Updated super agent ===> " + save);

        return new GenericResponseDTO("00", HttpStatus.OK, "success", save);

    }

    @Override
    public GenericResponseDTO getSuperAgentsByStatus(AgentStatus agentStatus) {
        List<SuperAgent> allSuperAgentByStatus = superAgentRepository.findAllByStatus(agentStatus);
        return new GenericResponseDTO("00", HttpStatus.OK, "success", allSuperAgentByStatus);
    }

    @Override
    public Optional<SuperAgent> findByAgentProfilePhoneNumber(String phoneNumber) {
        return superAgentRepository.findByAgentProfilePhoneNumber(utility.formatPhoneNumber(phoneNumber));
    }

    @Override
    public Optional<SuperAgent> findByAgent(Agent agent) {
        return superAgentRepository.findByAgent(agent);
    }

    @Override
    public GenericResponseDTO createSuperAgents(List<CreateAgentDTO> createAgentDTOs, HttpSession session) {
        List<CreateAgentResponseDTO> createAgentResponseDTOS = new ArrayList<>();
        for (CreateAgentDTO createAgentDTO : createAgentDTOs) {
            GenericResponseDTO genericResponseDTO = createSuperAgent(createAgentDTO, session);
            CreateAgentResponseDTO data = (CreateAgentResponseDTO) genericResponseDTO.getData();
            createAgentResponseDTOS.add(data);
        }

        return new GenericResponseDTO("00", HttpStatus.OK, "success", createAgentResponseDTOS);

    }
}
