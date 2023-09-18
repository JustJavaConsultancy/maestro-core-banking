package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.ApprovalGroup;
import ng.com.justjava.corebanking.repository.ApprovalGroupRepository;
import ng.com.justjava.corebanking.service.ApprovalGroupService;
import ng.com.justjava.corebanking.service.dto.ApprovalGroupDTO;
import ng.com.justjava.corebanking.service.mapper.ApprovalGroupMapper;
import ng.com.justjava.corebanking.ApiGatewayApp;

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
 * Integration tests for the {@link ApprovalGroupResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ApprovalGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ApprovalGroupRepository approvalGroupRepository;

    @Autowired
    private ApprovalGroupMapper approvalGroupMapper;

    @Autowired
    private ApprovalGroupService approvalGroupService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApprovalGroupMockMvc;

    private ApprovalGroup approvalGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalGroup createEntity(EntityManager em) {
        ApprovalGroup approvalGroup = new ApprovalGroup()
            .name(DEFAULT_NAME);
        return approvalGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalGroup createUpdatedEntity(EntityManager em) {
        ApprovalGroup approvalGroup = new ApprovalGroup()
            .name(UPDATED_NAME);
        return approvalGroup;
    }

    @BeforeEach
    public void initTest() {
        approvalGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createApprovalGroup() throws Exception {
        int databaseSizeBeforeCreate = approvalGroupRepository.findAll().size();
        // Create the ApprovalGroup
        ApprovalGroupDTO approvalGroupDTO = approvalGroupMapper.toDto(approvalGroup);
        restApprovalGroupMockMvc.perform(post("/api/approval-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(approvalGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the ApprovalGroup in the database
        List<ApprovalGroup> approvalGroupList = approvalGroupRepository.findAll();
        assertThat(approvalGroupList).hasSize(databaseSizeBeforeCreate + 1);
        ApprovalGroup testApprovalGroup = approvalGroupList.get(approvalGroupList.size() - 1);
        assertThat(testApprovalGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createApprovalGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = approvalGroupRepository.findAll().size();

        // Create the ApprovalGroup with an existing ID
        approvalGroup.setId(1L);
        ApprovalGroupDTO approvalGroupDTO = approvalGroupMapper.toDto(approvalGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApprovalGroupMockMvc.perform(post("/api/approval-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(approvalGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApprovalGroup in the database
        List<ApprovalGroup> approvalGroupList = approvalGroupRepository.findAll();
        assertThat(approvalGroupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalGroupRepository.findAll().size();
        // set the field null
        approvalGroup.setName(null);

        // Create the ApprovalGroup, which fails.
        ApprovalGroupDTO approvalGroupDTO = approvalGroupMapper.toDto(approvalGroup);


        restApprovalGroupMockMvc.perform(post("/api/approval-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(approvalGroupDTO)))
            .andExpect(status().isBadRequest());

        List<ApprovalGroup> approvalGroupList = approvalGroupRepository.findAll();
        assertThat(approvalGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApprovalGroups() throws Exception {
        // Initialize the database
        approvalGroupRepository.saveAndFlush(approvalGroup);

        // Get all the approvalGroupList
        restApprovalGroupMockMvc.perform(get("/api/approval-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(approvalGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void getApprovalGroup() throws Exception {
        // Initialize the database
        approvalGroupRepository.saveAndFlush(approvalGroup);

        // Get the approvalGroup
        restApprovalGroupMockMvc.perform(get("/api/approval-groups/{id}", approvalGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(approvalGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingApprovalGroup() throws Exception {
        // Get the approvalGroup
        restApprovalGroupMockMvc.perform(get("/api/approval-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApprovalGroup() throws Exception {
        // Initialize the database
        approvalGroupRepository.saveAndFlush(approvalGroup);

        int databaseSizeBeforeUpdate = approvalGroupRepository.findAll().size();

        // Update the approvalGroup
        ApprovalGroup updatedApprovalGroup = approvalGroupRepository.findById(approvalGroup.getId()).get();
        // Disconnect from session so that the updates on updatedApprovalGroup are not directly saved in db
        em.detach(updatedApprovalGroup);
        updatedApprovalGroup
            .name(UPDATED_NAME);
        ApprovalGroupDTO approvalGroupDTO = approvalGroupMapper.toDto(updatedApprovalGroup);

        restApprovalGroupMockMvc.perform(put("/api/approval-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(approvalGroupDTO)))
            .andExpect(status().isOk());

        // Validate the ApprovalGroup in the database
        List<ApprovalGroup> approvalGroupList = approvalGroupRepository.findAll();
        assertThat(approvalGroupList).hasSize(databaseSizeBeforeUpdate);
        ApprovalGroup testApprovalGroup = approvalGroupList.get(approvalGroupList.size() - 1);
        assertThat(testApprovalGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingApprovalGroup() throws Exception {
        int databaseSizeBeforeUpdate = approvalGroupRepository.findAll().size();

        // Create the ApprovalGroup
        ApprovalGroupDTO approvalGroupDTO = approvalGroupMapper.toDto(approvalGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovalGroupMockMvc.perform(put("/api/approval-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(approvalGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApprovalGroup in the database
        List<ApprovalGroup> approvalGroupList = approvalGroupRepository.findAll();
        assertThat(approvalGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApprovalGroup() throws Exception {
        // Initialize the database
        approvalGroupRepository.saveAndFlush(approvalGroup);

        int databaseSizeBeforeDelete = approvalGroupRepository.findAll().size();

        // Delete the approvalGroup
        restApprovalGroupMockMvc.perform(delete("/api/approval-groups/{id}", approvalGroup.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApprovalGroup> approvalGroupList = approvalGroupRepository.findAll();
        assertThat(approvalGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
