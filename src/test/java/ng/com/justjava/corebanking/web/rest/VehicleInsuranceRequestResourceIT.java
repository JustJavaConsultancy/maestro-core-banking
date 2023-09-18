package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.InsuranceType;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.VehicleInsuranceRequest;
import ng.com.justjava.corebanking.domain.enumeration.InsuranceOperation;
import ng.com.justjava.corebanking.repository.VehicleInsuranceRequestRepository;
import ng.com.justjava.corebanking.service.VehicleInsuranceRequestService;
import ng.com.justjava.corebanking.service.dto.VehicleInsuranceRequestDTO;
import ng.com.justjava.corebanking.service.mapper.VehicleInsuranceRequestMapper;
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
 * Integration tests for the {@link VehicleInsuranceRequestResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class VehicleInsuranceRequestResourceIT {

    private static final InsuranceOperation DEFAULT_OPERATION = InsuranceOperation.NEW;
    private static final InsuranceOperation UPDATED_OPERATION = InsuranceOperation.RENEW;

    private static final String DEFAULT_POLICY_NO = "AAAAAAAAAA";
    private static final String UPDATED_POLICY_NO = "BBBBBBBBBB";

    private static final String DEFAULT_CERTIFICATE_NO = "AAAAAAAAAA";
    private static final String UPDATED_CERTIFICATE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_OCCUPATION = "AAAAAAAAAA";
    private static final String UPDATED_OCCUPATION = "BBBBBBBBBB";

    private static final String DEFAULT_SECTOR = "AAAAAAAAAA";
    private static final String UPDATED_SECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_ID_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ID_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_VEHICLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_NO = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_NO = "BBBBBBBBBB";

    private static final String DEFAULT_VEH_MAKE = "AAAAAAAAAA";
    private static final String UPDATED_VEH_MAKE = "BBBBBBBBBB";

    private static final String DEFAULT_VEH_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_VEH_MODEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_VEH_YEAR = 1;
    private static final Integer UPDATED_VEH_YEAR = 2;

    private static final String DEFAULT_REGISTRATION_START = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_START = "BBBBBBBBBB";

    private static final String DEFAULT_EXPIRY_DATE = "AAAAAAAAAA";
    private static final String UPDATED_EXPIRY_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_MILEAGE = "AAAAAAAAAA";
    private static final String UPDATED_MILEAGE = "BBBBBBBBBB";

    private static final String DEFAULT_VEH_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_VEH_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_CHASSIS_NO = "AAAAAAAAAA";
    private static final String UPDATED_CHASSIS_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ENGINE_NO = "AAAAAAAAAA";
    private static final String UPDATED_ENGINE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ENGINE_CAPACITY = "AAAAAAAAAA";
    private static final String UPDATED_ENGINE_CAPACITY = "BBBBBBBBBB";

    private static final String DEFAULT_SEAT_CAPACITY = "AAAAAAAAAA";
    private static final String UPDATED_SEAT_CAPACITY = "BBBBBBBBBB";

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;

    @Autowired
    private VehicleInsuranceRequestRepository vehicleInsuranceRequestRepository;

    @Autowired
    private VehicleInsuranceRequestMapper vehicleInsuranceRequestMapper;

    @Autowired
    private VehicleInsuranceRequestService vehicleInsuranceRequestService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleInsuranceRequestMockMvc;

    private VehicleInsuranceRequest vehicleInsuranceRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleInsuranceRequest createEntity(EntityManager em) {
        VehicleInsuranceRequest vehicleInsuranceRequest = new VehicleInsuranceRequest()
            .operation(DEFAULT_OPERATION)
            .policyNo(DEFAULT_POLICY_NO)
            .certificateNo(DEFAULT_CERTIFICATE_NO)
            .occupation(DEFAULT_OCCUPATION)
            .sector(DEFAULT_SECTOR)
            .idType(DEFAULT_ID_TYPE)
            .idNumber(DEFAULT_ID_NUMBER)
            .vehicleType(DEFAULT_VEHICLE_TYPE)
            .registrationNo(DEFAULT_REGISTRATION_NO)
            .vehMake(DEFAULT_VEH_MAKE)
            .vehModel(DEFAULT_VEH_MODEL)
            .vehYear(DEFAULT_VEH_YEAR)
            .registrationStart(DEFAULT_REGISTRATION_START)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .mileage(DEFAULT_MILEAGE)
            .vehColor(DEFAULT_VEH_COLOR)
            .chassisNo(DEFAULT_CHASSIS_NO)
            .engineNo(DEFAULT_ENGINE_NO)
            .engineCapacity(DEFAULT_ENGINE_CAPACITY)
            .seatCapacity(DEFAULT_SEAT_CAPACITY)
            .balance(DEFAULT_BALANCE);
        // Add required entity
        Profile profile;
        if (TestUtil.findAll(em, Profile.class).isEmpty()) {
            profile = ProfileResourceIT.createEntity(em);
            em.persist(profile);
            em.flush();
        } else {
            profile = TestUtil.findAll(em, Profile.class).get(0);
        }
        vehicleInsuranceRequest.setProfile(profile);
        // Add required entity
        InsuranceType insuranceType;
        if (TestUtil.findAll(em, InsuranceType.class).isEmpty()) {
            insuranceType = InsuranceTypeResourceIT.createEntity(em);
            em.persist(insuranceType);
            em.flush();
        } else {
            insuranceType = TestUtil.findAll(em, InsuranceType.class).get(0);
        }
        vehicleInsuranceRequest.setInsuranceType(insuranceType);
        return vehicleInsuranceRequest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleInsuranceRequest createUpdatedEntity(EntityManager em) {
        VehicleInsuranceRequest vehicleInsuranceRequest = new VehicleInsuranceRequest()
            .operation(UPDATED_OPERATION)
            .policyNo(UPDATED_POLICY_NO)
            .certificateNo(UPDATED_CERTIFICATE_NO)
            .occupation(UPDATED_OCCUPATION)
            .sector(UPDATED_SECTOR)
            .idType(UPDATED_ID_TYPE)
            .idNumber(UPDATED_ID_NUMBER)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .registrationNo(UPDATED_REGISTRATION_NO)
            .vehMake(UPDATED_VEH_MAKE)
            .vehModel(UPDATED_VEH_MODEL)
            .vehYear(UPDATED_VEH_YEAR)
            .registrationStart(UPDATED_REGISTRATION_START)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .mileage(UPDATED_MILEAGE)
            .vehColor(UPDATED_VEH_COLOR)
            .chassisNo(UPDATED_CHASSIS_NO)
            .engineNo(UPDATED_ENGINE_NO)
            .engineCapacity(UPDATED_ENGINE_CAPACITY)
            .seatCapacity(UPDATED_SEAT_CAPACITY)
            .balance(UPDATED_BALANCE);
        // Add required entity
        Profile profile;
        if (TestUtil.findAll(em, Profile.class).isEmpty()) {
            profile = ProfileResourceIT.createUpdatedEntity(em);
            em.persist(profile);
            em.flush();
        } else {
            profile = TestUtil.findAll(em, Profile.class).get(0);
        }
        vehicleInsuranceRequest.setProfile(profile);
        // Add required entity
        InsuranceType insuranceType;
        if (TestUtil.findAll(em, InsuranceType.class).isEmpty()) {
            insuranceType = InsuranceTypeResourceIT.createUpdatedEntity(em);
            em.persist(insuranceType);
            em.flush();
        } else {
            insuranceType = TestUtil.findAll(em, InsuranceType.class).get(0);
        }
        vehicleInsuranceRequest.setInsuranceType(insuranceType);
        return vehicleInsuranceRequest;
    }

    @BeforeEach
    public void initTest() {
        vehicleInsuranceRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createVehicleInsuranceRequest() throws Exception {
        int databaseSizeBeforeCreate = vehicleInsuranceRequestRepository.findAll().size();
        // Create the VehicleInsuranceRequest
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);
        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isCreated());

        // Validate the VehicleInsuranceRequest in the database
        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeCreate + 1);
        VehicleInsuranceRequest testVehicleInsuranceRequest = vehicleInsuranceRequestList.get(vehicleInsuranceRequestList.size() - 1);
        assertThat(testVehicleInsuranceRequest.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testVehicleInsuranceRequest.getPolicyNo()).isEqualTo(DEFAULT_POLICY_NO);
        assertThat(testVehicleInsuranceRequest.getCertificateNo()).isEqualTo(DEFAULT_CERTIFICATE_NO);
        assertThat(testVehicleInsuranceRequest.getOccupation()).isEqualTo(DEFAULT_OCCUPATION);
        assertThat(testVehicleInsuranceRequest.getSector()).isEqualTo(DEFAULT_SECTOR);
        assertThat(testVehicleInsuranceRequest.getIdType()).isEqualTo(DEFAULT_ID_TYPE);
        assertThat(testVehicleInsuranceRequest.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testVehicleInsuranceRequest.getVehicleType()).isEqualTo(DEFAULT_VEHICLE_TYPE);
        assertThat(testVehicleInsuranceRequest.getRegistrationNo()).isEqualTo(DEFAULT_REGISTRATION_NO);
        assertThat(testVehicleInsuranceRequest.getVehMake()).isEqualTo(DEFAULT_VEH_MAKE);
        assertThat(testVehicleInsuranceRequest.getVehModel()).isEqualTo(DEFAULT_VEH_MODEL);
        assertThat(testVehicleInsuranceRequest.getVehYear()).isEqualTo(DEFAULT_VEH_YEAR);
        assertThat(testVehicleInsuranceRequest.getRegistrationStart()).isEqualTo(DEFAULT_REGISTRATION_START);
        assertThat(testVehicleInsuranceRequest.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testVehicleInsuranceRequest.getMileage()).isEqualTo(DEFAULT_MILEAGE);
        assertThat(testVehicleInsuranceRequest.getVehColor()).isEqualTo(DEFAULT_VEH_COLOR);
        assertThat(testVehicleInsuranceRequest.getChassisNo()).isEqualTo(DEFAULT_CHASSIS_NO);
        assertThat(testVehicleInsuranceRequest.getEngineNo()).isEqualTo(DEFAULT_ENGINE_NO);
        assertThat(testVehicleInsuranceRequest.getEngineCapacity()).isEqualTo(DEFAULT_ENGINE_CAPACITY);
        assertThat(testVehicleInsuranceRequest.getSeatCapacity()).isEqualTo(DEFAULT_SEAT_CAPACITY);
        assertThat(testVehicleInsuranceRequest.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createVehicleInsuranceRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vehicleInsuranceRequestRepository.findAll().size();

        // Create the VehicleInsuranceRequest with an existing ID
        vehicleInsuranceRequest.setId(1L);
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleInsuranceRequest in the database
        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setOperation(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOccupationIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setOccupation(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSectorIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setSector(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setIdType(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setIdNumber(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVehicleTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setVehicleType(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegistrationNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setRegistrationNo(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVehMakeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setVehMake(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVehModelIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setVehModel(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVehYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setVehYear(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegistrationStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setRegistrationStart(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpiryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setExpiryDate(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMileageIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setMileage(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVehColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setVehColor(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChassisNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setChassisNo(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEngineNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setEngineNo(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEngineCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setEngineCapacity(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSeatCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehicleInsuranceRequestRepository.findAll().size();
        // set the field null
        vehicleInsuranceRequest.setSeatCapacity(null);

        // Create the VehicleInsuranceRequest, which fails.
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);


        restVehicleInsuranceRequestMockMvc.perform(post("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVehicleInsuranceRequests() throws Exception {
        // Initialize the database
        vehicleInsuranceRequestRepository.saveAndFlush(vehicleInsuranceRequest);

        // Get all the vehicleInsuranceRequestList
        restVehicleInsuranceRequestMockMvc.perform(get("/api/vehicle-insurance-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleInsuranceRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].policyNo").value(hasItem(DEFAULT_POLICY_NO)))
            .andExpect(jsonPath("$.[*].certificateNo").value(hasItem(DEFAULT_CERTIFICATE_NO)))
            .andExpect(jsonPath("$.[*].occupation").value(hasItem(DEFAULT_OCCUPATION)))
            .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR)))
            .andExpect(jsonPath("$.[*].idType").value(hasItem(DEFAULT_ID_TYPE)))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].vehicleType").value(hasItem(DEFAULT_VEHICLE_TYPE)))
            .andExpect(jsonPath("$.[*].registrationNo").value(hasItem(DEFAULT_REGISTRATION_NO)))
            .andExpect(jsonPath("$.[*].vehMake").value(hasItem(DEFAULT_VEH_MAKE)))
            .andExpect(jsonPath("$.[*].vehModel").value(hasItem(DEFAULT_VEH_MODEL)))
            .andExpect(jsonPath("$.[*].vehYear").value(hasItem(DEFAULT_VEH_YEAR)))
            .andExpect(jsonPath("$.[*].registrationStart").value(hasItem(DEFAULT_REGISTRATION_START)))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE)))
            .andExpect(jsonPath("$.[*].mileage").value(hasItem(DEFAULT_MILEAGE)))
            .andExpect(jsonPath("$.[*].vehColor").value(hasItem(DEFAULT_VEH_COLOR)))
            .andExpect(jsonPath("$.[*].chassisNo").value(hasItem(DEFAULT_CHASSIS_NO)))
            .andExpect(jsonPath("$.[*].engineNo").value(hasItem(DEFAULT_ENGINE_NO)))
            .andExpect(jsonPath("$.[*].engineCapacity").value(hasItem(DEFAULT_ENGINE_CAPACITY)))
            .andExpect(jsonPath("$.[*].seatCapacity").value(hasItem(DEFAULT_SEAT_CAPACITY)))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }

    @Test
    @Transactional
    public void getVehicleInsuranceRequest() throws Exception {
        // Initialize the database
        vehicleInsuranceRequestRepository.saveAndFlush(vehicleInsuranceRequest);

        // Get the vehicleInsuranceRequest
        restVehicleInsuranceRequestMockMvc.perform(get("/api/vehicle-insurance-requests/{id}", vehicleInsuranceRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleInsuranceRequest.getId().intValue()))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()))
            .andExpect(jsonPath("$.policyNo").value(DEFAULT_POLICY_NO))
            .andExpect(jsonPath("$.certificateNo").value(DEFAULT_CERTIFICATE_NO))
            .andExpect(jsonPath("$.occupation").value(DEFAULT_OCCUPATION))
            .andExpect(jsonPath("$.sector").value(DEFAULT_SECTOR))
            .andExpect(jsonPath("$.idType").value(DEFAULT_ID_TYPE))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER))
            .andExpect(jsonPath("$.vehicleType").value(DEFAULT_VEHICLE_TYPE))
            .andExpect(jsonPath("$.registrationNo").value(DEFAULT_REGISTRATION_NO))
            .andExpect(jsonPath("$.vehMake").value(DEFAULT_VEH_MAKE))
            .andExpect(jsonPath("$.vehModel").value(DEFAULT_VEH_MODEL))
            .andExpect(jsonPath("$.vehYear").value(DEFAULT_VEH_YEAR))
            .andExpect(jsonPath("$.registrationStart").value(DEFAULT_REGISTRATION_START))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE))
            .andExpect(jsonPath("$.mileage").value(DEFAULT_MILEAGE))
            .andExpect(jsonPath("$.vehColor").value(DEFAULT_VEH_COLOR))
            .andExpect(jsonPath("$.chassisNo").value(DEFAULT_CHASSIS_NO))
            .andExpect(jsonPath("$.engineNo").value(DEFAULT_ENGINE_NO))
            .andExpect(jsonPath("$.engineCapacity").value(DEFAULT_ENGINE_CAPACITY))
            .andExpect(jsonPath("$.seatCapacity").value(DEFAULT_SEAT_CAPACITY))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingVehicleInsuranceRequest() throws Exception {
        // Get the vehicleInsuranceRequest
        restVehicleInsuranceRequestMockMvc.perform(get("/api/vehicle-insurance-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVehicleInsuranceRequest() throws Exception {
        // Initialize the database
        vehicleInsuranceRequestRepository.saveAndFlush(vehicleInsuranceRequest);

        int databaseSizeBeforeUpdate = vehicleInsuranceRequestRepository.findAll().size();

        // Update the vehicleInsuranceRequest
        VehicleInsuranceRequest updatedVehicleInsuranceRequest = vehicleInsuranceRequestRepository.findById(vehicleInsuranceRequest.getId()).get();
        // Disconnect from session so that the updates on updatedVehicleInsuranceRequest are not directly saved in db
        em.detach(updatedVehicleInsuranceRequest);
        updatedVehicleInsuranceRequest
            .operation(UPDATED_OPERATION)
            .policyNo(UPDATED_POLICY_NO)
            .certificateNo(UPDATED_CERTIFICATE_NO)
            .occupation(UPDATED_OCCUPATION)
            .sector(UPDATED_SECTOR)
            .idType(UPDATED_ID_TYPE)
            .idNumber(UPDATED_ID_NUMBER)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .registrationNo(UPDATED_REGISTRATION_NO)
            .vehMake(UPDATED_VEH_MAKE)
            .vehModel(UPDATED_VEH_MODEL)
            .vehYear(UPDATED_VEH_YEAR)
            .registrationStart(UPDATED_REGISTRATION_START)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .mileage(UPDATED_MILEAGE)
            .vehColor(UPDATED_VEH_COLOR)
            .chassisNo(UPDATED_CHASSIS_NO)
            .engineNo(UPDATED_ENGINE_NO)
            .engineCapacity(UPDATED_ENGINE_CAPACITY)
            .seatCapacity(UPDATED_SEAT_CAPACITY)
            .balance(UPDATED_BALANCE);
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(updatedVehicleInsuranceRequest);

        restVehicleInsuranceRequestMockMvc.perform(put("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isOk());

        // Validate the VehicleInsuranceRequest in the database
        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeUpdate);
        VehicleInsuranceRequest testVehicleInsuranceRequest = vehicleInsuranceRequestList.get(vehicleInsuranceRequestList.size() - 1);
        assertThat(testVehicleInsuranceRequest.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testVehicleInsuranceRequest.getPolicyNo()).isEqualTo(UPDATED_POLICY_NO);
        assertThat(testVehicleInsuranceRequest.getCertificateNo()).isEqualTo(UPDATED_CERTIFICATE_NO);
        assertThat(testVehicleInsuranceRequest.getOccupation()).isEqualTo(UPDATED_OCCUPATION);
        assertThat(testVehicleInsuranceRequest.getSector()).isEqualTo(UPDATED_SECTOR);
        assertThat(testVehicleInsuranceRequest.getIdType()).isEqualTo(UPDATED_ID_TYPE);
        assertThat(testVehicleInsuranceRequest.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testVehicleInsuranceRequest.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
        assertThat(testVehicleInsuranceRequest.getRegistrationNo()).isEqualTo(UPDATED_REGISTRATION_NO);
        assertThat(testVehicleInsuranceRequest.getVehMake()).isEqualTo(UPDATED_VEH_MAKE);
        assertThat(testVehicleInsuranceRequest.getVehModel()).isEqualTo(UPDATED_VEH_MODEL);
        assertThat(testVehicleInsuranceRequest.getVehYear()).isEqualTo(UPDATED_VEH_YEAR);
        assertThat(testVehicleInsuranceRequest.getRegistrationStart()).isEqualTo(UPDATED_REGISTRATION_START);
        assertThat(testVehicleInsuranceRequest.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testVehicleInsuranceRequest.getMileage()).isEqualTo(UPDATED_MILEAGE);
        assertThat(testVehicleInsuranceRequest.getVehColor()).isEqualTo(UPDATED_VEH_COLOR);
        assertThat(testVehicleInsuranceRequest.getChassisNo()).isEqualTo(UPDATED_CHASSIS_NO);
        assertThat(testVehicleInsuranceRequest.getEngineNo()).isEqualTo(UPDATED_ENGINE_NO);
        assertThat(testVehicleInsuranceRequest.getEngineCapacity()).isEqualTo(UPDATED_ENGINE_CAPACITY);
        assertThat(testVehicleInsuranceRequest.getSeatCapacity()).isEqualTo(UPDATED_SEAT_CAPACITY);
        assertThat(testVehicleInsuranceRequest.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingVehicleInsuranceRequest() throws Exception {
        int databaseSizeBeforeUpdate = vehicleInsuranceRequestRepository.findAll().size();

        // Create the VehicleInsuranceRequest
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO = vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleInsuranceRequestMockMvc.perform(put("/api/vehicle-insurance-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vehicleInsuranceRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleInsuranceRequest in the database
        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVehicleInsuranceRequest() throws Exception {
        // Initialize the database
        vehicleInsuranceRequestRepository.saveAndFlush(vehicleInsuranceRequest);

        int databaseSizeBeforeDelete = vehicleInsuranceRequestRepository.findAll().size();

        // Delete the vehicleInsuranceRequest
        restVehicleInsuranceRequestMockMvc.perform(delete("/api/vehicle-insurance-requests/{id}", vehicleInsuranceRequest.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VehicleInsuranceRequest> vehicleInsuranceRequestList = vehicleInsuranceRequestRepository.findAll();
        assertThat(vehicleInsuranceRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
