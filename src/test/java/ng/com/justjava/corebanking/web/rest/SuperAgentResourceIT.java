package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.Agent;
import ng.com.justjava.corebanking.domain.Scheme;
import ng.com.justjava.corebanking.domain.SuperAgent;
import ng.com.justjava.corebanking.domain.enumeration.AgentStatus;
import ng.com.justjava.corebanking.repository.SuperAgentRepository;
import ng.com.justjava.corebanking.service.SuperAgentService;
import ng.com.justjava.corebanking.service.dto.SuperAgentDTO;
import ng.com.justjava.corebanking.service.mapper.SuperAgentMapper;
import ng.com.justjava.corebanking.ApiGatewayApp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SuperAgentResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SuperAgentResourceIT {

    private static final AgentStatus DEFAULT_STATUS = AgentStatus.APPROVED;
    private static final AgentStatus UPDATED_STATUS = AgentStatus.UNAPPROVED;

    @Autowired
    private SuperAgentRepository superAgentRepository;

    @Autowired
    private SuperAgentMapper superAgentMapper;

    @Autowired
    private SuperAgentService superAgentService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuperAgentMockMvc;

    private SuperAgent superAgent;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuperAgent createEntity(EntityManager em) {
        SuperAgent superAgent = new SuperAgent()
            .status(DEFAULT_STATUS);
        // Add required entity
        Agent agent;
        if (TestUtil.findAll(em, Agent.class).isEmpty()) {
            agent = null;
//            agent = AgentResourceIT.createEntity(em);
            em.persist(agent);
            em.flush();
        } else {
            agent = TestUtil.findAll(em, Agent.class).get(0);
        }
        superAgent.setAgent(agent);
        // Add required entity
        Scheme scheme;
        if (TestUtil.findAll(em, Scheme.class).isEmpty()) {
            scheme = SchemeResourceIT.createEntity(em);
            em.persist(scheme);
            em.flush();
        } else {
            scheme = TestUtil.findAll(em, Scheme.class).get(0);
        }
        superAgent.setScheme(scheme);
        return superAgent;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuperAgent createUpdatedEntity(EntityManager em) {
        SuperAgent superAgent = new SuperAgent()
            .status(UPDATED_STATUS);
        // Add required entity
        Agent agent;
        if (TestUtil.findAll(em, Agent.class).isEmpty()) {
            agent = null;
//            agent = AgentResourceIT.createUpdatedEntity(em);
            em.persist(agent);
            em.flush();
        } else {
            agent = TestUtil.findAll(em, Agent.class).get(0);
        }
        superAgent.setAgent(agent);
        // Add required entity
        Scheme scheme;
        if (TestUtil.findAll(em, Scheme.class).isEmpty()) {
            scheme = SchemeResourceIT.createUpdatedEntity(em);
            em.persist(scheme);
            em.flush();
        } else {
            scheme = TestUtil.findAll(em, Scheme.class).get(0);
        }
        superAgent.setScheme(scheme);
        return superAgent;
    }

    @BeforeEach
    public void initTest() {
        superAgent = createEntity(em);
    }

    @Test
    @Transactional
    public void createSuperAgent() throws Exception {
        int databaseSizeBeforeCreate = superAgentRepository.findAll().size();
        // Create the SuperAgent
        SuperAgentDTO superAgentDTO = superAgentMapper.toDto(superAgent);
        restSuperAgentMockMvc.perform(post("/api/super-agents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(superAgentDTO)))
            .andExpect(status().isCreated());

        // Validate the SuperAgent in the database
        List<SuperAgent> superAgentList = superAgentRepository.findAll();
        assertThat(superAgentList).hasSize(databaseSizeBeforeCreate + 1);
        SuperAgent testSuperAgent = superAgentList.get(superAgentList.size() - 1);
        Assertions.assertThat(testSuperAgent.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createSuperAgentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = superAgentRepository.findAll().size();

        // Create the SuperAgent with an existing ID
        superAgent.setId(1L);
        SuperAgentDTO superAgentDTO = superAgentMapper.toDto(superAgent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuperAgentMockMvc.perform(post("/api/super-agents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(superAgentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SuperAgent in the database
        List<SuperAgent> superAgentList = superAgentRepository.findAll();
        assertThat(superAgentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = superAgentRepository.findAll().size();
        // set the field null
        superAgent.setStatus(null);

        // Create the SuperAgent, which fails.
        SuperAgentDTO superAgentDTO = superAgentMapper.toDto(superAgent);


        restSuperAgentMockMvc.perform(post("/api/super-agents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(superAgentDTO)))
            .andExpect(status().isBadRequest());

        List<SuperAgent> superAgentList = superAgentRepository.findAll();
        assertThat(superAgentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSuperAgents() throws Exception {
        // Initialize the database
        superAgentRepository.saveAndFlush(superAgent);

        // Get all the superAgentList
        restSuperAgentMockMvc.perform(get("/api/super-agents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(superAgent.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getSuperAgent() throws Exception {
        // Initialize the database
        superAgentRepository.saveAndFlush(superAgent);

        // Get the superAgent
        restSuperAgentMockMvc.perform(get("/api/super-agents/{id}", superAgent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(superAgent.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSuperAgent() throws Exception {
        // Get the superAgent
        restSuperAgentMockMvc.perform(get("/api/super-agents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSuperAgent() throws Exception {
        // Initialize the database
        superAgentRepository.saveAndFlush(superAgent);

        int databaseSizeBeforeUpdate = superAgentRepository.findAll().size();

        // Update the superAgent
        SuperAgent updatedSuperAgent = superAgentRepository.findById(superAgent.getId()).get();
        // Disconnect from session so that the updates on updatedSuperAgent are not directly saved in db
        em.detach(updatedSuperAgent);
        updatedSuperAgent
            .status(UPDATED_STATUS);
        SuperAgentDTO superAgentDTO = superAgentMapper.toDto(updatedSuperAgent);

        restSuperAgentMockMvc.perform(put("/api/super-agents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(superAgentDTO)))
            .andExpect(status().isOk());

        // Validate the SuperAgent in the database
        List<SuperAgent> superAgentList = superAgentRepository.findAll();
        assertThat(superAgentList).hasSize(databaseSizeBeforeUpdate);
        SuperAgent testSuperAgent = superAgentList.get(superAgentList.size() - 1);
        Assertions.assertThat(testSuperAgent.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingSuperAgent() throws Exception {
        int databaseSizeBeforeUpdate = superAgentRepository.findAll().size();

        // Create the SuperAgent
        SuperAgentDTO superAgentDTO = superAgentMapper.toDto(superAgent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuperAgentMockMvc.perform(put("/api/super-agents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(superAgentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SuperAgent in the database
        List<SuperAgent> superAgentList = superAgentRepository.findAll();
        assertThat(superAgentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSuperAgent() throws Exception {
        // Initialize the database
        superAgentRepository.saveAndFlush(superAgent);

        int databaseSizeBeforeDelete = superAgentRepository.findAll().size();

        // Delete the superAgent
        restSuperAgentMockMvc.perform(delete("/api/super-agents/{id}", superAgent.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SuperAgent> superAgentList = superAgentRepository.findAll();
        assertThat(superAgentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
