package ng.com.systemspecs.apigateway.web.rest;

import ng.com.systemspecs.apigateway.ApiGatewayApp;
import ng.com.systemspecs.apigateway.domain.ApprovalWorkflow;
import ng.com.systemspecs.apigateway.domain.ApprovalGroup;
import ng.com.systemspecs.apigateway.domain.Right;
import ng.com.systemspecs.apigateway.repository.ApprovalWorkflowRepository;
import ng.com.systemspecs.apigateway.service.ApprovalWorkflowService;
import ng.com.systemspecs.apigateway.service.dto.ApprovalWorkflowDTO;
import ng.com.systemspecs.apigateway.service.mapper.ApprovalWorkflowMapper;

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
 * Integration tests for the {@link ApprovalWorkflowResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ApprovalWorkflowResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ApprovalWorkflowRepository approvalWorkflowRepository;

    @Autowired
    private ApprovalWorkflowMapper approvalWorkflowMapper;

    @Autowired
    private ApprovalWorkflowService approvalWorkflowService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApprovalWorkflowMockMvc;

    private ApprovalWorkflow approvalWorkflow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalWorkflow createEntity(EntityManager em) {
        ApprovalWorkflow approvalWorkflow = new ApprovalWorkflow()
            .name(DEFAULT_NAME);
        // Add required entity
        ApprovalGroup approvalGroup;
        if (TestUtil.findAll(em, ApprovalGroup.class).isEmpty()) {
            approvalGroup = ApprovalGroupResourceIT.createEntity(em);
            em.persist(approvalGroup);
            em.flush();
        } else {
            approvalGroup = TestUtil.findAll(em, ApprovalGroup.class).get(0);
        }
        approvalWorkflow.setInitiator(approvalGroup);
        // Add required entity
        approvalWorkflow.setApprover(approvalGroup);
        // Add required entity
        Right right = null;
        if (TestUtil.findAll(em, Right.class).isEmpty()) {
//            right = RightResourceIT.createEntity(em);
//            em.persist(right);
//            em.flush();
        } else {
            right = TestUtil.findAll(em, Right.class).get(0);
        }
        approvalWorkflow.setTransactionType(right);
        return approvalWorkflow;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalWorkflow createUpdatedEntity(EntityManager em) {
        ApprovalWorkflow approvalWorkflow = new ApprovalWorkflow()
            .name(UPDATED_NAME);
        // Add required entity
        ApprovalGroup approvalGroup;
        if (TestUtil.findAll(em, ApprovalGroup.class).isEmpty()) {
            approvalGroup = ApprovalGroupResourceIT.createUpdatedEntity(em);
            em.persist(approvalGroup);
            em.flush();
        } else {
            approvalGroup = TestUtil.findAll(em, ApprovalGroup.class).get(0);
        }
        approvalWorkflow.setInitiator(approvalGroup);
        // Add required entity
        approvalWorkflow.setApprover(approvalGroup);
        // Add required entity
        Right right = null;
        if (TestUtil.findAll(em, Right.class).isEmpty()) {
//            right = RightResourceIT.createUpdatedEntity(em);
//            em.persist(right);
//            em.flush();
        } else {
            right = TestUtil.findAll(em, Right.class).get(0);
        }
        approvalWorkflow.setTransactionType(right);
        return approvalWorkflow;
    }

    @BeforeEach
    public void initTest() {
        approvalWorkflow = createEntity(em);
    }

    @Test
    @Transactional
    public void createApprovalWorkflow() throws Exception {
        int databaseSizeBeforeCreate = approvalWorkflowRepository.findAll().size();
        // Create the ApprovalWorkflow
        ApprovalWorkflowDTO approvalWorkflowDTO = approvalWorkflowMapper.toDto(approvalWorkflow);
        restApprovalWorkflowMockMvc.perform(post("/api/approval-workflows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(approvalWorkflowDTO)))
            .andExpect(status().isCreated());

        // Validate the ApprovalWorkflow in the database
        List<ApprovalWorkflow> approvalWorkflowList = approvalWorkflowRepository.findAll();
        assertThat(approvalWorkflowList).hasSize(databaseSizeBeforeCreate + 1);
        ApprovalWorkflow testApprovalWorkflow = approvalWorkflowList.get(approvalWorkflowList.size() - 1);
        assertThat(testApprovalWorkflow.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createApprovalWorkflowWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = approvalWorkflowRepository.findAll().size();

        // Create the ApprovalWorkflow with an existing ID
        approvalWorkflow.setId(1L);
        ApprovalWorkflowDTO approvalWorkflowDTO = approvalWorkflowMapper.toDto(approvalWorkflow);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApprovalWorkflowMockMvc.perform(post("/api/approval-workflows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(approvalWorkflowDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApprovalWorkflow in the database
        List<ApprovalWorkflow> approvalWorkflowList = approvalWorkflowRepository.findAll();
        assertThat(approvalWorkflowList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalWorkflowRepository.findAll().size();
        // set the field null
        approvalWorkflow.setName(null);

        // Create the ApprovalWorkflow, which fails.
        ApprovalWorkflowDTO approvalWorkflowDTO = approvalWorkflowMapper.toDto(approvalWorkflow);


        restApprovalWorkflowMockMvc.perform(post("/api/approval-workflows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(approvalWorkflowDTO)))
            .andExpect(status().isBadRequest());

        List<ApprovalWorkflow> approvalWorkflowList = approvalWorkflowRepository.findAll();
        assertThat(approvalWorkflowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApprovalWorkflows() throws Exception {
        // Initialize the database
        approvalWorkflowRepository.saveAndFlush(approvalWorkflow);

        // Get all the approvalWorkflowList
        restApprovalWorkflowMockMvc.perform(get("/api/approval-workflows?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(approvalWorkflow.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void getApprovalWorkflow() throws Exception {
        // Initialize the database
        approvalWorkflowRepository.saveAndFlush(approvalWorkflow);

        // Get the approvalWorkflow
        restApprovalWorkflowMockMvc.perform(get("/api/approval-workflows/{id}", approvalWorkflow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(approvalWorkflow.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingApprovalWorkflow() throws Exception {
        // Get the approvalWorkflow
        restApprovalWorkflowMockMvc.perform(get("/api/approval-workflows/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApprovalWorkflow() throws Exception {
        // Initialize the database
        approvalWorkflowRepository.saveAndFlush(approvalWorkflow);

        int databaseSizeBeforeUpdate = approvalWorkflowRepository.findAll().size();

        // Update the approvalWorkflow
        ApprovalWorkflow updatedApprovalWorkflow = approvalWorkflowRepository.findById(approvalWorkflow.getId()).get();
        // Disconnect from session so that the updates on updatedApprovalWorkflow are not directly saved in db
        em.detach(updatedApprovalWorkflow);
        updatedApprovalWorkflow
            .name(UPDATED_NAME);
        ApprovalWorkflowDTO approvalWorkflowDTO = approvalWorkflowMapper.toDto(updatedApprovalWorkflow);

        restApprovalWorkflowMockMvc.perform(put("/api/approval-workflows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(approvalWorkflowDTO)))
            .andExpect(status().isOk());

        // Validate the ApprovalWorkflow in the database
        List<ApprovalWorkflow> approvalWorkflowList = approvalWorkflowRepository.findAll();
        assertThat(approvalWorkflowList).hasSize(databaseSizeBeforeUpdate);
        ApprovalWorkflow testApprovalWorkflow = approvalWorkflowList.get(approvalWorkflowList.size() - 1);
        assertThat(testApprovalWorkflow.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingApprovalWorkflow() throws Exception {
        int databaseSizeBeforeUpdate = approvalWorkflowRepository.findAll().size();

        // Create the ApprovalWorkflow
        ApprovalWorkflowDTO approvalWorkflowDTO = approvalWorkflowMapper.toDto(approvalWorkflow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovalWorkflowMockMvc.perform(put("/api/approval-workflows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(approvalWorkflowDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApprovalWorkflow in the database
        List<ApprovalWorkflow> approvalWorkflowList = approvalWorkflowRepository.findAll();
        assertThat(approvalWorkflowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApprovalWorkflow() throws Exception {
        // Initialize the database
        approvalWorkflowRepository.saveAndFlush(approvalWorkflow);

        int databaseSizeBeforeDelete = approvalWorkflowRepository.findAll().size();

        // Delete the approvalWorkflow
        restApprovalWorkflowMockMvc.perform(delete("/api/approval-workflows/{id}", approvalWorkflow.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApprovalWorkflow> approvalWorkflowList = approvalWorkflowRepository.findAll();
        assertThat(approvalWorkflowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
