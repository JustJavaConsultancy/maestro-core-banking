package ng.com.systemspecs.apigateway.web.rest;

import ng.com.systemspecs.apigateway.ApiGatewayApp;
import ng.com.systemspecs.apigateway.domain.ContactUs;
import ng.com.systemspecs.apigateway.domain.enumeration.ComplaintCategory;
import ng.com.systemspecs.apigateway.domain.enumeration.ContactCategory;
import ng.com.systemspecs.apigateway.domain.enumeration.ContactStatus;
import ng.com.systemspecs.apigateway.domain.enumeration.EnquiryCategory;
import ng.com.systemspecs.apigateway.repository.ContactUsRepository;
import ng.com.systemspecs.apigateway.service.ContactUsService;
import ng.com.systemspecs.apigateway.service.dto.ContactUsDTO;
import ng.com.systemspecs.apigateway.service.mapper.ContactUsMapper;
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
 * Integration tests for the {@link ContactUsResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ContactUsResourceIT {

    private static final ContactCategory DEFAULT_CONTACT_CATEGORY = ContactCategory.COMPLAINT;
    private static final ContactCategory UPDATED_CONTACT_CATEGORY = ContactCategory.ENQUIRY;

    private static final ComplaintCategory DEFAULT_COMPLAINT_CATEGORY = ComplaintCategory.ACCOUNT_RELOADED;
    private static final ComplaintCategory UPDATED_COMPLAINT_CATEGORY = ComplaintCategory.KYC_UPGRADE;

    private static final EnquiryCategory DEFAULT_ENQUIRY_CATEGORY = EnquiryCategory.BUSINESS_PARTNERSHIPS;
    private static final EnquiryCategory UPDATED_ENQUIRY_CATEGORY = EnquiryCategory.PRICING;

    private static final ContactStatus DEFAULT_STATUS = ContactStatus.OPEN;
    private static final ContactStatus UPDATED_STATUS = ContactStatus.ASSIGNED;

    @Autowired
    private ContactUsRepository contactUsRepository;

    @Autowired
    private ContactUsMapper contactUsMapper;

    @Autowired
    private ContactUsService contactUsService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactUsMockMvc;

    private ContactUs contactUs;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactUs createEntity(EntityManager em) {
        ContactUs contactUs = new ContactUs()
            .contactCategory(DEFAULT_CONTACT_CATEGORY)
            .complaintCategory(DEFAULT_COMPLAINT_CATEGORY)
            .enquiryCategory(DEFAULT_ENQUIRY_CATEGORY)
            .status(DEFAULT_STATUS);
        return contactUs;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactUs createUpdatedEntity(EntityManager em) {
        ContactUs contactUs = new ContactUs()
            .contactCategory(UPDATED_CONTACT_CATEGORY)
            .complaintCategory(UPDATED_COMPLAINT_CATEGORY)
            .enquiryCategory(UPDATED_ENQUIRY_CATEGORY)
            .status(UPDATED_STATUS);
        return contactUs;
    }

    @BeforeEach
    public void initTest() {
        contactUs = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactUs() throws Exception {
        int databaseSizeBeforeCreate = contactUsRepository.findAll().size();
        // Create the ContactUs
        ContactUsDTO contactUsDTO = contactUsMapper.toDto(contactUs);
        restContactUsMockMvc.perform(post("/api/contactuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactUsDTO)))
            .andExpect(status().isCreated());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeCreate + 1);
        ContactUs testContactUs = contactUsList.get(contactUsList.size() - 1);
        assertThat(testContactUs.getContactCategory()).isEqualTo(DEFAULT_CONTACT_CATEGORY);
        assertThat(testContactUs.getComplaintCategory()).isEqualTo(DEFAULT_COMPLAINT_CATEGORY);
        assertThat(testContactUs.getEnquiryCategory()).isEqualTo(DEFAULT_ENQUIRY_CATEGORY);
        assertThat(testContactUs.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createContactUsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactUsRepository.findAll().size();

        // Create the ContactUs with an existing ID
        contactUs.setId(1L);
        ContactUsDTO contactUsDTO = contactUsMapper.toDto(contactUs);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactUsMockMvc.perform(post("/api/contactuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactUsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllContactuses() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        // Get all the contactUsList
        restContactUsMockMvc.perform(get("/api/contactuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactUs.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactCategory").value(hasItem(DEFAULT_CONTACT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].complaintCategory").value(hasItem(DEFAULT_COMPLAINT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].enquiryCategory").value(hasItem(DEFAULT_ENQUIRY_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getContactUs() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        // Get the contactUs
        restContactUsMockMvc.perform(get("/api/contactuses/{id}", contactUs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactUs.getId().intValue()))
            .andExpect(jsonPath("$.contactCategory").value(DEFAULT_CONTACT_CATEGORY.toString()))
            .andExpect(jsonPath("$.complaintCategory").value(DEFAULT_COMPLAINT_CATEGORY.toString()))
            .andExpect(jsonPath("$.enquiryCategory").value(DEFAULT_ENQUIRY_CATEGORY.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContactUs() throws Exception {
        // Get the contactUs
        restContactUsMockMvc.perform(get("/api/contactuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactUs() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        int databaseSizeBeforeUpdate = contactUsRepository.findAll().size();

        // Update the contactUs
        ContactUs updatedContactUs = contactUsRepository.findById(contactUs.getId()).get();
        // Disconnect from session so that the updates on updatedContactUs are not directly saved in db
        em.detach(updatedContactUs);
        updatedContactUs
            .contactCategory(UPDATED_CONTACT_CATEGORY)
            .complaintCategory(UPDATED_COMPLAINT_CATEGORY)
            .enquiryCategory(UPDATED_ENQUIRY_CATEGORY)
            .status(UPDATED_STATUS);
        ContactUsDTO contactUsDTO = contactUsMapper.toDto(updatedContactUs);

        restContactUsMockMvc.perform(put("/api/contactuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactUsDTO)))
            .andExpect(status().isOk());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeUpdate);
        ContactUs testContactUs = contactUsList.get(contactUsList.size() - 1);
        assertThat(testContactUs.getContactCategory()).isEqualTo(UPDATED_CONTACT_CATEGORY);
        assertThat(testContactUs.getComplaintCategory()).isEqualTo(UPDATED_COMPLAINT_CATEGORY);
        assertThat(testContactUs.getEnquiryCategory()).isEqualTo(UPDATED_ENQUIRY_CATEGORY);
        assertThat(testContactUs.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingContactUs() throws Exception {
        int databaseSizeBeforeUpdate = contactUsRepository.findAll().size();

        // Create the ContactUs
        ContactUsDTO contactUsDTO = contactUsMapper.toDto(contactUs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactUsMockMvc.perform(put("/api/contactuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactUsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContactUs() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        int databaseSizeBeforeDelete = contactUsRepository.findAll().size();

        // Delete the contactUs
        restContactUsMockMvc.perform(delete("/api/contactuses/{id}", contactUs.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
