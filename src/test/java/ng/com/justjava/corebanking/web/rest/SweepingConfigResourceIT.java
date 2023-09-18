package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.SweepingConfig;
import ng.com.justjava.corebanking.repository.SweepingConfigRepository;
import ng.com.justjava.corebanking.service.SweepingConfigService;
import ng.com.justjava.corebanking.service.dto.SweepingConfigDTO;
import ng.com.justjava.corebanking.service.mapper.SweepingConfigMapper;
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
 * Integration tests for the {@link SweepingConfigResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SweepingConfigResourceIT {

    private static final String DEFAULT_VENDOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_BANK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_BANK_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_ACCOUNT = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Double DEFAULT_PERCENTAGE = 1D;
    private static final Double UPDATED_PERCENTAGE = 2D;

    @Autowired
    private SweepingConfigRepository sweepingConfigRepository;

    @Autowired
    private SweepingConfigMapper sweepingConfigMapper;

    @Autowired
    private SweepingConfigService sweepingConfigService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSweepingConfigMockMvc;

    private SweepingConfig sweepingConfig;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SweepingConfig createEntity(EntityManager em) {
        SweepingConfig sweepingConfig = new SweepingConfig()
            .vendorName(DEFAULT_VENDOR_NAME)
            .sourceAccount(DEFAULT_SOURCE_ACCOUNT)
            .destinationBankCode(DEFAULT_DESTINATION_BANK_CODE)
            .destinationAccount(DEFAULT_DESTINATION_ACCOUNT)
            .amount(DEFAULT_AMOUNT)
            .percentage(DEFAULT_PERCENTAGE);
        return sweepingConfig;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SweepingConfig createUpdatedEntity(EntityManager em) {
        SweepingConfig sweepingConfig = new SweepingConfig()
            .vendorName(UPDATED_VENDOR_NAME)
            .sourceAccount(UPDATED_SOURCE_ACCOUNT)
            .destinationBankCode(UPDATED_DESTINATION_BANK_CODE)
            .destinationAccount(UPDATED_DESTINATION_ACCOUNT)
            .amount(UPDATED_AMOUNT)
            .percentage(UPDATED_PERCENTAGE);
        return sweepingConfig;
    }

    @BeforeEach
    public void initTest() {
        sweepingConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createSweepingConfig() throws Exception {
        int databaseSizeBeforeCreate = sweepingConfigRepository.findAll().size();
        // Create the SweepingConfig
        SweepingConfigDTO sweepingConfigDTO = sweepingConfigMapper.toDto(sweepingConfig);
        restSweepingConfigMockMvc.perform(post("/api/sweeping-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sweepingConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the SweepingConfig in the database
        List<SweepingConfig> sweepingConfigList = sweepingConfigRepository.findAll();
        assertThat(sweepingConfigList).hasSize(databaseSizeBeforeCreate + 1);
        SweepingConfig testSweepingConfig = sweepingConfigList.get(sweepingConfigList.size() - 1);
        assertThat(testSweepingConfig.getVendorName()).isEqualTo(DEFAULT_VENDOR_NAME);
        assertThat(testSweepingConfig.getSourceAccount()).isEqualTo(DEFAULT_SOURCE_ACCOUNT);
        assertThat(testSweepingConfig.getDestinationBankCode()).isEqualTo(DEFAULT_DESTINATION_BANK_CODE);
        assertThat(testSweepingConfig.getDestinationAccount()).isEqualTo(DEFAULT_DESTINATION_ACCOUNT);
        assertThat(testSweepingConfig.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testSweepingConfig.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
    }

    @Test
    @Transactional
    public void createSweepingConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sweepingConfigRepository.findAll().size();

        // Create the SweepingConfig with an existing ID
        sweepingConfig.setId(1L);
        SweepingConfigDTO sweepingConfigDTO = sweepingConfigMapper.toDto(sweepingConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSweepingConfigMockMvc.perform(post("/api/sweeping-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sweepingConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SweepingConfig in the database
        List<SweepingConfig> sweepingConfigList = sweepingConfigRepository.findAll();
        assertThat(sweepingConfigList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkVendorNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sweepingConfigRepository.findAll().size();
        // set the field null
        sweepingConfig.setVendorName(null);

        // Create the SweepingConfig, which fails.
        SweepingConfigDTO sweepingConfigDTO = sweepingConfigMapper.toDto(sweepingConfig);


        restSweepingConfigMockMvc.perform(post("/api/sweeping-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sweepingConfigDTO)))
            .andExpect(status().isBadRequest());

        List<SweepingConfig> sweepingConfigList = sweepingConfigRepository.findAll();
        assertThat(sweepingConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSourceAccountIsRequired() throws Exception {
        int databaseSizeBeforeTest = sweepingConfigRepository.findAll().size();
        // set the field null
        sweepingConfig.setSourceAccount(null);

        // Create the SweepingConfig, which fails.
        SweepingConfigDTO sweepingConfigDTO = sweepingConfigMapper.toDto(sweepingConfig);


        restSweepingConfigMockMvc.perform(post("/api/sweeping-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sweepingConfigDTO)))
            .andExpect(status().isBadRequest());

        List<SweepingConfig> sweepingConfigList = sweepingConfigRepository.findAll();
        assertThat(sweepingConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDestinationBankCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sweepingConfigRepository.findAll().size();
        // set the field null
        sweepingConfig.setDestinationBankCode(null);

        // Create the SweepingConfig, which fails.
        SweepingConfigDTO sweepingConfigDTO = sweepingConfigMapper.toDto(sweepingConfig);


        restSweepingConfigMockMvc.perform(post("/api/sweeping-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sweepingConfigDTO)))
            .andExpect(status().isBadRequest());

        List<SweepingConfig> sweepingConfigList = sweepingConfigRepository.findAll();
        assertThat(sweepingConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = sweepingConfigRepository.findAll().size();
        // set the field null
        sweepingConfig.setAmount(null);

        // Create the SweepingConfig, which fails.
        SweepingConfigDTO sweepingConfigDTO = sweepingConfigMapper.toDto(sweepingConfig);


        restSweepingConfigMockMvc.perform(post("/api/sweeping-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sweepingConfigDTO)))
            .andExpect(status().isBadRequest());

        List<SweepingConfig> sweepingConfigList = sweepingConfigRepository.findAll();
        assertThat(sweepingConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSweepingConfigs() throws Exception {
        // Initialize the database
        sweepingConfigRepository.saveAndFlush(sweepingConfig);

        // Get all the sweepingConfigList
        restSweepingConfigMockMvc.perform(get("/api/sweeping-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sweepingConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].vendorName").value(hasItem(DEFAULT_VENDOR_NAME)))
            .andExpect(jsonPath("$.[*].sourceAccount").value(hasItem(DEFAULT_SOURCE_ACCOUNT)))
            .andExpect(jsonPath("$.[*].destinationBankCode").value(hasItem(DEFAULT_DESTINATION_BANK_CODE)))
            .andExpect(jsonPath("$.[*].destinationAccount").value(hasItem(DEFAULT_DESTINATION_ACCOUNT)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())));
    }

    @Test
    @Transactional
    public void getSweepingConfig() throws Exception {
        // Initialize the database
        sweepingConfigRepository.saveAndFlush(sweepingConfig);

        // Get the sweepingConfig
        restSweepingConfigMockMvc.perform(get("/api/sweeping-configs/{id}", sweepingConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sweepingConfig.getId().intValue()))
            .andExpect(jsonPath("$.vendorName").value(DEFAULT_VENDOR_NAME))
            .andExpect(jsonPath("$.sourceAccount").value(DEFAULT_SOURCE_ACCOUNT))
            .andExpect(jsonPath("$.destinationBankCode").value(DEFAULT_DESTINATION_BANK_CODE))
            .andExpect(jsonPath("$.destinationAccount").value(DEFAULT_DESTINATION_ACCOUNT))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSweepingConfig() throws Exception {
        // Get the sweepingConfig
        restSweepingConfigMockMvc.perform(get("/api/sweeping-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSweepingConfig() throws Exception {
        // Initialize the database
        sweepingConfigRepository.saveAndFlush(sweepingConfig);

        int databaseSizeBeforeUpdate = sweepingConfigRepository.findAll().size();

        // Update the sweepingConfig
        SweepingConfig updatedSweepingConfig = sweepingConfigRepository.findById(sweepingConfig.getId()).get();
        // Disconnect from session so that the updates on updatedSweepingConfig are not directly saved in db
        em.detach(updatedSweepingConfig);
        updatedSweepingConfig
            .vendorName(UPDATED_VENDOR_NAME)
            .sourceAccount(UPDATED_SOURCE_ACCOUNT)
            .destinationBankCode(UPDATED_DESTINATION_BANK_CODE)
            .destinationAccount(UPDATED_DESTINATION_ACCOUNT)
            .amount(UPDATED_AMOUNT)
            .percentage(UPDATED_PERCENTAGE);
        SweepingConfigDTO sweepingConfigDTO = sweepingConfigMapper.toDto(updatedSweepingConfig);

        restSweepingConfigMockMvc.perform(put("/api/sweeping-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sweepingConfigDTO)))
            .andExpect(status().isOk());

        // Validate the SweepingConfig in the database
        List<SweepingConfig> sweepingConfigList = sweepingConfigRepository.findAll();
        assertThat(sweepingConfigList).hasSize(databaseSizeBeforeUpdate);
        SweepingConfig testSweepingConfig = sweepingConfigList.get(sweepingConfigList.size() - 1);
        assertThat(testSweepingConfig.getVendorName()).isEqualTo(UPDATED_VENDOR_NAME);
        assertThat(testSweepingConfig.getSourceAccount()).isEqualTo(UPDATED_SOURCE_ACCOUNT);
        assertThat(testSweepingConfig.getDestinationBankCode()).isEqualTo(UPDATED_DESTINATION_BANK_CODE);
        assertThat(testSweepingConfig.getDestinationAccount()).isEqualTo(UPDATED_DESTINATION_ACCOUNT);
        assertThat(testSweepingConfig.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testSweepingConfig.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingSweepingConfig() throws Exception {
        int databaseSizeBeforeUpdate = sweepingConfigRepository.findAll().size();

        // Create the SweepingConfig
        SweepingConfigDTO sweepingConfigDTO = sweepingConfigMapper.toDto(sweepingConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSweepingConfigMockMvc.perform(put("/api/sweeping-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sweepingConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SweepingConfig in the database
        List<SweepingConfig> sweepingConfigList = sweepingConfigRepository.findAll();
        assertThat(sweepingConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSweepingConfig() throws Exception {
        // Initialize the database
        sweepingConfigRepository.saveAndFlush(sweepingConfig);

        int databaseSizeBeforeDelete = sweepingConfigRepository.findAll().size();

        // Delete the sweepingConfig
        restSweepingConfigMockMvc.perform(delete("/api/sweeping-configs/{id}", sweepingConfig.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SweepingConfig> sweepingConfigList = sweepingConfigRepository.findAll();
        assertThat(sweepingConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
