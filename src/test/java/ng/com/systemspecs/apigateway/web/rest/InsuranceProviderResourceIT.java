package ng.com.systemspecs.apigateway.web.rest;

import ng.com.systemspecs.apigateway.ApiGatewayApp;
import ng.com.systemspecs.apigateway.domain.InsuranceProvider;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.repository.InsuranceProviderRepository;
import ng.com.systemspecs.apigateway.service.InsuranceProviderService;
import ng.com.systemspecs.apigateway.service.dto.InsuranceProviderDTO;
import ng.com.systemspecs.apigateway.service.mapper.InsuranceProviderMapper;
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
 * Integration tests for the {@link InsuranceProviderResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InsuranceProviderResourceIT {

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;

    private static final String DEFAULT_PREFERRED_TENURE = "AAAAAAAAAA";
    private static final String UPDATED_PREFERRED_TENURE = "BBBBBBBBBB";

    @Autowired
    private InsuranceProviderRepository insuranceProviderRepository;

    @Autowired
    private InsuranceProviderMapper insuranceProviderMapper;

    @Autowired
    private InsuranceProviderService insuranceProviderService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceProviderMockMvc;

    private InsuranceProvider insuranceProvider;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceProvider createEntity(EntityManager em) {
        InsuranceProvider insuranceProvider = new InsuranceProvider()
            .rate(DEFAULT_RATE)
            .preferred_tenure(DEFAULT_PREFERRED_TENURE);
        // Add required entity
        Profile profile;
        if (TestUtil.findAll(em, Profile.class).isEmpty()) {
            profile = ProfileResourceIT.createEntity(em);
            em.persist(profile);
            em.flush();
        } else {
            profile = TestUtil.findAll(em, Profile.class).get(0);
        }
        insuranceProvider.setProfile(profile);
        return insuranceProvider;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceProvider createUpdatedEntity(EntityManager em) {
        InsuranceProvider insuranceProvider = new InsuranceProvider()
            .rate(UPDATED_RATE)
            .preferred_tenure(UPDATED_PREFERRED_TENURE);
        // Add required entity
        Profile profile;
        if (TestUtil.findAll(em, Profile.class).isEmpty()) {
            profile = ProfileResourceIT.createUpdatedEntity(em);
            em.persist(profile);
            em.flush();
        } else {
            profile = TestUtil.findAll(em, Profile.class).get(0);
        }
        insuranceProvider.setProfile(profile);
        return insuranceProvider;
    }

    @BeforeEach
    public void initTest() {
        insuranceProvider = createEntity(em);
    }

    @Test
    @Transactional
    public void createInsuranceProvider() throws Exception {
        int databaseSizeBeforeCreate = insuranceProviderRepository.findAll().size();
        // Create the InsuranceProvider
        InsuranceProviderDTO insuranceProviderDTO = insuranceProviderMapper.toDto(insuranceProvider);
        restInsuranceProviderMockMvc.perform(post("/api/insurance-providers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProviderDTO)))
            .andExpect(status().isCreated());

        // Validate the InsuranceProvider in the database
        List<InsuranceProvider> insuranceProviderList = insuranceProviderRepository.findAll();
        assertThat(insuranceProviderList).hasSize(databaseSizeBeforeCreate + 1);
        InsuranceProvider testInsuranceProvider = insuranceProviderList.get(insuranceProviderList.size() - 1);
        assertThat(testInsuranceProvider.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testInsuranceProvider.getPreferred_tenure()).isEqualTo(DEFAULT_PREFERRED_TENURE);
    }

    @Test
    @Transactional
    public void createInsuranceProviderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = insuranceProviderRepository.findAll().size();

        // Create the InsuranceProvider with an existing ID
        insuranceProvider.setId(1L);
        InsuranceProviderDTO insuranceProviderDTO = insuranceProviderMapper.toDto(insuranceProvider);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceProviderMockMvc.perform(post("/api/insurance-providers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProviderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InsuranceProvider in the database
        List<InsuranceProvider> insuranceProviderList = insuranceProviderRepository.findAll();
        assertThat(insuranceProviderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceProviderRepository.findAll().size();
        // set the field null
        insuranceProvider.setRate(null);

        // Create the InsuranceProvider, which fails.
        InsuranceProviderDTO insuranceProviderDTO = insuranceProviderMapper.toDto(insuranceProvider);


        restInsuranceProviderMockMvc.perform(post("/api/insurance-providers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProviderDTO)))
            .andExpect(status().isBadRequest());

        List<InsuranceProvider> insuranceProviderList = insuranceProviderRepository.findAll();
        assertThat(insuranceProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPreferred_tenureIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceProviderRepository.findAll().size();
        // set the field null
        insuranceProvider.setPreferred_tenure(null);

        // Create the InsuranceProvider, which fails.
        InsuranceProviderDTO insuranceProviderDTO = insuranceProviderMapper.toDto(insuranceProvider);


        restInsuranceProviderMockMvc.perform(post("/api/insurance-providers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProviderDTO)))
            .andExpect(status().isBadRequest());

        List<InsuranceProvider> insuranceProviderList = insuranceProviderRepository.findAll();
        assertThat(insuranceProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInsuranceProviders() throws Exception {
        // Initialize the database
        insuranceProviderRepository.saveAndFlush(insuranceProvider);

        // Get all the insuranceProviderList
        restInsuranceProviderMockMvc.perform(get("/api/insurance-providers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].preferred_tenure").value(hasItem(DEFAULT_PREFERRED_TENURE)));
    }

    @Test
    @Transactional
    public void getInsuranceProvider() throws Exception {
        // Initialize the database
        insuranceProviderRepository.saveAndFlush(insuranceProvider);

        // Get the insuranceProvider
        restInsuranceProviderMockMvc.perform(get("/api/insurance-providers/{id}", insuranceProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceProvider.getId().intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
            .andExpect(jsonPath("$.preferred_tenure").value(DEFAULT_PREFERRED_TENURE));
    }

    @Test
    @Transactional
    public void getNonExistingInsuranceProvider() throws Exception {
        // Get the insuranceProvider
        restInsuranceProviderMockMvc.perform(get("/api/insurance-providers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInsuranceProvider() throws Exception {
        // Initialize the database
        insuranceProviderRepository.saveAndFlush(insuranceProvider);

        int databaseSizeBeforeUpdate = insuranceProviderRepository.findAll().size();

        // Update the insuranceProvider
        InsuranceProvider updatedInsuranceProvider = insuranceProviderRepository.findById(insuranceProvider.getId()).get();
        // Disconnect from session so that the updates on updatedInsuranceProvider are not directly saved in db
        em.detach(updatedInsuranceProvider);
        updatedInsuranceProvider
            .rate(UPDATED_RATE)
            .preferred_tenure(UPDATED_PREFERRED_TENURE);
        InsuranceProviderDTO insuranceProviderDTO = insuranceProviderMapper.toDto(updatedInsuranceProvider);

        restInsuranceProviderMockMvc.perform(put("/api/insurance-providers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProviderDTO)))
            .andExpect(status().isOk());

        // Validate the InsuranceProvider in the database
        List<InsuranceProvider> insuranceProviderList = insuranceProviderRepository.findAll();
        assertThat(insuranceProviderList).hasSize(databaseSizeBeforeUpdate);
        InsuranceProvider testInsuranceProvider = insuranceProviderList.get(insuranceProviderList.size() - 1);
        assertThat(testInsuranceProvider.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testInsuranceProvider.getPreferred_tenure()).isEqualTo(UPDATED_PREFERRED_TENURE);
    }

    @Test
    @Transactional
    public void updateNonExistingInsuranceProvider() throws Exception {
        int databaseSizeBeforeUpdate = insuranceProviderRepository.findAll().size();

        // Create the InsuranceProvider
        InsuranceProviderDTO insuranceProviderDTO = insuranceProviderMapper.toDto(insuranceProvider);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceProviderMockMvc.perform(put("/api/insurance-providers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(insuranceProviderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InsuranceProvider in the database
        List<InsuranceProvider> insuranceProviderList = insuranceProviderRepository.findAll();
        assertThat(insuranceProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInsuranceProvider() throws Exception {
        // Initialize the database
        insuranceProviderRepository.saveAndFlush(insuranceProvider);

        int databaseSizeBeforeDelete = insuranceProviderRepository.findAll().size();

        // Delete the insuranceProvider
        restInsuranceProviderMockMvc.perform(delete("/api/insurance-providers/{id}", insuranceProvider.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InsuranceProvider> insuranceProviderList = insuranceProviderRepository.findAll();
        assertThat(insuranceProviderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
