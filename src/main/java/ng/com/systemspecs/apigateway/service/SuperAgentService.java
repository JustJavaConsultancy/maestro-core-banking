package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.Agent;
import ng.com.systemspecs.apigateway.domain.SuperAgent;
import ng.com.systemspecs.apigateway.domain.enumeration.AgentStatus;
import ng.com.systemspecs.apigateway.service.dto.CreateAgentDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.SuperAgentDTO;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.SuperAgent}.
 */
public interface SuperAgentService {

    /**
     * Save a superAgent.
     *
     * @param superAgentDTO the entity to save.
     * @return the persisted entity.
     */
    SuperAgentDTO save(SuperAgentDTO superAgentDTO);

    /**
     * Get all the superAgents.
     *
     * @return the list of entities.
     */
    List<SuperAgentDTO> findAll();


    /**
     * Get the "id" superAgent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SuperAgentDTO> findOne(Long id);

    /**
     * Delete the "id" superAgent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    GenericResponseDTO createSuperAgent(CreateAgentDTO createAgentDTO, HttpSession session);

    GenericResponseDTO changeSuperAgentStatus(String phoneNumber, AgentStatus agentStatus);

    GenericResponseDTO getSuperAgentsByStatus(AgentStatus agentStatus);

    Optional<SuperAgent> findByAgentProfilePhoneNumber(String phoneNumber);

    Optional<SuperAgent> findByAgent(Agent agent);

    GenericResponseDTO createSuperAgents(List<CreateAgentDTO> createAgentDTOs, HttpSession session);
}
