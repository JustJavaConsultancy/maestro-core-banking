package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.Agent;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.Agent}.
 */
public interface AgentService {

    /**
     * Save a Agent.
     *
     * @param AgentDTO the entity to save.
     * @return the persisted entity.
     */

    AgentDTO save(AgentDTO AgentDTO);

    /**
     * Get all the addresses.
     *
     * @return the list of entities.
     */
    List<AgentDTO> findAll();


    /**
     * Get the "id" Agent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AgentDTO> findOne(Long id);

    /**
     * Delete the "id" Agent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<Agent> findAllBySuperAgent_Id(Long superAgentId);

    List<Agent> findByLocationLike(String location);

    Optional<Agent> findAllByProfilePhoneNumber(String agentPhoneNumber);

    AgentDTO upgradeToAnAgent(String phoneNumber, String location);

    GenericResponseDTO createAgent(CreateAgentDTO createAgentDTO, HttpSession session);

    GenericResponseDTO addAgentToSuperAgent(String superAgentPhoneNo, String agentPhoneNo);

    GenericResponseDTO addTellerToAgent(String agentPhoneNo, String tellerPhoneNo);

    WalletExternalDTO convertWalletAccountDTO(CreateAgentDTO createAgentDTO, Long walletAccountTypeId, int kyc);

    GenericResponseDTO allocateFund(String superAgentPhoneNumber, String agentPhoneNumber, String pin, double amount, Long accountTypeId);

    AgentDTO upgradeToAnAgentGeo(String phoneNumber, String location, double latitude, double longitude);

    Page<AgentDTO> getAllAgents(Pageable pageable);

    AgentDTO getAgentDetails(String phoneNumber);

    List<AgentDTO> getAllASuperAgentAgents(String phoneNumber);

    List<Agent> getAllSuperAgentAgents(String phoneNumber);

    Optional<Agent> findByProfile(Profile profile);

    GenericResponseDTO sendInvite(AgentInviteDTO agentInviteDTO);

    SuperAgentDataDTO getSuperAgentsMetrics();

    GenericResponseDTO becomeAnAgent(CreateAgentDTO createAgentDTO, HttpSession session);

    GenericResponseDTO createAgents(List<CreateAgentDTO> createAgentDTOS, HttpSession session);
}
