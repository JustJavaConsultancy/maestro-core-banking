package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.Lender;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.enumeration.Tenure;
import ng.com.justjava.corebanking.repository.LenderRepository;
import ng.com.justjava.corebanking.service.LenderService;
import ng.com.justjava.corebanking.service.dto.LenderDTO;
import ng.com.justjava.corebanking.service.mapper.LenderMapper;
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
 * Integration tests for the {@link LenderResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LenderResourceIT {

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;

    private static final Tenure DEFAULT_PREFERED_TENURE = Tenure.MONTHLY;
    private static final Tenure UPDATED_PREFERED_TENURE = Tenure.QUARTERLY;

    @Autowired
    private LenderRepository lenderRepository;

    @Autowired
    private LenderMapper lenderMapper;

    @Autowired
    private LenderService lenderService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLenderMockMvc;

    private Lender lender;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lender createEntity(EntityManager em) {
        Lender lender = new Lender()
            .rate(DEFAULT_RATE)
            .preferredTenure(DEFAULT_PREFERED_TENURE);
        // Add required entity
        Profile profile;
        if (TestUtil.findAll(em, Profile.class).isEmpty()) {
            profile = ProfileResourceIT.createEntity(em);
            em.persist(profile);
            em.flush();
        } else {
            profile = TestUtil.findAll(em, Profile.class).get(0);
        }
        lender.setProfile(profile);
        return lender;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lender createUpdatedEntity(EntityManager em) {
        Lender lender = new Lender()
            .rate(UPDATED_RATE)
            .preferredTenure(UPDATED_PREFERED_TENURE);
        // Add required entity
        Profile profile;
        if (TestUtil.findAll(em, Profile.class).isEmpty()) {
            profile = ProfileResourceIT.createUpdatedEntity(em);
            em.persist(profile);
            em.flush();
        } else {
            profile = TestUtil.findAll(em, Profile.class).get(0);
        }
        lender.setProfile(profile);
        return lender;
    }

    @BeforeEach
    public void initTest() {
        lender = createEntity(em);
    }

    @Test
    @Transactional
    public void createLender() throws Exception {
        int databaseSizeBeforeCreate = lenderRepository.findAll().size();
        // Create the Lender
        LenderDTO lenderDTO = lenderMapper.toDto(lender);
        restLenderMockMvc.perform(post("/api/lenders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lenderDTO)))
            .andExpect(status().isCreated());

        // Validate the Lender in the database
        List<Lender> lenderList = lenderRepository.findAll();
        assertThat(lenderList).hasSize(databaseSizeBeforeCreate + 1);
        Lender testLender = lenderList.get(lenderList.size() - 1);
        assertThat(testLender.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testLender.getPreferredTenure()).isEqualTo(DEFAULT_PREFERED_TENURE);
    }

    @Test
    @Transactional
    public void createLenderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lenderRepository.findAll().size();

        // Create the Lender with an existing ID
        lender.setId(1L);
        LenderDTO lenderDTO = lenderMapper.toDto(lender);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLenderMockMvc.perform(post("/api/lenders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lenderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lender in the database
        List<Lender> lenderList = lenderRepository.findAll();
        assertThat(lenderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = lenderRepository.findAll().size();
        // set the field null
        lender.setRate(null);

        // Create the Lender, which fails.
        LenderDTO lenderDTO = lenderMapper.toDto(lender);


        restLenderMockMvc.perform(post("/api/lenders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lenderDTO)))
            .andExpect(status().isBadRequest());

        List<Lender> lenderList = lenderRepository.findAll();
        assertThat(lenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPreferedTenureIsRequired() throws Exception {
        int databaseSizeBeforeTest = lenderRepository.findAll().size();
        // set the field null
        lender.setPreferredTenure(null);

        // Create the Lender, which fails.
        LenderDTO lenderDTO = lenderMapper.toDto(lender);


        restLenderMockMvc.perform(post("/api/lenders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lenderDTO)))
            .andExpect(status().isBadRequest());

        List<Lender> lenderList = lenderRepository.findAll();
        assertThat(lenderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLenders() throws Exception {
        // Initialize the database
        lenderRepository.saveAndFlush(lender);

        // Get all the lenderList
        restLenderMockMvc.perform(get("/api/lenders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lender.getId().intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].preferedTenure").value(hasItem(DEFAULT_PREFERED_TENURE.toString())));
    }

    @Test
    @Transactional
    public void getLender() throws Exception {
        // Initialize the database
        lenderRepository.saveAndFlush(lender);

        // Get the lender
        restLenderMockMvc.perform(get("/api/lenders/{id}", lender.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lender.getId().intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
            .andExpect(jsonPath("$.preferedTenure").value(DEFAULT_PREFERED_TENURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLender() throws Exception {
        // Get the lender
        restLenderMockMvc.perform(get("/api/lenders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLender() throws Exception {
        // Initialize the database
        lenderRepository.saveAndFlush(lender);

        int databaseSizeBeforeUpdate = lenderRepository.findAll().size();

        // Update the lender
        Lender updatedLender = lenderRepository.findById(lender.getId()).get();
        // Disconnect from session so that the updates on updatedLender are not directly saved in db
        em.detach(updatedLender);
        updatedLender
            .rate(UPDATED_RATE)
            .preferredTenure(UPDATED_PREFERED_TENURE);
        LenderDTO lenderDTO = lenderMapper.toDto(updatedLender);

        restLenderMockMvc.perform(put("/api/lenders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lenderDTO)))
            .andExpect(status().isOk());

        // Validate the Lender in the database
        List<Lender> lenderList = lenderRepository.findAll();
        assertThat(lenderList).hasSize(databaseSizeBeforeUpdate);
        Lender testLender = lenderList.get(lenderList.size() - 1);
        assertThat(testLender.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testLender.getPreferredTenure()).isEqualTo(UPDATED_PREFERED_TENURE);
    }

    @Test
    @Transactional
    public void updateNonExistingLender() throws Exception {
        int databaseSizeBeforeUpdate = lenderRepository.findAll().size();

        // Create the Lender
        LenderDTO lenderDTO = lenderMapper.toDto(lender);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLenderMockMvc.perform(put("/api/lenders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lenderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lender in the database
        List<Lender> lenderList = lenderRepository.findAll();
        assertThat(lenderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLender() throws Exception {
        // Initialize the database
        lenderRepository.saveAndFlush(lender);

        int databaseSizeBeforeDelete = lenderRepository.findAll().size();

        // Delete the lender
        restLenderMockMvc.perform(delete("/api/lenders/{id}", lender.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lender> lenderList = lenderRepository.findAll();
        assertThat(lenderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
