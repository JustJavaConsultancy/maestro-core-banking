package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.MyDevice;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.enumeration.DeviceStatus;
import ng.com.justjava.corebanking.repository.MyDeviceRepository;
import ng.com.justjava.corebanking.service.MyDeviceService;
import ng.com.justjava.corebanking.service.dto.MyDeviceDTO;
import ng.com.justjava.corebanking.service.mapper.MyDeviceMapper;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static ng.com.justjava.corebanking.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Integration tests for the {@link MyDeviceResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MyDeviceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final DeviceStatus DEFAULT_STATUS = DeviceStatus.ACTIVE;
    private static final DeviceStatus UPDATED_STATUS = DeviceStatus.INACTIVE;

    private static final ZonedDateTime DEFAULT_LAST_LOGIN_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_LOGIN_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_NOTIFICATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_NOTIFICATION_TOKEN = "BBBBBBBBBB";

    @Autowired
    private MyDeviceRepository myDeviceRepository;

    @Autowired
    private MyDeviceMapper myDeviceMapper;

    @Autowired
    private MyDeviceService myDeviceService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMyDeviceMockMvc;

    private MyDevice myDevice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyDevice createEntity(EntityManager em) {
        MyDevice myDevice = new MyDevice()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS)
            .last_login_date(DEFAULT_LAST_LOGIN_DATE)
            .deviceId(DEFAULT_DEVICE_ID)
            .deviceNotificationToken(DEFAULT_DEVICE_NOTIFICATION_TOKEN);
        // Add required entity
        Profile profile;
        if (TestUtil.findAll(em, Profile.class).isEmpty()) {
            profile = ProfileResourceIT.createEntity(em);
            em.persist(profile);
            em.flush();
        } else {
            profile = TestUtil.findAll(em, Profile.class).get(0);
        }
        myDevice.setProfile(profile);
        return myDevice;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyDevice createUpdatedEntity(EntityManager em) {
        MyDevice myDevice = new MyDevice()
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .last_login_date(UPDATED_LAST_LOGIN_DATE)
            .deviceId(UPDATED_DEVICE_ID)
            .deviceNotificationToken(UPDATED_DEVICE_NOTIFICATION_TOKEN);
        // Add required entity
        Profile profile;
        if (TestUtil.findAll(em, Profile.class).isEmpty()) {
            profile = ProfileResourceIT.createUpdatedEntity(em);
            em.persist(profile);
            em.flush();
        } else {
            profile = TestUtil.findAll(em, Profile.class).get(0);
        }
        myDevice.setProfile(profile);
        return myDevice;
    }

    @BeforeEach
    public void initTest() {
        myDevice = createEntity(em);
    }

    @Test
    @Transactional
    public void createMyDevice() throws Exception {
        int databaseSizeBeforeCreate = myDeviceRepository.findAll().size();
        // Create the MyDevice
        MyDeviceDTO myDeviceDTO = myDeviceMapper.toDto(myDevice);
        restMyDeviceMockMvc.perform(post("/api/my-devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(myDeviceDTO)))
            .andExpect(status().isCreated());

        // Validate the MyDevice in the database
        List<MyDevice> myDeviceList = myDeviceRepository.findAll();
        assertThat(myDeviceList).hasSize(databaseSizeBeforeCreate + 1);
        MyDevice testMyDevice = myDeviceList.get(myDeviceList.size() - 1);
        assertThat(testMyDevice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMyDevice.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMyDevice.getLast_login_date()).isEqualTo(DEFAULT_LAST_LOGIN_DATE);
        assertThat(testMyDevice.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testMyDevice.getDeviceNotificationToken()).isEqualTo(DEFAULT_DEVICE_NOTIFICATION_TOKEN);
    }

    @Test
    @Transactional
    public void createMyDeviceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = myDeviceRepository.findAll().size();

        // Create the MyDevice with an existing ID
        myDevice.setId(1L);
        MyDeviceDTO myDeviceDTO = myDeviceMapper.toDto(myDevice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyDeviceMockMvc.perform(post("/api/my-devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(myDeviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MyDevice in the database
        List<MyDevice> myDeviceList = myDeviceRepository.findAll();
        assertThat(myDeviceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = myDeviceRepository.findAll().size();
        // set the field null
        myDevice.setName(null);

        // Create the MyDevice, which fails.
        MyDeviceDTO myDeviceDTO = myDeviceMapper.toDto(myDevice);


        restMyDeviceMockMvc.perform(post("/api/my-devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(myDeviceDTO)))
            .andExpect(status().isBadRequest());

        List<MyDevice> myDeviceList = myDeviceRepository.findAll();
        assertThat(myDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = myDeviceRepository.findAll().size();
        // set the field null
        myDevice.setStatus(null);

        // Create the MyDevice, which fails.
        MyDeviceDTO myDeviceDTO = myDeviceMapper.toDto(myDevice);


        restMyDeviceMockMvc.perform(post("/api/my-devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(myDeviceDTO)))
            .andExpect(status().isBadRequest());

        List<MyDevice> myDeviceList = myDeviceRepository.findAll();
        assertThat(myDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeviceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = myDeviceRepository.findAll().size();
        // set the field null
        myDevice.setDeviceId(null);

        // Create the MyDevice, which fails.
        MyDeviceDTO myDeviceDTO = myDeviceMapper.toDto(myDevice);


        restMyDeviceMockMvc.perform(post("/api/my-devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(myDeviceDTO)))
            .andExpect(status().isBadRequest());

        List<MyDevice> myDeviceList = myDeviceRepository.findAll();
        assertThat(myDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMyDevices() throws Exception {
        // Initialize the database
        myDeviceRepository.saveAndFlush(myDevice);

        // Get all the myDeviceList
        restMyDeviceMockMvc.perform(get("/api/my-devices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].last_login_date").value(hasItem(sameInstant(DEFAULT_LAST_LOGIN_DATE))))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].deviceNotificationToken").value(hasItem(DEFAULT_DEVICE_NOTIFICATION_TOKEN)));
    }

    @Test
    @Transactional
    public void getMyDevice() throws Exception {
        // Initialize the database
        myDeviceRepository.saveAndFlush(myDevice);

        // Get the myDevice
        restMyDeviceMockMvc.perform(get("/api/my-devices/{id}", myDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(myDevice.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.last_login_date").value(sameInstant(DEFAULT_LAST_LOGIN_DATE)))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID))
            .andExpect(jsonPath("$.deviceNotificationToken").value(DEFAULT_DEVICE_NOTIFICATION_TOKEN));
    }
    @Test
    @Transactional
    public void getNonExistingMyDevice() throws Exception {
        // Get the myDevice
        restMyDeviceMockMvc.perform(get("/api/my-devices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMyDevice() throws Exception {
        // Initialize the database
        myDeviceRepository.saveAndFlush(myDevice);

        int databaseSizeBeforeUpdate = myDeviceRepository.findAll().size();

        // Update the myDevice
        MyDevice updatedMyDevice = myDeviceRepository.findById(myDevice.getId()).get();
        // Disconnect from session so that the updates on updatedMyDevice are not directly saved in db
        em.detach(updatedMyDevice);
        updatedMyDevice
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .last_login_date(UPDATED_LAST_LOGIN_DATE)
            .deviceId(UPDATED_DEVICE_ID)
            .deviceNotificationToken(UPDATED_DEVICE_NOTIFICATION_TOKEN);
        MyDeviceDTO myDeviceDTO = myDeviceMapper.toDto(updatedMyDevice);

        restMyDeviceMockMvc.perform(put("/api/my-devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(myDeviceDTO)))
            .andExpect(status().isOk());

        // Validate the MyDevice in the database
        List<MyDevice> myDeviceList = myDeviceRepository.findAll();
        assertThat(myDeviceList).hasSize(databaseSizeBeforeUpdate);
        MyDevice testMyDevice = myDeviceList.get(myDeviceList.size() - 1);
        assertThat(testMyDevice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMyDevice.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMyDevice.getLast_login_date()).isEqualTo(UPDATED_LAST_LOGIN_DATE);
        assertThat(testMyDevice.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testMyDevice.getDeviceNotificationToken()).isEqualTo(UPDATED_DEVICE_NOTIFICATION_TOKEN);
    }

    @Test
    @Transactional
    public void updateNonExistingMyDevice() throws Exception {
        int databaseSizeBeforeUpdate = myDeviceRepository.findAll().size();

        // Create the MyDevice
        MyDeviceDTO myDeviceDTO = myDeviceMapper.toDto(myDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyDeviceMockMvc.perform(put("/api/my-devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(myDeviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MyDevice in the database
        List<MyDevice> myDeviceList = myDeviceRepository.findAll();
        assertThat(myDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMyDevice() throws Exception {
        // Initialize the database
        myDeviceRepository.saveAndFlush(myDevice);

        int databaseSizeBeforeDelete = myDeviceRepository.findAll().size();

        // Delete the myDevice
        restMyDeviceMockMvc.perform(delete("/api/my-devices/{id}", myDevice.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MyDevice> myDeviceList = myDeviceRepository.findAll();
        assertThat(myDeviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
