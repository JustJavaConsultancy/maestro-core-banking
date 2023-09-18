package ng.com.systemspecs.apigateway.web.rest;

import ng.com.systemspecs.apigateway.ApiGatewayApp;
import ng.com.systemspecs.apigateway.domain.InsuranceType;
import ng.com.systemspecs.apigateway.repository.InsuranceTypeRepository;
import ng.com.systemspecs.apigateway.service.InsuranceTypeService;
import ng.com.systemspecs.apigateway.service.dto.InsuranceTypeDTO;
import ng.com.systemspecs.apigateway.service.mapper.InsuranceTypeMapper;

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
 * Integration tests for the {@link InsuranceTypeResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InsuranceTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_CHARGE = 1D;
    private static final Double UPDATED_CHARGE = 2D;

    @Autowired
    private InsuranceTypeRepository insuranceTypeRepository;

    @Autowired
    private InsuranceTypeMapper insuranceTypeMapper;

    @Autowired
    private InsuranceTypeService insuranceTypeService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceTypeMockMvc;

    private InsuranceType insuranceType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceType createEntity(EntityManager em) {
        InsuranceType insuranceType = new InsuranceType()
            .name(DEFAULT_NAME)
            .charge(DEFAULT_CHARGE);
        return insuranceType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceType createUpdatedEntity(EntityManager em) {
        InsuranceType insuranceType = new InsuranceType()
            .name(UPDATED_NAME)
            .charge(UPDATED_CHARGE);
        return insuranceType;
    }

    @BeforeEach
    public void initTest() {
        insuranceType = createEntity(em);
    }

    @Test
    @Transactional
    public void createInsuranceType() throws Exception {
        int databaseSizeBeforeCreate = insuranceTypeRepository.findAll().size();
        // Create the InsuranceType
        InsuranceTypeDTO insuranceTypeDTO = insuranceTypeMapper.toDto(insuranceType);
        restInsuranceTypeMockMvc.perform(post("/api/insurance-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(insuranceTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the InsuranceType in the database
        List<InsuranceType> insuranceTypeList = insuranceTypeRepository.findAll();
        assertThat(insuranceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        InsuranceType testInsuranceType = insuranceTypeList.get(insuranceTypeList.size() - 1);
        assertThat(testInsuranceType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInsuranceType.getCharge()).isEqualTo(DEFAULT_CHARGE);
    }

    @Test
    @Transactional
    public void createInsuranceTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = insuranceTypeRepository.findAll().size();

        // Create the InsuranceType with an existing ID
        insuranceType.setId(1L);
        InsuranceTypeDTO insuranceTypeDTO = insuranceTypeMapper.toDto(insuranceType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceTypeMockMvc.perform(post("/api/insurance-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(insuranceTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InsuranceType in the database
        List<InsuranceType> insuranceTypeList = insuranceTypeRepository.findAll();
        assertThat(insuranceTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceTypeRepository.findAll().size();
        // set the field null
        insuranceType.setName(null);

        // Create the InsuranceType, which fails.
        InsuranceTypeDTO insuranceTypeDTO = insuranceTypeMapper.toDto(insuranceType);


        restInsuranceTypeMockMvc.perform(post("/api/insurance-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(insuranceTypeDTO)))
            .andExpect(status().isBadRequest());

        List<InsuranceType> insuranceTypeList = insuranceTypeRepository.findAll();
        assertThat(insuranceTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInsuranceTypes() throws Exception {
        // Initialize the database
        insuranceTypeRepository.saveAndFlush(insuranceType);

        // Get all the insuranceTypeList
        restInsuranceTypeMockMvc.perform(get("/api/insurance-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].charge").value(hasItem(DEFAULT_CHARGE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getInsuranceType() throws Exception {
        // Initialize the database
        insuranceTypeRepository.saveAndFlush(insuranceType);

        // Get the insuranceType
        restInsuranceTypeMockMvc.perform(get("/api/insurance-types/{id}", insuranceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.charge").value(DEFAULT_CHARGE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingInsuranceType() throws Exception {
        // Get the insuranceType
        restInsuranceTypeMockMvc.perform(get("/api/insurance-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInsuranceType() throws Exception {
        // Initialize the database
        insuranceTypeRepository.saveAndFlush(insuranceType);

        int databaseSizeBeforeUpdate = insuranceTypeRepository.findAll().size();

        // Update the insuranceType
        InsuranceType updatedInsuranceType = insuranceTypeRepository.findById(insuranceType.getId()).get();
        // Disconnect from session so that the updates on updatedInsuranceType are not directly saved in db
        em.detach(updatedInsuranceType);
        updatedInsuranceType
            .name(UPDATED_NAME)
            .charge(UPDATED_CHARGE);
        InsuranceTypeDTO insuranceTypeDTO = insuranceTypeMapper.toDto(updatedInsuranceType);

        restInsuranceTypeMockMvc.perform(put("/api/insurance-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(insuranceTypeDTO)))
            .andExpect(status().isOk());

        // Validate the InsuranceType in the database
        List<InsuranceType> insuranceTypeList = insuranceTypeRepository.findAll();
        assertThat(insuranceTypeList).hasSize(databaseSizeBeforeUpdate);
        InsuranceType testInsuranceType = insuranceTypeList.get(insuranceTypeList.size() - 1);
        assertThat(testInsuranceType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInsuranceType.getCharge()).isEqualTo(UPDATED_CHARGE);
    }

    @Test
    @Transactional
    public void updateNonExistingInsuranceType() throws Exception {
        int databaseSizeBeforeUpdate = insuranceTypeRepository.findAll().size();

        // Create the InsuranceType
        InsuranceTypeDTO insuranceTypeDTO = insuranceTypeMapper.toDto(insuranceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceTypeMockMvc.perform(put("/api/insurance-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(insuranceTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InsuranceType in the database
        List<InsuranceType> insuranceTypeList = insuranceTypeRepository.findAll();
        assertThat(insuranceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInsuranceType() throws Exception {
        // Initialize the database
        insuranceTypeRepository.saveAndFlush(insuranceType);

        int databaseSizeBeforeDelete = insuranceTypeRepository.findAll().size();

        // Delete the insuranceType
        restInsuranceTypeMockMvc.perform(delete("/api/insurance-types/{id}", insuranceType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InsuranceType> insuranceTypeList = insuranceTypeRepository.findAll();
        assertThat(insuranceTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
