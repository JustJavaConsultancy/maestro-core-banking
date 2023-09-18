package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.justjava.corebanking.domain.Loan;
import ng.com.justjava.corebanking.service.LoanService;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.LoanDTO;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Loan}.
 */
@RestController
@RequestMapping("/api")
public class LoanResource {

    private final Logger log = LoggerFactory.getLogger(LoanResource.class);

    private static final String ENTITY_NAME = "loan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoanService loanService;

    public LoanResource(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * {@code POST  /loans} : Create a new loan.
     *
     * @param loanDTO the loanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loanDTO, or with status {@code 400 (Bad Request)} if the loan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loans")
    public ResponseEntity<GenericResponseDTO> createLoan(@Valid @RequestBody LoanDTO loanDTO, HttpSession session) throws URISyntaxException {
        log.debug("REST request to save Loan : {}", loanDTO);
        if (loanDTO.getId() != null) {
            throw new BadRequestAlertException("A new loan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GenericResponseDTO genericResponseDTO = loanService.createLoan(loanDTO, session);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    /**
     * {@code PUT  /loans} : Updates an existing loan.
     *
     * @param loanDTO the loanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanDTO,
     * or with status {@code 400 (Bad Request)} if the loanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loans")
    public ResponseEntity<LoanDTO> updateLoan(@Valid @RequestBody LoanDTO loanDTO) throws URISyntaxException {
        log.debug("REST request to update Loan : {}", loanDTO);
        if (loanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LoanDTO result = loanService.save(loanDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /loans} : get all the loans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loans in body.
     */
    @GetMapping("/loans")
    public List<LoanDTO> getAllLoans() {
        log.debug("REST request to get all Loans");
        return loanService.findAll();
    }

    @GetMapping("/loans/profile")
    public  ResponseEntity<GenericResponseDTO> getLoanMyLoans() {
    	 GenericResponseDTO genericResponseDTO =  loanService.findAllByProfileId();
    	 return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());

    }

    /**
     * {@code GET  /loans/:id} : get the "id" loan.
     *
     * @param id the id of the loanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loans/{id}")
    public ResponseEntity<LoanDTO> getLoan(@PathVariable Long id) {
        log.debug("REST request to get Loan : {}", id);
        Optional<LoanDTO> loanDTO = loanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loanDTO);
    }

    /**
     * {@code DELETE  /loans/:id} : delete the "id" loan.
     *
     * @param id the id of the loanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loans/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        log.debug("REST request to delete Loan : {}", id);
        loanService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
