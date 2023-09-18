package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.BonusPoint;
import ng.com.justjava.corebanking.domain.Journal;
import ng.com.justjava.corebanking.repository.BonusPointRepository;
import ng.com.justjava.corebanking.service.BonusPointService;
import ng.com.justjava.corebanking.service.dto.BonusPointDTO;
import ng.com.justjava.corebanking.service.mapper.BonusPointMapper;
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
 * Integration tests for the {@link BonusPointResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BonusPointResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    @Autowired
    private BonusPointRepository bonusPointRepository;

    @Autowired
    private BonusPointMapper bonusPointMapper;

    @Autowired
    private BonusPointService bonusPointService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBonusPointMockMvc;

    private BonusPoint bonusPoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BonusPoint createEntity(EntityManager em) {
        BonusPoint bonusPoint = new BonusPoint()
            .amount(DEFAULT_AMOUNT)
            .remark(DEFAULT_REMARK);
        // Add required entity
        Journal journal;
        if (TestUtil.findAll(em, Journal.class).isEmpty()) {
            journal = null;
            em.persist(journal);
            em.flush();
        } else {
            journal = TestUtil.findAll(em, Journal.class).get(0);
        }
        bonusPoint.setJournal(journal);
        return bonusPoint;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BonusPoint createUpdatedEntity(EntityManager em) {
        BonusPoint bonusPoint = new BonusPoint()
            .amount(UPDATED_AMOUNT)
            .remark(UPDATED_REMARK);
        // Add required entity
        Journal journal;
        if (TestUtil.findAll(em, Journal.class).isEmpty()) {
            journal = null;
            em.persist(journal);
            em.flush();
        } else {
            journal = TestUtil.findAll(em, Journal.class).get(0);
        }
        bonusPoint.setJournal(journal);
        return bonusPoint;
    }

    @BeforeEach
    public void initTest() {
        bonusPoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createBonusPoint() throws Exception {
        int databaseSizeBeforeCreate = bonusPointRepository.findAll().size();
        // Create the BonusPoint
        BonusPointDTO bonusPointDTO = bonusPointMapper.toDto(bonusPoint);
        restBonusPointMockMvc.perform(post("/api/bonus-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bonusPointDTO)))
            .andExpect(status().isCreated());

        // Validate the BonusPoint in the database
        List<BonusPoint> bonusPointList = bonusPointRepository.findAll();
        assertThat(bonusPointList).hasSize(databaseSizeBeforeCreate + 1);
        BonusPoint testBonusPoint = bonusPointList.get(bonusPointList.size() - 1);
        assertThat(testBonusPoint.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testBonusPoint.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    public void createBonusPointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bonusPointRepository.findAll().size();

        // Create the BonusPoint with an existing ID
        bonusPoint.setId(1L);
        BonusPointDTO bonusPointDTO = bonusPointMapper.toDto(bonusPoint);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBonusPointMockMvc.perform(post("/api/bonus-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bonusPointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BonusPoint in the database
        List<BonusPoint> bonusPointList = bonusPointRepository.findAll();
        assertThat(bonusPointList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBonusPoints() throws Exception {
        // Initialize the database
        bonusPointRepository.saveAndFlush(bonusPoint);

        // Get all the bonusPointList
        restBonusPointMockMvc.perform(get("/api/bonus-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bonusPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));
    }

    @Test
    @Transactional
    public void getBonusPoint() throws Exception {
        // Initialize the database
        bonusPointRepository.saveAndFlush(bonusPoint);

        // Get the bonusPoint
        restBonusPointMockMvc.perform(get("/api/bonus-points/{id}", bonusPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bonusPoint.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK));
    }
    @Test
    @Transactional
    public void getNonExistingBonusPoint() throws Exception {
        // Get the bonusPoint
        restBonusPointMockMvc.perform(get("/api/bonus-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBonusPoint() throws Exception {
        // Initialize the database
        bonusPointRepository.saveAndFlush(bonusPoint);

        int databaseSizeBeforeUpdate = bonusPointRepository.findAll().size();

        // Update the bonusPoint
        BonusPoint updatedBonusPoint = bonusPointRepository.findById(bonusPoint.getId()).get();
        // Disconnect from session so that the updates on updatedBonusPoint are not directly saved in db
        em.detach(updatedBonusPoint);
        updatedBonusPoint
            .amount(UPDATED_AMOUNT)
            .remark(UPDATED_REMARK);
        BonusPointDTO bonusPointDTO = bonusPointMapper.toDto(updatedBonusPoint);

        restBonusPointMockMvc.perform(put("/api/bonus-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bonusPointDTO)))
            .andExpect(status().isOk());

        // Validate the BonusPoint in the database
        List<BonusPoint> bonusPointList = bonusPointRepository.findAll();
        assertThat(bonusPointList).hasSize(databaseSizeBeforeUpdate);
        BonusPoint testBonusPoint = bonusPointList.get(bonusPointList.size() - 1);
        assertThat(testBonusPoint.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testBonusPoint.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    public void updateNonExistingBonusPoint() throws Exception {
        int databaseSizeBeforeUpdate = bonusPointRepository.findAll().size();

        // Create the BonusPoint
        BonusPointDTO bonusPointDTO = bonusPointMapper.toDto(bonusPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBonusPointMockMvc.perform(put("/api/bonus-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bonusPointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BonusPoint in the database
        List<BonusPoint> bonusPointList = bonusPointRepository.findAll();
        assertThat(bonusPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBonusPoint() throws Exception {
        // Initialize the database
        bonusPointRepository.saveAndFlush(bonusPoint);

        int databaseSizeBeforeDelete = bonusPointRepository.findAll().size();

        // Delete the bonusPoint
        restBonusPointMockMvc.perform(delete("/api/bonus-points/{id}", bonusPoint.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BonusPoint> bonusPointList = bonusPointRepository.findAll();
        assertThat(bonusPointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
