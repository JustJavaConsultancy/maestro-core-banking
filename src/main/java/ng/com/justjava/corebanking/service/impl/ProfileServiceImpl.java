package ng.com.justjava.corebanking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ng.com.justjava.corebanking.repository.AuthorityRepository;
import ng.com.justjava.corebanking.repository.ProfileRepository;
import ng.com.justjava.corebanking.security.AuthoritiesConstants;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.mapper.ProfileMapper;
import ng.com.justjava.corebanking.client.ExternalOTPRESTClient;
import ng.com.justjava.corebanking.config.AsyncConfiguration;
import ng.com.justjava.corebanking.domain.Authority;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.Scheme;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.service.JournalLineService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.SchemeService;
import ng.com.justjava.corebanking.service.UserService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.justjava.corebanking.util.RemitaCarmelUtils;
import ng.com.justjava.corebanking.util.Utility;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Profile}.
 */
@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final ExternalOTPRESTClient externalOTPRESTClient;

    private final Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private final ProfileRepository profileRepository;
    private final AsyncConfiguration asyncConfiguration;
    @Value("${app.sms.message.otp}")
    private String otpMessage;

    @Value("${app.nin.publicKey}")
    private String ninPublicKey;

    @Value("${app.nin.refId}")
    private String refId;

    @Value("${app.nin.authCode}")
    private String authCode;

    @Value("${app.nin.secretKey}")
    private String secretKey;

    @Value("${app.nin.ninUrl}")
    private String ninUrl;

    private final AuthorityRepository authorityRepository;

    private final ProfileMapper profileMapper;

    private final JournalLineService journalLineService;

    private final WalletAccountService walletAccountService;

    private final SchemeService schemeService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final Utility utility;

    private final RemitaCarmelUtils remitaCarmelUtils;

    private final Environment environment;

    @Value("${app.nin.mock-json.validate-nin}")
    private String validateNinResponse;

    public ProfileServiceImpl(ProfileRepository profileRepository, ExternalOTPRESTClient externalOTPRESTClient,
                              AsyncConfiguration asyncConfiguration, AuthorityRepository authorityRepository, ProfileMapper profileMapper,
                              JournalLineService journalLineService, @Lazy WalletAccountService walletAccountService,
                              SchemeService schemeService, @Lazy UserService userService, PasswordEncoder passwordEncoder,
                              @Lazy Utility utility, Environment environment, RemitaCarmelUtils remitaCarmelUtils) {
        this.profileRepository = profileRepository;
        this.externalOTPRESTClient = externalOTPRESTClient;
        this.asyncConfiguration = asyncConfiguration;
        this.authorityRepository = authorityRepository;
        this.profileMapper = profileMapper;
        this.journalLineService = journalLineService;
        this.walletAccountService = walletAccountService;
        this.schemeService = schemeService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.utility = utility;
        this.environment = environment;
        this.remitaCarmelUtils = remitaCarmelUtils;
    }

    @Override
    public ProfileDTO save(ProfileDTO profileDTO) {
		log.debug("Request to save Profile : {}", profileDTO);
		Profile profile = profileMapper.toEntity(profileDTO);
		profile = profileRepository.save(profile);
        return profileMapper.toDto(profile);
    }

    @Override
    public Profile save(Profile profile) {
        log.debug("Request to save Profile : {}", profile);
        profile = profileRepository.save(profile);
        return profile;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Profiles");
        return profileRepository.findAll(pageable).map(profileMapper::toDto);
    }

    @Override
    public Page<ProfileDTO> findAllWithKeyword(Pageable pageable, String keyword) {
        log.debug("Request to search all Profiles using keyword");
        return profileRepository.searchProfileByKeyword(pageable, keyword).map(profileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileDTO> findOne(Long id) {
        log.debug("Request to get Profile : {}", id);
        return profileRepository.findById(id).map(profileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Profile : {}", id);
        profileRepository.deleteById(id);
    }

    @Override
    public Optional<ProfileDTO> findOneByPhoneNumber(String phoneNumber) {
        Profile profile = profileRepository.findOneByPhoneNumber(phoneNumber);
        return Optional.ofNullable(profileMapper.toDto(profile));
    }

    @Override
    public Profile findByPhoneNumber(String phoneNumber) {
        // TODO Auto-generated method stub
        return profileRepository.findOneByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<ProfileDTO> findByUserIsCurrentUser() {
        Profile profile = profileRepository.findByUserIsCurrentUser();
        if (profile != null) {
            return Optional.ofNullable(profileMapper.toDto(profile));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Profile> findCurrentUserProfile() {
        Profile profile = profileRepository.findByUserIsCurrentUser();
        if (profile != null) {
            return Optional.of(profile);
        }

        return Optional.empty();
    }

    @Override
    public Page<ProfileDTO> findAllByCreatedDateBetween(Pageable pageable, LocalDate fromDate, LocalDate toDate) {
        Instant fromInstant = fromDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant toInstant = toDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        return profileRepository.findAllByCreatedDateBetween(pageable, fromInstant, toInstant).map(profileMapper::toDto);
    }

    @Override
    public Boolean canSpendOnAccount(String phoneNumber, String accountNumber, Double amount) {
        Profile profile = profileRepository.findOneByPhoneNumber(phoneNumber);
        Double todaySpending = journalLineService.getAccountDailyTransactionAmount(accountNumber);
        return ((todaySpending + amount) <= profile.getKyc().getDailyTransactionLimit());

    }

    @Override
    public Boolean canAccummulateOnAccount(String phoneNumber, String accountNumber, Double amount) {

        // Profile profile = profileRepository.findOneByPhoneNumber(phoneNumber);

        WalletAccount benAccount = walletAccountService.findOneByAccountNumber(String.valueOf(accountNumber));
        Profile profile = benAccount.getAccountOwner();
        if (profile == null)
            return false;
        // Set<WalletAccount> accounts = profile.getWalletAccounts();

        if (profile.getKyc().getKycLevel() == 3) {
            return true;
        }

        Boolean response = false;
        Double accumulatedAmount = 0.00;

        accumulatedAmount = Double.parseDouble(benAccount.getCurrentBalance()) + amount;

        System.out.println(" The account number here ==========" + benAccount.getAccountNumber()
            + " accumulatedAmount==" + accumulatedAmount + "   profile.getKyc().getCumulativeBalanceLimit()===="
            + profile.getKyc().getCumulativeBalanceLimit());
        response = accumulatedAmount <= profile.getKyc().getCumulativeBalanceLimit();

        return response;

    }

    @Override
    public Boolean shouldSendApproachLimitNotification(String phoneNumber, String accountNumber, Double amount) {
        WalletAccount benAccount = walletAccountService.findOneByAccountNumber(String.valueOf(accountNumber));

        double percentage = 0.85;

        Profile profile = benAccount.getAccountOwner();
        if (profile == null)
            return false;

        if (profile.getKyc().getKycLevel() == 3) {
            return false;
        }
        // Set<WalletAccount> accounts = profile.getWalletAccounts();
        Boolean response = false;
        Double accumulatedAmount = 0.00;

        accumulatedAmount = Double.parseDouble(benAccount.getCurrentBalance()) + amount;

        Double cumulativeBalanceLimit = profile.getKyc().getCumulativeBalanceLimit();
        System.out.println(" The account number here ==========" + benAccount.getAccountNumber()
            + " accumulatedAmount==" + accumulatedAmount + "   profile.getKyc().getCumulativeBalanceLimit()===="
            + cumulativeBalanceLimit);

        response = accumulatedAmount >= cumulativeBalanceLimit / percentage;

        return response;
    }

    @Override
    public Page<ProfileDTO> findAllWithKeywordWithScheme(Pageable pageable, String key, String schemeId) {
        List<WalletAccount> walletAccounts = walletAccountService.findAllBySchemeIDAndAccountOwnerIsNotNullAndSearch(schemeId, key);

        log.info("Wallet Account From Scheme =====> {}", walletAccounts.get(0));


        Scheme scheme = schemeService.findBySchemeID(schemeId);
        log.info("Scheme =====> {}", scheme);

        List<ProfileDTO> profileDTOS = walletAccounts
            .stream()
            .filter(walletAccount -> walletAccount.getAccountOwner() != null && walletAccount.getScheme().getSchemeID().equals(scheme.getSchemeID()))
            .peek(walletAccount -> {
                log.info("WalletAccount Peek =====> {}", walletAccount.getScheme().getId());
                log.info("WalletAccount Peek =====> {}", scheme.getId());
            })
            .map(WalletAccount::getAccountOwner)
            .sorted(Comparator.comparing(Profile::getCreatedDate))
            .distinct()
            .map(profileMapper::toDto)
            .collect(Collectors.toList());

        log.info("Profile From Scheme =====> {}", profileDTOS.get(0));

        int start = (int) pageable.getOffset();
        int totalSize = profileDTOS.size();

        int end = Math.min((start + pageable.getPageSize()), totalSize);
        if (start > totalSize) {
            return new PageImpl<>(new ArrayList<>(), pageable, totalSize);
        }

        return new PageImpl<>(profileDTOS.subList(start, end), pageable, totalSize);
    }

    @Override
    public List<ProfileDTO> getUserListByAdminAndSuperAdminRole() {

        Authority admin = authorityRepository.findById(AuthoritiesConstants.ADMIN).orElseThrow(RuntimeException::new);
        Authority superAdmin =
            authorityRepository.findById(AuthoritiesConstants.SUPER_ADMIN).orElseThrow(RuntimeException::new);

        Set<Authority> authorities = new HashSet<>();
        authorities.add(admin);
        authorities.add(superAdmin);

        ArrayList<Authority> authoritiesList = new ArrayList<>(authorities);

        List<User> allByAuthorities = userService.findAllByAuthorities(authoritiesList);

        List<Profile> profileList = profileRepository.findAllByUserIn(allByAuthorities);

        return profileList.stream().map(profileMapper::toDto).collect(Collectors.toList());

    }

    @Override
    public List<Profile> findByDeviceNotificationToken(String deviceNotificationToken) {
        return profileRepository.findByDeviceNotificationToken(deviceNotificationToken);
    }

    @Override
    public Double getUserTotalBonus() {

        String phoneNumber = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            phoneNumber = ((UserDetails) principal).getUsername();
        } else
            phoneNumber = principal.toString();

        Profile loginUserProfile = profileRepository.findOneByPhoneNumber(phoneNumber);
        if (loginUserProfile != null) {

            return loginUserProfile.getTotalBonus();
        }

        return null;

    }

    @Override
    public GenericResponseDTO validateSecurityAnswer(ValidateSecretDTO validateSecretDTO) {

        String phoneNumber = utility.formatPhoneNumber(validateSecretDTO.getPhoneNumber());

        Profile profile = profileRepository.findOneByPhoneNumber(phoneNumber);

        if (profile != null) {
            String secretAnswer = profile.getSecretAnswer();
            String secretQuestion = profile.getSecretQuestion();

            if (secretQuestion == null) {

                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User security question not set", null);
            }
            if (secretAnswer == null) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User security answer not set", null);
            }

            if (secretAnswer.equalsIgnoreCase(validateSecretDTO.getAnswer()) &&
                secretQuestion.equalsIgnoreCase(validateSecretDTO.getQuestion())) {
                return new GenericResponseDTO("00", HttpStatus.OK, "Security answer validated", null);
            } else {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid security Question/Answer", null);
            }

        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Profile not found!", null);

    }

    public GenericResponseDTO updatePin(LostPinDTO lostPinDTO) {
        log.info("Lost pin dto ===> " + lostPinDTO);

        String phoneNumber = utility.formatPhoneNumber(lostPinDTO.getPhoneNumber());

        Profile profile = findByPhoneNumber(phoneNumber);
        User user = profile.getUser();

        if (profile != null && user != null) {

            String encodedPin = passwordEncoder.encode(lostPinDTO.getNewPin());

            profile.setPin(encodedPin);

            Profile save = save(profile);
            log.info("Updated profile pin ===> " + save);

            utility.sendAlertEmailForAction(user, "Change Password", LocalDateTime.now());

            return new GenericResponseDTO("00", HttpStatus.OK, "success", null);

        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User profile not found!", null);

    }

    public GenericResponseDTO validateUserPin(LostPasswordDTO lostPasswordDTO) {
        String phoneNumber = utility.formatPhoneNumber(lostPasswordDTO.getPhoneNumber());

        Profile profile = findByPhoneNumber(phoneNumber);

        if (profile != null) {

            String encryptedPin = profile.getPin();

            if (!passwordEncoder.matches(lostPasswordDTO.getPin(), encryptedPin)) {

                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Pin", null);
            } else {
                return new GenericResponseDTO("00", HttpStatus.OK, "success", null);
            }
        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User not found!", null);

    }

    @Override
    public GenericResponseDTO updateSecurityQuestion(ValidateSecretDTO validateSecretDTO) {

        User currentUser = utility.getCurrentUser();
        if (currentUser != null) {

            String phoneNumber = currentUser.getLogin();

            Profile profile = profileRepository.findOneByPhoneNumber(phoneNumber);

            if (profile != null) {

                if (validateSecretDTO.getOtp() == null){
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Otp Required", null);
                }

                if (validateSecretDTO.getTrigger()){

                    ResponseEntity<Boolean> responseEntity = userService.verifyOTP(Long.valueOf(validateSecretDTO.getOtp()), phoneNumber);

                    if (Boolean.FALSE.equals(responseEntity.getBody())) {
                        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Otp", null);
                    }

                }

                profile.setSecretQuestion(validateSecretDTO.getQuestion());
                profile.setSecretAnswer(validateSecretDTO.getAnswer());

                Profile save = save(profile);
                log.info("updated secret question/answer ====> " + save);

                User user = save.getUser();
                if (user != null) {
                    utility.sendAlertEmailForAction(user, "Change Security Question", LocalDateTime.now());
                }

                return new GenericResponseDTO("00", HttpStatus.OK, "success", null);

            }
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User profile not found", null);
        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Failed to retrieve login user", null);

    }

    @Override
    public GenericResponseDTO hmUpdateSecurityQuestion(ValidateSecretDTO validateSecretDTO) {

        String phoneNumber = utility.formatPhoneNumber(validateSecretDTO.getPhoneNumber());

        Profile profile = profileRepository.findOneByPhoneNumber(phoneNumber);

        if (profile != null) {

            if (validateSecretDTO.getOtp() == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Otp Required", null);
            }

            if (validateSecretDTO.getTrigger()){

                ResponseEntity<Boolean> responseEntity = userService.verifyOTP(Long.valueOf(validateSecretDTO.getOtp()), phoneNumber);

                if (Boolean.FALSE.equals(responseEntity.getBody())) {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Otp", null);
                }

            }

            if (validateSecretDTO.getQuestion().equals(profile.getSecretQuestion()) && validateSecretDTO.getAnswer().equals(profile.getSecretAnswer())){
                return new GenericResponseDTO("00", HttpStatus.OK, "Security Question Verified", null);
            }

            profile.setSecretQuestion(validateSecretDTO.getQuestion());
            profile.setSecretAnswer(validateSecretDTO.getAnswer());

            Profile save = save(profile);
            log.info("updated secret question/answer ====> " + save);

            User user = save.getUser();
            if (user != null) {
                utility.sendAlertEmailForAction(user, "Change Security Question", LocalDateTime.now());
            }

            return new GenericResponseDTO("00", HttpStatus.OK, "success", null);

        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User profile not found", null);

    }

    @Override
    public GenericResponseDTO validateNin(String nin, HttpSession session) {

        //TOdo call endpoint to validate Nin



//        NINRequestDTO ninRequestDTO = new NINRequestDTO();
//        NINExamplePayload ninExamplePayload = new NINExamplePayload();
//        ninExamplePayload.setValue(nin);
//        ArrayList<NINExamplePayload> customFields = new ArrayList<>();
//        customFields.add(ninExamplePayload);
//        ninRequestDTO.setCustomFields(customFields);
//
//        NINDataResponseDTO ninDataResponseDTO;

        RemitaNINRequest remitaNINRequest = new RemitaNINRequest();
        remitaNINRequest.setNumber(nin);

        try {
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                return new GenericResponseDTO("00", HttpStatus.OK, "Successful", new Gson().fromJson(validateNinResponse, NINResponseDTO.class));
            }else {

                RemitaNINResponse ninDataResponseDTO = remitaCarmelUtils.ninResponse(remitaNINRequest);
                if (ninDataResponseDTO != null) {
                    RemitaNINResponseData ninResponseDTO = ninDataResponseDTO.getData();

                    if (ninResponseDTO != null) {

                        String phoneNumber = ninResponseDTO.getTelephoneno();
                        String otp = String.valueOf(ThreadLocalRandom.current().nextLong(100000L, 900000L));
                        phoneNumber = utility.formatPhoneNumber(phoneNumber);
                        SendSMSDTO sendSMSDTO = new SendSMSDTO();
                        sendSMSDTO.setMobileNumber(phoneNumber);
                        sendSMSDTO.setSmsMessage(otpMessage + " " + otp);

                        session.getServletContext().setAttribute(nin, otp);

                        String s = externalOTPRESTClient.sendSMS(sendSMSDTO);

                        return new GenericResponseDTO("00", HttpStatus.OK, "success", s);
                    }

                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "fail", null);
                }

            }


        } catch (Exception e) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), null);
        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "validation failed", null);

    }

    @Override
    public GenericResponseDTO retrieveNINDetails(String nin, String otp, HttpSession session) {
        //Todo retrieve nin details

        String retrievedOtp = (String) session.getServletContext().getAttribute(nin);

        if (otp != null && otp.equalsIgnoreCase(retrievedOtp)) {
            try {
//                NINRequestDTO ninRequestDTO = new NINRequestDTO();
//                NINExamplePayload ninExamplePayload = new NINExamplePayload();
//                ninExamplePayload.setValue(nin);
//                ArrayList<NINExamplePayload> customFields = new ArrayList<>();
//                customFields.add(ninExamplePayload);
//                ninRequestDTO.setCustomFields(customFields);
//
//                NINDataResponseDTO ninDataResponseDTO = getNINDetails(ninRequestDTO);

                RemitaNINRequest remitaNINRequest = new RemitaNINRequest();
                remitaNINRequest.setNumber(nin);

                RemitaNINResponse ninDataResponseDTO = remitaCarmelUtils.ninResponse(remitaNINRequest);

                if (ninDataResponseDTO != null) {
                    RemitaNINResponseData ninResponseDTO = ninDataResponseDTO.getData();

                    return new GenericResponseDTO("00", HttpStatus.OK, "success", ninResponseDTO);
                } else {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Nin", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), null);
            }
        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid otp", null);

    }

    @Override
    public NINDataResponseDTO getNINDetails(NINRequestDTO ninRequestDTO) {

        String signature = new DigestUtils("SHA-512").digestAsHex(refId + authCode + secretKey);

        log.error("SECRET KEY " + secretKey);

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("publicKey", ninPublicKey);
            headers.set("X-API-SIGNATURE", signature);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            log.error("PUBLIC KEY " + ninPublicKey);

            HttpEntity<String> request =
                new HttpEntity<>(new ObjectMapper().writeValueAsString(ninRequestDTO), headers);

            NINDataResponseDTO ninDataResponseDTO =
                restTemplate.postForObject(ninUrl, request, NINDataResponseDTO.class);

            log.debug("response============================================= " + ninDataResponseDTO);

            if (ninDataResponseDTO != null) {
                log.debug("response============================================= " + ninDataResponseDTO);

                if (ninDataResponseDTO.getData() != null) {
                    log.debug("InlineStatusResponse============================================= " + ninDataResponseDTO.getData().toString());
                    if ("00".equalsIgnoreCase(ninDataResponseDTO.getResponseCode())) {
                        return ninDataResponseDTO;
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    @Override
    public ResponseEntity<PostResponseDTO> updateDeviceToken(DeviceTokenDTO deviceTokenDTO, HttpSession session) {

        String phoneNumber = (String) session.getAttribute("phoneNumber");
        System.out.println(" phoneNumber ===" + phoneNumber);
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        PostResponseDataDTO postResponseDataDTO = new PostResponseDataDTO();
        postResponseDTO.setMessage("Device Token successfully Updated");
        if (Strings.isEmpty(phoneNumber)) {
            postResponseDTO.setMessage("unable to validate user");
            postResponseDataDTO.setCode("10");
            postResponseDataDTO.setDescription("PhoneNumber cannot be empty");
            postResponseDTO.setPostResponseDataDTO(postResponseDataDTO);
            return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
        if (Strings.isEmpty(deviceTokenDTO.getDeviceToken())) {
            postResponseDTO.setMessage("unable to validate user");
            postResponseDataDTO.setCode("10");
            postResponseDataDTO.setDescription("Device token cannot be empty");
            postResponseDTO.setPostResponseDataDTO(postResponseDataDTO);
            return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
        Profile profile = findByPhoneNumber(phoneNumber);
        profile.setDeviceNotificationToken(deviceTokenDTO.getDeviceToken());
        profile = save(profile);

        postResponseDTO.setMessage("Device Token successfully created");
        postResponseDataDTO.setCode("00");
        postResponseDataDTO.setDescription("Device Token Successfully Created!");
        postResponseDTO.setPostResponseDataDTO(postResponseDataDTO);

        return new ResponseEntity<>(postResponseDTO, new HttpHeaders(), HttpStatus.OK);
    }
}
