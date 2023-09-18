package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.AppUpdate;
import ng.com.justjava.corebanking.domain.enumeration.DeviceType;
import ng.com.justjava.corebanking.repository.AppUpdateRepository;
import ng.com.justjava.corebanking.service.AppUpdateService;
import ng.com.justjava.corebanking.service.dto.AppUpdateDTO;
import ng.com.justjava.corebanking.service.mapper.AppUpdateMapper;
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
 * Integration tests for the {@link AppUpdateResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AppUpdateResourceIT {

    private static final DeviceType DEFAULT_DEVICE = DeviceType.ALL;
    private static final DeviceType UPDATED_DEVICE = DeviceType.ANDROID;

    private static final String DEFAULT_ANDROIDURL = "AAAAAAAAAA";
    private static final String UPDATED_ANDROIDURL = "BBBBBBBBBB";

    private static final String DEFAULT_IOSURL = "AAAAAAAAAA";
    private static final String UPDATED_IOSURL = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_ANDROIDVERSION = "AAAAAAAAAA";
    private static final String UPDATED_ANDROIDVERSION = "BBBBBBBBBB";

    @Autowired
    private AppUpdateRepository appUpdateRepository;

    @Autowired
    private AppUpdateMapper appUpdateMapper;

    @Autowired
    private AppUpdateService appUpdateService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppUpdateMockMvc;

    private AppUpdate appUpdate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUpdate createEntity(EntityManager em) {
        AppUpdate appUpdate = new AppUpdate()
            .device(DEFAULT_DEVICE)
            .androidurl(DEFAULT_ANDROIDURL)
            .iosurl(DEFAULT_IOSURL)
            .comments(DEFAULT_COMMENTS)
            .androidversion(DEFAULT_ANDROIDVERSION);
        return appUpdate;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUpdate createUpdatedEntity(EntityManager em) {
        AppUpdate appUpdate = new AppUpdate()
            .device(UPDATED_DEVICE)
            .androidurl(UPDATED_ANDROIDURL)
            .iosurl(UPDATED_IOSURL)
            .comments(UPDATED_COMMENTS)
            .androidversion(UPDATED_ANDROIDVERSION);
        return appUpdate;
    }

    @BeforeEach
    public void initTest() {
        appUpdate = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppUpdate() throws Exception {
        int databaseSizeBeforeCreate = appUpdateRepository.findAll().size();
        // Create the AppUpdate
        AppUpdateDTO appUpdateDTO = appUpdateMapper.toDto(appUpdate);
        restAppUpdateMockMvc.perform(post("/api/app-updates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appUpdateDTO)))
            .andExpect(status().isCreated());

        // Validate the AppUpdate in the database
        List<AppUpdate> appUpdateList = appUpdateRepository.findAll();
        assertThat(appUpdateList).hasSize(databaseSizeBeforeCreate + 1);
        AppUpdate testAppUpdate = appUpdateList.get(appUpdateList.size() - 1);
        Assertions.assertThat(testAppUpdate.getDevice()).isEqualTo(DEFAULT_DEVICE);
        assertThat(testAppUpdate.getAndroidurl()).isEqualTo(DEFAULT_ANDROIDURL);
        assertThat(testAppUpdate.getIosurl()).isEqualTo(DEFAULT_IOSURL);
        assertThat(testAppUpdate.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testAppUpdate.getAndroidversion()).isEqualTo(DEFAULT_ANDROIDVERSION);
    }

    @Test
    @Transactional
    public void createAppUpdateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appUpdateRepository.findAll().size();

        // Create the AppUpdate with an existing ID
        appUpdate.setId(1L);
        AppUpdateDTO appUpdateDTO = appUpdateMapper.toDto(appUpdate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppUpdateMockMvc.perform(post("/api/app-updates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appUpdateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppUpdate in the database
        List<AppUpdate> appUpdateList = appUpdateRepository.findAll();
        assertThat(appUpdateList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAppUpdates() throws Exception {
        // Initialize the database
        appUpdateRepository.saveAndFlush(appUpdate);

        // Get all the appUpdateList
        restAppUpdateMockMvc.perform(get("/api/app-updates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUpdate.getId().intValue())))
            .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE.toString())))
            .andExpect(jsonPath("$.[*].androidurl").value(hasItem(DEFAULT_ANDROIDURL)))
            .andExpect(jsonPath("$.[*].iosurl").value(hasItem(DEFAULT_IOSURL)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].androidversion").value(hasItem(DEFAULT_ANDROIDVERSION)));
    }

    @Test
    @Transactional
    public void getAppUpdate() throws Exception {
        // Initialize the database
        appUpdateRepository.saveAndFlush(appUpdate);

        // Get the appUpdate
        restAppUpdateMockMvc.perform(get("/api/app-updates/{id}", appUpdate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appUpdate.getId().intValue()))
            .andExpect(jsonPath("$.device").value(DEFAULT_DEVICE.toString()))
            .andExpect(jsonPath("$.androidurl").value(DEFAULT_ANDROIDURL))
            .andExpect(jsonPath("$.iosurl").value(DEFAULT_IOSURL))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.androidversion").value(DEFAULT_ANDROIDVERSION));
    }
    @Test
    @Transactional
    public void getNonExistingAppUpdate() throws Exception {
        // Get the appUpdate
        restAppUpdateMockMvc.perform(get("/api/app-updates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppUpdate() throws Exception {
        // Initialize the database
        appUpdateRepository.saveAndFlush(appUpdate);

        int databaseSizeBeforeUpdate = appUpdateRepository.findAll().size();

        // Update the appUpdate
        AppUpdate updatedAppUpdate = appUpdateRepository.findById(appUpdate.getId()).get();
        // Disconnect from session so that the updates on updatedAppUpdate are not directly saved in db
        em.detach(updatedAppUpdate);
        updatedAppUpdate
            .device(UPDATED_DEVICE)
            .androidurl(UPDATED_ANDROIDURL)
            .iosurl(UPDATED_IOSURL)
            .comments(UPDATED_COMMENTS)
            .androidversion(UPDATED_ANDROIDVERSION);
        AppUpdateDTO appUpdateDTO = appUpdateMapper.toDto(updatedAppUpdate);

        restAppUpdateMockMvc.perform(put("/api/app-updates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appUpdateDTO)))
            .andExpect(status().isOk());

        // Validate the AppUpdate in the database
        List<AppUpdate> appUpdateList = appUpdateRepository.findAll();
        assertThat(appUpdateList).hasSize(databaseSizeBeforeUpdate);
        AppUpdate testAppUpdate = appUpdateList.get(appUpdateList.size() - 1);
        Assertions.assertThat(testAppUpdate.getDevice()).isEqualTo(UPDATED_DEVICE);
        assertThat(testAppUpdate.getAndroidurl()).isEqualTo(UPDATED_ANDROIDURL);
        assertThat(testAppUpdate.getIosurl()).isEqualTo(UPDATED_IOSURL);
        assertThat(testAppUpdate.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testAppUpdate.getAndroidversion()).isEqualTo(UPDATED_ANDROIDVERSION);
    }

    @Test
    @Transactional
    public void updateNonExistingAppUpdate() throws Exception {
        int databaseSizeBeforeUpdate = appUpdateRepository.findAll().size();

        // Create the AppUpdate
        AppUpdateDTO appUpdateDTO = appUpdateMapper.toDto(appUpdate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUpdateMockMvc.perform(put("/api/app-updates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appUpdateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppUpdate in the database
        List<AppUpdate> appUpdateList = appUpdateRepository.findAll();
        assertThat(appUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAppUpdate() throws Exception {
        // Initialize the database
        appUpdateRepository.saveAndFlush(appUpdate);

        int databaseSizeBeforeDelete = appUpdateRepository.findAll().size();

        // Delete the appUpdate
        restAppUpdateMockMvc.perform(delete("/api/app-updates/{id}", appUpdate.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppUpdate> appUpdateList = appUpdateRepository.findAll();
        assertThat(appUpdateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
