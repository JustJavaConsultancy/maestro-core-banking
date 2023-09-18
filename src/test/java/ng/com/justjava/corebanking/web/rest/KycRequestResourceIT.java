package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.KycRequest;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.enumeration.KycRequestStatus;
import ng.com.justjava.corebanking.repository.KycRequestRepository;
import ng.com.justjava.corebanking.service.KycRequestService;
import ng.com.justjava.corebanking.service.dto.KycRequestDTO;
import ng.com.justjava.corebanking.service.mapper.KycRequestMapper;
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
 * Integration tests for the {@link KycRequestResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class KycRequestResourceIT {

    private static final Integer DEFAULT_CURRENT_LEVEL = 1;
    private static final Integer UPDATED_CURRENT_LEVEL = 2;

    private static final KycRequestStatus DEFAULT_STATUS = KycRequestStatus.AWAITING_APPROVAL;
    private static final KycRequestStatus UPDATED_STATUS = KycRequestStatus.APPROVED;

    @Autowired
    private KycRequestRepository kycRequestRepository;

    @Autowired
    private KycRequestMapper kycRequestMapper;

    @Autowired
    private KycRequestService kycRequestService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKycRequestMockMvc;

    private KycRequest kycRequest;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycRequest createEntity(EntityManager em) {
        KycRequest kycRequest = new KycRequest()
            .currentLevel(DEFAULT_CURRENT_LEVEL)
            .status(DEFAULT_STATUS);
        // Add required entity
        Profile profile;
        if (TestUtil.findAll(em, Profile.class).isEmpty()) {
            profile = ProfileResourceIT.createEntity(em);
            em.persist(profile);
            em.flush();
        } else {
            profile = TestUtil.findAll(em, Profile.class).get(0);
        }
        kycRequest.setProfile(profile);
        // Add required entity
        kycRequest.setSenderProfile(profile);
        return kycRequest;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycRequest createUpdatedEntity(EntityManager em) {
        KycRequest kycRequest = new KycRequest()
            .currentLevel(UPDATED_CURRENT_LEVEL)
            .status(UPDATED_STATUS);
        // Add required entity
        Profile profile;
        if (TestUtil.findAll(em, Profile.class).isEmpty()) {
            profile = ProfileResourceIT.createUpdatedEntity(em);
            em.persist(profile);
            em.flush();
        } else {
            profile = TestUtil.findAll(em, Profile.class).get(0);
        }
        kycRequest.setProfile(profile);
        // Add required entity
        kycRequest.setSenderProfile(profile);
        return kycRequest;
    }

    @BeforeEach
    public void initTest() {
        kycRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createKycRequest() throws Exception {
        int databaseSizeBeforeCreate = kycRequestRepository.findAll().size();
        // Create the KycRequest
        KycRequestDTO kycRequestDTO = kycRequestMapper.toDto(kycRequest);
        restKycRequestMockMvc.perform(post("/api/kyc-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kycRequestDTO)))
            .andExpect(status().isCreated());

        // Validate the KycRequest in the database
        List<KycRequest> kycRequestList = kycRequestRepository.findAll();
        assertThat(kycRequestList).hasSize(databaseSizeBeforeCreate + 1);
        KycRequest testKycRequest = kycRequestList.get(kycRequestList.size() - 1);
        assertThat(testKycRequest.getCurrentLevel()).isEqualTo(DEFAULT_CURRENT_LEVEL);
        Assertions.assertThat(testKycRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createKycRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = kycRequestRepository.findAll().size();

        // Create the KycRequest with an existing ID
        kycRequest.setId(1L);
        KycRequestDTO kycRequestDTO = kycRequestMapper.toDto(kycRequest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKycRequestMockMvc.perform(post("/api/kyc-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kycRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the KycRequest in the database
        List<KycRequest> kycRequestList = kycRequestRepository.findAll();
        assertThat(kycRequestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCurrentLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = kycRequestRepository.findAll().size();
        // set the field null
        kycRequest.setCurrentLevel(null);

        // Create the KycRequest, which fails.
        KycRequestDTO kycRequestDTO = kycRequestMapper.toDto(kycRequest);


        restKycRequestMockMvc.perform(post("/api/kyc-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kycRequestDTO)))
            .andExpect(status().isBadRequest());

        List<KycRequest> kycRequestList = kycRequestRepository.findAll();
        assertThat(kycRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = kycRequestRepository.findAll().size();
        // set the field null
        kycRequest.setStatus(null);

        // Create the KycRequest, which fails.
        KycRequestDTO kycRequestDTO = kycRequestMapper.toDto(kycRequest);


        restKycRequestMockMvc.perform(post("/api/kyc-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kycRequestDTO)))
            .andExpect(status().isBadRequest());

        List<KycRequest> kycRequestList = kycRequestRepository.findAll();
        assertThat(kycRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllKycRequests() throws Exception {
        // Initialize the database
        kycRequestRepository.saveAndFlush(kycRequest);

        // Get all the kycRequestList
        restKycRequestMockMvc.perform(get("/api/kyc-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kycRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].currentLevel").value(hasItem(DEFAULT_CURRENT_LEVEL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getKycRequest() throws Exception {
        // Initialize the database
        kycRequestRepository.saveAndFlush(kycRequest);

        // Get the kycRequest
        restKycRequestMockMvc.perform(get("/api/kyc-requests/{id}", kycRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kycRequest.getId().intValue()))
            .andExpect(jsonPath("$.currentLevel").value(DEFAULT_CURRENT_LEVEL))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingKycRequest() throws Exception {
        // Get the kycRequest
        restKycRequestMockMvc.perform(get("/api/kyc-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKycRequest() throws Exception {
        // Initialize the database
        kycRequestRepository.saveAndFlush(kycRequest);

        int databaseSizeBeforeUpdate = kycRequestRepository.findAll().size();

        // Update the kycRequest
        KycRequest updatedKycRequest = kycRequestRepository.findById(kycRequest.getId()).get();
        // Disconnect from session so that the updates on updatedKycRequest are not directly saved in db
        em.detach(updatedKycRequest);
        updatedKycRequest
            .currentLevel(UPDATED_CURRENT_LEVEL)
            .status(UPDATED_STATUS);
        KycRequestDTO kycRequestDTO = kycRequestMapper.toDto(updatedKycRequest);

        restKycRequestMockMvc.perform(put("/api/kyc-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kycRequestDTO)))
            .andExpect(status().isOk());

        // Validate the KycRequest in the database
        List<KycRequest> kycRequestList = kycRequestRepository.findAll();
        assertThat(kycRequestList).hasSize(databaseSizeBeforeUpdate);
        KycRequest testKycRequest = kycRequestList.get(kycRequestList.size() - 1);
        assertThat(testKycRequest.getCurrentLevel()).isEqualTo(UPDATED_CURRENT_LEVEL);
        Assertions.assertThat(testKycRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingKycRequest() throws Exception {
        int databaseSizeBeforeUpdate = kycRequestRepository.findAll().size();

        // Create the KycRequest
        KycRequestDTO kycRequestDTO = kycRequestMapper.toDto(kycRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycRequestMockMvc.perform(put("/api/kyc-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(kycRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the KycRequest in the database
        List<KycRequest> kycRequestList = kycRequestRepository.findAll();
        assertThat(kycRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteKycRequest() throws Exception {
        // Initialize the database
        kycRequestRepository.saveAndFlush(kycRequest);

        int databaseSizeBeforeDelete = kycRequestRepository.findAll().size();

        // Delete the kycRequest
        restKycRequestMockMvc.perform(delete("/api/kyc-requests/{id}", kycRequest.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<KycRequest> kycRequestList = kycRequestRepository.findAll();
        assertThat(kycRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
