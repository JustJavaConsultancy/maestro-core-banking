package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.justjava.corebanking.client.ExternalRESTClient2;
import ng.com.justjava.corebanking.client.ExternalRESTClient3;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.Scheme;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.AccountStatus;
import ng.com.justjava.corebanking.domain.enumeration.Gender;
import ng.com.justjava.corebanking.domain.enumeration.UserStatus;
import ng.com.justjava.corebanking.repository.ProfileRepository;
import ng.com.justjava.corebanking.repository.UserRepository;
import ng.com.justjava.corebanking.repository.WalletAccountRepository;
import ng.com.justjava.corebanking.security.SecurityUtils;
import ng.com.justjava.corebanking.security.jwt.TokenProvider;
import ng.com.justjava.corebanking.service.*;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.dto.stp.AccountDto;
import ng.com.justjava.corebanking.service.dto.stp.DefaultApiResponse;
import ng.com.justjava.corebanking.service.dto.stp.OpenAccountResponseDto;
import ng.com.justjava.corebanking.service.fcm.PushNotificationService;
import ng.com.justjava.corebanking.service.impl.WalletAccountServiceImpl;
import ng.com.justjava.corebanking.service.mapper.WalletAccountMapper;
import ng.com.justjava.corebanking.util.RemitaCarmelUtils;
import ng.com.justjava.corebanking.util.TimeGranularity;
import ng.com.justjava.corebanking.util.Utility;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;
import ng.com.justjava.corebanking.web.rest.vm.ManagedUserVM;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.systemspecs.remitarits.bankenquiry.Banks;
import ng.com.systemspecs.remitarits.bankenquiry.GetActiveBank;
import ng.com.systemspecs.remitarits.bankenquiry.GetActiveBankResponse;
import ng.com.systemspecs.remitarits.bulkpayment.BulkPaymentRequest;
import ng.com.systemspecs.remitarits.bulkpayment.BulkPaymentResponse;
import ng.com.systemspecs.remitarits.bulkpaymentstatus.BulkPaymentStatusRequest;
import ng.com.systemspecs.remitarits.bulkpaymentstatus.BulkPaymentStatusResponse;
import ng.com.systemspecs.remitarits.singlepayment.SinglePaymentRequest;
import ng.com.systemspecs.remitarits.singlepayment.SinglePaymentResponse;
import ng.com.systemspecs.remitarits.singlepaymentstatus.PaymentStatusRequest;
import ng.com.systemspecs.remitarits.singlepaymentstatus.PaymentStatusResponse;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.ap.internal.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.util.ListUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

/**
 * REST controller for managing
 * {@link WalletAccount}.
 */

@Validated
@RestController
@RequestMapping("/api")
public class WalletAccountResource {

    private static final String ENTITY_NAME = "walletAccount";
    private static final long Lower_Bond = 1000000000L;
    private static final long Upper_Bond = 9000000000L;
    public static List<String> bankNames = new ArrayList<>();
    public static HashMap<String, Banks> activeBankCache = new HashMap<>();
    private final ExternalRESTClient3 externalRESTClient3;
    private final Logger log = LoggerFactory.getLogger(WalletAccountResource.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KyclevelService kyclevelService;
    // @Autowired
    private final WalletAccountService walletAccountService;
    private final WalletAccountRepository walletAccountRepository;
    private final WalletAccountMapper walletAccountMapper;
    private final IdempotentService idempotentService;
    private final ProfileService profileService;
    private final ExternalRESTClient2 externalRESTClient2;
    private final Utility externalRestClientNewAccount;
    private final RITSService rITSService;
    private final BankService bankService;
    private final PushNotificationService pushNotificationService;
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final WalletAccountTypeService walletAccountTypeService;
    private final TokenProvider tokenProvider;
    private final AddressService addressService;
    private final SchemeService schemeService;
    private final Utility utility;
    private final ProfileRepository profileRepository;
    private final RemitaCarmelUtils remitaCarmelUtils;
    @Value("${app.constants.inline.secret-key}")
    private String secretKey;
    @Value("${app.constants.inline.inline-pmt-status-public-key}")
    private String inlinePmtStatusPublicKey;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    @Value("${app.constants.inline.api-public-key}")
    private String apiPublicKey;
    @Value("${app.image-url}")
    private String imageUrl;
    private User theUser;
    private User user;

    public WalletAccountResource(WalletAccountService walletAccountService, ProfileService profileService,
                                 UserRepository userRepository, PasswordEncoder passwordEncoder, ExternalRESTClient3 externalRESTClient3, KyclevelService kyclevelService, WalletAccountRepository walletAccountRepository, WalletAccountMapper walletAccountMapper, IdempotentService idempotentService, RITSService rITSService,
                                 ExternalRESTClient2 externalRESTClient2, Utility externalRestClientNewAccount, BankService bankService, PushNotificationService pushNotificationService,
                                 UserService userService, AuthenticationManagerBuilder authenticationManagerBuilder,
                                 WalletAccountTypeService walletAccountTypeService, TokenProvider tokenProvider, AddressService addressService, SchemeService schemeService, Utility utility, ProfileRepository profileRepository, RemitaCarmelUtils remitaCarmelUtils) {

        this.walletAccountService = walletAccountService;
        this.userRepository = userRepository;
        this.profileService = profileService;
        this.passwordEncoder = passwordEncoder;
        this.externalRESTClient3 = externalRESTClient3;
        this.kyclevelService = kyclevelService;
        this.walletAccountRepository = walletAccountRepository;
        this.walletAccountMapper = walletAccountMapper;
        this.idempotentService = idempotentService;
        this.rITSService = rITSService;
        this.externalRESTClient2 = externalRESTClient2;
        this.externalRestClientNewAccount = externalRestClientNewAccount;
        this.bankService = bankService;
        this.pushNotificationService = pushNotificationService;
        this.userService = userService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.walletAccountTypeService = walletAccountTypeService;
        this.tokenProvider = tokenProvider;
        this.addressService = addressService;
        this.schemeService = schemeService;
        this.utility = utility;
        this.profileRepository = profileRepository;
        this.remitaCarmelUtils = remitaCarmelUtils;
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) && password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH
            && password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }

    /**
     * {@code POST  /wallet-accounts} : Create a new walletAccount.
     *
     * @param walletAccountDTO the walletAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     * body the new walletAccountDTO, or with status
     * {@code 400 (Bad Request)} if the walletAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wallet-accounts")
    public ResponseEntity<WalletAccountDTO> createWalletAccount(@RequestBody WalletAccountDTO walletAccountDTO, HttpSession session)
        throws URISyntaxException {
        log.debug("REST request to save WalletAccount : {}", walletAccountDTO);

        if (walletAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new walletAccount cannot already have an ID", ENTITY_NAME,
                "id exists");
        }
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            this.theUser = user;
        });
        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());

        AdditionalWalletAccountDTO newWalletAccountDTO = new AdditionalWalletAccountDTO();
        newWalletAccountDTO.setRequestId(String.valueOf(System.currentTimeMillis()));

        newWalletAccountDTO.setAccountTypeId("PERSONAL");

        Optional<User> userOptional = userRepository.findOneByLogin(this.theUser.getLogin());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            newWalletAccountDTO.setFirstName(user.getFirstName());
            newWalletAccountDTO.setSurname(user.getLastName());
        }

        Gender gender = profile.getGender();
        newWalletAccountDTO.setGender(gender.toString());
        newWalletAccountDTO.setDateOfBirth(String.valueOf(profile.getDateOfBirth()));

//        newWalletAccountDTO.setCity(lastPageDTO.getAddress());
//        newWalletAccountDTO.setLocalGovernment();

        String phoneNumberNew;
        if (this.theUser.getLogin().startsWith("+234")) {
            phoneNumberNew = this.theUser.getLogin().substring(4, 14);
            phoneNumberNew = "0" + phoneNumberNew;
        } else {
            phoneNumberNew = this.theUser.getLogin();
        }

        newWalletAccountDTO.setCustomerId(phoneNumberNew);
        newWalletAccountDTO.setPhone(phoneNumberNew);
        newWalletAccountDTO.setEmail(this.theUser.getEmail());
        newWalletAccountDTO.setSecretKey("1234");  //TODO to be changed moving to production
        newWalletAccountDTO.setTransactionPin(profile.getPin()); // Todo user pin
        newWalletAccountDTO.setSchemeCode("598");
        newWalletAccountDTO.setRestrictedWallet(false);

//        long accountNumber = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);

        Long accountNumber = utility.getUniqueAccountNumber();

        /*
         * NewWalletAccountResponse newWalletAccount =
         * externalRestClientNewAccount.getAdditionalWalletAccount(newWalletAccountDTO);
         * if (newWalletAccount != null &&
         * newWalletAccount.getStatus().equalsIgnoreCase("success") &&
         * newWalletAccount.getData().getProcessCode().equalsIgnoreCase("00")) {
         * accountNumber = newWalletAccount.getData().getAccountNumber(); } else {
         * return new ResponseEntity<>(walletAccountDTO, new HttpHeaders(),
         * HttpStatus.BAD_REQUEST); }
         */

//        long accountNumber = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);

        walletAccountDTO.setAccountNumber(String.valueOf(accountNumber));
        walletAccountDTO.setAccountOwnerPhoneNumber(this.theUser.getLogin());
        walletAccountDTO.setAccountOwnerId(profile.getId());
        walletAccountDTO.setAccountName(walletAccountDTO.getAccountName());
        walletAccountDTO.setDateOpened(LocalDate.now());
        walletAccountDTO.setCurrentBalance(0.00);
        walletAccountDTO.setActualBalance(walletAccountDTO.getCurrentBalance());
        Scheme schemeFromSession = utility.getSchemeFromSession(session);
        walletAccountDTO.setSchemeId(schemeFromSession.getId());
        walletAccountDTO.setWalletAccountTypeId(1L);
        walletAccountDTO.setStatus(AccountStatus.ACTIVE);


        WalletAccountDTO result = walletAccountService.save(walletAccountDTO);
        return ResponseEntity
            .created(new URI("/api/wallet-accounts/" + result.getId())).headers(HeaderUtil
                .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /wallet-accounts} : Create a new walletAccount.
     *
     * @param walletAccountDTO the walletAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     * body the new walletAccountDTO, or with status
     * {@code 400 (Bad Request)} if the walletAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/add-new-wallet-account")
    public ResponseEntity<WalletAccountDTO> addNewWalletAccount(@RequestBody MinimumNewWalletDTO walletAccountDTO, HttpSession session)
        throws URISyntaxException {
        log.debug("REST request to save WalletAccount : {}", walletAccountDTO);

        String phoneNumber = utility.formatPhoneNumber(walletAccountDTO.getPhoneNumber());
        walletAccountDTO.setPhoneNumber(phoneNumber);

        Profile profile = profileService.findByPhoneNumber(walletAccountDTO.getPhoneNumber());
        if (profile == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        WalletAccountDTO accountDTO = new WalletAccountDTO();
        AdditionalWalletAccountDTO newWalletAccountDTO = new AdditionalWalletAccountDTO();
        newWalletAccountDTO.setRequestId(String.valueOf(System.currentTimeMillis()));

        newWalletAccountDTO.setAccountTypeId("PERSONAL");

        Optional<User> userOptional = userRepository.findOneByLogin(walletAccountDTO.getPhoneNumber());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            newWalletAccountDTO.setFirstName(user.getFirstName());
            newWalletAccountDTO.setSurname(user.getLastName());
        }

        Gender gender = profile.getGender();
        newWalletAccountDTO.setGender(gender.toString());
        newWalletAccountDTO.setDateOfBirth(String.valueOf(profile.getDateOfBirth()));

//        newWalletAccountDTO.setCity(lastPageDTO.getAddress());
//        newWalletAccountDTO.setLocalGovernment();

        String phoneNumberNew = utility.formatPhoneNumber(walletAccountDTO.getPhoneNumber());

        newWalletAccountDTO.setCustomerId(phoneNumberNew);
        newWalletAccountDTO.setPhone(phoneNumberNew);
        newWalletAccountDTO.setSecretKey("1234");  //TODO to be changed moving to production
        newWalletAccountDTO.setTransactionPin(profile.getPin()); // Todo user pin
        newWalletAccountDTO.setSchemeCode("598");
        newWalletAccountDTO.setRestrictedWallet(false);

//        long accountNumber = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);

        Long accountNumber = utility.getUniqueAccountNumber();

        /*
         * NewWalletAccountResponse newWalletAccount =
         * externalRestClientNewAccount.getAdditionalWalletAccount(newWalletAccountDTO);
         * if (newWalletAccount != null &&
         * newWalletAccount.getStatus().equalsIgnoreCase("success") &&
         * newWalletAccount.getData().getProcessCode().equalsIgnoreCase("00")) {
         * accountNumber = newWalletAccount.getData().getAccountNumber(); } else {
         * return new ResponseEntity<>(walletAccountDTO, new HttpHeaders(),
         * HttpStatus.BAD_REQUEST); }
         */

//        long accountNumber = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);

        Scheme schemeFromSession = utility.getSchemeFromSession(session);

        accountDTO.setAccountNumber(String.valueOf(accountNumber));
        accountDTO.setAccountOwnerPhoneNumber(walletAccountDTO.getPhoneNumber());
        accountDTO.setAccountOwnerId(profile.getId());
        accountDTO.setAccountName(walletAccountDTO.getAccountName());
        accountDTO.setDateOpened(LocalDate.now());
        accountDTO.setCurrentBalance(0.00);
        accountDTO.setActualBalance(accountDTO.getCurrentBalance());
        accountDTO.setSchemeId(schemeFromSession.getId());
        accountDTO.setAccountOwnerPhoneNumber(walletAccountDTO.getPhoneNumber());
        accountDTO.setWalletAccountTypeId(1L);
        accountDTO.setStatus(AccountStatus.ACTIVE);

        WalletAccountDTO result = null;

        try {
            List<WalletAccount> accountOptional = walletAccountService.findByAccountOwnerPhoneNumberAndAccountNameAndScheme_SchemeID(walletAccountDTO.getPhoneNumber(), walletAccountDTO.getAccountName(), schemeFromSession.getSchemeID());
            if (!accountOptional.isEmpty()) {
                WalletAccount walletAccount = accountOptional.get(0);
                log.info("Wallet found " + walletAccount);
                result = walletAccountMapper.toDto(walletAccount);
            } else {
                result = walletAccountService.save(accountDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity
            .created(new URI("/api/wallet-accounts/" + result.getId())).headers(HeaderUtil
                .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/wallet/{accountName}/{accountNumber}")
    public ResponseEntity<GenericResponseDTO>
    addSpecialAccount(@PathVariable String accountName, @PathVariable String accountNumber) throws URISyntaxException {
        return walletAccountService.addSpecialAccount(accountName, accountNumber);
    }

    @PostMapping("/special-wallet/{accountName}/{accountNumber}/{scheme}")
    public ResponseEntity<GenericResponseDTO>
    addSpecialSchemeAccount(@PathVariable String accountName, @PathVariable String accountNumber, @PathVariable String scheme) throws URISyntaxException {
        return walletAccountService.addSpecialAccount(accountName, accountNumber, scheme);
    }

    @PostMapping("/callback")
    public ResponseEntity<GenericResponseDTO> callbackUrl(@RequestBody TransactionCallBackDTO transactionCallBackDTO) {
        log.info("call back request payload ====> " + transactionCallBackDTO);
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", "success"), HttpStatus.OK);
    }

    /**
     * {@code PUT  /wallet-accounts} : Updates an existing walletAccount.
     *
     * @param walletAccountDTO the walletAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated walletAccountDTO, or with status
     * {@code 400 (Bad Request)} if the walletAccountDTO is not valid, or
     * with status {@code 500 (Internal Server Error)} if the
     * walletAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wallet-accounts")
    public ResponseEntity<WalletAccountDTO> updateWalletAccount(@RequestBody WalletAccountDTO walletAccountDTO)
        throws URISyntaxException {
        log.debug("REST request to update WalletAccount : {}", walletAccountDTO);
        if (walletAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WalletAccountDTO result = walletAccountService.save(walletAccountDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
            walletAccountDTO.getId().toString())).body(result);
    }

    /**
     * {@code GET  /wallet-accounts} : get all the walletAccounts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     * of walletAccounts in body.
     */
    @GetMapping("/wallet-accounts")
    @Secured(value = "ROLE_ADMIN")
    public ResponseEntity<GenericResponseDTO> getAllWalletAccounts(Pageable pageable) {
        log.debug("REST request to get all WalletAccounts");
        Page<WalletAccountDTO> wallets = walletAccountService.findAll(pageable);

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), wallets);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", wallets.getSize());
        metaMap.put("totalNumberOfRecords", wallets.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", wallets.getContent(), metaMap), headers, HttpStatus.OK);
    }

    /**
     * {@code GET  /wallet-accounts} : get all the walletAccounts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     * of walletAccounts in body.
     */
    @GetMapping("/wallet-accounts/{schemeId}/search")
    public ResponseEntity<GenericResponseDTO> getAllWalletAccountsByScheme(@PathVariable String schemeId, @RequestParam String key, Pageable pageable) {
        log.debug("REST request to get all WalletAccounts by SchemeId {}" + schemeId);
        Page<WalletAccountDTO> wallets = walletAccountService.findAllBySchemeAndSearchByKey(schemeId, key, pageable);

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), wallets);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", wallets.getSize());
        metaMap.put("totalNumberOfRecords", wallets.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", wallets.getContent(), metaMap), headers, HttpStatus.OK);
    }

    @GetMapping("/wallet-accounts/customers")
    public ResponseEntity<GenericResponseDTO> getAllCustomerWalletAccounts(Pageable pageable) {
        log.debug("REST request to get all Customer WalletAccounts");
        Page<WalletAccountDTO> wallets = walletAccountService.findAllByAccountOwnerIsNotNull(pageable);

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), wallets);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", wallets.getSize());
        metaMap.put("totalNumberOfRecords", wallets.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", wallets.getContent(), metaMap), headers, HttpStatus.OK);
    }

    @GetMapping("/wallet-accounts/customers/{scheme}")
    public ResponseEntity<GenericResponseDTO> getAllCustomerWalletAccountsByScheme(@PathVariable String scheme) {
        log.debug("REST request to get all Customer WalletAccounts");
        List<WalletAccountDTO> wallets = walletAccountService.findAllByAccountOwnerIsNotNullAndSchemeAndStatusNot(scheme);

        if (wallets != null) {
            Map<String, Object> metaMap = new LinkedHashMap<>();
            metaMap.put("totalNumberOfRecords", wallets.size());

            return new ResponseEntity<>(new GenericResponseDTO("00", "success", wallets, metaMap), HttpStatus.OK);
        }

        return new ResponseEntity<>(new GenericResponseDTO("99", "You don't have access to view this report", null), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/wallet-accounts/customers/search")
    public ResponseEntity<GenericResponseDTO> searchAllCustomerWalletAccountsByKeyword(Pageable pageable, @RequestParam(required = false) String key) {
        log.debug("REST request to get all Customer WalletAccounts");
        Page<WalletAccountDTO> wallets = walletAccountService.findAllByAccountOwnerIsNotNullSearchKeyword(pageable, key);

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), wallets);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", wallets.getSize());
        metaMap.put("totalNumberOfRecords", wallets.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", wallets.getContent(), metaMap), headers, HttpStatus.OK);
    }

    @GetMapping("/wallet-accounts/customers/{schemeId}/search")
    public ResponseEntity<GenericResponseDTO> searchAllCustomerWalletAccountsByKeywordAndScheme(Pageable pageable, @RequestParam(required = false) String key, @PathVariable String schemeId) {
        log.debug("REST request to get all Customer WalletAccounts");
        Page<WalletAccountDTO> wallets =
            walletAccountService.findAllByAccountOwnerIsNotNullSearchKeywordAndScheme(key, schemeId, pageable);

        if (wallets != null) {
            HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), wallets);

            Map<String, Object> metaMap = new LinkedHashMap<>();
            metaMap.put("size", wallets.getSize());
            metaMap.put("totalNumberOfRecords", wallets.getTotalElements());

            return new ResponseEntity<>(new GenericResponseDTO("00", "success", wallets.getContent(), metaMap), headers, HttpStatus.OK);
        }

        return new ResponseEntity<>(new GenericResponseDTO("99", "You don't have access to view this report", null), new HttpHeaders(), HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/wallet-accounts/customers/primary/{schemeId}/search")
    public ResponseEntity<GenericResponseDTO> searchAllCustomerPrimaryWalletAccountsByKeywordAndScheme(Pageable pageable, @RequestParam(required = false) String key, @PathVariable String schemeId) {
        log.debug("REST request to get all Customer WalletAccounts");
        Page<WalletAccountDTO> wallets =
            walletAccountService.findAllPrimaryWalletByAccountOwnerIsNotNullSearchKeywordAndScheme(key, schemeId, pageable);

        if (wallets != null) {
            HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), wallets);

            Map<String, Object> metaMap = new LinkedHashMap<>();
            metaMap.put("size", wallets.getSize());
            metaMap.put("totalNumberOfRecords", wallets.getTotalElements());

            return new ResponseEntity<>(new GenericResponseDTO("00", "success", wallets.getContent(), metaMap), headers, HttpStatus.OK);
        }

        return new ResponseEntity<>(new GenericResponseDTO("99", "You don't have access to view this report", null), new HttpHeaders(), HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/wallet-accounts/special")
    public ResponseEntity<GenericResponseDTO> getAllSpecialAccounts() {
        log.debug("REST request to get all Special WalletAccounts");
        List<WalletAccountDTO> wallets = walletAccountService.findAllByAccountOwnerIsNull();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", wallets), HttpStatus.OK);
    }

    @GetMapping("/wallet-accounts/special/{schemeId}")
    public ResponseEntity<GenericResponseDTO> getSchemeSpecialAccounts(@PathVariable String schemeId) {
        log.debug("REST request to get all Special WalletAccounts");
        Scheme scheme = schemeService.findBySchemeID(schemeId);
        List<WalletAccountDTO> wallets = walletAccountService.findAllBySchemeAndAccountOwnerIsNull(scheme);

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", wallets), HttpStatus.OK);
    }

    @GetMapping("/customer_wallet-accounts")
    public List<WalletAccountDTO> getAllCustomerWalletAccountsByScheme(HttpSession session) {
        log.debug("REST request to get all WalletAccounts");
        return walletAccountService.findByUserIsCurrentUser(session);
    }

    @GetMapping("/customer-wallet-accounts-scheme/{phoneNumber}/{schemeId}")
    public List<WalletAccountDTO> getCustomerWalletAccountsBySchemePhonenumber(@PathVariable String phoneNumber, @PathVariable String schemeId) {
        log.debug("REST request to get Customer Wallet Accounts");
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        return walletAccountService.findByPhoneNumberAndScheme(phoneNumber, schemeId).stream().map(walletAccountMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/customer_wallets")
    public List<WalletAccountDTO> getAllCustomerWalletAccounts() {
        log.debug("REST request to get all WalletAccounts");
        return walletAccountService.findByUserIsCurrentUser();
    }

    @GetMapping("/wallet/customer/{phoneNumber}")
    public List<WalletAccountDTO> findAllByAccountOwnerPhoneNumber(@PathVariable String phoneNumber, HttpSession session) {
        log.debug("REST request to get all WalletAccounts");
        return walletAccountService.findAllByAccountOwnerPhoneNumber(phoneNumber, session);
    }

    @GetMapping("/wallet/agent/{phoneNumber}")
    public List<WalletAccountDTO> findAgentPrimaryAccount(@PathVariable String phoneNumber) {
        log.debug("REST request to get all WalletAccounts");
        return walletAccountService.findAgentPrimaryAccount(phoneNumber, 2L);
    }

    @GetMapping("/wallet/teller/{phoneNumber}")
    public List<WalletAccountDTO> findTellerPrimaryAccount(@PathVariable String phoneNumber) {
        log.debug("REST request to get all Teller Primary wallet");
        return walletAccountService.findAgentPrimaryAccount(phoneNumber, 5L);
    }

    /**
     * {@code GET  /wallet-accounts/:id} : get the "id" walletAccount.
     *
     * @param id the id of the walletAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the walletAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wallet-accounts/{id}")
    public ResponseEntity<WalletAccountDTO> getWalletAccount(@PathVariable Long id) {
        log.debug("REST request to get WalletAccount : {}", id);
        Optional<WalletAccountDTO> walletAccountDTO = walletAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(walletAccountDTO);
    }


    @GetMapping("/wallet-accounts/avs/{accountNumber}")
    public ResponseEntity<String> getWalletAccountByAccountNumber(@PathVariable Long accountNumber) {
        log.debug("REST request to get WalletAccount : {}", accountNumber);
        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(String.valueOf(accountNumber));
        String response = "AccountNotFound";
        if (walletAccount != null) {
            if (walletAccount.getAccountOwner() == null) {
                response = walletAccount.getAccountName();
                return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
            }
            response = walletAccount.getAccountOwner().getFullName();
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/wallets/avs/{accountNumber}")
    public ResponseEntity<AccountDetailsDTO> getWalletAccountDetails(@PathVariable Long accountNumber) {
        log.debug("REST request to get WalletAccount : {}", accountNumber);
        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(String.valueOf(accountNumber));
        AccountDetailsDTO response = new AccountDetailsDTO();
        if (walletAccount != null) {
            Profile accountOwner = walletAccount.getAccountOwner();
            response.setAccountName(walletAccount.getAccountFullName());
            response.setNubanAccountNumber(walletAccount.getNubanAccountNo());
            response.setTrackingRef(walletAccount.getTrackingRef());
            if (accountOwner != null) {
                response.setPhoneNumber(accountOwner.getPhoneNumber());
            }
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * {@code DELETE  /wallet-accounts/:id} : delete the "id" walletAccount.
     *
     * @param id the id of the walletAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wallet-accounts/{id}")
    public ResponseEntity<Void> deleteWalletAccount(@PathVariable Long id) {
        log.debug("REST request to delete WalletAccount : {}", id);
        walletAccountService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
/*
    @PostMapping(path = "/fund-wallet", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponseDTO> fundWalletAccount(@Valid @RequestHeader(value = "Idempotent-key", required = false) String idempotentKey, @RequestBody FundDTO fundDTO) {
        Optional<Idempotent> idempotentOptional = idempotentService.findByIdempotentKey(idempotentKey);
        if (idempotentOptional.isPresent()){
            String response = idempotentOptional.get().getResponse();
            PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
            try {
                paymentResponseDTO = (PaymentResponseDTO) utility.deserializeIdempotentResponse(response, paymentResponseDTO);
                if (paymentResponseDTO != null && paymentResponseDTO.getError()){
                    return new ResponseEntity<>(paymentResponseDTO, HttpStatus.BAD_REQUEST);
                }else {
                    return new ResponseEntity<>(paymentResponseDTO, HttpStatus.OK);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {

            ResponseEntity<PaymentResponseDTO> fundResponseEntity = walletAccountService.fund(fundDTO);
            try {
                PaymentResponseDTO body = fundResponseEntity.getBody();
                String response = utility.serializeIdempotentResponse(body);
                Idempotent idempotent = new Idempotent();
                idempotent.setIdempotentKey(idempotentKey);
                idempotent.setResponse(response);
                idempotentService.save(idempotent);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return fundResponseEntity;
        }
        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
        responseDTO.setError(true);
        responseDTO.setCode("Failed");
        responseDTO.setMessage("Error while processing");
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }*/

    @PostMapping(path = "/fund-wallet", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponseDTO> fundWalletAccount(@Valid @RequestBody FundDTO fundDTO, HttpSession session) {
        return walletAccountService.fund(fundDTO);
    }

    @PostMapping(path = "/fund-wallet/{transRef}")
    public ResponseEntity<PaymentResponseDTO> fundWalletAccountExternal(@PathVariable String transRef) {
        log.info("REST request to fund wallet external with transRef : {}", transRef);
        return walletAccountService.fundWalletExternal(transRef);
    }

    @PostMapping(path = "/fund-wallet/external")
    public ResponseEntity<PaymentResponseDTO> fundWalletExternal(@RequestParam String transRef) {
        log.info("REST request to fund wallet external with transRef as request param: {}", transRef);
        return walletAccountService.fundWalletExternal(transRef);
    }

    @PostMapping(path = "/fund-wallet/external/header")
    public ResponseEntity<PaymentResponseDTO> fundWalletExternalFromHeader(@RequestHeader String transRef) {
        log.info("REST request to fund wallet external with transRef as request param: {}", transRef);
        return walletAccountService.fundWalletExternal(transRef);
    }

    @PostMapping(path = "/demo/fund-wallet/{transRef}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponseDTO> fundWalletAccountExternalDemo(@PathVariable String transRef) {
        return walletAccountService.fundWalletExternalDemo(transRef);
    }

    @PostMapping(path = "/ipg/fund-wallet", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IpgResponseDTO> fundWalletAccountIPG(@Valid @RequestBody FundDTO fundDTO, HttpSession session) {
        try {
            ResponseEntity<PaymentResponseDTO> fund = walletAccountService.fund(fundDTO);
            IpgResponseDTO ipgResponseDTO = new IpgResponseDTO();
            ipgResponseDTO.setStatus(fund.getBody().getCode());
            ipgResponseDTO.setMessage(fund.getBody().getMessage());
            ipgResponseDTO.setCode(fund.getStatusCodeValue());
            ipgResponseDTO.setData(fund.getBody());
            return new ResponseEntity<>(ipgResponseDTO, fund.getStatusCode());
        } catch (Exception e) {
            IpgResponseDTO ipgResponseDTO = new IpgResponseDTO();
            ipgResponseDTO.setCode(400);
            ipgResponseDTO.setMessage("failed");
            ipgResponseDTO.setStatus("Bad Request");
            return new ResponseEntity<>(ipgResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/send-money")
    public ResponseEntity<PaymentResponseDTO> sendMoney(@Valid @RequestBody FundDTO sendMoneyDTO, HttpSession session)
        throws URISyntaxException {
        // this.pinCorrect = true;

        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            this.theUser = user;
        });

        PaymentResponseDTO response = new PaymentResponseDTO();

        int pinFailureCount = getPinFailureCount(session);
        boolean sendMoneyRequestCount = canSendMoney(session, sendMoneyDTO.getAmount(), sendMoneyDTO.getAccountNumber());

        if (!sendMoneyRequestCount) {
            return buildDuplicateExceededResponse(response);
        }

        if (pinFailureCount >= 3) {
            return buildPinAttemptExceededResponse(response, session);
        }



        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());
        String currentEncryptedPin = profile.getPin();
        log.info("SendMoneyDTO pin ===> " + sendMoneyDTO.getPin());

        if (!passwordEncoder.matches(sendMoneyDTO.getPin(), currentEncryptedPin)) {

            return buildPinErrorResponse(session, pinFailureCount);
        }

        session.removeAttribute("pinFailureCount");

        out.println("The fundDTO in send money here === " + sendMoneyDTO);

        response = walletAccountService.sendMoney(sendMoneyDTO);

        if (response.getError()) {
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    private int getPinFailureCount(HttpSession session) {
        int pinFailureCount = 1;

        try {
            if (session != null) {
                Object loginCountStr = session.getAttribute("pinFailureCount");
                if (loginCountStr != null) {
                    pinFailureCount = (int) loginCountStr;
                    out.println("Pin failure Counter ====> " + pinFailureCount);
                } else {
                    pinFailureCount = 1;
                }
            }
        } catch (Exception e) {
            pinFailureCount = 1;
        }
        return pinFailureCount;
    }

    private boolean canSendMoney(HttpSession session, double a, String destinationAccount) {
        boolean sendMoney = false;
        try {
            if (session != null) {
                Object amt = session.getAttribute("sendRequestAmt");
                Object intvl = session.getAttribute("sendRequestInterval");
                Object dAcct = session.getAttribute("sendRequestDestinationAccount");
                if (amt != null && intvl !=null) {
                    Double amount = (Double) amt;
                    Date pastTime = (Date) intvl;
                    String destination = (String) dAcct;
                    pastTime =  new Date(pastTime.getTime()-(1 * 5 * 60 * 1000));
                    if(amount == a && destination == destinationAccount){
                        long interval = calculateTimeAgoByTimeGranularity(new Date(), pastTime , TimeGranularity.MINUTES);
                        if(interval >= 5){
                            sendMoney=true;
                        }
                    }
                    else sendMoney = true;
                }
                else{
                    sendMoney=true;
                    session.setAttribute("sendRequestAmt", a);
                    session.setAttribute("sendRequestDestinationAccount", destinationAccount);
                    session.getAttribute("sendRequestInterval");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return sendMoney;
        }
        out.println("Send Money Interval lock: "+sendMoney);
        return sendMoney;
    }

    static long calculateTimeAgoByTimeGranularity(Date currentTime, Date pastTime, TimeGranularity granularity) {
        long timeDifferenceInMillis = currentTime.getTime() - pastTime.getTime();
        return timeDifferenceInMillis / granularity.toMillis();
    }

    @PostMapping("/send-money-external")
    public ResponseEntity<PaymentResponseDTO> sendMoneyExternal(@Valid @RequestBody FundDTO sendMoneyDTO)
        throws URISyntaxException {
        // this.pinCorrect = true;
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            this.theUser = user;
        });
        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());
        // Profile profile = profileService.
        String currentEncryptedPin = profile.getPin();
        if (!passwordEncoder.matches(sendMoneyDTO.getPin(), "$2a$10$ny7.PC67b/MAzHgvwZucmelsR70vbm0RNoetnpImMJbRzlQGaD1lq")) {
            // throw new InvalidPasswordException();
            // pinCorrect = false;
            PaymentResponseDTO response = new PaymentResponseDTO();
            response.setMessage("invalid pin");
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }

        out.println(" The fundDTO in send money here === " + sendMoneyDTO);

        PaymentResponseDTO response = walletAccountService.sendMoney(sendMoneyDTO);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (response.getError()) {
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/send-money-correspondent/{sourceAccountNumber}/{accountNumber}/{amount}")
    public ResponseEntity<PaymentResponseDTO> sendMoneyWithCorrespondence(@PathVariable String sourceAccountNumber,
                                                                          @PathVariable String accountNumber,
                                                                          @PathVariable double amount)
        throws URISyntaxException {
        out.println(" The Source account number in send money here === " + sourceAccountNumber);
        out.println(" The destination account number in send money here === " + accountNumber);

        PaymentResponseDTO response = walletAccountService.sendMoneyWithCorrespondence(sourceAccountNumber, accountNumber, amount);

        if (response.getError()) {
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @PostMapping("/send-money-correspondent/{sourceAccountNumber}/{accountNumber}/{amount}/{narration}")
    public ResponseEntity<PaymentResponseDTO> sendMoneyWithCorrespondence(@PathVariable String sourceAccountNumber,
                                                                          @PathVariable String accountNumber,
                                                                          @PathVariable double amount,
                                                                          @PathVariable String narration)
        throws URISyntaxException {
        out.println(" The Source account number in send money here === " + sourceAccountNumber);
        out.println(" The destination account number in send money here === " + accountNumber);

        PaymentResponseDTO response = walletAccountService.sendMoneyWithCorrespondence(sourceAccountNumber, accountNumber,
            amount, narration);

        if (response.getError()) {
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/send-money-bank-correspondent")
    public ResponseEntity<PaymentResponseDTO> sendMoneyToBankWithCorrespondence(@RequestBody FundDTO fundDTO)
        throws URISyntaxException {

        PaymentResponseDTO response = walletAccountService.sendMoneyToBankWithCorrespondence(fundDTO);

        if (response.getError()) {
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/mini-send-money")
    public ResponseEntity<PaymentResponseDTO> sendIbileMoney(@Valid @RequestBody MiniFundDTO sendMoneyDTO)
        throws URISyntaxException {
        // this.pinCorrect = true;
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            this.theUser = user;
        });
        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());
        // Profile profile = profileService.
        String currentEncryptedPin = profile.getPin();
        if (!passwordEncoder.matches(sendMoneyDTO.getTransPin(), currentEncryptedPin)) {
            // throw new InvalidPasswordException();
            // pinCorrect = false;
            PaymentResponseDTO response = new PaymentResponseDTO();
            response.setMessage("invalid pin");
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }

        out.println(" The fundDTO in send money here === " + sendMoneyDTO);
        FundDTO fundDTO = new FundDTO();
        fundDTO.setAccountNumber("0838383838");
        fundDTO.setAmount(Double.parseDouble(sendMoneyDTO.getAmount()));
        fundDTO.setChannel("wallettobank");
        fundDTO.setDestBankCode("024");
        fundDTO.setNarration("Ibile Trasnaction");
        fundDTO.setSourceAccountNumber(sendMoneyDTO.getWalletID());
        fundDTO.setSourceBankCode("ABC");
        fundDTO.setSpecificChannel("ibile");
        fundDTO.setTransRef("TTTTTTTTTT");
        PaymentResponseDTO response = walletAccountService.sendMoney(fundDTO);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (response.getError()) {
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/ipg/send-money")
    public ResponseEntity<IpgResponseDTO> sendMoneyIPG(@Valid @RequestBody FundDTO sendMoneyDTO)
        throws URISyntaxException {
        // this.pinCorrect = true;
        IpgResponseDTO ipgResponseDTO = new IpgResponseDTO();
        String currentEncryptedPin;

        out.println("sendMoneyIPG ==> " + sendMoneyDTO);
        sendMoneyDTO.setPhoneNumber(utility.formatPhoneNumber(sendMoneyDTO.getPhoneNumber()));

//        if (sendMoneyDTO.getPhoneNumber() != null) {
//            Profile profile = profileService.findByPhoneNumber(sendMoneyDTO.getPhoneNumber());
//            // Profile profile = profileService.
//            if (profile != null) {
//                currentEncryptedPin = profile.getPin();
//            }else {
//                ipgResponseDTO.setCode(400);
//                ipgResponseDTO.setMessage("Account owner not found");
//                ipgResponseDTO.setStatus("failed");
//                ipgResponseDTO.setData(null);
//                return new ResponseEntity<>(ipgResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
//            }
//        }

        String sourceAccountNumber = sendMoneyDTO.getSourceAccountNumber();
        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(sourceAccountNumber);
        if (walletAccount == null) {
            ipgResponseDTO.setCode(400);
            ipgResponseDTO.setMessage("Invalid source accountNumber");
            ipgResponseDTO.setStatus("failed");
            ipgResponseDTO.setData(null);
            return new ResponseEntity<>(ipgResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        Profile accountOwner = walletAccount.getAccountOwner();
        if (accountOwner == null) {
            ipgResponseDTO.setCode(400);
            ipgResponseDTO.setMessage("Account owner not found");
            ipgResponseDTO.setStatus("failed");
            ipgResponseDTO.setData(null);
            return new ResponseEntity<>(ipgResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        currentEncryptedPin = accountOwner.getPin();

        if (!passwordEncoder.matches(sendMoneyDTO.getPin(), currentEncryptedPin)) {
            // throw new InvalidPasswordException();
            // pinCorrect = false;
            IpgResponseDTO response = new IpgResponseDTO();
            response.setCode(401);
            response.setMessage("invalid pin");
            response.setStatus("failed");
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }
        try {
            PaymentResponseDTO response = walletAccountService.sendMoney(sendMoneyDTO);

            if (response.getError()) {
                ipgResponseDTO.setCode(400);
                ipgResponseDTO.setMessage(response.getMessage());
                ipgResponseDTO.setStatus("failed");
                ipgResponseDTO.setData(response.getPaymentTransactionDTO());
                return new ResponseEntity<>(ipgResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            } else {
                ipgResponseDTO.setCode(200);
                ipgResponseDTO.setMessage(response.getMessage());
                ipgResponseDTO.setStatus("success");
                ipgResponseDTO.setData(response.getPaymentTransactionDTO());
                return new ResponseEntity<>(ipgResponseDTO, new HttpHeaders(), HttpStatus.OK);
            }
        } catch (Exception e) {
            ipgResponseDTO.setCode(400);
            ipgResponseDTO.setMessage("failed");
            ipgResponseDTO.setStatus("Bad Request");
            return new ResponseEntity<>(ipgResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/request-money")
    public ResponseEntity<PaymentResponseDTO> requestMoney(@Valid @RequestBody FundDTO requestMoneyDTO, HttpSession session)
        throws URISyntaxException {


        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            this.theUser = user;
        });

        if ("null".equalsIgnoreCase(requestMoneyDTO.getPhoneNumber()) ||
            utility.checkStringIsNotValid(requestMoneyDTO.getPhoneNumber())) {
            requestMoneyDTO.setPhoneNumber("");
        }
        out.println(" Sending requestMoneyDTO from the front end==" + requestMoneyDTO);

        // Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());

        String s = utility.formatPhoneNumber(requestMoneyDTO.getPhoneNumber());
        requestMoneyDTO.setPhoneNumber(s);

        PaymentResponseDTO response = walletAccountService.requestMoney(requestMoneyDTO);

        if (Boolean.TRUE.equals(response.getError())) {
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else if (response.getCode().equalsIgnoreCase("00")) {
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/treat-invoice", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponseDTO> treatInvoice(@RequestBody InvoiceDTO invoiceDTO, HttpSession session) {

        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            this.theUser = user;
        });
        PaymentResponseDTO response = new PaymentResponseDTO();

        int pinFailureCount = getPinFailureCount(session);

        if (pinFailureCount >= 3) {
            return buildPinAttemptExceededResponse(response, session);
        }

        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());
        // Profile profile = profileService.
        String currentEncryptedPin = profile.getPin();
        if (!passwordEncoder.matches(invoiceDTO.getPin(), currentEncryptedPin)) {
            // throw new InvalidPasswordException();
            // pinCorrect = false;

            return buildPinErrorResponse(session, pinFailureCount);
        }

        session.removeAttribute("pinFailureCount");
        PaymentResponseDTO paymentResponseDTO = null;
        try {
            paymentResponseDTO = walletAccountService.treatInvoice(invoiceDTO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (Boolean.TRUE.equals(paymentResponseDTO.getError())) {
            return new ResponseEntity<>(paymentResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(paymentResponseDTO, new HttpHeaders(), HttpStatus.ACCEPTED);

    }

    private ResponseEntity<PaymentResponseDTO> buildPinAttemptExceededResponse(PaymentResponseDTO response, HttpSession session) {
        response.setMessage("Maximum failed pin reached, account deactivated");
        response.setCode("46");
        this.theUser.setActivated(false);
        this.theUser.setStatus(UserStatus.DEACTIVATE_CUSTOMER_PIN.getName());
        userRepository.save(this.theUser);

        session.removeAttribute("pinFailureCount");

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<PaymentResponseDTO> buildDuplicateExceededResponse(PaymentResponseDTO response) {
        response.setMessage("Duplicate transaction. Wait 5 minutes and try again!");
        response.setCode("40");
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<PaymentResponseDTO> buildPinErrorResponse(HttpSession session, int pinFailureCount) {
        PaymentResponseDTO response;
        session.setAttribute("pinFailureCount", ++pinFailureCount);

        response = new PaymentResponseDTO();
        response.setMessage("invalid pin");
        response.setCode("51");
        response.setError(true);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping({"/payment", "/rits-payment"})
    public SinglePaymentResponse singlePayment(@RequestBody SinglePaymentRequest singleRequest) {
        return rITSService.singlePayment(singleRequest);
    }

    @PostMapping({"/bulk-payment", "/rits-bulk-payment"})
    public BulkPaymentResponse postBulkPayment(@RequestBody BulkPaymentRequest request) {
        return rITSService.postBulkPayment(request);
    }

    @PostMapping({"/payment-status", "/rits-payment-status"})
    public PaymentStatusResponse singlePaymentStatus(@RequestBody PaymentStatusRequest request) {
        return rITSService.singlePaymentStatus(request);
    }

    @PostMapping({"/bulk-payment-status", "/rits-bulk-payment-status"})
    public BulkPaymentStatusResponse bulkPaymentStatus(@RequestBody BulkPaymentStatusRequest request) {
        return rITSService.bulkPaymentStatus(request);
    }

    @GetMapping({"/banks/all", "/rits-banks"})
    public GetActiveBankResponse getActiveBanks() {
        return rITSService.getActiveBanks();
    }

    @GetMapping("/banks/refresh")
    public GetActiveBankResponse getSaveActiveBanks() {
        GetActiveBankResponse activeBanks = rITSService.getActiveBanks();
        if (activeBanks != null && activeBanks.getStatus().equalsIgnoreCase("success")) {
            List<Banks> banks = activeBanks.getData().getBanks();
            banks.forEach(bank -> {
                if (!bankService.findByBankCode(bank.getBankCode()).isPresent()) {
                    BankDTO bankDTO = new BankDTO();
                    bankDTO.setBankAccronym(bank.getBankAccronym());
                    bankDTO.setBankCode(bank.getBankCode());
                    bankDTO.setBankName(bank.getBankName());
                    bankDTO.setType(bank.getType());

                    BankDTO save = bankService.save(bankDTO);
                    log.info("SAVED BANK " + save);
                } else {
                    log.info("SKIPPING BANK ====> " + bank.getBankName());
                }
            });
        }
        return activeBanks;
    }

    @GetMapping("/banks")
    public ResponseEntity<GenericResponseDTO> GetAllBanks() {

        List<BankDTO> banks = bankService.getAllActiveBanks();

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", banks), HttpStatus.OK);

    }

    @GetMapping("/banks/regular")
    public ResponseEntity<GenericResponseDTO> GetAllRegularBanks(
        @PageableDefault(page = 0, size = 8)
        @SortDefault.SortDefaults({
            @SortDefault(sort = "type", direction = Sort.Direction.ASC),
            @SortDefault(sort = "bankCode", direction = Sort.Direction.ASC)
        })
            Pageable pageable) {

        Page<BankDTO> banks = bankService.findAllRegularBanks(pageable);

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), banks);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", banks.getSize());
        metaMap.put("totalNumberOfRecords", banks.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", banks.getContent(), metaMap), headers, HttpStatus.OK);

    }

    @GetMapping("/banks/commercial")
    public ResponseEntity<GenericResponseDTO> GetAllRegularBanks() {
        List<BankDTO> banks = bankService.findCommercialBanks();
        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", banks.size());
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", banks, metaMap), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/banks/microfinance")
    public ResponseEntity<GenericResponseDTO> GetMicrofinanceBanks() {
        List<BankDTO> banks = bankService.findMicrofinanceBanks();
        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", banks.size());
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", banks, metaMap), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/active/banks")
    public ResponseEntity<GenericResponseDTO> GetAllActiveBanks(
        @PageableDefault(page = 0, size = 20)
        @SortDefault.SortDefaults({
            @SortDefault(sort = "type", direction = Sort.Direction.ASC),
            @SortDefault(sort = "bankCode", direction = Sort.Direction.ASC)
        }) Pageable pageable) {
        Page<BankDTO> banks = bankService.getAllActiveBanks(pageable);
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), banks);
        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", banks.getSize());
        metaMap.put("totalNumberOfRecords", banks.getTotalElements());
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", banks.getContent(), metaMap), headers, HttpStatus.OK);
    }

    @GetMapping({"/bank/{partbankname}/filter", "/rits-bank/{partbankname}/filter"})
    public GetActiveBankResponse getSearchedActiveBank(@PathVariable("partbankname") String partbankname) {
        List<Banks> foundBanks = new ArrayList<>();
        try {
            if (MapUtils.isEmpty(activeBankCache)) {
                synchronized (this) {
                    GetActiveBankResponse getActiveBankResponse = rITSService.getActiveBanks();
                    if (getActiveBankResponse != null
                        && getActiveBankResponse.getData().getResponseCode().equals("00")) {
                        GetActiveBank activeBanks = getActiveBankResponse.getData();
                        for (Banks bank : activeBanks.getBanks()) {
                            activeBankCache.put(bank.getBankName().toLowerCase() + bank.getBankCode(), bank);
                            bankNames.add(bank.getBankName().toLowerCase() + bank.getBankCode());
                        }
                    }
                }
            }

            List<String> foundBankNamex = new ArrayList<>();
            Stream<String> foundBankNames = bankNames.stream().filter(x -> x.contains(partbankname.toLowerCase()));
            foundBankNames.forEach(foundBankNamex::add);
            log.debug("Banks Found SIZE  = " + foundBankNamex.size());

            if (!ListUtils.isEmpty(foundBankNamex)) {
                for (String foundBankes : foundBankNamex) {
                    foundBanks.add(activeBankCache.get(foundBankes));
                }
            }
        } catch (Exception ex) {
            return getResponse("11", "failed", "Error connecting to RITS interface ", new ArrayList<Banks>());
        }
        return getResponse("00", "success", foundBanks.size() + " banks found", foundBanks);
    }

    private GetActiveBankResponse getResponse(String responseCode, String status, String responseMessage,
                                              List<Banks> returnData) {
        GetActiveBankResponse responseObject = new GetActiveBankResponse();
        responseObject.setStatus(status);
        GetActiveBank getActiveBank = new GetActiveBank();
        getActiveBank.setResponseCode(responseCode);
        getActiveBank.setResponseDescription(responseMessage);
        getActiveBank.setBanks(returnData);
        getActiveBank.setResponseId(String.valueOf(System.currentTimeMillis()));

        responseObject.setData(getActiveBank);
        return responseObject;
    }

    @PostMapping("/validate-bvn")
    public ResponseEntity<GenericResponseDTO> validateBvn(@RequestParam("bvn") String bvn) {
        Map<String, String> headers = new java.util.HashMap<>();
        headers.put("X-API-PUBLIC-KEY", apiPublicKey);
//        LinkedHashMap response = (LinkedHashMap) externalRESTClient2.validateBvn(headers, bvn);
//        LinkedHashMap data = (LinkedHashMap) response.get("data");

        RemitaBVNRequest remitaBVNRequest = new RemitaBVNRequest();
        remitaBVNRequest.setRequestReference("{{transRef}}");
        remitaBVNRequest.setBvn(bvn);

//        GenericResponseDTO bvnResponse = remitaCarmelUtils.requestWithPayload("/remita/validation/bvn", remitaBVNRequest, HttpMethod.POST);
//
//        if (bvnResponse.getStatus() != HttpStatus.OK){
//            return new ResponseEntity<>(bvnResponse, bvnResponse.getStatus());
//        }
//
//        String genericResponseDTOData = (String) bvnResponse.getData();

        RemitaBVNResponse response = remitaCarmelUtils.bvnResponse(remitaBVNRequest);

        if (response == null){
            return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.BAD_REQUEST, "BVN Verification Failed"), HttpStatus.BAD_REQUEST);
        }

        RemitaBVNResponseData data = (RemitaBVNResponseData) response.getData();

        GenericResponseDTO genericResponseDTO = new GenericResponseDTO();
//        if (!data.isEmpty()) {
        if (data != null) {
            ProfileDTO profileDTO = profileService.findByUserIsCurrentUser().get();
            out.println("Response ====================" + response);
            out.println("data====================" + data);
            out.println("profileDTO====================" + profileDTO);
            out.println("Profile User====================" + profileDTO.getUser().toString());
            LocalDate dob = LocalDate.parse(data.getDateOfBirth(), DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
//            if (profileDTO.getPhoneNumber().equalsIgnoreCase(String.valueOf(response.get("phoneNumber"))) ||//TODO change to dateOfBirth
//                profileDTO.getUser().getFirstName().equalsIgnoreCase(String.valueOf(data.get("firstName"))) ||
//                profileDTO.getUser().getLastName().equalsIgnoreCase(String.valueOf(data.get("lastName")))) {
            if (profileDTO.getDateOfBirth().compareTo(dob) == 0 ||
                profileDTO.getUser().getFirstName().equalsIgnoreCase(data.getFirstName()) ||
                profileDTO.getUser().getLastName().equalsIgnoreCase(data.getLastName())) {
                genericResponseDTO.setCode("00");
                genericResponseDTO.setData(data);
                genericResponseDTO.setMessage("Verification Successful");
                genericResponseDTO.setStatus(HttpStatus.OK);
                return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
            }
        }
        genericResponseDTO.setCode("01");
        genericResponseDTO.setData(data);
        genericResponseDTO.setMessage("Verification Failed");
        genericResponseDTO.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/new-wallet-account")
    public ResponseEntity<NewWalletAccountResponse> newWalletAccount(@RequestBody NewWalletAccountDTO newWalletAccountDTO) {
        NewWalletAccountResponse newWalletAccountResponse = null;
        try {
            //newWalletAccountResponse = externalRESTClientLogin.getNewWalletAccount(newWalletAccountDTO);
            newWalletAccountResponse = walletAccountService.openNewWalletAccountByForeignEndPoint(newWalletAccountDTO);
        } catch (Exception ex) {
            ex.printStackTrace();
            newWalletAccountResponse = new NewWalletAccountResponse();
            newWalletAccountResponse.setStatus("failed");
            return new ResponseEntity<>(newWalletAccountResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newWalletAccountResponse, HttpStatus.OK);
    }

    @PostMapping("/additional-wallet-account")
    public ResponseEntity<NewWalletAccountResponse> addWalletAccount(@RequestBody AdditionalWalletAccountDTO additionalWalletAccountDTO) {
        NewWalletAccountResponse newWalletAccountResponse = null;
        try {
            newWalletAccountResponse = externalRESTClient2.getAdditionalWalletAccount(additionalWalletAccountDTO);
        } catch (Exception ex) {
            ex.printStackTrace();
            newWalletAccountResponse = new NewWalletAccountResponse();
            newWalletAccountResponse.setStatus("failed");
            new ResponseEntity<>(newWalletAccountResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newWalletAccountResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/wallet-external")
    public ResponseEntity<GenericResponseDTO> createWalletExternal(@Valid @RequestBody WalletExternalDTO walletExternalDTO,
                                                                   HttpSession session) {

        return walletAccountService.createWalletExternal(walletExternalDTO, session);

    }

    @PostMapping(path = "/nuban/retrieve/{phoneNumber}")
    public ResponseEntity<GenericResponseDTO> retrieveNuban(@PathVariable String phoneNumber) {

        return walletAccountService.retrieveNuban(phoneNumber);

    }

    @PostMapping(path = "/polaris/nuban/retrieve/{phoneNumber}/{schemeId}")
    public ResponseEntity<GenericResponseDTO> retrievePolarisNuban(
        @PathVariable String phoneNumber,
        @PathVariable String schemeId) {

        log.info("REST request to retrieve user nuban number by phonenumber and scheme {} {}", phoneNumber, schemeId);
        return walletAccountService.retrieveNubanByScheme(phoneNumber, schemeId);

    }

    @PostMapping(path = "/account/open")
    public ResponseEntity<ng.com.justjava.corebanking.service.dto.stp.DefaultApiResponse> createWalletExternal(@Valid @RequestBody AccountDto accountDto,
                                                                                                               HttpSession session) {

        ng.com.justjava.corebanking.service.dto.stp.DefaultApiResponse defaultApiResponse = new DefaultApiResponse();

        if (Strings.isEmpty(accountDto.getPhone())) {
            defaultApiResponse.setMessage("Phone Number Cannot be Empty");
            defaultApiResponse.setStatus("Failed");
            return new ResponseEntity<>(defaultApiResponse, HttpStatus.BAD_REQUEST);
        }

        if (accountDto.getPhone().length() < 11 || accountDto.getPhone().length() > 15) {
            defaultApiResponse.setMessage("Phone number must be between 11 to 13 digits");
            defaultApiResponse.setStatus("Failed");
            return new ResponseEntity<>(defaultApiResponse, HttpStatus.BAD_REQUEST);
        }

        if (!checkPasswordLength(accountDto.getSecretKey())) {
            defaultApiResponse.setMessage("Password Cannot be Empty");
            defaultApiResponse.setStatus("Failed");
            return new ResponseEntity<>(defaultApiResponse, HttpStatus.BAD_REQUEST);
        }

        String phoneNumber = utility.formatPhoneNumber(accountDto.getPhone());
        accountDto.setPhone(phoneNumber);
        //CREATE PIN
        if (Strings.isEmpty(accountDto.getTransactionPin())) {
            defaultApiResponse.setMessage("unable to validate user");
            defaultApiResponse.setStatus("Failed");
            return new ResponseEntity<>(defaultApiResponse, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

        RegisteredUserDTO registeredUserDTO = new RegisteredUserDTO();
        registeredUserDTO.setFirstName(accountDto.getFirstName());
        registeredUserDTO.setLastName(accountDto.getSurname());
        registeredUserDTO.setPhoneNumber(accountDto.getPhone());
        registeredUserDTO.setPassword(accountDto.getSecretKey());
        registeredUserDTO.setEmail(accountDto.getEmail());
        String deviceNotificationToken = String.valueOf(UUID.randomUUID());
        registeredUserDTO.setDeviceNotificationToken(deviceNotificationToken);

        Profile userProfile = null;


        try {
            user = userService.registerUser(registeredUserDTO, registeredUserDTO.getPassword());

            WalletAccountServiceImpl.manageRegistrationSession(session, registeredUserDTO, authenticationManagerBuilder, tokenProvider);

            //CREATE PROFILE
            Profile profile = profileService.findByPhoneNumber(accountDto.getPhone());
            profile.setPhoneNumber(accountDto.getPhone());
            profile.setProfileID("1");
            profile.setTotalBonus(0.0);
//        String dateOfBirthStr = accountDto.getDateOfBirth();
            profile.setDeviceNotificationToken(deviceNotificationToken);

            String gender = accountDto.getGender();
            Gender gender1 = Gender.valueOf(gender.toUpperCase());
            profile.setGender(gender1);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr, formatter);

//        profile.setDateOfBirth(dateOfBirth);
            profile.setDateOfBirth(accountDto.getDateOfBirth());

            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setAddress(accountDto.getStreet() + " " + accountDto.getCountry());

            profile.setAddress(accountDto.getStreet() + " " + accountDto.getCountry());

            String encryptedPin = passwordEncoder.encode(accountDto.getTransactionPin());
            profile.setPin(encryptedPin);
            profile.setProfileID("2");

            Profile savedProfile = profileService.save(profile);

            savedProfile.setUser(user);

            addressDTO.setAddressOwner(savedProfile);
            addressService.save(addressDTO);

            userProfile = savedProfile;

        } catch (UsernameAlreadyUsedException e) {

            user = userRepository.findByLogin(registeredUserDTO.getPhoneNumber());

            Profile profile = profileService.findByPhoneNumber(registeredUserDTO.getPhoneNumber());
            if (profile != null) {
                userProfile = profile;
            }
        }

        //CREATE WALLET
//        long accountNumber = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);
        if (userProfile == null) {
            defaultApiResponse.setMessage("Profile Not found for existing user");
            defaultApiResponse.setStatus("Failed");
            return new ResponseEntity<>(defaultApiResponse, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

        Long accountNumber = Long.valueOf(accountDto.getAccountNumber());
        boolean b = utility.searchAccountNumber(String.valueOf(accountNumber));
        if (b) {
            accountNumber = utility.getUniqueAccountNumber();
        }

        WalletAccountDTO walletAccountDTO = new WalletAccountDTO();
        walletAccountDTO.setAccountNumber(String.valueOf(accountNumber));
        walletAccountDTO.setAccountOwnerPhoneNumber(accountDto.getPhone());
        walletAccountDTO.setAccountOwnerId(userProfile.getId());
        walletAccountDTO.setAccountName(accountDto.getAccountName());
        walletAccountDTO.setDateOpened(LocalDate.now());
        walletAccountDTO.setCurrentBalance(0.00);
        walletAccountDTO.setActualBalance(walletAccountDTO.getCurrentBalance());
        Scheme schemeFromSession = utility.getSchemeFromSession(session);
        walletAccountDTO.setSchemeId(schemeFromSession.getId());
        walletAccountDTO.setWalletAccountTypeId(1L);
        walletAccountDTO.setStatus(AccountStatus.ACTIVE);

        try {
            List<WalletAccount> accountOptional = walletAccountService.findByAccountOwnerPhoneNumberAndAccountNameAndScheme_SchemeID(accountDto.getPhone(), walletAccountDTO.getAccountName(), schemeFromSession.getSchemeID());
            if (!accountOptional.isEmpty()) {
                WalletAccount walletAccount = accountOptional.get(0);
                log.info("Wallet found " + walletAccount);
                walletAccountMapper.toDto(walletAccount);
                defaultApiResponse.setStatus("failed");
                defaultApiResponse.setMessage("Wallet account with the same name(" + walletAccountDTO.getAccountName() + ") exists!");
                return new ResponseEntity<>(defaultApiResponse, HttpStatus.BAD_REQUEST);

            } else {
                walletAccountService.save(walletAccountDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            defaultApiResponse.setStatus("failed");
            defaultApiResponse.setMessage("Wallet account with the same name(" + walletAccountDTO.getAccountName() + ") exists!");
            return new ResponseEntity<>(defaultApiResponse, HttpStatus.BAD_REQUEST);
        }

        defaultApiResponse.setStatus("success");
        defaultApiResponse.setMessage("Wallet created successfully");
        List<WalletAccount> save = walletAccountService.findByAccountOwnerPhoneNumber(accountDto.getPhone());

        OpenAccountResponseDto account = new OpenAccountResponseDto();
        WalletAccount walletAccount = save.get(0);
        account.setAccountNumber(walletAccount.getAccountNumber());
        account.setCustomerId(accountDto.getCustomerId());
        account.setRequestId(accountDto.getRequestId());
        account.setProcessCode("00");
        account.setProcessMessage("Wallet created successfully");
        account.setAccountOpeningDate(String.valueOf(walletAccount.getDateOpened()));

        defaultApiResponse.setData(account);

        return new ResponseEntity<>(defaultApiResponse, HttpStatus.OK);

    }

    /*
     * <<<<<<< HEAD
     *
     * @PostMapping("/transfer/recieve/frombank") public Object
     * bankToWalletTransfer(@RequestBody BankToWalletDTO bankToWalletDTO) { return
     * externalRESTClient2.bankToWalletTransfer(bankToWalletDTO); =======
     * walletAccountService.save(walletAccountDTO);
     *
     * genericResponseDTO.setCode("success");
     * genericResponseDTO.setMessage("Wallet created successfully");
     * List<WalletAccount> save =
     * walletAccountService.findByAccountOwnerPhoneNumber(walletExternalDTO.
     * getPhoneNumber()); genericResponseDTO.setData(save);
     *
     * return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
     *
     * }
     */

    @PostMapping("/transfer/recieve/frombank")
    public Object bankToWalletTransfer(@RequestBody BankToWalletDTO bankToWalletDTO) {
        return externalRESTClient2.bankToWalletTransfer(bankToWalletDTO);
    }

    @PostMapping("/transfer/internal/bank")
    public Object transferWithinBank(@RequestBody TransferWithinBankDTO transferWithinBankDTO) {
        return externalRESTClient2.transferWithinBank(transferWithinBankDTO);
    }

    @PostMapping("/transfer/other/bank")
    public Object transferToAnotherBank(@RequestBody TransferToAnotherBankDTO transferToAnotherBankDTO) {
        return externalRESTClient2.transferToAnotherBank(transferToAnotherBankDTO);
    }

    @GetMapping("/verify/{transRef}")
    public InlineResponseDatum verifyTransaction(@PathVariable("transRef") String transRef) {
        /*
         * Map<String, String> headers = new HashedMap();
         *
         * String hash = new DigestUtils("SHA-512").digestAsHex(transRef + secretKey.txt);
         *
         * headers.put("publicKey.txt", inlinePmtStatusPublicKey); headers.put("TXN_HASH",
         * hash); Object o = externalRESTClient3.verifyTransaction(headers, transRef);
         */

        return walletAccountService.verifyTransRef(transRef);

    }

    @PostMapping("/wallet-account/status/{accountNumber}/{status}")
    public ResponseEntity<GenericResponseDTO> changeAccountStatus(@PathVariable String accountNumber, @PathVariable String status) {

        GenericResponseDTO genericResponseDTO = walletAccountService.changeAccountStatus(accountNumber, status);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());

    }

    @GetMapping("/wallet-account/status/{accountNumber}")
    public ResponseEntity<GenericResponseDTO> checkAccountStatus(@PathVariable String accountNumber) {

        GenericResponseDTO genericResponseDTO = walletAccountService.checkAccountStatus(accountNumber);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());

    }

    @GetMapping("/wallet-accounts/nuban")
    public ResponseEntity<GenericResponseDTO> getAccountsWithNuban() {

        List<WalletAccount> walletAccounts = walletAccountService.findAllByNubanAccountNoNotNull();
        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", walletAccounts), HttpStatus.OK);

    }

    @GetMapping("/wallet-accounts/nuban/balances")
    public ResponseEntity<GenericResponseDTO> getAccountsWithNubanWithBalances() {

        List<WalletAccountDTO> walletAccounts = walletAccountService.findAllByNubanAccountNoNotNullWithBalances();
        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", walletAccounts), HttpStatus.OK);

    }

    @RequestMapping(value = "/send-money-correspondent",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json")
    public ResponseEntity<GenericResponseDTO> bulkCorrespondentPayment(
        @RequestBody() BulkCorrespondentPaymentDTO[] paymentResponseDTOS) {

        List<BulkCorrespondentPaymentDTO> bulkCorrespondentPaymentDTOList = new ArrayList<>();
        GenericResponseDTO genericResponseDTO = new GenericResponseDTO();

        for (BulkCorrespondentPaymentDTO responseDTO : paymentResponseDTOS) {
            bulkCorrespondentPaymentDTOList.add(responseDTO);

            walletAccountService.sendMoneyWithCorrespondence(
                responseDTO.getSourceAccountNumber(),
                responseDTO.getAccountNumber(),
                responseDTO.getAmount(),
                responseDTO.getNarration());
        }
        genericResponseDTO.setCode("00");
        genericResponseDTO.setMessage("success");
        genericResponseDTO.setData(bulkCorrespondentPaymentDTOList);
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/send-money-bulk")
    public ResponseEntity<PaymentResponseDTO> sendMoneyBulkTransaction(@Valid @RequestBody FundDTO sendMoneyDTO, HttpSession session)
        throws URISyntaxException {

        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            this.theUser = user;
        });

        PaymentResponseDTO response = new PaymentResponseDTO();

        int pinFailureCount = getPinFailureCount(session);

        if (pinFailureCount >= 3) {
            return buildPinAttemptExceededResponse(response, session);
        }

        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());
        String currentEncryptedPin = profile.getPin();
        log.info("SendMoneyDTO pin ===> " + sendMoneyDTO.getPin());

        if (!passwordEncoder.matches(sendMoneyDTO.getPin(), currentEncryptedPin)) {

            return buildPinErrorResponse(session, pinFailureCount);
        }

        session.removeAttribute("pinFailureCount");

        out.println("The fundDTO in send money here === " + sendMoneyDTO);

        response = walletAccountService.sendMoneyBulkTransaction(sendMoneyDTO);

        if (response.getError()) {
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/send-money/bulk")
    public ResponseEntity<PaymentResponseDTO> sendBulkTransaction(@Valid @RequestBody FundDTO sendBulkMoneyDTO,
                                                                  HttpSession session)
        throws URISyntaxException {

        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            this.theUser = user;
        });

        PaymentResponseDTO response = new PaymentResponseDTO();

        int pinFailureCount = getPinFailureCount(session);

        if (pinFailureCount >= 3) {
            return buildPinAttemptExceededResponse(response, session);
        }

        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());
        String currentEncryptedPin = profile.getPin();
        log.info("SendMoneyDTO pin ===> " + sendBulkMoneyDTO.getPin());

        if (!passwordEncoder.matches(sendBulkMoneyDTO.getPin(), currentEncryptedPin)) {

            return buildPinErrorResponse(session, pinFailureCount);
        }

        session.removeAttribute("pinFailureCount");

        out.println("The fundDTO in send money here === " + sendBulkMoneyDTO);

        response = walletAccountService.sendBulkTransaction(sendBulkMoneyDTO);

        if (response.getError()) {
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/send-money-advert")
    public ResponseEntity<PaymentResponseDTO> sendMoneyAdvertPayment(@Valid @RequestBody FundDTO sendMoneyDTO, HttpSession session)
        throws URISyntaxException {
        log.debug("The fundDTO in Debit for Advert === " + sendMoneyDTO);
        sendMoneyDTO.setTransRef(utility.getUniqueTransRef());
        PaymentResponseDTO response = walletAccountService.sendMoney(sendMoneyDTO);

        if (response.getError()) {
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/reset-wallet-balance/{resetBalance}/{accountNumber}")
    public ResponseEntity<GenericResponseDTO> resetWalletBalance(@PathVariable double resetBalance, @PathVariable String accountNumber)
        throws URISyntaxException {
        log.debug("Reseting account balance === ");
        WalletAccount walletAccount = walletAccountService.resetBalances(resetBalance,accountNumber);
        log.info("Wallet to reset {}", walletAccount);
        GenericResponseDTO genericResponseDTO = new GenericResponseDTO();
        genericResponseDTO.setCode("00");
        genericResponseDTO.setMessage("success");
        genericResponseDTO.setData(walletAccount);
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/sync-wallet-balance/{accountNumber}")
    public ResponseEntity<GenericResponseDTO> syncWalletBalance(@PathVariable String accountNumber)
        throws URISyntaxException {
        log.debug("Syncing account balance === ");
        WalletAccount walletAccount = walletAccountService.syncBalances(accountNumber);
        log.info("Wallet to sync {}", walletAccount);
        GenericResponseDTO genericResponseDTO = new GenericResponseDTO();
        genericResponseDTO.setCode("00");
        genericResponseDTO.setMessage("success");
        genericResponseDTO.setData(walletAccount);
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
    }


    @GetMapping("/wallet-accounts/accounts/unequal-balances")
    public ResponseEntity<GenericResponseDTO> getAccountsWithUnequalBalances() {
        List<WalletAccountDTO> walletAccounts = walletAccountService.findAllCurrentBalanceNotEqualActualBalance();
        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", walletAccounts), HttpStatus.OK);
    }

    @GetMapping("/get-wallet-by-account/{accountNumber}")
    public ResponseEntity<GenericResponseDTO> getWalletbyAcct(@PathVariable String accountNumber) throws URISyntaxException {
        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
        WalletAccount walletAccount2 = walletAccountRepository.findOneByAccountNumber(accountNumber);
        out.println("Repository find::>>>  "+walletAccount2);
        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", walletAccount), HttpStatus.OK);
    }

    @GetMapping("/get-polaris-accounts")
    public ResponseEntity<GenericResponseDTO> getPolarisAccounts() throws URISyntaxException {
        log.info("GETTING POLARIS WALLETS");
        List<WalletAccountDTO> walletAccounts = walletAccountService.findAllByAccountOwnerIsNull()
            .stream()
            .filter(w -> w.getAccountName().toLowerCase().contains("polaris"))
            .collect(Collectors.toList());
        out.println("Polaris Accounts ====>>>  "+walletAccounts);
        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", walletAccounts), HttpStatus.OK);
    }

    @GetMapping("/get-account-details/{accountNumber}")
    public ResponseEntity<GenericResponseDTO> getWalletAccountDetails(@PathVariable String accountNumber) {
        log.info("GETTING WALLET ACCOUNT DETAILS");
        WalletAccount walletAccount = walletAccountService.findByAccountNumber(accountNumber);

        if (walletAccount == null){
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Wallet Account not found", null), HttpStatus.BAD_REQUEST);
        }

        WalletAccountDTO walletAccountDTO = walletAccountMapper.toDto(walletAccount);
        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", walletAccountDTO), HttpStatus.OK);
    }

    @PostMapping("/get-bvn/{bvn}")
    public ResponseEntity<GenericResponseDTO> getBvn(@PathVariable String bvn) {
        log.debug("BVN === " + bvn);

        RemitaBVNRequest remitaBVNRequest = new RemitaBVNRequest();
        remitaBVNRequest.setRequestReference("{{transRef}}");
        remitaBVNRequest.setBvn(bvn);
        RemitaBVNResponse remitaNINResponse = remitaCarmelUtils.bvnResponse(remitaBVNRequest);

        log.info("BVN Response === ", remitaNINResponse);
        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", remitaNINResponse), HttpStatus.OK);
    }

    @PostMapping("/get-nin/{nin}")
    public ResponseEntity<GenericResponseDTO> getNin(@PathVariable String nin) {
        log.debug("NIN === " + nin);

        RemitaNINRequest remitaNINRequest = new RemitaNINRequest();
        remitaNINRequest.setNumber(nin);
        RemitaNINResponse remitaNINResponse = remitaCarmelUtils.ninResponse(remitaNINRequest);

        log.info("NIN Response === ", remitaNINResponse);
        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", remitaNINResponse), HttpStatus.OK);
    }

    @DeleteMapping("/delete-wallet/{accountNumber}")
    public ResponseEntity<GenericResponseDTO> deleteWalletAccount(@PathVariable String accountNumber) {
        log.info("DELETING WALLET ACCOUNT DETAILS");

        walletAccountService.deleteByAccountNumber(accountNumber);

        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", null), HttpStatus.OK);
    }

}
