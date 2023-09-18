package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.Loan;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.enumeration.LoanStatus;
import ng.com.justjava.corebanking.repository.LoanRepository;
import ng.com.justjava.corebanking.service.LoanService;
import ng.com.justjava.corebanking.service.dto.LoanDTO;
import ng.com.justjava.corebanking.service.mapper.LoanMapper;
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
 * Integration tests for the {@link LoanResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LoanResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String DEFAULT_TENURE = "AAAAAAAAAA";
    private static final String UPDATED_TENURE = "BBBBBBBBBB";

    private static final LoanStatus DEFAULT_STATUS = LoanStatus.APPROVED;
    private static final LoanStatus UPDATED_STATUS = LoanStatus.CANCELED;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanService loanService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoanMockMvc;

    private Loan loan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loan createEntity(EntityManager em) {
        Loan loan = new Loan()
            .amount(DEFAULT_AMOUNT)
            .tenure(DEFAULT_TENURE)
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
        loan.setProfile(profile);
        return loan;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loan createUpdatedEntity(EntityManager em) {
        Loan loan = new Loan()
            .amount(UPDATED_AMOUNT)
            .tenure(UPDATED_TENURE)
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
        loan.setProfile(profile);
        return loan;
    }

    @BeforeEach
    public void initTest() {
        loan = createEntity(em);
    }

    @Test
    @Transactional
    public void createLoan() throws Exception {
        int databaseSizeBeforeCreate = loanRepository.findAll().size();
        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);
        restLoanMockMvc.perform(post("/api/loans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(loanDTO)))
            .andExpect(status().isCreated());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeCreate + 1);
        Loan testLoan = loanList.get(loanList.size() - 1);
        assertThat(testLoan.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testLoan.getTenure()).isEqualTo(DEFAULT_TENURE);
        assertThat(testLoan.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createLoanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = loanRepository.findAll().size();

        // Create the Loan with an existing ID
        loan.setId(1L);
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanMockMvc.perform(post("/api/loans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(loanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLoans() throws Exception {
        // Initialize the database
        loanRepository.saveAndFlush(loan);

        // Get all the loanList
        restLoanMockMvc.perform(get("/api/loans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loan.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].tenure").value(hasItem(DEFAULT_TENURE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getLoan() throws Exception {
        // Initialize the database
        loanRepository.saveAndFlush(loan);

        // Get the loan
        restLoanMockMvc.perform(get("/api/loans/{id}", loan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loan.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.tenure").value(DEFAULT_TENURE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingLoan() throws Exception {
        // Get the loan
        restLoanMockMvc.perform(get("/api/loans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoan() throws Exception {
        // Initialize the database
        loanRepository.saveAndFlush(loan);

        int databaseSizeBeforeUpdate = loanRepository.findAll().size();

        // Update the loan
        Loan updatedLoan = loanRepository.findById(loan.getId()).get();
        // Disconnect from session so that the updates on updatedLoan are not directly saved in db
        em.detach(updatedLoan);
        updatedLoan
            .amount(UPDATED_AMOUNT)
            .tenure(UPDATED_TENURE)
            .status(UPDATED_STATUS);
        LoanDTO loanDTO = loanMapper.toDto(updatedLoan);

        restLoanMockMvc.perform(put("/api/loans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(loanDTO)))
            .andExpect(status().isOk());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate);
        Loan testLoan = loanList.get(loanList.size() - 1);
        assertThat(testLoan.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testLoan.getTenure()).isEqualTo(UPDATED_TENURE);
        assertThat(testLoan.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingLoan() throws Exception {
        int databaseSizeBeforeUpdate = loanRepository.findAll().size();

        // Create the Loan
        LoanDTO loanDTO = loanMapper.toDto(loan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanMockMvc.perform(put("/api/loans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(loanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLoan() throws Exception {
        // Initialize the database
        loanRepository.saveAndFlush(loan);

        int databaseSizeBeforeDelete = loanRepository.findAll().size();

        // Delete the loan
        restLoanMockMvc.perform(delete("/api/loans/{id}", loan.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
