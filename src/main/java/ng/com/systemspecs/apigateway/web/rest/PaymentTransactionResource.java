package ng.com.systemspecs.apigateway.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.systemspecs.apigateway.domain.IpgSynchTransaction;
import ng.com.systemspecs.apigateway.service.IpgSynchTransactionService;
import ng.com.systemspecs.apigateway.service.PaymentTransactionService;
import ng.com.systemspecs.apigateway.service.dto.IpgResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.PaymentTransactionDTO;
import ng.com.systemspecs.apigateway.service.dto.stp.IPGSynchTransactionDTO;
import ng.com.systemspecs.apigateway.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing
 * {@link ng.com.systemspecs.apigateway.domain.PaymentTransaction}.
 */
@RestController
@RequestMapping("/api")
public class PaymentTransactionResource {

    private static final String ENTITY_NAME = "paymentTransaction";
    private final Logger log = LoggerFactory.getLogger(PaymentTransactionResource.class);
    private final PaymentTransactionService paymentTransactionService;
    private final IpgSynchTransactionService ipgSynchTransactionService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PaymentTransactionResource(PaymentTransactionService paymentTransactionService, IpgSynchTransactionService ipgSynchTransactionService) {
        this.paymentTransactionService = paymentTransactionService;
        this.ipgSynchTransactionService = ipgSynchTransactionService;
    }

    /**
     * {@code POST  /payment-transactions} : Create a new paymentTransaction.
     *
     * @param paymentTransactionDTO the paymentTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     * body the new paymentTransactionDTO, or with status
     * {@code 400 (Bad Request)} if the paymentTransaction has already an
     * ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-transactions")
    public ResponseEntity<PaymentTransactionDTO> createPaymentTransaction(
        @Valid @RequestBody PaymentTransactionDTO paymentTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save PaymentTransaction : {}", paymentTransactionDTO);
        if (paymentTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentTransaction cannot already have an ID", ENTITY_NAME,
                "idexists");
        }
        PaymentTransactionDTO result = paymentTransactionService.save(paymentTransactionDTO);
        return ResponseEntity
            .created(new URI("/api/payment-transactions/" + result.getId())).headers(HeaderUtil
                .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

	/**
	 * {@code PUT  /payment-transactions} : Updates an existing paymentTransaction.
	 *
	 * @param paymentTransactionDTO the paymentTransactionDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated paymentTransactionDTO, or with status
	 *         {@code 400 (Bad Request)} if the paymentTransactionDTO is not valid,
	 *         or with status {@code 500 (Internal Server Error)} if the
	 *         paymentTransactionDTO couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/payment-transactions")
	public ResponseEntity<PaymentTransactionDTO> updatePaymentTransaction(
			@Valid @RequestBody PaymentTransactionDTO paymentTransactionDTO) throws URISyntaxException {
		log.debug("REST request to update PaymentTransaction : {}", paymentTransactionDTO);
		if (paymentTransactionDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		PaymentTransactionDTO result = paymentTransactionService.save(paymentTransactionDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
				paymentTransactionDTO.getId().toString())).body(result);
	}

	/**
	 * {@code GET  /payment-transactions} : get all the paymentTransactions.
	 *
	 * @param pageable the pagination information.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of paymentTransactions in body.
	 */
	@GetMapping("/payment-transactions")
	public ResponseEntity<List<PaymentTransactionDTO>> getAllPaymentTransactions(Pageable pageable) {
		log.debug("REST request to get a page of PaymentTransactions");
		Page<PaymentTransactionDTO> page = paymentTransactionService.findAll(pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /payment-transactions/:id} : get the "id" paymentTransaction.
	 *
	 * @param id the id of the paymentTransactionDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the paymentTransactionDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/payment-transactions/{id}")
	public ResponseEntity<PaymentTransactionDTO> getPaymentTransaction(@PathVariable Long id) {
		log.debug("REST request to get PaymentTransaction : {}", id);
		Optional<PaymentTransactionDTO> paymentTransactionDTO = paymentTransactionService.findOne(id);
		return ResponseUtil.wrapOrNotFound(paymentTransactionDTO);
	}

	/**
	 * {@code DELETE  /payment-transactions/:id} : delete the "id"
	 * paymentTransaction.
	 *
	 * @param id the id of the paymentTransactionDTO to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/payment-transactions/{id}")
	public ResponseEntity<Void> deletePaymentTransaction(@PathVariable Long id) {
		log.debug("REST request to delete PaymentTransaction : {}", id);
		paymentTransactionService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}

	@GetMapping("/fund-transactions/{accountNumber}")
	public ResponseEntity<List<PaymentTransactionDTO>> getFundTransaction(@PathVariable String accountNumber) {
		log.debug("REST request to get PaymentTransaction : {}", accountNumber);
		List<PaymentTransactionDTO> paymentTransactionDTO = paymentTransactionService
				.findByDestinationAccount(accountNumber);
		return new ResponseEntity<>(paymentTransactionDTO, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/send-money-transactions/{accountNumber}")
	public ResponseEntity<List<PaymentTransactionDTO>> getSendMoneyTransaction(@PathVariable String accountNumber) {
		log.debug("REST request to get PaymentTransaction : {}", accountNumber);
		List<PaymentTransactionDTO> paymentTransactionDTO = paymentTransactionService
				.findBySourceAccount(accountNumber);
		return new ResponseEntity<>(paymentTransactionDTO, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/customer-transactions")
	public ResponseEntity<List<PaymentTransactionDTO>> getAllMyTransaction() {

		String phoneNumber = null;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            phoneNumber = ((UserDetails) principal).getUsername();
        } else
            phoneNumber = principal.toString();
        log.debug("REST request to all customers PaymentTransaction : {}", phoneNumber);
        List<PaymentTransactionDTO> paymentTransactionDTO = paymentTransactionService.findBySourceAccountName(phoneNumber);
        return new ResponseEntity<>(paymentTransactionDTO, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/synch-transaction")
    public ResponseEntity<IpgResponseDTO> synchPaymentTransaction(@Valid @RequestBody IPGSynchTransactionDTO ipgSynchTransactionDTO) throws URISyntaxException {
        log.debug("REST request to synch ipgSynchTransactionDTO : {}", ipgSynchTransactionDTO.toString());

        IpgResponseDTO response = new IpgResponseDTO();
        List<IpgSynchTransaction> transactionOptional = ipgSynchTransactionService.findByTransactionRef(ipgSynchTransactionDTO.getTransactionRef().trim());

        if (!transactionOptional.isEmpty()){
            response.setCode(400);
            response.setMessage("Transaction reference not unique");
            response.setStatus("Failed");
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        IPGSynchTransactionDTO save = ipgSynchTransactionService.save(ipgSynchTransactionDTO);
        log.debug("Saved synch transaction : {}", save);

        response.setCode(200);
        response.setMessage("Transaction received successfully");
        response.setStatus("successful");
        //response.setData(new Object());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }
}
