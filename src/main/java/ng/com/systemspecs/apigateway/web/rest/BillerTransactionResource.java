package ng.com.systemspecs.apigateway.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.systemspecs.apigateway.client.ExternalAgencyRESTClient;
import ng.com.systemspecs.apigateway.client.ExternalRESTClient;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.User;
import ng.com.systemspecs.apigateway.domain.WalletAccount;
import ng.com.systemspecs.apigateway.domain.enumeration.SpecificChannel;
import ng.com.systemspecs.apigateway.domain.enumeration.UserStatus;
import ng.com.systemspecs.apigateway.repository.UserRepository;
import ng.com.systemspecs.apigateway.security.SecurityUtils;
import ng.com.systemspecs.apigateway.service.BillerTransactionService;
import ng.com.systemspecs.apigateway.service.ProfileService;
import ng.com.systemspecs.apigateway.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.accounting.AccountingService;
import ng.com.systemspecs.apigateway.service.dto.AgencyPaymentNotifyDTO;
import ng.com.systemspecs.apigateway.service.dto.AgentAirtimeDTO;
import ng.com.systemspecs.apigateway.service.dto.BillerTransactionDTO;
import ng.com.systemspecs.apigateway.service.dto.BuyAirtimeDTO;
import ng.com.systemspecs.apigateway.service.dto.BuyDataDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.IPGPaymentResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.PayBillDTO;
import ng.com.systemspecs.apigateway.service.dto.PayRRRDTO;
import ng.com.systemspecs.apigateway.service.dto.UtilDTO;
import ng.com.systemspecs.apigateway.web.rest.errors.BadRequestAlertException;
import ng.com.systemspecs.remitabillinggateway.billers.GetBillerResponse;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponse;
import ng.com.systemspecs.remitabillinggateway.generaterrr.GenerateResponse;
import ng.com.systemspecs.remitabillinggateway.paymentstatus.GetTransactionStatusResponse;
import ng.com.systemspecs.remitabillinggateway.rrrdetails.GetRRRDetailsResponse;
import ng.com.systemspecs.remitabillinggateway.servicetypes.GetServiceResponse;
import ng.com.systemspecs.remitabillinggateway.servicetypes.GetServiceResponseData;
import ng.com.systemspecs.remitabillinggateway.validate.ValidateRequest;
import ng.com.systemspecs.remitabillinggateway.validate.ValidateResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * REST controller for managing {@link ng.com.systemspecs.apigateway.domain.BillerTransaction}.
 */
@RestController
@RequestMapping("/api")
public class BillerTransactionResource {

    private final Logger log = LoggerFactory.getLogger(BillerTransactionResource.class);

    private static final String ENTITY_NAME = "billerTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private User theUser = null;

    private final BillerTransactionService billerTransactionService;

    private final WalletAccountService walletAccountService;

    private final AccountingService accountingService;

    private final ExternalRESTClient externalRESTClient;

    private final ExternalAgencyRESTClient externalAgencyRESTClient;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final ProfileService profileService;


    public BillerTransactionResource(BillerTransactionService billerTransactionService,
                                     WalletAccountService walletAccountService, AccountingService accountingService, ExternalRESTClient externalRESTClient,
                                     ExternalAgencyRESTClient externalAgencyRESTClient, PasswordEncoder passwordEncoder, UserRepository userRepository, ProfileService profileService) {
        this.billerTransactionService = billerTransactionService;
        this.walletAccountService = walletAccountService;
        this.accountingService = accountingService;
        this.externalRESTClient = externalRESTClient;
        this.externalAgencyRESTClient = externalAgencyRESTClient;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.profileService = profileService;
    }

    /**
     * {@code POST  /biller-transactions} : Create a new billerTransaction.
     *
     * @param billerTransactionDTO the billerTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billerTransactionDTO, or with status {@code 400 (Bad Request)} if the billerTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/biller-transactions")
    public ResponseEntity<BillerTransactionDTO> createBillerTransaction(@RequestBody BillerTransactionDTO billerTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save BillerTransaction : {}", billerTransactionDTO);
        if (billerTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new billerTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillerTransactionDTO result = billerTransactionService.save(billerTransactionDTO);
        return ResponseEntity.created(new URI("/api/biller-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /biller-transactions} : Updates an existing billerTransaction.
     *
     * @param billerTransactionDTO the billerTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billerTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the billerTransactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billerTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/biller-transactions")
    public ResponseEntity<BillerTransactionDTO> updateBillerTransaction(@RequestBody BillerTransactionDTO billerTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update BillerTransaction : {}", billerTransactionDTO);
        if (billerTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BillerTransactionDTO result = billerTransactionService.save(billerTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billerTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /biller-transactions} : get all the billerTransactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billerTransactions in body.
     */
    @GetMapping("/biller-transactions")
    public ResponseEntity<List<BillerTransactionDTO>> getAllBillerTransactions(Pageable pageable) {
        log.debug("REST request to get a page of BillerTransactions");
        Page<BillerTransactionDTO> page = billerTransactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /biller-transactions/:id} : get the "id" billerTransaction.
     *
     * @param id the id of the billerTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billerTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/biller-transactions/{id}")
    public ResponseEntity<BillerTransactionDTO> getBillerTransaction(@PathVariable Long id) {
        log.debug("REST request to get BillerTransaction : {}", id);
        Optional<BillerTransactionDTO> billerTransactionDTO = billerTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billerTransactionDTO);
    }

    /**
     * {@code DELETE  /biller-transactions/:id} : delete the "id" billerTransaction.
     *
     * @param id the id of the billerTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/biller-transactions/{id}")
    public ResponseEntity<Void> deleteBillerTransaction(@PathVariable Long id) {
        log.debug("REST request to delete BillerTransaction : {}", id);
        billerTransactionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/validate-request")
    public ValidateResponse validate(@RequestBody ValidateRequest validateRequest) {
        return billerTransactionService.validate(validateRequest, String.valueOf(System.currentTimeMillis()));
    }

    @PostMapping("/generate-rrr")
    public GenerateResponse generateRRR(@RequestBody ValidateRequest validateRequest) {
        return billerTransactionService.generateRRR(validateRequest, String.valueOf(System.currentTimeMillis()));
    }

    private int getPinFailureCount(HttpSession session) {
        int pinFailureCount = 1;

        try {
            if (session != null) {
                Object loginCountStr = session.getAttribute("pinFailureCount");
                if (loginCountStr != null) {
                    pinFailureCount = (int) loginCountStr;
                    System.out.println("Pin failure Counter ====> " + pinFailureCount);
                } else {
                    pinFailureCount = 1;
                }
            }
        } catch (Exception e) {
            pinFailureCount = 1;
        }
        return pinFailureCount;
    }

    private ResponseEntity<GenericResponseDTO> buildPinAttemptExceededResponse(GenericResponseDTO response, HttpSession session) {

        response.setMessage("Maximum failed pin reached, account deactivated");
        response.setCode("46");
        this.theUser.setActivated(false);
        this.theUser.setStatus(UserStatus.DEACTIVATE_CUSTOMER_PIN.getName());
        userRepository.save(this.theUser);

        session.removeAttribute("pinFailureCount");

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<GenericResponseDTO> buildPinErrorResponse(HttpSession session, int pinFailureCount) {
        GenericResponseDTO response;
        session.setAttribute("pinFailureCount", ++pinFailureCount);

        response = new GenericResponseDTO();
        response.setMessage("Invalid / wrong PIN entered");
        response.setCode("51");
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/billers/pay")
    public GenericResponseDTO payBiller(@RequestBody PayBillDTO payBillDTO, @RequestParam Optional<Boolean> flag, HttpSession session) {
         if(flag.isPresent()&&flag.get()) {
            HttpHeaders headers = new HttpHeaders();
            return new GenericResponseDTO("00", "success", null);
        }
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                this.theUser = user;
            });

        if (theUser == null) {
            return new GenericResponseDTO("64", HttpStatus.BAD_REQUEST, "User session expired", true);
        }

        GenericResponseDTO response = new GenericResponseDTO();

        int pinFailureCount = getPinFailureCount(session);

        if (pinFailureCount >= 3) {
            response.setMessage("Maximum failed pin reached, account deactivated");
            response.setCode("46");
            this.theUser.setActivated(false);
            this.theUser.setStatus(UserStatus.DEACTIVATE_CUSTOMER.getName());
            userRepository.save(this.theUser);
            response.setStatus(HttpStatus.BAD_REQUEST);

            return response;
        }

        String sourceAccountNumber = payBillDTO.getSourceAccountNo();
        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(sourceAccountNumber);
        Double currentBalance = Double.valueOf(walletAccount.getCurrentBalance());
        BigDecimal amount = payBillDTO.getValidateRequest().getAmount();
        Double intraFee = accountingService.getTransactionFee(amount.doubleValue(),
            SpecificChannel.PAY_BILLER.getName(), null);

        Profile sourceAccountOwner = walletAccount.getAccountOwner();


        if (currentBalance <= amount.doubleValue() + intraFee) {
            return new GenericResponseDTO("50", HttpStatus.BAD_REQUEST, "Insufficient balance!", payBillDTO);
        }

        if (sourceAccountOwner != null) {
            double totalBonus = sourceAccountOwner.getTotalBonus();

            if (totalBonus < payBillDTO.getBonusAmount() && payBillDTO.getBonusAmount() > 0) {
                return new GenericResponseDTO("50", HttpStatus.BAD_REQUEST, "Insufficient Amount in the BonusPot", payBillDTO);
            }

        }

        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());
        String currentEncryptedPin = profile.getPin();

        if (!passwordEncoder.matches(payBillDTO.getPin(), currentEncryptedPin) || StringUtils.isEmpty(payBillDTO.getPin())) {

            session.setAttribute("pinFailureCount", ++pinFailureCount);

            return new GenericResponseDTO("51", HttpStatus.UNAUTHORIZED, "Invalid / wrong PIN entered", null);
        }

        return billerTransactionService.validateAndPayBiller(payBillDTO.getValidateRequest(), payBillDTO.getSourceAccountNo(), payBillDTO.getTransRef(),
            "payBiller", payBillDTO.getNarration(), payBillDTO.getAgentRef(), payBillDTO.getBonusAmount(), payBillDTO.isRedeemBonus());
    }

    @PostMapping("/check/{transactionId}")
    public GetTransactionStatusResponse getTransactionStatus(@PathVariable String transactionId) {
        return billerTransactionService.getTransactionStatus(transactionId);
    }

    @GetMapping("/billing/receipt/{rrr}/{requestId}/rest.reg")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<byte[]> getRRRReceipt(@PathVariable("rrr") String rrr,
                                                @PathVariable("requestId") String requestId) {
        String fileName = "biller_payment_reciept_" + System.currentTimeMillis() + ".pdf";
        String publicKey = "dC5vbW9udWJpQGdtYWlsLmNvbXxiM2RjMDhjZDRlZTc5ZDIxZDQwMjdjOWM3MmI5ZWY0ZDA3MTk2YTRkNGRkMjY3NjNkMGZkYzA4MjM1MzI4OWFhODE5OGM4MjM0NTI2YWI2ZjZkYzNhZmQzNDNkZmIzYmUwNTkxODlmMmNkOTkxNmM5MjVhNjYwZjk0ZTk1OTkwNw==";
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ContentDisposition contentDisposition = ContentDisposition.builder("inline")
            .filename(fileName)
            .build();

        responseHeaders.setContentDisposition(contentDisposition);
        log.info(Arrays.toString(externalRESTClient.getRRRReceipt(publicKey, rrr, requestId)));
        return new ResponseEntity<byte[]>(externalRESTClient.getRRRReceipt(publicKey, rrr, requestId), responseHeaders, HttpStatus.OK);

    }

    @GetMapping("/agent/{rrr}/lookup")
    public IPGPaymentResponseDTO validateAgentRRR(@PathVariable("rrr")  String  rrr) {
        Map<String, String> headers = new java.util.HashMap<>();
        headers.put("X-API-PUBLIC-KEY", "QzAwMDAyNTE5MzV8NTcwODYyNDl8NTk0ZGFkZTE2OWM3N2UyZTMyMzlhYWJiZjM4NjdiZThhYWQyMTUxMWVlMjNiMjdjYmFmODVlNTZlMDFlNTA5ZGZjNDVhOTliM2Q2OGNhZmE0YmI3YzllODBhMTdmNmIxMDc4ZWRlMWM5MDc1OTc4ZGYxMDQ5ZjIxYWU2Mjc0YjQ");
        // headers.put("X-API-PUBLIC-KEY", "U09MRHwyNjk5MzI0MjIzfGRiMjZlNjY5NDVjOGQxMjVjMjBkNzIwZWQ2NTE1ZTgxNTEwNzEyMGRiZGQ3MzZlOTIyYzk1MzA1ZjM4YjM2ZTk5MDUxYTE1YmZhZTc4MDcyM2VmZWU5NGQ0MzM1YmM0NzYxMzJjNDk3M2YzMWI5NWMyOWY5OWUwNDEwMWNjOTEx");
        return externalAgencyRESTClient.validateAgentRRR(headers, rrr);
    }


    @PostMapping("/agent-payment-notify")
    public IPGPaymentResponseDTO agentPaymentNotify(@RequestBody AgencyPaymentNotifyDTO agencyPaymentNotifyDTO) {
        String secretKey = "95ab7ab7b2dc3152e3ab776c8f4bbe0ec5fe87526ee129617f319fb9edf79263a6fd15f1efe78f38ad6f04634dff993ccf9f075bf91f37aa52b61a9bd61c881e";
        String signatureSource = agencyPaymentNotifyDTO.getRrr() + agencyPaymentNotifyDTO.getPaymentAuthCode() + agencyPaymentNotifyDTO.getTransactionId()
            + secretKey;
        String signature = new DigestUtils("SHA-512").digestAsHex(signatureSource);
        Map<String, String> headers = new java.util.HashMap<>();
        headers.put("X-API-PUBLIC-KEY", "QzAwMDAyNTE5MzV8NTcwODYyNDl8NTk0ZGFkZTE2OWM3N2UyZTMyMzlhYWJiZjM4NjdiZThhYWQyMTUxMWVlMjNiMjdjYmFmODVlNTZlMDFlNTA5ZGZjNDVhOTliM2Q2OGNhZmE0YmI3YzllODBhMTdmNmIxMDc4ZWRlMWM5MDc1OTc4ZGYxMDQ5ZjIxYWU2Mjc0YjQ");
        // headers.put("X-API-PUBLIC-KEY", "U09MRHwyNjk5MzI0MjIzfGRiMjZlNjY5NDVjOGQxMjVjMjBkNzIwZWQ2NTE1ZTgxNTEwNzEyMGRiZGQ3MzZlOTIyYzk1MzA1ZjM4YjM2ZTk5MDUxYTE1YmZhZTc4MDcyM2VmZWU5NGQ0MzM1YmM0NzYxMzJjNDk3M2YzMWI5NWMyOWY5OWUwNDEwMWNjOTEx");
        headers.put("X-API-SIGNATURE", signature);
        return externalAgencyRESTClient.doAgentPaymentAndNotify(headers, agencyPaymentNotifyDTO);
    }


    @RequestMapping(path = "/airtime/buy", method = RequestMethod.POST)
    public ResponseEntity<GenericResponseDTO> buyAirtime(@RequestBody BuyAirtimeDTO buyAirtimeDTO, @RequestParam Optional<Boolean> flag, HttpSession session) {

        if(flag.isPresent()&& flag.get()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), headers,
                HttpStatus.OK);
        }
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                this.theUser = user;
            });

        if (theUser == null) {
            return new ResponseEntity<>(new GenericResponseDTO("64", HttpStatus.BAD_REQUEST, "User session expired", null), HttpStatus.BAD_REQUEST);
        }

        GenericResponseDTO response = new GenericResponseDTO();

        int pinFailureCount = getPinFailureCount(session);

        if (pinFailureCount >= 3) {
            return buildPinAttemptExceededResponse(response, session);
        }

        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());

        String currentEncryptedPin = profile.getPin();
        if (!passwordEncoder.matches(buyAirtimeDTO.getPin(), currentEncryptedPin) || StringUtils.isEmpty(buyAirtimeDTO.getPin())) {

            return buildPinErrorResponse(session, pinFailureCount);
        }

        GenericResponseDTO genericResponseDTO = billerTransactionService.buyAirtime(buyAirtimeDTO);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());

    }


    @RequestMapping(path = "/data/buy", method = RequestMethod.POST)
    public ResponseEntity<GenericResponseDTO> buyData(@RequestBody BuyDataDTO buyDataDTO, @RequestParam Optional<Boolean> flag, HttpSession session) {
        //this.pinCorrect = true;

        if(flag.isPresent()&&flag.get()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), headers,
                HttpStatus.OK);
        }
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                this.theUser = user;
            });

        GenericResponseDTO response = new GenericResponseDTO();

        int pinFailureCount = getPinFailureCount(session);

        if (pinFailureCount >= 3) {
            return buildPinAttemptExceededResponse(response, session);
        }


        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());
        //Profile profile = profileService.
        String currentEncryptedPin = profile.getPin();
        if (!passwordEncoder.matches(buyDataDTO.getPin(), currentEncryptedPin)) {
            //throw new InvalidPasswordException();
            //pinCorrect = false;
            return buildPinErrorResponse(session, pinFailureCount);

        }

        GenericResponseDTO genericResponseDTO = billerTransactionService.buyData(buyDataDTO);
        return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), genericResponseDTO.getStatus());
    }


    @GetMapping("/billers/{billerId}/service-types")
    public ResponseEntity<GenericResponseDTO> getServices(@PathVariable String billerId) {
        List<GetServiceResponseData> serviceResponseDataList = billerTransactionService.getService(billerId);
        if (serviceResponseDataList != null && serviceResponseDataList.size() > 0) {
            return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "Successful", serviceResponseDataList), HttpStatus.OK);
        }

        return new ResponseEntity<>(new GenericResponseDTO("failed", "failed", null), HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/rrr/details/{rrr}")
    public GetRRRDetailsResponse getRRRDetails(@PathVariable String rrr) {
        return billerTransactionService.getRRRDetails(rrr, String.valueOf(System.currentTimeMillis()));
    }

    @GetMapping("/rrr/status/{rrr}")
    public boolean getRRRSuccessfulStatus(@PathVariable String rrr) {
        GetRRRDetailsResponse rrrDetails = billerTransactionService.getRRRDetails(rrr, String.valueOf(System.currentTimeMillis()));
        if (rrrDetails.getResponseCode().equalsIgnoreCase("00")) {
            String retrievedRRR = rrrDetails.getResponseData().get(0).getRrr();
            return retrievedRRR == null;
        }
        return false;
    }


    @PostMapping("/rrr/pay")
    public ResponseEntity<GenericResponseDTO> payRRR(@RequestBody PayRRRDTO payRRRDTO, @RequestParam Optional<Boolean> flag, HttpSession session) {
        log.info("PAY RRR payload " + payRRRDTO);
        if(flag.isPresent()&flag.get()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), headers,
                HttpStatus.OK);
        }
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                this.theUser = user;
            });

        GenericResponseDTO response = new GenericResponseDTO();

        int pinFailureCount = getPinFailureCount(session);

        if (pinFailureCount >= 3) {
            return buildPinAttemptExceededResponse(response, session);
        }

        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());

        String currentEncryptedPin = profile.getPin();
        if (!passwordEncoder.matches(payRRRDTO.getPin(), currentEncryptedPin)) {

            return buildPinErrorResponse(session, pinFailureCount);
        }

        GenericResponseDTO genericResponseDTO = billerTransactionService.payRRR(payRRRDTO);
        return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), genericResponseDTO.getStatus());
    }


    @RequestMapping(path = "/utils/pay", method = RequestMethod.POST)
    public ResponseEntity<GenericResponseDTO> payForUtils(@RequestBody UtilDTO utilDTO, @RequestParam Optional<Boolean> flag, HttpSession session) {
        log.info("UTILDATADTO ====> " + utilDTO);
        if(flag.isPresent()&&flag.get()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), headers,
                HttpStatus.OK);
        }
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                this.theUser = user;
            });

        GenericResponseDTO response = new GenericResponseDTO();

        int pinFailureCount = getPinFailureCount(session);

        if (pinFailureCount >= 3) {
            return buildPinAttemptExceededResponse(response, session);
        }

        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());

        String currentEncryptedPin = profile.getPin();
        if (!passwordEncoder.matches(utilDTO.getPin(), currentEncryptedPin)) {

            return buildPinErrorResponse(session, pinFailureCount);
        }

        GenericResponseDTO genericResponseDTO = billerTransactionService.payForUtils(utilDTO);
        return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), genericResponseDTO.getStatus());
    }

    @RequestMapping(path = "/electricity/pay", method = RequestMethod.POST)
    public ResponseEntity<GenericResponseDTO> payElectricity(@RequestBody UtilDTO utilDTO, @RequestParam Optional<Boolean> flag, HttpSession session) {

        if(flag.isPresent()&&flag.get()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), headers,
                HttpStatus.OK);
        }

        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                this.theUser = user;
            });

        GenericResponseDTO response = new GenericResponseDTO();

        int pinFailureCount = getPinFailureCount(session);

        if (pinFailureCount >= 3) {
            return buildPinAttemptExceededResponse(response, session);
        }


        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());

        String currentEncryptedPin = profile.getPin();
        if (!passwordEncoder.matches(utilDTO.getPin(), currentEncryptedPin)) {

            return buildPinErrorResponse(session, pinFailureCount);
        }

        ValidateRequest validateRequest = (ValidateRequest) session.getAttribute("validateRequest");
        if (validateRequest != null) {

            GenericResponseDTO genericResponseDTO = billerTransactionService.payElectricity(utilDTO, validateRequest);
            return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), genericResponseDTO.getStatus());
        }

        response.setCode("failed");
        response.setMessage("session timeout");
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path = "/validate/meter", method = RequestMethod.POST)
    public ResponseEntity<GenericResponseDTO> validateMeter(@RequestBody UtilDTO utilDTO, HttpSession session) throws JsonProcessingException {

        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                this.theUser = user;
            });

        GenericResponseDTO genericResponseDTO = billerTransactionService.validateMeter(utilDTO, session);
        return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), genericResponseDTO.getStatus());
    }

    @GetMapping("/custom-fields/{serviceTypeId}")
    public GetCustomFieldResponse getCustomeFields(@PathVariable("serviceTypeId") String serviceTypeId) throws Exception {
        return billerTransactionService.getCustomField(serviceTypeId);
    }

    @GetMapping("/billers-ipg")
    public GetBillerResponse getAllBillersFromIpg() {
        return billerTransactionService.getAllBillersFromIpg();
    }

    @GetMapping("/services-ipg/{serviceTypeId}")
    public GetServiceResponse getServicesFromIpg(@PathVariable("serviceTypeId") String serviceTypeId) {
        return billerTransactionService.getServicesFromIpg(serviceTypeId);
    }

    @PostMapping("/agents/airtime/buy")
    public ResponseEntity<GenericResponseDTO> buyAirtimeForCustomer(@RequestBody AgentAirtimeDTO agentAirtimeDTO, HttpSession session) {

        log.debug("REST request for agents to buy Airtime for Customer : {}", agentAirtimeDTO);

        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                this.theUser = user;
            });

        if (theUser == null) {
            return new ResponseEntity<>(new GenericResponseDTO("64", HttpStatus.BAD_REQUEST, "User session expired", null), HttpStatus.BAD_REQUEST);
        }

        GenericResponseDTO response = new GenericResponseDTO();

        int pinFailureCount = getPinFailureCount(session);

        if (pinFailureCount >= 3) {
            return buildPinAttemptExceededResponse(response, session);
        }


        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());

        String currentEncryptedPin = profile.getPin();
        if (!passwordEncoder.matches(agentAirtimeDTO.getPin(), currentEncryptedPin) || StringUtils.isEmpty(agentAirtimeDTO.getPin())) {
            return buildPinErrorResponse(session, pinFailureCount);
        }

        GenericResponseDTO genericResponseDTO = billerTransactionService.buyAirtimeForCustomer(agentAirtimeDTO);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/agents/data/buy")
    public ResponseEntity<GenericResponseDTO> buyDataForCustomer(@RequestBody AgentAirtimeDTO agentAirtimeDTO, HttpSession session) {

        log.debug("REST request for agents to buy Airtime for Customer : {}", agentAirtimeDTO);

        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                this.theUser = user;
            });

        if (theUser == null) {
            return new ResponseEntity<>(new GenericResponseDTO("64", HttpStatus.BAD_REQUEST, "User session expired", null), HttpStatus.BAD_REQUEST);
        }

        GenericResponseDTO response = new GenericResponseDTO();

        int pinFailureCount = getPinFailureCount(session);

        if (pinFailureCount >= 3) {
            return buildPinAttemptExceededResponse(response, session);
        }


        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());

        String currentEncryptedPin = profile.getPin();
        if (!passwordEncoder.matches(agentAirtimeDTO.getPin(), currentEncryptedPin) || StringUtils.isEmpty(agentAirtimeDTO.getPin())) {
            return buildPinErrorResponse(session, pinFailureCount);
        }

        GenericResponseDTO genericResponseDTO = billerTransactionService.buyDataForCustomer(agentAirtimeDTO);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

}
