package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.justjava.corebanking.client.ExternalRESTClient2;
import ng.com.justjava.corebanking.client.ExternalRESTClient3;
import ng.com.justjava.corebanking.config.AsyncConfiguration;
import ng.com.justjava.corebanking.repository.AddressRepository;
import ng.com.justjava.corebanking.repository.CustomAuditEventRepository;
import ng.com.justjava.corebanking.repository.KycRequestRepository;
import ng.com.justjava.corebanking.repository.UserRepository;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.exception.GenericException;
import ng.com.justjava.corebanking.service.mapper.WalletAccountMapper;
import ng.com.justjava.corebanking.service.validation.LastPageValidation;
import ng.com.justjava.corebanking.util.RemitaCarmelUtils;
import ng.com.justjava.corebanking.util.Utility;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;
import ng.com.justjava.corebanking.domain.Address;
import ng.com.justjava.corebanking.domain.KycRequest;
import ng.com.justjava.corebanking.domain.Kyclevel;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.ProfileType;
import ng.com.justjava.corebanking.domain.Scheme;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.AccountStatus;
import ng.com.justjava.corebanking.domain.enumeration.DocumentType;
import ng.com.justjava.corebanking.domain.enumeration.Gender;
import ng.com.justjava.corebanking.domain.enumeration.KycRequestDocType;
import ng.com.justjava.corebanking.domain.enumeration.KycRequestStatus;
import ng.com.justjava.corebanking.service.AddressService;
import ng.com.justjava.corebanking.service.CashConnectService;
import ng.com.justjava.corebanking.service.KycRequestService;
import ng.com.justjava.corebanking.service.KyclevelService;
import ng.com.justjava.corebanking.service.MailService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.ProfileTypeService;
import ng.com.justjava.corebanking.service.SchemeService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.WalletAccountTypeService;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.out;

/**
 * REST controller for managing
 * {@link Profile}.
 */
@RestController
@RequestMapping("/api")
public class ProfileResource {

    private static final long WALLET_ID = 100000;
    private static final long Lower_Bond = 10000000000L;
    private static final long Upper_Bond = 90000000000L;
    private static final String ENTITY_NAME = "profile";
    private final Logger log = LoggerFactory.getLogger(ProfileResource.class);
    private final ProfileService profileService;
    private final KycRequestService kycRequestService;
    private final WalletAccountService walleAccountService;
    private final AsyncConfiguration asyncConfiguration;
    private final WalletAccountTypeService walletAccountTypeService;
    private final WalletAccountMapper walletAccountMapper;
    private final AddressService addressService;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final CustomAuditEventRepository customAuditEventRepository;
    private final KyclevelService kyclevelService;
    private final ProfileTypeService profileTypeService;
    private final PasswordEncoder passwordEncoder;
    private final ExternalRESTClient3 externalRESTClient3;
    private final ExternalRESTClient2 externalRESTClient2;
    private final Utility externalRestClientNewAccount;
    private final MailService mailService;
    private final CashConnectService cashConnectService;
    private final SchemeService schemeService;
    private final Utility utility;
    private final KycRequestRepository kycRequestRepository;
    private final Environment environment;
    private  final RemitaCarmelUtils remitaCarmelUtils;
    String kazeem = "akinrinde@justjava.com.ng";
    String tunji = "moronkola@systemspecs.com.ng";
    String bolaji = "ogeyingbo@systemspecs.com.ng";
    String demola = "igbalajobi@systemspecs.com.ng";
    String Ozioma = "enechukwu@systemspecs.com.ng";
    String Mike = "oshadami@systemspecs.com.ng";
    String Tokunbo = "omonubi@systemspecs.com.ng";
    String Ameze = "ogunfuwa@systemspecs.com.ng";
    String Maryam = "maliki@systemspecs.com.ng";
    String Seun = "adesanya@systemspecs.com.ng";
    String Adeibukun = "aadeniyi@systemspecs.com.ng";
    @Value("${app.image-url}")
    private String imageUrl;
    @Value("${app.document-url}")
    private String documentUrl;
    @Value("${app.email.subjects.success-reg}")
    private String successRegSubject;
    @Value("${app.email.contents.success-reg}")
    private String successRegContent;
    @Value("${app.constants.inline.api-public-key}")
    private String apiPublicKey;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    @Value("${app.scheme.ibile}")
    private String IBILE_SCHEME;
    @Value("${app.scheme.systemspecs}")
    private String SYSTEMSPECS_SCHEME;

    public ProfileResource(ProfileService profileService, KycRequestService kycRequestService,
                           WalletAccountService walleAccountService,
                           AsyncConfiguration asyncConfiguration, WalletAccountTypeService walletAccountTypeService,
                           WalletAccountMapper walletAccountMapper, UserRepository userRepository, AddressService addressService,
                           AddressRepository addressRepository, CustomAuditEventRepository customAuditEventRepository, KyclevelService kyclevelService,
                           ProfileTypeService profileTypeService, PasswordEncoder passwordEncoder,
                           ExternalRESTClient3 externalRESTClient3, ExternalRESTClient2 externalRESTClient2,
                           Utility externalRestClientNewAccount, MailService mailService, CashConnectService cashConnectService,
                           SchemeService schemeService, Utility utility, KycRequestRepository kycRequestRepository, Environment environment, RemitaCarmelUtils remitaCarmelUtils) {

        this.profileService = profileService;
        this.kycRequestService = kycRequestService;
        this.walleAccountService = walleAccountService;
        this.asyncConfiguration = asyncConfiguration;
        this.walletAccountTypeService = walletAccountTypeService;
        this.walletAccountMapper = walletAccountMapper;
        this.addressService = addressService;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.customAuditEventRepository = customAuditEventRepository;

        this.kyclevelService = kyclevelService;
        this.profileTypeService = profileTypeService;
        this.passwordEncoder = passwordEncoder;
        this.externalRESTClient3 = externalRESTClient3;
        this.externalRESTClient2 = externalRESTClient2;
        this.externalRestClientNewAccount = externalRestClientNewAccount;
        this.mailService = mailService;
        this.cashConnectService = cashConnectService;
        this.schemeService = schemeService;
        this.utility = utility;
        this.kycRequestRepository = kycRequestRepository;
        this.environment = environment;
        this.remitaCarmelUtils = remitaCarmelUtils;
    }

    /**
     * {@code POST  /profiles} : Create a new profile.
     *
     * @param profileDTO the profileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     * body the new profileDTO, or with status {@code 400 (Bad Request)} if
     * the profile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/profiles")
    public ResponseEntity<ProfileDTO> createProfile(@RequestBody ProfileDTO profileDTO) throws URISyntaxException {
        log.debug("REST request to save Profile : {}", profileDTO);
        if (profileDTO.getId() != null) {
            throw new BadRequestAlertException("A new profile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfileDTO result = profileService.save(profileDTO);
        return ResponseEntity
            .created(new URI("/api/profiles/" + result.getId())).headers(HeaderUtil
                .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /profiles/update} : Updates an existing profile.
     *
     * @param profileDTO the profileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated profileDTO, or with status {@code 400 (Bad Request)} if
     * the profileDTO is not valid, or with status
     * {@code 500 (Internal Server Error)} if the profileDTO couldn't be
     * updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/profiles/update")
    public ResponseEntity<ProfileDTO> updateProfile(@RequestBody ProfileDTO profileDTO) throws URISyntaxException {
        log.debug("REST request to update Profile : {}", profileDTO);
        if (profileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProfileDTO result = profileService.save(profileDTO);
        return ResponseEntity.ok().headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /profiles} : get all the profiles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     * of profiles in body.
     */
    @GetMapping("/profiles")
    public ResponseEntity<GenericResponseDTO> getAllProfiles(Pageable pageable) {
        log.debug("REST request to get a profiles of Profiles");
        Page<ProfileDTO> profiles = profileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), profiles);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", profiles.getSize());
        metaMap.put("totalNumberOfRecords", profiles.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", profiles.getContent(), metaMap), headers,
            HttpStatus.OK);

    }

    @GetMapping("/profiles/search")
    public ResponseEntity<GenericResponseDTO> searchAllProfilesByKeyword(Pageable pageable, @RequestParam String key) {
        log.debug("REST request to search all profiles with a Keyword");
        Page<ProfileDTO> profiles = profileService.findAllWithKeyword(pageable, key);
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), profiles);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", profiles.getSize());
        metaMap.put("totalNumberOfRecords", profiles.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", profiles.getContent(), metaMap), headers,
            HttpStatus.OK);

    }

    /**
     * {@code GET  /profiles/:id} : get the "id" profile.
     *
     * @param id the id of the profileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the profileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/profiles_/{id}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long id) {
        log.debug("REST request to get Profile : {}", id);
        Optional<ProfileDTO> profileDTO = profileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profileDTO);
    }

    /**
     * {@code GET  /profiles/:id} : get the "id" profile.
     *
     * @param phoneNumber the id of the profileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the profileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/profiles/{phoneNumber}")
    public ResponseEntity<ProfileDTO> getProfileByPhoneNumber(@PathVariable String phoneNumber) {
        log.debug("REST request to get Profile : {}", phoneNumber);
        Optional<ProfileDTO> profileDTO = profileService.findOneByPhoneNumber(phoneNumber);
        return ResponseUtil.wrapOrNotFound(profileDTO);
    }

    /**
     * {@code DELETE  /profiles/:id} : delete the "id" profile.
     *
     * @param id the id of the profileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/profiles/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        log.debug("REST request to delete Profile : {}", id);
        profileService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/lastpage")
    public ResponseEntity<RegisterCompleteResponseDTO<WalletAccountDTO>> updateAccount(
            @Valid @RequestBody RegistrationLastPageDTO lastPageDTO, HttpSession session) {

        RegisterCompleteResponseDTO<WalletAccountDTO> ResponseDTO = new RegisterCompleteResponseDTO<>();

        LastPageValidation validate = new LastPageValidation(lastPageDTO);

        if (!validate.checkErrors()) {
            ResponseDTO.setMessage(validate.getErrors());
            return new ResponseEntity<>(ResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        log.debug(lastPageDTO.toString());
        String bvn = lastPageDTO.getBvn();

        ResponseEntity<GenericResponseDTO> responseEntity = null;

        User currentUser = utility.getCurrentUser();

        log.debug(currentUser.getLogin());
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        log.debug("Phone number " + phoneNumber);
        if (phoneNumber == null) {
            phoneNumber = currentUser.getLogin();
        }
        Profile profile = profileService.findByPhoneNumber(phoneNumber);

        String firstName;
        String lastName;
        String dataPhoneNumber;
        String dateOfBirth;

        // verifyBvn
        if (utility.checkStringIsValid(bvn)) {
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {

//                LocalDate currentUserDateOfBirth = profile.getDateOfBirth();
//
//                firstName = currentUser.getFirstName();
//                lastName = currentUser.getLastName();
//                dataPhoneNumber = utility.formatPhoneNumber(currentUser.getLogin());
//                dateOfBirth = currentUserDateOfBirth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


            }else {

//                try {
//                    responseEntity = cashConnectService.getBvn(bvn);
//                } catch (Exception e) {
//                    ResponseDTO.setMessage("BVN validation failed error");
//                    ResponseDTO.setWallet(null);
//                    return new ResponseEntity<>(ResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
//                }

                RemitaBVNRequest remitaBVNRequest = new RemitaBVNRequest();
                remitaBVNRequest.setRequestReference("{{transRef}}");
                remitaBVNRequest.setBvn(bvn);

                RemitaBVNResponse remitaNINResponse = remitaCarmelUtils.bvnResponse(remitaBVNRequest);

//                if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                if (remitaNINResponse == null) {
                    ResponseDTO.setMessage("BVN validation failed!");
                    ResponseDTO.setWallet(null);
                    return new ResponseEntity<>(ResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                } else {
                    try {

                        RemitaBVNResponseData data = (RemitaBVNResponseData) remitaNINResponse.getData();

//                        GenericResponseDTO body = responseEntity.getBody();

//                        GetBvnResponseData data = new ObjectMapper().readValue((String) body.getData(),
//                            GetBvnResponseData.class);

                        firstName = data.getFirstName();
                        lastName = data.getLastName();
                        phoneNumber = data.getPhoneNumber1();
                        dateOfBirth = data.getDateOfBirth();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                        LocalDate parse = LocalDate.parse(dateOfBirth, formatter);

                        int count = 0;

                        String msg = "Validation failed.";

                        if (profile != null) {
                            LocalDate currentUserDateOfBirth = profile.getDateOfBirth();
                            if (currentUserDateOfBirth.compareTo(parse) == 0) {
                                count++;
                            } else {
                                msg = msg + "Date is not equal";
                            }
                        }
                        if (firstName.equalsIgnoreCase(currentUser.getFirstName())) {
                            count++;
                        } else {
                            msg = msg + ". Firstname is not equal";
                        }
                        if (lastName.equalsIgnoreCase(currentUser.getLastName())) {
                            count++;
                        } else {
                            msg = msg + ". Lastname is not equal";
                        }
                        if (utility.formatPhoneNumber(currentUser.getLogin())
                            .equalsIgnoreCase(utility.formatPhoneNumber(data.getPhoneNumber1()))) {
                            count++;
                        } else {
                            msg = msg + ". PhoneNumber is not equal";

                        }
                        if (count < 3) {
                            ResponseDTO.setMessage(msg);
                            ResponseDTO.setWallet(null);
                            return new ResponseEntity<>(ResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }

        Optional<User> userOptional = userRepository.findOneByLogin(phoneNumber);
        // user.setEmail();
        String email = lastPageDTO.getEmail();
        userOptional.ifPresent(theUser -> {
            // System.out.println(theUser.getFirstName());
            theUser.setEmail(email);
            userRepository.save(theUser);
        });

        // User user = UserRepository
        // profile.setAddress(profileDTO.getAddress());
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddress(lastPageDTO.getAddress());
        addressDTO.setState(lastPageDTO.getState());
        addressDTO.setLocalGovt(lastPageDTO.getLocalGovt());
        addressDTO.setLatitude(lastPageDTO.getLatitude());
        addressDTO.setLongitude(lastPageDTO.getLongitude());
        addressDTO.setAddressOwner(profile);
        addressService.save(addressDTO);

        String encodedString = lastPageDTO.getPhoto();
        String photoName = "photo" + System.currentTimeMillis() + ".jpg";
        String outputFileName = imageUrl + photoName;

        try {
            log.debug("ABOUT TO DECODE");
            byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
            log.debug("AFTER DECODE");

            decodedBytes = utility.resizeImage(decodedBytes);

            File file = new File(outputFileName);
            log.debug("Path name = " + outputFileName);
            log.debug("File absolute path = " + file.getAbsolutePath());
            FileUtils.writeByteArrayToFile(file, decodedBytes);

            userOptional.ifPresent(theUser -> {
                // System.out.println(theUser.getFirstName());
                theUser.setImageUrl(photoName);
                userRepository.save(theUser);

                log.debug(theUser.toString());
            });

        } catch (IOException e) {
            e.printStackTrace();
            log.debug(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug(String.valueOf(lastPageDTO.getDateOfBirth()));

        profile.setDateOfBirth(lastPageDTO.getDateOfBirth());
        log.debug("KYC Level for " + profile.getFullName());//y + " ::: " + profile.getKyc().getKycLevel());
//        profile.setKyc(kyclevelService.findByKycLevel(1));

        if (utility.checkStringIsValid(bvn)) {
            profile.setBvn(bvn);
        }

        ProfileType profileType = profileTypeService.findByProfiletype("Customer");
        profile.setProfileType(profileType);
        profile.setProfileID("4");
        profile.setGender(Gender.valueOf(lastPageDTO.getGender()));
        profile.setSecretQuestion(lastPageDTO.getSecretQuestion());
        profile.setSecretAnswer(lastPageDTO.getSecretAnswer());
        profile.setTotalBonus(0.0);

//        Optional<WalletAccountTypeDTO> walletAccountTypeOptional = walletAccountTypeService.findOne(1L);

        profile = profileService.save(profile);
        User user = profile.getUser();
        List<Address> addresses = addressRepository.findAllByAddressOwner(profile);
        Scheme bySchemeID = null;
        if(lastPageDTO.getScheme() != null){
            bySchemeID = schemeService.findBySchemeID(lastPageDTO.getScheme());
        }

        long accountNumber = utility.getUniqueAccountNumber();

        WalletAccountDTO walletAccountDTO = new WalletAccountDTO();
        walletAccountDTO.setAccountNumber(String.valueOf(accountNumber));
        walletAccountDTO.setAccountOwnerPhoneNumber(profile.getPhoneNumber());
        walletAccountDTO.setAccountOwnerId(profile.getId());

        walletAccountDTO.setAccountName(user.getFirstName());
        walletAccountDTO.setDateOpened(LocalDate.now());
        walletAccountDTO.setCurrentBalance(0.00);
        walletAccountDTO.setActualBalance(0.00);
        if(bySchemeID != null){
            walletAccountDTO.setSchemeId(bySchemeID.getId());
        }else {
            walletAccountDTO.setSchemeId(1L);
        }
        walletAccountDTO.setStatus(AccountStatus.ACTIVE);
        walletAccountDTO.setWalletAccountTypeId(1L);

        Optional<SchemeDTO> one = schemeService.findOne(walletAccountDTO.getSchemeId());

        WalletAccountDTO wallet = null;

        try {
            List<WalletAccount> accountOptional = walleAccountService
                .findByAccountOwnerPhoneNumberAndAccountNameAndScheme_SchemeID(phoneNumber,
                    user.getFirstName(), one.get().getSchemeID());
            if (!accountOptional.isEmpty()) {
                WalletAccount walletAccount = accountOptional.get(0);
                log.info("Wallet found " + walletAccount);
                wallet = walletAccountMapper.toDto(walletAccount);

            } else {
                wallet = walleAccountService.save(walletAccountDTO);

                Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
                if (asyncExecutor != null) {
                    WalletAccountDTO finalWallet = wallet;
                    Profile finalProfile = profile;

                    asyncExecutor.execute(() -> {
                        try {
                            out.println("Creating NUBAN account for new User ===> "
                                + finalProfile.getPhoneNumber());

                            out.println("Inside polaris ===>");

                            Long schemeId = finalWallet.getSchemeId();
                            Optional<Scheme> schemeOptional = schemeService.findSchemeId(schemeId);
                            if (schemeOptional.isPresent()) {
                                out.println("Inside scheme optional ==> ");
                                utility.createPolarisAccount(finalProfile.getPhoneNumber(), schemeOptional.get().getSchemeID());
                            }
                            out.println("Outside of scheme optional ==> ");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResponseDTO.setMessage(
                "You already have a wallet with the same name(" + user.getFirstName() + ")");
            ResponseDTO.setWallet(null);
            return new ResponseEntity<>(ResponseDTO, new HttpHeaders(), HttpStatus.OK);

        }

        ResponseDTO.setMessage("Registration successful");
        ResponseDTO.setWallet(wallet);

        try {
            if (email != null) {
                sendOnboardingEmail(currentUser, email, wallet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(ResponseDTO, new HttpHeaders(), HttpStatus.OK);
    }

    private void sendOnboardingEmail(User currentUser, String email, WalletAccountDTO wallet) {

        /*
         * String content = "Dear <b>" + currentUser.getFirstName() + "</b>, <p><p> " +
         * successRegContent + wallet.getAccountNumber() + ")<p>Thank you.";
         * utility.sendEmail(email, successRegSubject, content );
         */

        String content = "Dear <b>" + currentUser.getFirstName() + "</b>, <p><p> "
            + "Welcome to Pouchii. You have indeed chosen a <b>Simplified</b> way for all your financial transactions."
            + "<p><p> " + "Your wallet ID is: <b>" + wallet.getAccountNumber() + "</p>" + "<p><p>"
            + "Here are a few things to get you started: " + "<ul>"
            + "<li> Fund your wallet through Debit Cards, USSD, Bank Account, at Agent location etc. </li>"
            + "<li> You can upgrade your profile to increase your transaction limits </li>"
            + "<li> Perform various financial transactions such as Send Money; Request Money; Take a Loan; Buy Insurance; Take a Micro Pension; Pay Federal, State and Local Governments; Pay Bills; Buy Airtime/Data and much more...</li>"
            + "</ul>" + "Thank you for choosing us." + "<p><p>"
            + "If you didn't register for Pouchii, please immediately contact our Customer Service on <tel>+234 (0)16348010</tel>  or send an email to pouchii@systemspecs.com.ng immediately."
            + "<u><p></p></u>"
            + "<i>Please keep your login details secure always and do not disclose by phone, email or on suspicious websites. Neither SystemSpecs Holdings nor its staff will request your login details at any point in time.</i>"
            + " <p><p>"
            + "<i>Why are we sending this? We take security very seriously and we want to keep you aware of all important actions on your wallet.</i>";

        utility.sendEmail(email, successRegSubject, content);
    }

    /*
     * @PostMapping("/updatewithphonenumber")
     *
     * @ResponseStatus(HttpStatus.ACCEPTED) public void
     * updateProfileWithPhoneNumber(@Valid @RequestBody ProfileDTO profileDTO) {
     * Profile profile =
     * profileService.findByPhoneNumber(profileDTO.getPhoneNumber());
     * profile.setAddress(profileDTO.getAddress());
     * profile.setDateOfBirth(profileDTO.getDateOfBirth()); profile.setKyc(null);
     * profile.setGender(Gender.valueOf(profileDTO.getGender()));
     * profile=profileService.save(profile);
     *
     * }
     */
    @PostMapping("/pin")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<PostResponseDTO> createUpdatePin(@Valid @RequestBody PinDTO pinDTO, HttpSession session) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        out.println(" phoneNumber ===" + phoneNumber);
        String profileID = null;
        Optional<ProfileDTO> currentUserOptional = profileService.findByUserIsCurrentUser();

        if (currentUserOptional.isPresent()) {
            ProfileDTO profileDTO = currentUserOptional.get();
            phoneNumber = profileDTO.getPhoneNumber();
            profileID = profileDTO.getProfileID();
        }

        if (profileID == null) {
            profileID = (String) session.getAttribute("PROFILE_ID");
        }

        phoneNumber = utility.formatPhoneNumber(phoneNumber.trim());
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        PostResponseDataDTO postResponseDataDTO = new PostResponseDataDTO();
        postResponseDTO.setMessage("pin successfully created");

        if (profileID == null) {
            postResponseDTO.setMessage("Profile Id not set");
            postResponseDataDTO.setCode("10");
            postResponseDataDTO.setDescription("Invalid registration step");
            postResponseDTO.setPostResponseDataDTO(postResponseDataDTO);
            return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

        if ("1".equalsIgnoreCase(profileID)) {
            postResponseDTO.setMessage("You have not yet validated your phone number");
            postResponseDataDTO.setCode("10");
            postResponseDataDTO.setDescription("Invalid registration step");
            postResponseDTO.setPostResponseDataDTO(postResponseDataDTO);
            return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

        if (Strings.isEmpty(phoneNumber)) {
            postResponseDTO.setMessage("unable to validate user");
            postResponseDataDTO.setCode("10");
            postResponseDataDTO.setDescription("section authentication failed");
            postResponseDTO.setPostResponseDataDTO(postResponseDataDTO);
            return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
        if (Strings.isEmpty(pinDTO.getPin())) {
            postResponseDTO.setMessage("unable to validate user");
            postResponseDataDTO.setCode("10");
            postResponseDataDTO.setDescription("section authentication failed");
            postResponseDTO.setPostResponseDataDTO(postResponseDataDTO);
            return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

        Profile profile = profileService.findByPhoneNumber(phoneNumber);
        String encryptedPassword = passwordEncoder.encode(pinDTO.getPin());
        profile.setPin(encryptedPassword);
        profile.setProfileID("3");
        profile = profileService.save(profile);

        postResponseDTO.setMessage("pin successfully created");
        postResponseDataDTO.setCode("00");
        postResponseDataDTO.setDescription("Pin Successfully Created!");
        postResponseDTO.setPostResponseDataDTO(postResponseDataDTO);

        return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/deviceToken")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<PostResponseDTO> updateDeviceToken(@Valid @RequestBody DeviceTokenDTO deviceTokenDTO,
                                                             HttpSession session) {

        return profileService.updateDeviceToken(deviceTokenDTO, session);
    }

    @PostMapping("/verify-otp")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<PostResponseDTO>
    verifyOTP(@Valid @RequestBody OTPDTO otpDTO, HttpSession session) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        out.println(" phoneNumber === " + phoneNumber);

        if (utility.checkStringIsNotValid(phoneNumber)) {
            throw new GenericException("Phone number can not be empty");
        }

        PostResponseDTO postResponseDTO = new PostResponseDTO();
        PostResponseDataDTO postResponseDataDTO = new PostResponseDataDTO();
        postResponseDTO.setMessage("otp successfully validated");
        postResponseDataDTO.setCode("00");
        postResponseDataDTO.setDescription("OTP successfully validated!");

        String sentOTP = otpDTO.getOtp();
        if (utility.checkStringIsNotValid(sentOTP)) {
            postResponseDTO.setMessage("you must enter otp");
            postResponseDataDTO.setCode("08");
            postResponseDataDTO.setDescription(sentOTP);
            return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

        User byLogin = userRepository.findByLogin(phoneNumber);

        if (byLogin == null) {
            postResponseDTO.setMessage("Invalid Login user");
            postResponseDataDTO.setCode("99");
            postResponseDataDTO.setDescription(sentOTP);
            return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

        String otp = byLogin.getKey();
        if (utility.checkStringIsNotValid(otp)) {
            postResponseDTO.setMessage("you must enter otp");
            postResponseDataDTO.setCode("08");
            postResponseDataDTO.setDescription(otp);
            return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
//        byLogin.setKey("");
//        userRepository.save(byLogin);

        Profile profile = profileService.findByPhoneNumber(byLogin.getLogin());
        if (profile == null) {
            postResponseDTO.setMessage("Invalid Login user profile");
            postResponseDataDTO.setCode("99");
            postResponseDataDTO.setDescription(sentOTP);
            return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

        if (!sentOTP.trim().equalsIgnoreCase(otp.trim())) {
            postResponseDTO.setMessage("invalid otp");
            postResponseDataDTO.setCode("08");
            postResponseDataDTO.setDescription(sentOTP);
            return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

        postResponseDTO.setPostResponseDataDTO(postResponseDataDTO);

        boolean status = otp.equalsIgnoreCase(sentOTP);
        if (status) {
            if (utility.checkStringIsValid(profile.getProfileID()) && Integer.parseInt(profile.getProfileID()) < 2) {
                profile.setProfileID("2");
                profile = profileService.save(profile);
                out.println("Updated profile ===> " + profile);
            }
            return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.OK);
        }

        out.println(" session otp ===" + otp);

        postResponseDTO.setMessage("otp validation failed");
        postResponseDataDTO.setCode("99");
        postResponseDataDTO.setDescription("OTP validation failed!");
        return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/regStage")
    public String getMyRegStage() {
        log.info("Rest request to getReg stage ===> ");
        Optional<ProfileDTO> profileDTOOptional = profileService.findByUserIsCurrentUser();
        out.println("Retrieved login profile optional ===> " + profileDTOOptional);
        if (profileDTOOptional.isPresent()) {
            ProfileDTO profileDTO = profileDTOOptional.get();
            out.println("regState profile ===> " + profileDTO);
            return profileDTO.getProfileID();
        } else {
            return "Invalid user details ++===+++>";
        }
    }

    @PostMapping("/become_an_agent")
    public ResponseEntity<ResponseDTO> becomeAnAgent(@Valid @RequestBody BecomeAnAgentDTO becomeAnAgentDTO,
                                                     HttpSession session) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (Strings.isEmpty(becomeAnAgentDTO.getBvn())) {
            return new ResponseEntity<>(responseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        Profile profile = profileService.findByPhoneNumber(phoneNumber);
        ProfileType profileType = profileTypeService.findByProfiletype("Agent");
        profile.setProfileType(profileType);
        profile.setProfileID("5");
        profile = profileService.save(profile);
        WalletAccountDTO walletAccountDTO = new WalletAccountDTO();
        walletAccountDTO.setAccountNumber(String.valueOf(ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond)));
        walletAccountDTO.setAccountOwnerPhoneNumber(profile.getPhoneNumber());
        walletAccountDTO.setAccountOwnerId(profile.getId());
        walletAccountDTO.setAccountName(profile.getUser().getFirstName());
        walletAccountDTO.setDateOpened(LocalDate.now());
        walletAccountDTO.setCurrentBalance(0.0);
        walletAccountDTO.setActualBalance(0.0);
        walletAccountDTO.setStatus(AccountStatus.ACTIVE);
        walletAccountDTO.setSchemeId(1L);
        walletAccountDTO.setWalletAccountTypeId(2L);
        WalletAccountDTO wallet = walleAccountService.save(walletAccountDTO);
        responseDTO.setMessage("Migration to Agent successfull");
        responseDTO.setTrasactionReference(wallet.getAccountOwnerPhoneNumber());
        return new ResponseEntity<>(responseDTO, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value = "/referencedataninandfingerprint")
    public Object getFingerPrintData(@RequestBody NinFingerPrintDTO ninFingerPrintDTO) {
        String base64StringData = "";
        String refId = "nimcDetailsByNin";
        String authCode = "67777777";
        String secretKey = "610a64055d214207ee638c1dd7c610b1751dcc0510563fdc52c0a4f9f8e36e275c686c6cbae1ea4e636131a265f20f07e8d610a4733f4df974c0f915465048a1";
        String signature = "cac9b29a138c039b8e761293c258a999740bc144638b8582c3c4d9cf11ec96792075097eaa44c7f435b9eae831a6d5175f47ecea27fa5fcaed591d12d44410b8";
        Map<String, String> headers = new java.util.HashMap<>();
        headers.put("X-API-PUBLIC-KEY",
            "QzAwMDAxMTU0MDF8MTUwOTM3NzUwMjMzNXw2MGFmMDZjYTk4ZWYwNzgyMjIzMDQ5MTY4MmZhMWYwODFlMTAwODg3NDczMzRkYjFjNWQ5MGMzZmM5ZDQwNDEyMmQ1ZThhZjAwM2YyMmU5ZDA1ZjZkM2QyNTg3OWYyZDFhMDRlYjE4NDM3MjVhODYwOGYxMjdhYmJmNzRkYmQwMA");

        headers.put("X-API-SIGNATURE", signature);
        return externalRESTClient3.getFingerPrintData(headers, ninFingerPrintDTO);

    }

    // TODO upgrade KYC upgrade-kyc4
    @PostMapping(path = "/upgrade-kyc")
    public ResponseEntity<GenericResponseDTO> upgradeKYCLevel(@RequestBody UpgradeKYCLevelDTO upgradeKYCLevelDTO, @RequestParam Optional<Boolean> flag) {
        GenericResponseDTO genericResponseDTO = new GenericResponseDTO();

        if (flag.isPresent() && flag.get()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), headers,
                HttpStatus.OK);
        }
        log.debug("UpgradeKYCLevelDTO ====>" + upgradeKYCLevelDTO);
        ResponseEntity<GenericResponseDTO> responseEntity = null;
        String bvn = upgradeKYCLevelDTO.getBvn();

        User currentUser = utility.getCurrentUser();

        if (currentUser != null) {

            Profile profile = profileService.findByPhoneNumber(currentUser.getLogin());
            // if awaiting approval return: "You have an already pending request."
            if (checkKycRequestStatus(profile.getId())) {
                genericResponseDTO.setCode("99");
                genericResponseDTO.setMessage("You have an already pending request.");
                return new ResponseEntity<>(genericResponseDTO, HttpStatus.TOO_MANY_REQUESTS);
            }

            log.debug("KYC Upgrade ==> BVN Validation Begins");

            String firstName = currentUser.getFirstName();
            String lastName = currentUser.getLastName();
            String phoneNumber = utility.formatPhoneNumber(currentUser.getLogin());
            LocalDate currentUserDateOfBirth = profile.getDateOfBirth();
            LocalDate parse = currentUserDateOfBirth;

            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                log.debug("KYC Upgrade ==> BVN Validation Demo");
        }else {

//                try {
//                    responseEntity = cashConnectService.getBvn(bvn);
//                } catch (Exception e) {
//                    return new ResponseEntity<>(new GenericResponseDTO("99", e.getLocalizedMessage(), null),
//                        HttpStatus.BAD_REQUEST);
//                }
//                if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {

//                    GenericResponseDTO body = responseEntity.getBody();

                RemitaBVNRequest remitaBVNRequest = new RemitaBVNRequest();
                remitaBVNRequest.setRequestReference("{{transRef}}");
                remitaBVNRequest.setBvn(bvn);

                RemitaBVNResponse response = remitaCarmelUtils.bvnResponse(remitaBVNRequest);

//                    if (body != null && body.getCode().equalsIgnoreCase("00")) {
                    if (response != null) {
                        log.debug("KYC Upgrade ==> BVN Validated");

                        try {

//                            NewBvnResponseData data = new ObjectMapper().readValue((String) body.getData(),
//                                NewBvnResponseData.class);
                            RemitaBVNResponseData data = (RemitaBVNResponseData) response.getData();

                            firstName = data.getFirstName();
                            lastName = data.getLastName();
                            phoneNumber = data.getPhoneNumber1();
                            String dateOfBirth = data.getDateOfBirth();

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                            parse = LocalDate.parse(dateOfBirth, formatter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        genericResponseDTO.setMessage("Bvn verification failed");
                        genericResponseDTO.setCode("Failed");
                        genericResponseDTO.setData(bvn);
                        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
                    }
//                }
            }


                    out.println("Comparing ==== \n " +
                        "Firstname: " + firstName + " === " + currentUser.getFirstName() +
                        "\nLastname: " + lastName + " === " + currentUser.getLastName() +
                        "\nphoneNumber: " + phoneNumber + " === " + currentUser.getLogin() +
                        "\ndateOfBirth: " + parse + " === " + profile.getDateOfBirth()
                    );

                    int count = 0;

                    String msg = "Validation failed for ";

                    if (profile != null) {
                        currentUserDateOfBirth = profile.getDateOfBirth();
                        if (currentUserDateOfBirth.compareTo(parse) == 0) {
                            count++;
                        } else {
                            msg = msg + "Date of birth";
                        }
                    }
                    if (firstName.equalsIgnoreCase(currentUser.getFirstName())) {
                        count++;
                    } else {
                        msg = msg + ", firstname ";
                    }
                    if (lastName.equalsIgnoreCase(currentUser.getLastName())) {
                        count++;
                    } else {
                        msg = msg + ", lastname";
                    }
                    if (utility.formatPhoneNumber(phoneNumber)
                        .equalsIgnoreCase(utility.formatPhoneNumber(currentUser.getLogin()))) {
                        count++;
                    } else {
                        msg = msg + ", phoneNumber";

                    }
                    if (count < 3) {
                        return new ResponseEntity<>(new GenericResponseDTO("99", msg, null),
                            HttpStatus.BAD_REQUEST);
                    }

//                }

//            }



                    String encodedString = upgradeKYCLevelDTO.getDocFile();
                    String documentName;

                    if (upgradeKYCLevelDTO.getDocFormat().equalsIgnoreCase(DocumentType.JPG.name())) {
                        documentName = "document-kyc2-" + utility.returnPhoneNumberFormat(currentUser.getLogin())
                            + ".jpg";
                    } else if (upgradeKYCLevelDTO.getDocFormat().equalsIgnoreCase(DocumentType.PDF.name())) {
                        documentName = "document-kyc2-" + utility.returnPhoneNumberFormat(currentUser.getLogin())
                            + ".pdf";
                    } else {
                        return new ResponseEntity<>(
                            new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Document type", null),
                            HttpStatus.BAD_REQUEST);
                    }
                    String outputFileName = documentUrl + documentName;

                    boolean saved = false;
                    KycRequestDocType kycRequestDocType = KycRequestDocType.NIN;
                    if (utility.checkStringIsNotValid(encodedString)
                        || "NIN".equalsIgnoreCase(upgradeKYCLevelDTO.getDocType())) {
                        log.debug("Document Type is NIN");
                        saved = true;
                    } else {
                        if ("Driver's License".equalsIgnoreCase(upgradeKYCLevelDTO.getDocType())) {
                            kycRequestDocType = KycRequestDocType.DRIVERS_LICENSE;
                        } else if ("Voters Card".equalsIgnoreCase(upgradeKYCLevelDTO.getDocType())) {
                            kycRequestDocType = KycRequestDocType.VOTERS_CARD;
                        }
                        saved = utility.saveImage(encodedString, outputFileName);
                    }

                    if (saved) {

                        if (profile != null) {
                            Kyclevel kyc = profile.getKyc();
                            Integer currentKycLevel = kyc.getKycLevel();

                            if (currentKycLevel == 3) {
                                genericResponseDTO.setMessage("Maximum KYC level reached!");
                                genericResponseDTO.setCode("Failed");
                                genericResponseDTO.setData(profile);

                                sendUnsuccessfulKYCEmailUpgrade(profile, currentKycLevel, currentKycLevel + 1);

                                return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
                            } else if (currentKycLevel == 2) {
                                genericResponseDTO.setMessage("You are already in KYC level 2");
                                genericResponseDTO.setCode("Failed");
                                genericResponseDTO.setData(profile);

                                sendUnsuccessfulKYCEmailUpgrade(profile, currentKycLevel, currentKycLevel + 1);

                                return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
                            } else {
                                Kyclevel kyclevel = kyclevelService.findByKycLevel(currentKycLevel + 1);
                                // profile.setKyc(kyclevel);
                                profile.setBvn(bvn);
                                profile.setValidID(upgradeKYCLevelDTO.getVerificationId());
                                profile.setValidDocType(kycRequestDocType);
                                Profile save = profileService.save(profile);

                                KycRequest kycRequest = new KycRequest();
                                kycRequest.setCurrentLevel(currentKycLevel);
                                kycRequest.setNextLevel(currentKycLevel + 1);
                                kycRequest.setProfile(profile);
                                kycRequest.setSenderProfile(profile);
                                kycRequest.setStatus(KycRequestStatus.AWAITING_APPROVAL);
                                kycRequest.setRequestDocType(kycRequestDocType);
                                kycRequest.setDocumentId(upgradeKYCLevelDTO.getVerificationId());

                                KycRequest savedKycRequest = kycRequestService.save(kycRequest);

                                log.info("Saved KYC ====> " + savedKycRequest);

                                genericResponseDTO.setMessage("KYC level upgraded to " + kyclevel.getKycLevel());
                                genericResponseDTO.setCode("Success");
                                genericResponseDTO.setData(save.getKyc());

//                                String fileLink = "https://wallet.remita.net/api/documents/kyc/"
//                                    + utility.returnPhoneNumberFormat(save.getPhoneNumber());

                                String fileLink = "https://wallet.remita.net/kyc-request";

                                sendEmailToCustomerSupport(save, currentKycLevel, fileLink);

                                auditKycUpgrade(save, currentKycLevel, outputFileName);

                                sendSuccessKYCEmailUpgrade(save, currentKycLevel, currentKycLevel + 1);

                                return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
                            }
                        }
//                        else {
//                            genericResponseDTO.setMessage("Invalid profile!");
//                            genericResponseDTO.setCode("Failed");
//                            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
//                        }

                    } else {
                        genericResponseDTO.setMessage("Invalid saved document");
                        genericResponseDTO.setCode("Failed");
                        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
                    }
//                }
//            }
        } else {
            genericResponseDTO.setMessage("Session ended!, login again.");
            genericResponseDTO.setCode("Failed");
            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
        }

        genericResponseDTO.setMessage("Kyc upgrade failed!");
        genericResponseDTO.setCode("Failed");
        genericResponseDTO.setData(currentUser);
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
    }

    private void sendUnsuccessfulKYCEmailUpgrade(Profile profile, Integer currentKycLevel, Integer nextLevel) {

        User user = profile.getUser();
        if (user != null) {
            String email = user.getEmail();
            if (utility.checkStringIsValid(email)) {

                String msg = "Dear " + user.getFirstName() + "," + "<br/>" + "<br/>" + "<br/>" + "<br/>" + "<br/>"
                    + "This is to inform you that your KYC upgrade request from " + currentKycLevel + " to "
                    + nextLevel + " has been declined." + "<br/>" + "<br/>" + "<br/>"
                    + "This could be one of the following reasons: " + "<br/>" + "<ul>"
                    + "<li> Inapplicable document has been uploaded </li>" + "</ul>" + "<br/>" + "<br/>" + "<br/>"
                    + "Kindly retry as applicable to enable you enjoy the benefits!" + "<br/>" + "<br/>" + "<br/>"
                    + "<b>" + "<i>"
                    + "You can also buy airtime, buy power, pay for TV subscription and pay other Billers with your wallet.</i></b>";

                try {
                    utility.sendEmail(email, "KYC UPGRADE NOTIFICATION", msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendSuccessKYCEmailUpgrade(Profile profile, Integer currentKycLevel, Integer nextLevel) {

        User user = profile.getUser();
        if (user != null) {
            String email = user.getEmail();
            if (utility.checkStringIsValid(email)) {

                String msg = "Dear " + user.getFirstName().toUpperCase() + "," + "<br/>" + "<br/>" + "<br/>" + "<br/>"
                    + "<br/>" + "Your KYC Request to be upgraded from " + currentKycLevel + " to " + nextLevel
                    + " has been sent to a staff for review." + "<br/>"
                    + "An update will be provided to you via email.<br/>" + "<br/>"
                    + "Thank you for choosing Pouchii! " + "<br/>" + "<br/>" + "<br/>";

                try {
                    utility.sendEmail(email, "KYC UPGRADE NOTIFICATION", msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void auditKycUpgrade(Profile save, Integer currentKycLevel, String outputFileName) {

        Map<String, Object> data = new HashMap<>();
        data.put("current kyc level", currentKycLevel);
        data.put("upgraded kyc level", currentKycLevel + 1);
        data.put("kyc document url", outputFileName);
        AuditEvent testUserEvent = new AuditEvent(save.getPhoneNumber(), "Kyc Upgrade", data);

        customAuditEventRepository.add(testUserEvent);
    }

    @PostMapping(path = "/upgrade-kyc3")
    public ResponseEntity<GenericResponseDTO> upgradeToKYCLevel3(@RequestBody UpgradeKYCLevel3DTO upgradeKYCLevel3DTO, @RequestParam Optional<Boolean> flag) {
        GenericResponseDTO genericResponseDTO = new GenericResponseDTO();
        if (flag.isPresent() && flag.get()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), headers,
                HttpStatus.OK);
        }
        User currentUser = utility.getCurrentUser();
        if (currentUser != null) {

            String encodedString = upgradeKYCLevel3DTO.getDocFile();

            String documentName;

            if (upgradeKYCLevel3DTO.getDocFormat().equalsIgnoreCase(DocumentType.JPG.name())) {
                documentName = "document-kyc3-" + utility.returnPhoneNumberFormat(currentUser.getLogin()) + ".jpg";
            } else if (upgradeKYCLevel3DTO.getDocFormat().equalsIgnoreCase(DocumentType.PDF.name())) {
                documentName = "document-kyc3-" + utility.returnPhoneNumberFormat(currentUser.getLogin()) + ".pdf";
            } else {
                return new ResponseEntity<>(
                    new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Document type", null),
                    HttpStatus.BAD_REQUEST);
            }
            String outputFileName = documentUrl + documentName;

            if (encodedString != null && !encodedString.isEmpty()) {

                boolean saved = utility.saveImage(encodedString, outputFileName);

                KycRequestDocType kycRequestDocType = KycRequestDocType.ELECTRICITY_BILL;
                if ("Waste Bill".equalsIgnoreCase(upgradeKYCLevel3DTO.getDocType())) {
                    kycRequestDocType = KycRequestDocType.WASTE_BILL;
                } else if ("Water Bill".equalsIgnoreCase(upgradeKYCLevel3DTO.getDocType())) {
                    kycRequestDocType = KycRequestDocType.WATER_BILL;
                } else if ("Letter from Public Authority".equalsIgnoreCase(upgradeKYCLevel3DTO.getDocType())) {
                    kycRequestDocType = KycRequestDocType.LETTER_FROM_PUBLIC_AUTHORITY;
                }

                if (saved) {
                    Profile profile = profileService.findByPhoneNumber(currentUser.getLogin());
                    if (profile != null) {
                        Kyclevel kyc = profile.getKyc();
                        Integer currentKycLevel = kyc.getKycLevel();

                        if (currentKycLevel == 3) {
                            genericResponseDTO.setMessage("Maximum KYC level reached!");
                            genericResponseDTO.setCode("Failed");
                            genericResponseDTO.setData(profile);

                            sendUnsuccessfulKYCEmailUpgrade(profile, currentKycLevel, currentKycLevel + 1);

                            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
                        } /*
                         * else if (currentKycLevel == 1) { genericResponseDTO.
                         * setMessage("You cannot move to Kyc level 3 from Kyc level 1!");
                         * genericResponseDTO.setCode("Failed"); genericResponseDTO.setData(profile);
                         *
                         * sendUnsuccessfulKYCEmailUpgrade(profile, currentKycLevel, currentKycLevel +
                         * 1);
                         *
                         * return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST); }
                         */ else {
                            KycRequest kycRequest = new KycRequest();
                            kycRequest.setCurrentLevel(currentKycLevel);
                            kycRequest.setNextLevel(3);
                            kycRequest.setProfile(profile);
                            kycRequest.setSenderProfile(profile);
                            kycRequest.setStatus(KycRequestStatus.AWAITING_APPROVAL);
                            kycRequest.setRequestDocType(kycRequestDocType);
                            kycRequest.setDocumentId(upgradeKYCLevel3DTO.getDocumentNumber());
                            kycRequest.setDocDateIssued(upgradeKYCLevel3DTO.getDateIssued());

                            KycRequest savedKycRequest = kycRequestService.save(kycRequest);

                            log.info("Saved KYC ====> " + savedKycRequest);

                            Kyclevel kyclevel = kyclevelService.findByKycLevel(currentKycLevel + 1);
//                            profile.setKyc(kyclevel);
//                            Profile save = profileService.save(profile);
//                            genericResponseDTO.setMessage("KYC level upgraded to " + kyclevel.getKycLevel());
                            genericResponseDTO.setMessage("Your document has been submitted for review");
                            genericResponseDTO.setCode("Success");
                            genericResponseDTO.setData(profile);

//                            String fileLink = "wallet.remita.net/api/" + outputFileName;
                            String fileLink = "https://wallet.remita.net/kyc-request";

                            // todo send email
                            sendEmailToCustomerSupport(profile, currentKycLevel, fileLink);

                            sendSuccessKYCEmailUpgrade(profile, currentKycLevel, currentKycLevel + 1);

                            return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
                        }
                    }
                }

                genericResponseDTO.setMessage("Failed to save document, please try again.");
                genericResponseDTO.setCode("Failed");
                return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);

            }
        }

        genericResponseDTO.setMessage("Session ended!, login again.");
        genericResponseDTO.setCode("Failed");
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);

    }

    private void sendEmailToCustomerSupport(Profile profile, Integer currentKycLevel, String fileLink) {

        Map<String, String> emails = utility.getEmailMap();

        List<WalletAccount> walletAccountList = walleAccountService
            .findByAccountOwnerPhoneNumber(profile.getPhoneNumber());

        int newKycLevel = currentKycLevel + 1;

        for (Map.Entry<String, String> entry : emails.entrySet()) {

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");

            LocalDateTime format = LocalDateTime.now();
            String now = format.format(dateFormatter);
            String time = format.format(timeFormatter);

            String accountNumber = "";
            if (!walletAccountList.isEmpty()) {
                Optional<WalletAccount> primaryWallet = utility.getPrimaryWallet(walletAccountList);
                if (primaryWallet.isPresent()) {
                    accountNumber = primaryWallet.get().getAccountNumber();
                }
            }
            String responsibility = "Olamide Abiwo ";
            String msg = "Dear Team," + "<br/>" + "<br/>" + "<br/>" + "<br/>"
                + "<b><u> KYC UPGRADE NOTIFICATION </u></b>" + "<br/>" + "<br/>" + "<br/>" + "Date : " + now
                + "<br/>" + "Time  : " + time + "<br/>" + "<br/>" + "<br/>" + "Username : " + profile.getFullName()
                + "<br/>" + "Wallet ID : " + accountNumber + "<br/>" + "Phone Number : " + profile.getPhoneNumber()
                + "<br/>" + "Current KYC : " + "KYC Level " + currentKycLevel + "<br/>" + "Upgrade KYC : "
                + "KYC Level " + newKycLevel + "<br/>" + "URL : <a href=" + fileLink + ">" + fileLink + "</a><br/>"
                + "Responsibility : " + responsibility + " (+234 913 1038 088)";

            try {
                utility.sendEmail(entry.getValue(), "KYC UPGRADE NOTIFICATION", msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        byte[] image = new byte[0];
        try {
            String pathname = imageUrl + filename;
            File file = new File(pathname);
            image = FileUtils.readFileToByteArray(file);
            log.debug("Image Pathname : " + pathname);
            log.debug("Image Byte : " + imageUrl);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(image);

    }

    @GetMapping("/documents/kyc/{phoneNumber}")
    public ResponseEntity<byte[]> getKycImage(@PathVariable String phoneNumber) {
        byte[] image = new byte[0];
        phoneNumber = utility.returnPhoneNumberFormat(phoneNumber);

        String filename = "document-kyc2-" + phoneNumber + ".jpg";
        String filename2 = "document-kyc2-" + phoneNumber + ".pdf";

        String pathname = documentUrl + filename;
        String pathname2 = documentUrl + filename2;

        try {
            File file = new File(pathname);
            image = FileUtils.readFileToByteArray(file);
            log.debug("Image Pathname : " + pathname);
            log.debug("Image Byte : " + documentUrl);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);

        } catch (IOException e) {
            e.printStackTrace();

            try {
                File file = new File(pathname2);
                image = FileUtils.readFileToByteArray(file);
                log.debug("Image Pathname : " + pathname2);
                log.debug("Image Byte : " + documentUrl);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(image);

            } catch (Exception xe) {
                xe.getSuppressed();
            }
        }

        return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(image);

    }

    public String getKycDocumentUrl(String phoneNumber) {

        byte[] image = new byte[0];
        phoneNumber = utility.returnPhoneNumberFormat(phoneNumber);

        String filename = "document-kyc2-" + phoneNumber + ".jpg";
        String filename2 = "document-kyc2-" + phoneNumber + ".pdf";

        String pathname = documentUrl + filename;
        String pathname2 = documentUrl + filename2;

        File file = new File(pathname);
        File file2 = new File(pathname2);
        if (file.exists()) {
            return file.getAbsolutePath();
        }

        if (file2.exists()) {
            return file2.getAbsolutePath();
        }

        return null;

    }

    @GetMapping("/documents/kyc3/{phoneNumber}")
    public ResponseEntity<byte[]> getKyc3Image(@PathVariable String phoneNumber) {
        byte[] image = new byte[0];
        phoneNumber = utility.returnPhoneNumberFormat(phoneNumber);

        String filename = "document-kyc3-" + phoneNumber + ".jpg";
        String filename2 = "document-kyc3-" + phoneNumber + ".pdf";

        String pathname = documentUrl + filename;
        String pathname2 = documentUrl + filename2;

        try {
            File file = new File(pathname);
            image = FileUtils.readFileToByteArray(file);
            log.debug("Image Pathname : " + pathname);
            log.debug("Image Byte : " + documentUrl);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);

        } catch (IOException e) {
            e.printStackTrace();

            try {
                File file = new File(pathname2);
                image = FileUtils.readFileToByteArray(file);
                log.debug("Image Pathname : " + pathname2);
                log.debug("Image Byte : " + documentUrl);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(image);

            } catch (Exception xe) {
                xe.getSuppressed();
            }
        }

        return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(image);

    }

    @GetMapping("/available/{phoneNumber}")
    public boolean isAvailable(@PathVariable String phoneNumber) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        out.println(" crashing on available phoneNumber==" + phoneNumber);
        Profile profile = profileService.findByPhoneNumber(phoneNumber);
        return profile != null;
    }

    @GetMapping("/profiles/{fromDate}/{toDate}")
    public ResponseEntity<GenericResponseDTO> getAllProfilesByDate(Pageable pageable, @PathVariable LocalDate fromDate,
                                                                   @PathVariable LocalDate toDate) {
        log.debug("REST request to get a page of Profiles by Date");

        if (fromDate == null || toDate == null) {
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), HttpStatus.OK);
        }
        Page<ProfileDTO> profiles = profileService.findAllByCreatedDateBetween(pageable, fromDate, toDate);
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), profiles);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", profiles.getSize());
        metaMap.put("totalNumberOfRecords", profiles.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", profiles.getContent(), metaMap), headers,
            HttpStatus.OK);
    }

    @GetMapping("/profiles/{schemeId}/search")
    public ResponseEntity<GenericResponseDTO> searchAllProfilesByKeywordAndScheme(Pageable pageable,
                                                                                  @RequestParam(required = false) String key, @PathVariable String schemeId) {
        log.debug("REST request to search all profiles with a Keyword" + schemeId);
        Page<ProfileDTO> profiles = profileService.findAllWithKeywordWithScheme(pageable, key, schemeId);
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), profiles);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", profiles.getSize());
        metaMap.put("totalNumberOfRecords", profiles.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", profiles.getContent(), metaMap), headers,
            HttpStatus.OK);

    }

    @GetMapping("/profiles/admin")
    public ResponseEntity<GenericResponseDTO> getAdminProfiles() {
        log.debug("REST request to get all admin profiles");
        List<ProfileDTO> profiles = profileService.getUserListByAdminAndSuperAdminRole();

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("totalNumberOfRecords", profiles.size());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", profiles, metaMap), HttpStatus.OK);

    }

    @GetMapping("/profiles/total-bonus")
    public ResponseEntity<GenericResponseDTO> getUserTotalBonus() {
        log.debug("REST request to get all user total bonus");
        Double totalBonus = profileService.getUserTotalBonus();

        if (totalBonus != null) {

            return new ResponseEntity<>(new GenericResponseDTO("00", "success", totalBonus), HttpStatus.OK);
        }

        return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid User details", totalBonus), HttpStatus.OK);

    }

    @PostMapping("/profiles/profile-picture")
    public ResponseEntity<GenericResponseDTO> uploadProfilePicture(
        @RequestBody UploadProfilePicture uploadProfilePicture) {

        Optional<Profile> byUserIsCurrentUser = profileService.findCurrentUserProfile();
        Profile profile = null;
        if (byUserIsCurrentUser.isPresent()) {
            Profile profileDTO = byUserIsCurrentUser.get();

            out.println("The Profile retrieved here  ====== " + profileDTO);

            profile = profileService.findByPhoneNumber(profileDTO.getPhoneNumber());

            if (profile != null) {

                out.println(" The KYC retrieved here ====== " + profile.getKyc().getId());
                String profilePictureName;
                if (uploadProfilePicture.getPhotoFormat().equalsIgnoreCase(DocumentType.JPG.name())) {
                    profilePictureName = "profile-pics-" + utility.returnPhoneNumberFormat(profileDTO.getPhoneNumber())
                        + ".jpg";
                } else if (uploadProfilePicture.getPhotoFormat().equalsIgnoreCase(DocumentType.PDF.name())) {
                    profilePictureName = "profile-pics-" + utility.returnPhoneNumberFormat(profileDTO.getPhoneNumber())
                        + ".pdf";
                } else {
                    return new ResponseEntity<>(
                        new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Document type", null),
                        HttpStatus.BAD_REQUEST);
                }

                String outputFileName = documentUrl + profilePictureName;

                String encodedString = uploadProfilePicture.getPhoto();

                if (encodedString != null && !encodedString.isEmpty()) {
                    boolean saved = utility.saveImage(encodedString, outputFileName);

                    if (saved) {
//                        profileDTO.setProfilePicture(profilePictureName);
                        byte[] photo = profileDTO.getPhoto();
                        if (photo != null) {
                            profile.setPhoto(Arrays.copyOf(photo, photo.length));
                        }
                        profile.setPhotoContentType(profileDTO.getPhotoContentType());

                        profile.setProfilePicture(profilePictureName);
                        profile = profileService.save(profile);
                        log.info("Saved profile picture ==> " + profile);
                        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", profileDTO),
                            HttpStatus.OK);
                    }
                }
            }

        }

        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed"),
            HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/profiles/pics/{phoneNumber}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String phoneNumber) {

        byte[] image = new byte[0];
        phoneNumber = utility.returnPhoneNumberFormat(phoneNumber);

        String filename = "profile-pics-" + phoneNumber + ".jpg";
        String filename2 = "profile-pics-" + phoneNumber + ".pdf";

        String pathname = documentUrl + filename;
        String pathname2 = documentUrl + filename2;

        try {
            File file = new File(pathname);
            image = FileUtils.readFileToByteArray(file);
            log.debug("Image Pathname : " + pathname);
            log.debug("Image Byte : " + documentUrl);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);

        } catch (IOException e) {
            e.printStackTrace();
            try {
                File file = new File(pathname2);
                image = FileUtils.readFileToByteArray(file);
                log.debug("Image Pathname : " + pathname2);
                log.debug("Image Byte : " + documentUrl);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(image);

            } catch (Exception xe) {
                xe.getSuppressed();
            }
        }

        return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(image);

    }

    @GetMapping("/get-kyc-status/{profileId}")
    public ResponseEntity<Boolean> getKycStatus(@PathVariable("profileId") Long profileId) {
        boolean hasPendingRequest = checkKycRequestStatus(profileId);
        return new ResponseEntity<>(hasPendingRequest, HttpStatus.OK);
    }

    private boolean checkKycRequestStatus(Long profileId) {
        return kycRequestRepository.existsByProfileIdAndStatus(profileId, KycRequestStatus.AWAITING_APPROVAL);
    }

    @PutMapping("/profile/update-external-user-profile")
    public ResponseEntity<GenericResponseDTO> updateExternalProfile(@Valid @RequestBody SchemePinPasswordImageDTO schemePinPasswordImageDTO){

        System.out.println(schemePinPasswordImageDTO);

        if (schemePinPasswordImageDTO.getPhoneNumber() == null){
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Phone Number cannot be empty", null), HttpStatus.BAD_REQUEST);
        }

        if (schemePinPasswordImageDTO.getPin() == null){
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Password cannot be empty", null), HttpStatus.BAD_REQUEST);
        }

        if (schemePinPasswordImageDTO.getPassword() == null){
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Pin cannot be empty", null), HttpStatus.BAD_REQUEST);
        }

        if (schemePinPasswordImageDTO.getImage() == null){
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Image cannot be empty", null), HttpStatus.BAD_REQUEST);
        }

        String phoneNumber = utility.formatPhoneNumber(schemePinPasswordImageDTO.getPhoneNumber().trim());
        String encryptedPin = passwordEncoder.encode(schemePinPasswordImageDTO.getPin());
        String encryptedPassword = passwordEncoder.encode(schemePinPasswordImageDTO.getPassword());
        String encodedString = schemePinPasswordImageDTO.getImage();

        String photoName = "photo" + System.currentTimeMillis() + ".jpg";
        String outputFileName = imageUrl + photoName;

        try {
            log.debug("ABOUT TO DECODE");
            byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
            log.debug("AFTER DECODE");

            decodedBytes = utility.resizeImage(decodedBytes);

            File file = new File(outputFileName);
            log.debug("Path name = " + outputFileName);
            log.debug("File absolute path = " + file.getAbsolutePath());
            FileUtils.writeByteArrayToFile(file, decodedBytes);

            Profile profile = profileService.findByPhoneNumber(phoneNumber);

            if (profile == null){
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User Profile Not Found.", null), HttpStatus.BAD_REQUEST);
            }

            System.out.println(profile);

            User user = profile.getUser();

            if (user == null){
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User Not Found.", null), HttpStatus.BAD_REQUEST);
            }

            System.out.println(user);

            user.setPassword(encryptedPassword);
            user.setImageUrl(photoName);
            profile.setPin(encryptedPin);
            profile.setProfileID("4");

            userRepository.save(user);
            profileService.save(profile);

            System.out.println("Update Successful");

            return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "Update Successful", profile), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.getLocalizedMessage());
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), null), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/profile/change-profile-id/{phoneNumber}/{profileId}")
    public ResponseEntity<GenericResponseDTO> changeProfileId(@Valid @PathVariable String phoneNumber, @PathVariable String profileId){

        try {

            if (phoneNumber == null){
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Phone Number cannot be empty", null), HttpStatus.BAD_REQUEST);
            }

            if (profileId == null){
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Profile Id cannot be empty", null), HttpStatus.BAD_REQUEST);
            }

            String phone = utility.formatPhoneNumber(phoneNumber);

            Profile profile = profileService.findByPhoneNumber(phone);

            if (profile != null){

                profile.setProfileID(profileId);
                profileService.save(profile);

                System.out.println("Update Successful");

                return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "Update Successful", null), HttpStatus.OK);

            }

            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User Profile Not Found.", null), HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
            log.debug(e.getLocalizedMessage());
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), null), HttpStatus.BAD_REQUEST);
        }

    }


    @PutMapping("/update-date-of-birth")
    ResponseEntity<GenericResponseDTO> updateDateOfBirth(@RequestBody DateOfBirthUpdateDTO dateOfBirthUpdateDTO){

        try{

            String phone = utility.formatPhoneNumber(dateOfBirthUpdateDTO.getPhoneNumber());

            Profile profile = profileService.findByPhoneNumber(phone);

            if (profile == null){
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Profile not found"), HttpStatus.BAD_REQUEST);
            }

            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthUpdateDTO.getDateOfBirth());
            profile.setDateOfBirth(dateOfBirth);

            profileService.save(profile);

            return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "Date of birth updated successfully"), HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/hm/upgrade-kyc")
    ResponseEntity<GenericResponseDTO> upgradehmkyc2(@RequestBody UpgradeKYCLevelHMDTO upgradeKYCLevelHMDTO, @RequestParam Optional<Boolean> flag){

        String phone = utility.formatPhoneNumber(upgradeKYCLevelHMDTO.getPhoneNumber());

        try{

            Profile profile = profileService.findByPhoneNumber(phone);

            if (profile == null){
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Profile not found"), HttpStatus.BAD_REQUEST);
            }

            LocalDate dateOfBirth = LocalDate.parse(upgradeKYCLevelHMDTO.getDateOfBirth());
            profile.setDateOfBirth(dateOfBirth);

            profileService.save(profile);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
        }

        GenericResponseDTO genericResponseDTO = new GenericResponseDTO();

        if (flag.isPresent() && flag.get()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), headers,
                HttpStatus.OK);
        }
        log.debug("UpgradeKYCLevelDTO ====>" + upgradeKYCLevelHMDTO);
        String bvn = upgradeKYCLevelHMDTO.getBvn();

        User currentUser = userRepository.findByLogin(phone);

        if (currentUser != null) {

            Profile profile = profileService.findByPhoneNumber(currentUser.getLogin());
            // if awaiting approval return: "You have an already pending request."
            if (checkKycRequestStatus(profile.getId())) {
                genericResponseDTO.setCode("99");
                genericResponseDTO.setMessage("You have an already pending request.");
                return new ResponseEntity<>(genericResponseDTO, HttpStatus.TOO_MANY_REQUESTS);
            }

            log.debug("KYC Upgrade ==> BVN Validation Begins");

            String firstName = currentUser.getFirstName();
            String lastName = currentUser.getLastName();
            String phoneNumber = utility.formatPhoneNumber(currentUser.getLogin());
            LocalDate currentUserDateOfBirth = profile.getDateOfBirth();
            LocalDate parse = currentUserDateOfBirth;

            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                log.debug("KYC Upgrade ==> BVN Validation Demo");
            }else {

//                try {
//                    responseEntity = cashConnectService.getBvn(bvn);
//                } catch (Exception e) {
//                    return new ResponseEntity<>(new GenericResponseDTO("99", e.getLocalizedMessage(), null),
//                        HttpStatus.BAD_REQUEST);
//                }
//                if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {

//                    GenericResponseDTO body = responseEntity.getBody();

                RemitaBVNRequest remitaBVNRequest = new RemitaBVNRequest();
                remitaBVNRequest.setRequestReference("{{transRef}}");
                remitaBVNRequest.setBvn(bvn);

                RemitaBVNResponse response = remitaCarmelUtils.bvnResponse(remitaBVNRequest);

//                    if (body != null && body.getCode().equalsIgnoreCase("00")) {
                if (response != null) {
                    log.debug("KYC Upgrade ==> BVN Validated");

                    try {

//                            NewBvnResponseData data = new ObjectMapper().readValue((String) body.getData(),
//                                NewBvnResponseData.class);
                        RemitaBVNResponseData data = (RemitaBVNResponseData) response.getData();

                        firstName = data.getFirstName();
                        lastName = data.getLastName();
                        phoneNumber = data.getPhoneNumber1();
                        String dateOfBirth = data.getDateOfBirth();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                        parse = LocalDate.parse(dateOfBirth, formatter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    genericResponseDTO.setMessage("Bvn verification failed");
                    genericResponseDTO.setCode("Failed");
                    genericResponseDTO.setData(bvn);
                    return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
                }
//                }
            }

            out.println("Comparing ==== \n " +
                "Firstname: " + firstName + " === " + currentUser.getFirstName() +
                "\nLastname: " + lastName + " === " + currentUser.getLastName() +
                "\nphoneNumber: " + phoneNumber + " === " + currentUser.getLogin() +
                "\ndateOfBirth: " + parse + " === " + profile.getDateOfBirth()
            );


            int count = 0;

            String msg = "Validation failed for ";

            if (profile != null) {
                currentUserDateOfBirth = profile.getDateOfBirth();
                if (currentUserDateOfBirth.compareTo(parse) == 0) {
                    count++;
                } else {
                    msg = msg + "Date of birth";
                }
            }
            if (firstName.equalsIgnoreCase(currentUser.getFirstName())) {
                count++;
            } else {
                msg = msg + ", firstname ";
            }
            if (lastName.equalsIgnoreCase(currentUser.getLastName())) {
                count++;
            } else {
                msg = msg + ", lastname";
            }
            if (utility.formatPhoneNumber(phoneNumber)
                .equalsIgnoreCase(utility.formatPhoneNumber(currentUser.getLogin()))) {
                count++;
            } else {
                msg = msg + ", phoneNumber";

            }
            if (count < 3) {
                return new ResponseEntity<>(new GenericResponseDTO("99", msg, null),
                    HttpStatus.BAD_REQUEST);
            }

            String encodedString = upgradeKYCLevelHMDTO.getDocFile();
            String documentName;

            if (upgradeKYCLevelHMDTO.getDocFormat().equalsIgnoreCase(DocumentType.JPG.name())) {
                documentName = "document-kyc2-" + utility.returnPhoneNumberFormat(currentUser.getLogin())
                    + ".jpg";
            } else if (upgradeKYCLevelHMDTO.getDocFormat().equalsIgnoreCase(DocumentType.PDF.name())) {
                documentName = "document-kyc2-" + utility.returnPhoneNumberFormat(currentUser.getLogin())
                    + ".pdf";
            } else {
                return new ResponseEntity<>(
                    new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Document type", null),
                    HttpStatus.BAD_REQUEST);
            }
            String outputFileName = documentUrl + documentName;

            boolean saved = false;
            KycRequestDocType kycRequestDocType = KycRequestDocType.NIN;
            if (utility.checkStringIsNotValid(encodedString)
                || "NIN".equalsIgnoreCase(upgradeKYCLevelHMDTO.getDocType())) {
                log.debug("Document Type is NIN");
                saved = true;
            } else {
                if ("Driver's License".equalsIgnoreCase(upgradeKYCLevelHMDTO.getDocType())) {
                    kycRequestDocType = KycRequestDocType.DRIVERS_LICENSE;
                } else if ("Voters Card".equalsIgnoreCase(upgradeKYCLevelHMDTO.getDocType())) {
                    kycRequestDocType = KycRequestDocType.VOTERS_CARD;
                }
                saved = utility.saveImage(encodedString, outputFileName);
            }

            if (saved) {

                if (profile != null) {
                    Kyclevel kyc = profile.getKyc();
                    Integer currentKycLevel = kyc.getKycLevel();

                    if (currentKycLevel == 3) {
                        genericResponseDTO.setMessage("Maximum KYC level reached!");
                        genericResponseDTO.setCode("Failed");
                        genericResponseDTO.setData(profile);

                        sendUnsuccessfulKYCEmailUpgrade(profile, currentKycLevel, currentKycLevel + 1);

                        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
                    } else if (currentKycLevel == 2) {
                        genericResponseDTO.setMessage("You are already in KYC level 2");
                        genericResponseDTO.setCode("Failed");
                        genericResponseDTO.setData(profile);

                        sendUnsuccessfulKYCEmailUpgrade(profile, currentKycLevel, currentKycLevel + 1);

                        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
                    } else {
                        Kyclevel kyclevel = kyclevelService.findByKycLevel(currentKycLevel + 1);
                        profile.setBvn(bvn);
                        profile.setValidID(upgradeKYCLevelHMDTO.getVerificationId());
                        profile.setValidDocType(kycRequestDocType);
                        Profile save = profileService.save(profile);

                        KycRequest kycRequest = new KycRequest();
                        kycRequest.setCurrentLevel(currentKycLevel);
                        kycRequest.setNextLevel(currentKycLevel + 1);
                        kycRequest.setProfile(profile);
                        kycRequest.setSenderProfile(profile);
                        kycRequest.setStatus(KycRequestStatus.AWAITING_APPROVAL);
                        kycRequest.setRequestDocType(kycRequestDocType);
                        kycRequest.setDocumentId(upgradeKYCLevelHMDTO.getVerificationId());

                        KycRequest savedKycRequest = kycRequestService.save(kycRequest);

                        log.info("Saved KYC ====> " + savedKycRequest);

                        genericResponseDTO.setMessage("KYC level upgraded to " + kyclevel.getKycLevel());
                        genericResponseDTO.setCode("Success");
                        genericResponseDTO.setData(save.getKyc());

                        String fileLink = "https://wallet.remita.net/kyc-request";

                        sendEmailToCustomerSupport(save, currentKycLevel, fileLink);

                        auditKycUpgrade(save, currentKycLevel, outputFileName);

                        sendSuccessKYCEmailUpgrade(save, currentKycLevel, currentKycLevel + 1);

                        return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
                    }
                }

            } else {
                genericResponseDTO.setMessage("Invalid saved document");
                genericResponseDTO.setCode("Failed");
                return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
            }

        } else {
            genericResponseDTO.setMessage("Session ended!, login again.");
            genericResponseDTO.setCode("Failed");
            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
        }

        genericResponseDTO.setMessage("Kyc upgrade failed!");
        genericResponseDTO.setCode("Failed");
        genericResponseDTO.setData(currentUser);
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);

    }

}
