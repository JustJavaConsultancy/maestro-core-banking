package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.CreateAgentDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.SuperAgentDTO;
import ng.com.justjava.corebanking.domain.Agent;
import ng.com.justjava.corebanking.domain.SuperAgent;
import ng.com.justjava.corebanking.domain.enumeration.AgentStatus;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link SuperAgent}.
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
