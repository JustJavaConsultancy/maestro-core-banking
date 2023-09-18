package ng.com.systemspecs.apigateway.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.github.jhipster.web.util.HeaderUtil;
import ng.com.systemspecs.apigateway.client.ExternalOTPRESTClient;
import ng.com.systemspecs.apigateway.config.AsyncConfiguration;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.systemspecs.apigateway.domain.enumeration.*;
import ng.com.systemspecs.apigateway.repository.UserRepository;
import ng.com.systemspecs.apigateway.repository.WalletAccountRepository;
import ng.com.systemspecs.apigateway.security.jwt.JWTFilter;
import ng.com.systemspecs.apigateway.security.jwt.TokenProvider;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.accounting.AccountingService;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.systemspecs.apigateway.service.dto.stp.ExtendedConstants;
import ng.com.systemspecs.apigateway.service.exception.GenericException;
import ng.com.systemspecs.apigateway.service.fcm.PushNotificationService;
import ng.com.systemspecs.apigateway.service.kafka.producer.NotificationProducer;
import ng.com.systemspecs.apigateway.service.kafka.producer.TransProducer;
import ng.com.systemspecs.apigateway.service.mapper.AddressMapper;
import ng.com.systemspecs.apigateway.service.mapper.WalletAccountMapper;
import ng.com.systemspecs.apigateway.util.PasswordValidatorConstraint;
import ng.com.systemspecs.apigateway.util.Utility;
import ng.com.systemspecs.apigateway.web.rest.vm.ManagedUserVM;
import ng.com.systemspecs.remitabillinggateway.configuration.Credentials;
import ng.com.systemspecs.remitabillinggateway.paymentstatus.GetTransactionStatusData;
import ng.com.systemspecs.remitabillinggateway.paymentstatus.GetTransactionStatusResponse;
import ng.com.systemspecs.remitabillinggateway.service.RemitaBillingGatewayService;
import ng.com.systemspecs.remitabillinggateway.service.impl.RemitaBillingGatewayServiceImpl;
import ng.com.systemspecs.remitabillinggateway.util.EnvironmentType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.ap.internal.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static java.lang.System.out;
import static ng.com.systemspecs.apigateway.service.dto.stp.ExtendedConstants.ResponseCode.*;

/**
 * Service Implementation for managing {@link WalletAccount}.
 */
@Service
@Transactional
public class WalletAccountServiceImpl implements WalletAccountService {

    public static final String HumanManagerSchemeid = "48756d616e4d616e61676572";
    private static final String ENTITY_NAME = "walletAccount";
    private static final long Lower_Bond = 1000000000L;
    private static final long Upper_Bond = 9000000000L;
    private static final String WARD366_SCHEME = "57617264333636";

    private static final String MCPHERSON_SCHEME = "4d6350686572736f6e";
    private final Logger log = LoggerFactory.getLogger(WalletAccountServiceImpl.class);
    private final AccountingService accountingService;
    private final AsyncConfiguration asyncConfiguration;
    private final WalletAccountRepository walletAccountRepository;
    private final SchemeService schemeService;
    private final BankService bankService;
    private final BeneficiaryService beneficiaryService;
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final KyclevelService kyclevelService;
    private final AddressService addressService;
    private final AddressMapper addressMapper;
    private final WalletAccountMapper walletAccountMapper;
    private final TransProducer producer;
    private final PushNotificationService pushNotificationService;
    private final ProfileService profileService;
    private final WalletAccountTypeService walletAccountTypeService;
    private final TransactionLogService transactionLogService;
    private final Utility utility;
    private final ExternalOTPRESTClient externalOTPRESTClient;
    private final JournalLineService journalLineService;
    private final UserRepository userRepository;
    private final NotificationProducer notificationProducer;
    private final Environment environment;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    @Value("${app.sms.message.otp}")
    private String smsMessage;
    @Value("${app.constants.inline.secret-key}")
    private String secretKey;
    @Value("${app.constants.inline.inline-pmt-status-public-key}")
    private String inlinePmtStatusPublicKey;
    @Value("${app.email.subjects.send-money}")
    private String sendMoneySubject;
    @Value("${app.email.subjects.fund-wallet}")
    private String fundWalletSubject;
    @Value("${app.email.subjects.request-money}")
    private String requestMoneySubject;
    @Value("${app.email.contents.send-money}")
    private String sendMoneyContent;
    @Value("${app.email.contents.fund-wallet}")
    private String fundWalletContent;
    @Value("${app.email.contents.request-money}")
    private String requestMoneyContent;
    @Value("${app.constants.inline.remita-live-url}")
    private String remitaLiveUrl;
    @org.springframework.beans.factory.annotation.Value("${app.constants.remita.public-key}")
    private String REMITA_PUBLIC_KEY;
    private PaymentResponseDTO responseDTO;
    private boolean status = false;
    @org.springframework.beans.factory.annotation.Value("${app.constants.remita.billing.secret-key}")
    private String REMITA_BILLING_SECRET_KEY;
    private User theUser;
    @Value("${app.constants.dfs.correspondence-account}")
    private String CORRESPONDENCE_ACCOUNT;
    @Value("${app.image-url}")
    private String imageUrl;
    @Value("${app.scheme.ibile}")
    private String IBILE_SCHEME;
    @Value("${app.scheme.systemspecs}")
    private String SYSTEMSPECS_SCHEME;
    String HUMAN_MANAGER_SCHEME = "48756d616e4d616e61676572";

    public WalletAccountServiceImpl(AsyncConfiguration asyncConfiguration, WalletAccountRepository walletAccountRepository,
                                    SchemeService schemeService, BankService bankService, BeneficiaryService beneficiaryService,
                                    UserService userService, AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider,
                                    PasswordEncoder passwordEncoder, KyclevelService kyclevelService, AddressService addressService, AddressMapper addressMapper, @Lazy Utility utility,
                                    WalletAccountMapper walletAccountMapper,
                                    TransProducer producer,
                                    PushNotificationService pushNotificationService,
                                    ProfileService profileService,
                                    WalletAccountTypeService walletAccountTypeService, TransactionLogService transactionLogService, JournalLineService journalLineService,
                                    @Lazy AccountingService accountingService,
                                    ExternalOTPRESTClient externalOTPRESTClient,
                                    UserRepository userRepository, NotificationProducer notificationProducer,
                                    Environment environment) {
        this.asyncConfiguration = asyncConfiguration;
        this.walletAccountRepository = walletAccountRepository;
        this.schemeService = schemeService;
        this.bankService = bankService;
        this.beneficiaryService = beneficiaryService;
        this.userService = userService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.kyclevelService = kyclevelService;
        this.addressService = addressService;
        this.addressMapper = addressMapper;
        this.walletAccountMapper = walletAccountMapper;
        this.producer = producer;
        this.pushNotificationService = pushNotificationService;
        this.profileService = profileService;
        this.utility = utility;
        this.walletAccountTypeService = walletAccountTypeService;
        this.transactionLogService = transactionLogService;
        this.journalLineService = journalLineService;
        this.accountingService = accountingService;
        this.externalOTPRESTClient = externalOTPRESTClient;
        this.userRepository = userRepository;
        this.notificationProducer = notificationProducer;
        this.environment = environment;
    }

    public static HttpSession manageRegistrationSession(HttpSession session, RegisteredUserDTO registeredUserDTO, AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            registeredUserDTO.getPhoneNumber(), registeredUserDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = true;
        String jwt = tokenProvider.createToken(authentication, rememberMe);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        session.setAttribute("phoneNumber", registeredUserDTO.getPhoneNumber());

        session.setAttribute("jwt", jwt);
        return session;
    }

    public static void main(String[] args) {
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.get("https://www.ecampus.fuoye.edu.ng/rest/getStudentRec?matno=csc/2019/1001")
                .asString();

            System.out.println("Response ====> " + response.getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        /*RestTemplate restTemplate = new RestTemplate();

        HashMap<String, String> params = new HashMap<>();



        params.put("matno", "csc/2019/1001");

        try {
            String baseURL = "https://ecampus.fuoye.edu.ng/rest/getStudentRec?matno={matno}";

            ResponseEntity<String> responseEntity = restTemplate.exchange(baseURL, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class, params);

            System.out.println("Response ======> " + responseEntity.getBody());
        }catch (Exception e){
            System.out.println("Exception ====> " + e);
        }

*/
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) && password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH
            && password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }

    @Override
    @Transactional
    public WalletAccountDTO save(WalletAccountDTO walletAccountDTO) {
        log.debug("Request to save WalletAccount : {}", walletAccountDTO);
        WalletAccount walletAccount = walletAccountMapper.toEntity(walletAccountDTO);
        walletAccount = walletAccountRepository.save(walletAccount);
        return walletAccountMapper.toDto(walletAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WalletAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WalletAccounts");
        return walletAccountRepository.findAll(pageable).map(walletAccountMapper::toDto);
    }

    @Override
    public List<WalletAccountDTO> findAll() {
        log.debug("Request to get all WalletAccounts");
        return walletAccountRepository.findAll().stream().map(walletAccountMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<WalletAccount> findAllWalletAccounts() {
        return walletAccountRepository.findAll();
    }

    @Override
    public Page<WalletAccountDTO> findAllBySchemeAndSearchByKey(String schemeId, String searchKey, Pageable pageable) {

        List<WalletAccount> walletAccounts = findAllBySchemeIDAndAccountOwnerIsNotNullAndSearch(schemeId, searchKey);

        List<WalletAccountDTO> walletAccountDTOS =
            walletAccounts.stream().map(walletAccountMapper::toDto).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int totalSize = walletAccountDTOS.size();

        int end = Math.min((start + pageable.getPageSize()), totalSize);
        if (start > totalSize) {
            return new PageImpl<>(new ArrayList<>(), pageable, totalSize);
        }

        return new PageImpl<>((walletAccountDTOS.subList(start, end)), pageable, totalSize);

    }

    @Override
    public List<WalletAccountDTO> findByUserIsCurrentUser() {
        return walletAccountRepository.findByUserIsCurrentUser().stream().map(walletAccountMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WalletAccountDTO> findOne(Long id) {
        log.debug("Request to get WalletAccount : {}", id);
        return walletAccountRepository.findById(id).map(walletAccountMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WalletAccount : {}", id);
        walletAccountRepository.deleteById(id);
    }

    @Override
    public List<WalletAccountDTO> findByUserIsCurrentUser(HttpSession session) {
        log.debug("Request to get all WalletAccounts of login user");
        Scheme schemeFromSession = utility.getSchemeFromSession(session);
        return walletAccountRepository.findByUserIsCurrentUserScheme(schemeFromSession)
            .stream()
            .map(walletAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<WalletAccountDTO> findByUserIsCurrentUserAndScheme(HttpSession session) {
        Scheme schemeFromSession = utility.getSchemeFromSession(session);
        return walletAccountRepository.findByUserIsCurrentUserScheme(schemeFromSession).stream().map(walletAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<WalletAccountDTO> findByUserIsCurrentUserOrderByDateOpen(HttpSession session) {
        log.debug("Request to get all WalletAccounts of login user");
        Scheme schemeFromSession = utility.getSchemeFromSession(session);
        return walletAccountRepository.findByUserIsCurrentUserOrderByDateOpenedScheme(schemeFromSession).stream().map(walletAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<WalletAccountDTO> findAllByAccountOwnerPhoneNumber(String phoneNumber, HttpSession session) {

        log.debug("Request to get all WalletAccounts of a user");

        Scheme schemeFromSession = utility.getSchemeFromSession(session);

        List<WalletAccount> walletAccounts = walletAccountRepository.findAllByAccountOwnerPhoneNumberAndSchemeAndStatusNot(phoneNumber, schemeFromSession, AccountStatus.INACTIVE);

        return walletAccounts.stream().map(walletAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

    }

    @Override
    public List<WalletAccountDTO> findAllByAccountOwnerPhoneNumber(String phoneNumber) {

        log.debug("Request to get all WalletAccounts of a user");

        List<WalletAccount> walletAccounts =
            walletAccountRepository.findByAccountOwnerPhoneNumber(utility.formatPhoneNumber(phoneNumber));

        return walletAccounts.stream().map(walletAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public ResponseEntity<PaymentResponseDTO> fund(FundDTO fundDTO) {
        responseDTO = new PaymentResponseDTO();

        ValidTransactionResponse validTransaction = utility.isValidTransaction(fundDTO.getChannel(), fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber(), fundDTO.getAmount(), fundDTO.getBonusAmount(), false);
        if (validTransaction.isValid()) {

            fundDTO.setSpecificChannel(SpecificChannel.FUND_WALLET.getName());

            String phoneNumber = "";

            User currentUser = utility.getCurrentUser();
            if (currentUser == null) {
                buildPaymentResponseDTO(true, "Invalid login user");
            }
            phoneNumber = currentUser.getLogin();

            WalletAccount desAccount = findOneByAccountNumber(fundDTO.getAccountNumber());
            if (desAccount != null && desAccount.getAccountOwner() != null) {
                fundDTO.setBeneficiaryName(desAccount.getAccountOwner().getFullName());
            } else if (desAccount != null) {
                fundDTO.setBeneficiaryName(desAccount.getAccountName());
            }

            if (desAccount == null) {
                buildPaymentResponseDTO(false, "Destination Account Not Founds!");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            sendLimitNotifications(fundDTO.getAccountNumber(), fundDTO.getAmount(), phoneNumber);

            //if channel is #wallet to wallet
            if (fundDTO.getChannel().equalsIgnoreCase("wallettowallet")) {
                // and if the currentWallet balance is >= amount
                fundDTO.setSpecificChannel(SpecificChannel.FUND_WALLET_INTRA.getName());
                WalletAccount sourceAccount = findOneByAccountNumber(fundDTO.getSourceAccountNumber());
                if (sourceAccount == null) {
                    buildPaymentResponseDTO(true, "Source Account Not Found!");
                    return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
                }
                if (sourceAccount.getAccountOwner() != null) {
                    fundDTO.setSourceAccountName(sourceAccount.getAccountOwner().getFullName());
                } else {
                    fundDTO.setSourceAccountName(sourceAccount.getAccountName());
                }

                if (!CORRESPONDENCE_ACCOUNT.equalsIgnoreCase(fundDTO.getSourceAccountNumber())) {
//                        double currentBalance = Double.parseDouble(sourceAccount.getCurrentBalance());
                    Profile sourceAccountOwner = sourceAccount.getAccountOwner();
                    if (sourceAccountOwner != null) {
                        double totalBonus = sourceAccountOwner.getTotalBonus();

                        if (totalBonus < fundDTO.getBonusAmount() && fundDTO.getBonusAmount() > 0) {
                            responseDTO.setError(true);//
                            responseDTO.setCode(IN_SUFFICIENT_FUNDS.getCode());
                            responseDTO.setMessage("Insufficient Amount in the BonusPot");
                            buildPaymentResponseDTO(true, IN_SUFFICIENT_FUNDS.getDescription());
                            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
                        }

                    }

                    double actualBalance = Double.parseDouble(sourceAccount.getActualBalance());
                    if (actualBalance >= fundDTO.getAmount()) {
                        fundDTO.setPhoneNumber(phoneNumber);

                        fundDTO = transactionLogService.save(fundDTO);

                        out.println("Funddto {} ====> " + fundDTO);
                        // send FundDTO to kafka consumer
                        producer.send(fundDTO);
                        buildPaymentResponseDTO(false, "Transaction Successful!");
                        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                    } else {
                        buildPaymentResponseDTO(true, IN_SUFFICIENT_FUNDS.getDescription());
                        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    fundDTO.setPhoneNumber(phoneNumber);
                    // send FundDTO to kafka consumer
                    fundDTO = transactionLogService.save(fundDTO);

                    out.println("Funddto {} ====> " + fundDTO);
                    producer.send(fundDTO);
                    buildPaymentResponseDTO(false, "Transaction Successful!");
                }

            } else if (fundDTO.getChannel().equalsIgnoreCase("banktowallet")) {
                //get transRef
                String transRef = fundDTO.getTransRef();

                System.out.println(" Entering the banktowallet ===================");
                InlineResponseDatum inlineResponseDatum = verifyTransRef(transRef);

                //boolean isVerified = true;
                log.debug("Transaction ref " + transRef);
//                boolean isVerified = verify(transRef);

                //if success, return PaymentSuccessDTO
                if (inlineResponseDatum != null) {
                    fundDTO.setRrr(inlineResponseDatum.getPaymentReference());
                    fundDTO.setPhoneNumber(inlineResponseDatum.getPhoneNumber());
                    fundDTO.setSourceAccountName(inlineResponseDatum.getFirstName() + " " + inlineResponseDatum.getLastName());
                    // and send FundDTO to kafka consumer
                    System.out.println(" Inside the second card fundDTO========================" + fundDTO.getAccountNumber());

                    fundDTO = transactionLogService.save(fundDTO);

                    out.println("Funddto {} ====> " + fundDTO);

                    producer.send(fundDTO);
                    buildPaymentResponseDTO(false, "Funding successful");

                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                } else {
                    buildPaymentResponseDTO(true, ExtendedConstants.ResponseCode.INVALID_TRANSACTION.getDescription());
                    return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
                }
            }

            buildPaymentResponseDTO(true, "Failed!");
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }
        buildPaymentResponseDTO(true, validTransaction.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> fundWalletStp(FundDTO fundDTO) {

        ValidTransactionResponse validTransaction = utility.isValidTransaction(fundDTO.getChannel(), fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber(), fundDTO.getAmount(), fundDTO.getBonusAmount(), false);
        if (validTransaction.isValid()) {
            WalletAccount desAccount = findOneByAccountNumber(fundDTO.getAccountNumber());

            if (desAccount == null) {
                return new ResponseEntity<>(new GenericResponseDTO(CUSTOMER_ACCOUNT_NOT_FOUND.getCode(), CUSTOMER_ACCOUNT_NOT_FOUND.getDescription()), HttpStatus.BAD_REQUEST);
            }
            if (desAccount.getAccountOwner() != null) {
                fundDTO.setBeneficiaryName(desAccount.getAccountOwner().getFullName());
            } else {
                fundDTO.setBeneficiaryName(desAccount.getAccountName());
            }
            fundDTO.setSourceAccountName("STP");

            String phoneNumber = desAccount.getAccountOwner().getPhoneNumber();

            sendLimitNotifications(fundDTO.getAccountNumber(), fundDTO.getAmount(), phoneNumber);


            String transRef = fundDTO.getTransRef();
            log.debug("Transaction ref " + transRef);

            System.out.println("Entering the " + fundDTO.getChannel() + " ===================");

            System.out.println(" Inside the second card fundDTO======================== " + fundDTO.getAccountNumber());
            fundDTO= utility.sanitize(fundDTO);
            fundDTO = transactionLogService.save(fundDTO);

            out.println("Funddto {} ====> " + fundDTO);

            producer.send(fundDTO);

            return new ResponseEntity<>(new GenericResponseDTO(SUCCESS.getCode(), "Funding successful", new ArrayList<>()), HttpStatus.OK);
        }

        return new ResponseEntity<>(new GenericResponseDTO(INVALID_ACCOUNT_STATUS.getCode(), HttpStatus.BAD_REQUEST, validTransaction.getMessage(), null), HttpStatus.BAD_REQUEST);


    }

    private void sendLimitNotifications(String accountNumber, Double amount, String phoneNumber) {
        Double cumulativeBalanceLimit = 0.0;

        Profile profile = profileService.findByPhoneNumber(phoneNumber);


        if (profile != null) {
            Kyclevel kyc = profile.getKyc();
            User currentUser = profile.getUser();
            if (kyc != null) {
                cumulativeBalanceLimit = kyc.getCumulativeBalanceLimit();
            }

            //checking balance limit
            if (profileService.canAccummulateOnAccount(phoneNumber, accountNumber, amount)) {

                if (profileService.shouldSendApproachLimitNotification(phoneNumber, accountNumber, amount)) {

                    String content = "Dear " + currentUser.getFirstName() + "," +
                        "<br/>" +
                        "<br/>" +
                        "<br/>" + "You are approaching your maximum balance limit of" + utility.formatMoney(cumulativeBalanceLimit) +
                        " allowed for your wallet. Kindly upgrade your wallet to enjoy higher limits." +
                        "<br/>" +
                        "<br/>" + "The information you will need to provide for the upgrade would depend on your current account level, and could include the following: " +
                        "<br/>" +
                        "<br/>" +
                        "<ol>" +
                        "<li> Bank Verification Number (BVN) </li> " +
                        "<li> National Identity Card, Driver’s License, Voters Card or International Passport (to upgrade to tier two) </li> " +
                        "<li> Utility Bill/Proof of Address (to upgrade to tier three) </li> " +
                        "</ol>" +
                        "<br/>" +
                        "<br/>" +
                        "<br/>" + "Click <a href=\"https://mywallet.remita.net\" >here</a> to upgrade your wallet or log on to your Pouchii App." +
                        "<br/>" +
                        "<br/>" + "Click on 'More';   select ‘Verify/upgrade your wallet’ and provide the required information/documents." +
                        "<br/>" +
                        "<br/>" +
                        "<br/>" +
                        "<br/>" + "Thank you for choosing Pouchii.";

                    String sms = "Dear " + currentUser.getFirstName() + ", \n" +
                        "You have exceeded your balance limit of " + utility.formatMoney(cumulativeBalanceLimit) + ". Kindly upgrade your wallet to enjoy higher limits.";

                    String email = currentUser.getEmail();

                    utility.sendToNotificationConsumer("", "", "", "Approaching Maximum Balance".toUpperCase(), content, sms, email, phoneNumber);
                }
            } else {

                GenericResponseDTO genericResponseDTO = changeAccountStatus(accountNumber, AccountStatus.DEBIT_ON_HOLD.toString());

                if (genericResponseDTO != null && HttpStatus.OK.equals(genericResponseDTO.getStatus())) {

                    String subject = "Maximum Balance Exceeded".toUpperCase();

                    String email = currentUser.getEmail();

                    String message = "Dear " + currentUser.getFirstName() + ", \n" +

                        "You have exceeded your balance limit of " + utility.formatMoney(cumulativeBalanceLimit) + ". Kindly upgrade your wallet to enjoy higher limits.";

                    String content = "Dear " + currentUser.getFirstName() + "," +
                        "<br/>" +
                        "<br/>" +
                        "<br/>" + "You have exceeded your balance limit of " + utility.formatMoney(cumulativeBalanceLimit) + " and will not be able to transact further on your wallet until you upgrade to a higher tier." +
                        "<br/>" +
                        "<br/>" + "The information you will need to provide for the upgrade would depend on your current account level, and could include the following: " +
                        "<br/>" +
                        "<br/>" +
                        "<ol>" +
                        "<li> Bank Verification Number (BVN) </li> " +
                        "<li> National Identity Card, Driver’s License, Voters Card or International Passport (to upgrade to tier two) </li> " +
                        "<li> Utility Bill/Proof of Address (to upgrade to tier three) </li> " +
                        "</ol>" +
                        "<br/>" +
                        "<br/>" +
                        "<br/>" + "Click <a href=\"https://mywallet.remita.net\" >here</a> to upgrade your wallet or log on to your Pouchii App." +
                        "<br/>" +
                        "<br/>" + "Click on 'More';   select ‘Verify/upgrade your wallet’ and provide the required information/documents." +
                        "<br/>" +
                        "<br/>" +
                        "<br/>" +
                        "<br/>" + "Thank you for choosing Pouchii.";


                    utility.sendToNotificationConsumer("", "", "", subject, content, message, email, phoneNumber);
                }


            }
        }
    }

    @Override
    public ResponseEntity<PaymentResponseDTO> fundSTP(FundDTO fundDTO) {
        responseDTO = new PaymentResponseDTO();

        ValidTransactionResponse validTransaction = utility.isValidTransaction(fundDTO.getChannel(), fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber(), fundDTO.getAmount(), fundDTO.getBonusAmount(), false);
        if (validTransaction.isValid()) {

            fundDTO.setSpecificChannel(SpecificChannel.THIRD_PARTY.getName());

            if (fundDTO.getChannel().equalsIgnoreCase("WalletToBank")) {

                //get transRef
                String transRef = fundDTO.getTransRef();
                log.debug("Transaction ref " + transRef);

                System.out.println("Entering the " + fundDTO.getChannel() + " ===================");
                WalletAccount account = walletAccountRepository.findOneByAccountNumber(fundDTO.getSourceAccountNumber());
                String sourceAccountName = account.getAccountOwner() != null ? account.getAccountOwner().getFullName() : account.getAccountName();

                fundDTO.setSourceAccountName(sourceAccountName);

//                if (Double.parseDouble(account.getCurrentBalance()) < fundDTO.getAmount()) {
                if (Double.parseDouble(account.getActualBalance()) < fundDTO.getAmount()) {
                    responseDTO.setError(true);//
                    responseDTO.setCode(IN_SUFFICIENT_FUNDS.getCode());
                    responseDTO.setMessage(IN_SUFFICIENT_FUNDS.getDescription());

                    return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
                }
                sendLimitNotifications(fundDTO.getSourceAccountNumber(), fundDTO.getAmount(), fundDTO.getPhoneNumber());
                fundDTO.setBeneficiaryName("STP");

                fundDTO = transactionLogService.save(fundDTO);

                out.println("Funddto {} ====> " + fundDTO);

                producer.send(fundDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            } else {

                sendLimitNotifications(fundDTO.getAccountNumber(), fundDTO.getAmount(), fundDTO.getPhoneNumber());
                //if channel is #wallet to wallet
                if (fundDTO.getChannel().equalsIgnoreCase("wallettowallet")) {
                    WalletAccount desAccount = findOneByAccountNumber(fundDTO.getAccountNumber());

                    if (desAccount == null) {
                        buildPaymentResponseDTO(false, "Destination Account Not Found!");
                        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
                    }

                    // and if the currentWallet balance is >= amount
                    WalletAccount sourceAccount = findOneByAccountNumber(fundDTO.getSourceAccountNumber());
                    if (sourceAccount == null) {
                        buildPaymentResponseDTO(false, "Source Account Not Found!");
                        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
                    }

                    WalletAccount destinationAcct = findOneByAccountNumber(fundDTO.getAccountNumber());
                    if (destinationAcct == null) {
                        buildPaymentResponseDTO(false, "Destination Account Not Found!");
                        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
                    }

                    String sourceAccountName = sourceAccount.getAccountOwner() != null ? sourceAccount.getAccountOwner().getFullName() : sourceAccount.getAccountName();
                    fundDTO.setSourceAccountName(sourceAccountName);

                    String destinationName = destinationAcct.getAccountOwner() != null ? destinationAcct.getAccountOwner().getFullName() : destinationAcct.getAccountName();
                    fundDTO.setBeneficiaryName(destinationName);

//                        double currentBalance = Double.parseDouble(sourceAccount.getCurrentBalance());
                    double actualBalances = Double.parseDouble(sourceAccount.getActualBalance());
                    if (actualBalances >= fundDTO.getAmount()) {

                        fundDTO = transactionLogService.save(fundDTO);

                        out.println("Funddto {} ====> " + fundDTO);

                        // send FundDTO to kafka consumer
                        producer.send(fundDTO);

                        buildPaymentResponseDTO(false, "Transaction Successful!");
                        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                    } else {
                        buildPaymentResponseDTO(true, IN_SUFFICIENT_FUNDS.getDescription());
                        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
                    }

                } else if (fundDTO.getChannel().equalsIgnoreCase("banktowallet")) {
                    WalletAccount desAccount = findOneByAccountNumber(fundDTO.getAccountNumber());

                    if (desAccount == null) {
                        buildPaymentResponseDTO(false, "Destination Account Not Founds!");
                        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
                    }

                    fundDTO.setSourceAccountName("STP");
                    String destinationName = desAccount.getAccountOwner() != null ? desAccount.getAccountOwner().getFullName() : desAccount.getAccountName();
                    fundDTO.setBeneficiaryName(destinationName);
                    //get transRef
                    String transRef = fundDTO.getTransRef();
                    log.debug("Transaction ref " + transRef);

                    System.out.println("Entering the " + fundDTO.getChannel() + " ===================");

                    System.out.println(" Inside the second card fundDTO======================== " + fundDTO.getAccountNumber());

                    fundDTO = transactionLogService.save(fundDTO);
                    fundDTO= utility.sanitize(fundDTO);
                    out.println("Funddto {} ====> " + fundDTO);

                    producer.send(fundDTO);
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                }
            }
        }

        buildPaymentResponseDTO(true, validTransaction.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<PaymentResponseDTO> fundWalletExternal(String transRef) {

        responseDTO = new PaymentResponseDTO();

        log.info("TransRef ==> " + transRef);

        InlineResponseDatum inlineResponseDatum = verifyTransRef(transRef);

        if (inlineResponseDatum != null) {
            String accountNumber = inlineResponseDatum.getCustomerId();
            WalletAccount desAccount = findOneByAccountNumber(accountNumber);

            if (desAccount == null) {
                buildPaymentResponseDTO(true, "Destination Account Not Found!");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            String phoneNumber = inlineResponseDatum.getPhoneNumber();
            phoneNumber = utility.formatPhoneNumber(phoneNumber);


            Double amount = inlineResponseDatum.getAmount();

            sendLimitNotifications(accountNumber, amount, phoneNumber);

            Profile userProfile = profileService.findByPhoneNumber(phoneNumber);
            if (userProfile == null) {
                buildPaymentResponseDTO(true, "User not found!");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            FundDTO fundDTO = new FundDTO();

            WalletAccount destinationAcct = walletAccountRepository.findOneByAccountNumber(accountNumber);
            String destinationName = destinationAcct.getAccountOwner() != null ? destinationAcct.getAccountOwner().getFullName() : destinationAcct.getAccountName();
            fundDTO.setBeneficiaryName(destinationName);

            fundDTO.setAccountNumber(accountNumber);
            fundDTO.setAmount(amount);
            fundDTO.setChannel("BankToWallet");
            fundDTO.setTransRef(transRef);
            fundDTO.setNarration(inlineResponseDatum.getNarration());
            fundDTO.setPhoneNumber(phoneNumber);
            fundDTO.setSpecificChannel(inlineResponseDatum.getProcessorId());
            fundDTO.setRrr(inlineResponseDatum.getPaymentReference());
            fundDTO.setSourceAccountName(inlineResponseDatum.getFirstName() + " " + inlineResponseDatum.getLastName());

            ValidTransactionResponse validTransaction = utility.isValidTransaction(fundDTO.getChannel(), fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber(), fundDTO.getAmount(), fundDTO.getBonusAmount(), false);
            if (validTransaction.isValid()) {

                fundDTO = transactionLogService.save(fundDTO);

                out.println("Funddto {} ====> " + fundDTO);

                producer.send(fundDTO);
                buildPaymentResponseDTO(false, "Funding successful");

                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            } else {
                buildPaymentResponseDTO(true, validTransaction.getMessage());
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        }

        buildPaymentResponseDTO(true, "verification failed!");
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<PaymentResponseDTO> fundWalletExternalDemo(String transRef) {
        responseDTO = new PaymentResponseDTO();

        buildPaymentResponseDTO(false, "Funding successful");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }

    private void buildPaymentResponseDTO(boolean hasError, String message) {
        responseDTO.setError(hasError);
        status = hasError;
        if (hasError) {
            responseDTO.setMessage(message);
            responseDTO.setCode("Failed");
        } else {
            responseDTO.setMessage(message);
            responseDTO.setCode("success");

        }
    }

    public RemitaBillingGatewayService getRemitaBillingGatewayService() {
        Credentials credentials = new Credentials();
        credentials.setPublicKey(REMITA_PUBLIC_KEY);
//        credentials.setPublicKey("U1BFQ1NSV3w2NzIwMjQwMjI4fDE1MDE1NTg0N2JkNGNhNGE0Y2E2NDJiOTkxNGE4ZjM1ZDRlYTU0Yzc0ODVkOTdkZjMzMGE2MDcwMmJmNDQ0ZThiN2Y1ZDZhYzlmMmQ4ZmNmNGFkMDEzMTExMDhjODU4MzNiOWIyNGNiOWVlNGY4NGQ4NjUxMmUwNzMwMzk1OTE3");
        credentials.setEnvironment(EnvironmentType.LIVE);
//        credentials.setEnvironment(EnvironmentType.DEMO);
        credentials.setTransactionId(String.valueOf(ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond)));
        credentials.setSecretKey(REMITA_BILLING_SECRET_KEY);
//        credentials.setSecretKey("66e8fd084e5bf408fd369521091e54adbbcebd0ecde376cab563575d545ea7fb23785a36dc9c015d1525fae04ad779d618e8a5352171a45c4445ae352e2f00d2");
        log.debug("Transaction Id = " + credentials.getTransactionId());
        String payNotfyTransId = credentials.getTransactionId();
        return new RemitaBillingGatewayServiceImpl(credentials);
    }

    public boolean verify(String transRef) {
        log.debug("Transaction ref in verify method " + transRef);
        RemitaBillingGatewayService remitaBillingGatewayService = getRemitaBillingGatewayService();

        if (remitaBillingGatewayService != null) {
            GetTransactionStatusResponse transactionStatus = remitaBillingGatewayService.getTransactionStatus(transRef);

            log.debug("TRANSACTION STATUS " + transactionStatus);
            if (transactionStatus.getResponseCode().equalsIgnoreCase("00")) {
                GetTransactionStatusData getTransactionStatusData = transactionStatus.getResponseData().get(0);
                log.debug("TRANSACTION STATUS DATA " + getTransactionStatusData);
                return true;
            }
        }
        return false;

    }

    public boolean verifyTransaction(String transRef) {

        String hash = new DigestUtils("SHA-512").digestAsHex(transRef + secretKey);

        log.error("SECRET KEY " + secretKey);
        log.error("HASH KEY " + hash);
        log.error("SECRET KEY " + secretKey);
        log.error("HASH KEY " + hash);
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, String> params = new HashMap<String, String>();
            params.put("transId", transRef);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("publicKey", inlinePmtStatusPublicKey);
            headers.set("TXN_HASH", hash);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            log.error("PUBLIC KEY " + inlinePmtStatusPublicKey);
            log.error("REMITDEMO LIVE URL " + remitaLiveUrl);

            HttpEntity<String> entity = new HttpEntity<String>(headers);

            //HttpEntity<String> exchange = restTemplate.exchange("http://login.remita.net/payment/v1/payment/query/{transId}", HttpMethod.GET, entity, String.class, params);

            ResponseEntity<InlineStatusResponse> response = restTemplate.exchange(remitaLiveUrl, HttpMethod.GET, entity, InlineStatusResponse.class, params);
            log.debug("response============================================= " + response);
            log.debug("InlineStatusResponse============================================= " + response.getBody().toString());
           /* log.debug(response.getHeaders().toString());
            log.debug(response.toString());*/
            log.debug(params.get("transId"));

//            InlineStatusResponse body = response.getBody();
            /* log.error("INLINE VERIFY BODY " + body);*/

            if (response.getBody() != null) {
                return "00".equalsIgnoreCase(response.getBody().getResponseCode());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public Page<WalletAccountDTO> findAllByAccountOwnerIsNotNull(Pageable pageable) {
        log.debug("Request to get all valid WalletAccounts");
        return walletAccountRepository.findAllByAccountOwnerIsNotNull(pageable).map(walletAccountMapper::toDto);
    }

    @Override
    public List<WalletAccount> findAllByAccountOwnerIsNotNull() {
        return walletAccountRepository.findAll();
    }

    @Override
    public List<WalletAccountDTO> findAllByAccountOwnerIsNotNullAndSchemeAndStatusNot(String schemeId) {
        log.debug("Request to get all valid WalletAccounts");
        Scheme scheme = schemeService.findBySchemeID(schemeId);

        List<Scheme> adminSchemes = journalLineService.getAdminSchemes();
        log.info("ADMIN Scheme response ===> " + adminSchemes);

        if (adminSchemes.contains(scheme)) {
            List<WalletAccount> walletAccounts = walletAccountRepository
                .findAllByAccountOwnerIsNotNullAndSchemeAndStatusNotOrderByDateOpenedDesc(scheme, AccountStatus.INACTIVE);

            ArrayList<WalletAccount> walletAccountList = new ArrayList<>();

            for (WalletAccount walletAccount : walletAccounts) {
                if (walletAccount.getScheme().equals(scheme)) {
                    walletAccountList.add(walletAccount);
                }
            }

            return walletAccountList.stream().map(walletAccountMapper::toDto).collect(Collectors.toList());

        }
        return null;

    }

    @Override
    public List<WalletAccount> findAllByAccountOwnerIsNotNullAndScheme(String schemeId) {
        Scheme scheme = schemeService.findBySchemeID(schemeId);
        return walletAccountRepository.findAllByAccountOwnerIsNotNullAndSchemeAndStatusNotOrderByDateOpenedDesc(scheme, AccountStatus.INACTIVE);
    }

    @Override
    public Page<WalletAccountDTO> findAllByAccountOwnerIsNotNullSearchKeyword(Pageable pageable, String keyword) {
        log.debug("Request to search all valid WalletAccounts using Keyword");
        return walletAccountRepository.findAllByAccountOwnerIsNotNullSearchKeyword(pageable, keyword).map(walletAccountMapper::toDto);
    }

    @Override
    public Page<WalletAccountDTO> findAllByAccountOwnerIsNotNullSearchKeywordAndScheme(String keyword, String schemeId, Pageable pageable) {
        log.debug("Request to search all valid WalletAccounts using Keyword");
        Scheme scheme = schemeService.findBySchemeID(schemeId);
        List<Scheme> adminSchemes = journalLineService.getAdminSchemes();
        log.info("ADMIN Scheme response ===> " + adminSchemes);

        if (adminSchemes.contains(scheme)) {
            List<WalletAccount> walletAccounts =
                walletAccountRepository.findAllByAccountOwnerIsNotNullSearchKeywordAndScheme(schemeId, keyword);

            ArrayList<WalletAccount> walletAccountList = new ArrayList<>();

            for (WalletAccount walletAccount : walletAccounts) {
                if (walletAccount.getScheme().equals(scheme) && walletAccount.getWalletAccountType().getAccountypeID() != 4) {
                    walletAccountList.add(walletAccount);
                }
            }

            List<WalletAccountDTO> walletAccountDTOS = walletAccountList.stream().map(walletAccountMapper::toDto).collect(Collectors.toList());

            int start = (int) pageable.getOffset();
            int totalSize = walletAccountDTOS.size();

            int end = Math.min((start + pageable.getPageSize()), totalSize);
            if (start > totalSize) {
                return new PageImpl<>(new ArrayList<>(), pageable, totalSize);
            }

            return new PageImpl<>(walletAccountDTOS.subList(start, end), pageable, totalSize);
        }
        return null;
    }

    @Override
    public Page<WalletAccountDTO> findAllPrimaryWalletByAccountOwnerIsNotNullSearchKeywordAndScheme(String keyword, String schemeId, Pageable pageable) {
        log.debug("Request to search all primary WalletAccounts using Keyword");
        /*Scheme scheme = schemeService.findBySchemeID(schemeId);
        List<Scheme> adminSchemes = journalLineService.getAdminSchemes();*/
//        log.info("ADMIN Scheme response ===> " + adminSchemes);
//        if (adminSchemes.contains(scheme)) {
//            List<WalletAccount> walletAccounts =
//                walletAccountRepository.findAllPrimaryWalletByAccountOwnerIsNotNullSearchKeywordAndScheme(schemeId, keyword);

        List<WalletAccount> walletAccounts =
            walletAccountRepository.findAllPrimaryWalletByAccountOwnerIsNotNullSearchKeyword(keyword);

        ArrayList<WalletAccount> walletAccountList = new ArrayList<>();

        for (WalletAccount walletAccount : walletAccounts) {
//                if (walletAccount.getScheme().equals(scheme) && walletAccount.getWalletAccountType().getAccountypeID() != 4
            if (walletAccount.getWalletAccountType().getAccountypeID() != 4
                && walletAccount.getAccountName().equalsIgnoreCase(walletAccount.getAccountOwner().getUser().getFirstName())) {
                walletAccountList.add(walletAccount);
            }
        }

        List<WalletAccountDTO> walletAccountDTOS = walletAccountList.stream().map(walletAccountMapper::toDto).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int totalSize = walletAccountDTOS.size();

        int end = Math.min((start + pageable.getPageSize()), totalSize);
        if (start > totalSize) {
            return new PageImpl<>(new ArrayList<>(), pageable, totalSize);
        }

        return new PageImpl<>(walletAccountDTOS.subList(start, end), pageable, totalSize);
//        }
//        return null;
    }

    @Override
    public List<WalletAccountDTO> findAllByAccountOwnerIsNull() {
        log.debug("Request to get Special WalletAccounts");
        List<WalletAccount> wallets = walletAccountRepository.findAllByAccountOwnerIsNull();
        return walletAccountMapper.toDto(wallets);
    }

    @Override
    public List<WalletAccountDTO> findAllBySchemeAndAccountOwnerIsNull(Scheme schemeId) {
        log.debug("Request to get Special WalletAccounts by Scheme");
        List<WalletAccount> wallets = walletAccountRepository.findAllBySchemeAndAccountOwnerIsNull(schemeId);
        return walletAccountMapper.toDto(wallets);
    }

    @Override
    public List<Profile> findAllBySchemeId(String schemeId) {
        return walletAccountRepository.findAccountOwnerByScheme_SchemeID(schemeId);
    }

    @Override
    public GenericResponseDTO changeAccountStatus(String accountNumber, String status) {
        WalletAccount walletAccount = walletAccountRepository.findByAccountNumber(accountNumber.trim());
        if (walletAccount != null) {
            try {
                AccountStatus accountStatus = AccountStatus.valueOf(status.trim().toUpperCase());
                walletAccount.setStatus(accountStatus);
                WalletAccount save = walletAccountRepository.save(walletAccount);
                log.info("Updated walletAccount status " + save);

                return new GenericResponseDTO("00", HttpStatus.OK, "success", walletAccountMapper.toDto(save));
            } catch (Exception e) {
                e.getLocalizedMessage();
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid status code (" + status + ")", null);

            }

        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Account Number does not exists", null);

    }

    @Override
    public GenericResponseDTO managerUserStatus(String phoneNumber, String status, Optional<String> accountNumber) {

        try{

            String phone = utility.formatPhoneNumber(phoneNumber);
            Profile profile = profileService.findByPhoneNumber(phone);

            if (profile == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Profile with phone number:  " + phoneNumber + " not found.", null);
            }

            if (accountNumber.isPresent()){

                if (status.equalsIgnoreCase("Activate")){
                    status = "ACTIVE";
                }else if (status.equalsIgnoreCase("Deactivate")){
                    status = "INACTIVE";
                }

                return changeAccountStatus(accountNumber.get(), status);

            }

            User user = profile.getUser();

            if (user == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User with phone number:  " + phoneNumber + " not found.", null);
            }

            if (status.equalsIgnoreCase("Activate")){

                user.setActivated(true);
                user.setStatus(UserStatus.OK.getName());
                userRepository.save(user);

            }else if (status.equalsIgnoreCase("Deactivate")){

                user.setActivated(false);
                user.setStatus(UserStatus.DEACTIVATE_CUSTOMER_USER.getName());
                userRepository.save(user);

            }

            return new GenericResponseDTO("00", HttpStatus.OK, "success", user);

        }catch (Exception e){
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", e.getLocalizedMessage());
        }

    }

    @Override
    public GenericResponseDTO checkAccountStatus(String accountNumber) {
        WalletAccount walletAccount = walletAccountRepository.findOneByAccountNumber(accountNumber.trim());
        if (walletAccount != null) {
            AccountStatus status = walletAccount.getStatus();

            return new GenericResponseDTO("00", HttpStatus.OK, "success", status.name());
        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Account Number does not exists", null);
    }

    @Override
    public boolean checkAccountIsActive(String accountNumber) {
        WalletAccount walletAccount = walletAccountRepository.findOneByAccountNumber(accountNumber.trim());
        if (walletAccount != null) {
            return AccountStatus.ACTIVE.equals(walletAccount.getStatus());
        }
        return false;
    }

    @Override
    public boolean checkAccountCanBeDebited(String accountNumber, double amount) {
        if (accountNumber != null) {
            out.println("0 Inside checkAccountCanBeDebited === accountNumber=" + accountNumber+" amount=="+amount);
            WalletAccount walletAccount = walletAccountRepository.findOneByAccountNumber(accountNumber.trim());
            out.println("1 Inside checkAccountCanBeDebited === walletAccount=" + walletAccount);

            if (walletAccount != null) {
                boolean equals = AccountStatus.ACTIVE.equals(walletAccount.getStatus());
                out.println("2 Inside checkAccountCanBeDebited === equals=" + equals);
                if (equals) {
                    String limit = walletAccount.getWalletLimit();
                    out.println("3 Inside checkAccountCanBeDebited === limit=" + limit);
                    if (StringUtils.isNotEmpty(limit)) {
                        if (Double.parseDouble(limit) > 0.0) {
                            return Double.parseDouble(limit) >= amount;
                        }
                        else {
                            if(walletAccount.getAccountOwner() !=null) return Double.parseDouble(walletAccount.getCurrentBalance())>=amount &&  Double.parseDouble(walletAccount.getActualBalance())>=amount;
                            else return true;
                        }
                    }
                }
                out.println("4 Inside checkAccountCanBeDebited === returning here with equals=" + equals);
                return equals;
            }
        }
        return false;
    }

    @Override
    public boolean checkAccountCanBeCredited(String accountNumber, double amount, String sourceAccountNumber) {
        if (accountNumber != null) {
            WalletAccount walletAccount = walletAccountRepository.findOneByAccountNumber(accountNumber.trim());
            if (walletAccount != null) {
                boolean canBeCredited = AccountStatus.ACTIVE.equals(walletAccount.getStatus()) || AccountStatus.DEBIT_ON_HOLD.equals(walletAccount.getStatus());
                if (canBeCredited) {
                    String limit = walletAccount.getWalletLimit();
                    if (StringUtils.isNotEmpty(limit)) {
                        if (Double.parseDouble(limit) > 0.0) {
                            double actualBalance = Double.parseDouble(walletAccount.getActualBalance());
                            boolean check = Double.parseDouble(limit) > actualBalance + amount;
                            if (check && walletAccount.getWalletAccountType().getAccountypeID() == 5L) {
                                WalletAccount parent = walletAccount.getParent();
                                if (parent != null) {
                                    String parentAccountNumber = parent.getAccountNumber();
                                    return utility.checkStringIsValid(parentAccountNumber) && parentAccountNumber.equalsIgnoreCase(sourceAccountNumber);
                                }

                                return false;
                            }
                            return check;
                        }
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean checkAccountCanBeCredited(List<BulkBeneficiaryDTO> accountNumbers, double amount, String sourceAccountNumber) {
        if (accountNumbers.size() > 0) {
            boolean walletAccount = containsValidAccount(accountNumbers);
            if (walletAccount) {
                boolean canBeCredited = existsByStatusAndAccountNumberIn(AccountStatus.INACTIVE, accountNumbers);
                return !canBeCredited;
            }
        }
        return false;
    }

/*    @Override
    public boolean checkAccountCanBeCredited(List<String> accountNumber, double amount, String sourceAccountNumber) {
        return false;
    }*/

    @Override
    public boolean existsByStatusAndAccountNumberIn(AccountStatus status, List<BulkBeneficiaryDTO> accountNumbers) {
        List<String> accts = new ArrayList<>();
        for (BulkBeneficiaryDTO b : accountNumbers) {
            accts.add(b.getAccountNumber());
        }
        return walletAccountRepository.existsByStatusAndAccountNumberIn(status, accts);
    }

    @Override
    public Long count() {
        return walletAccountRepository.count();
    }

    @Override
    public double getActualBalance(String accountNumber) {
        WalletAccount walletAccount = findOneByAccountNumber(accountNumber.trim());
        return Double.parseDouble(walletAccount.getActualBalance());
    }

    @Override
    public ResponseEntity<GenericResponseDTO>
    createWalletExternal(WalletExternalDTO walletExternalDTO,
                         HttpSession session) {

        out.println("WalletExternalDTO ===> " + walletExternalDTO);
        GenericResponseDTO genericResponseDTO = new GenericResponseDTO();
        String phoneNumber = utility.formatPhoneNumber(walletExternalDTO.getPhoneNumber());
        walletExternalDTO.setPhoneNumber(phoneNumber);

        String password = walletExternalDTO.getPassword();
        String generatedPassword = Utility.generateSecurePassword().trim();
        String generatedPin = Utility.generateSecurePin();
        String firstName = walletExternalDTO.getFirstName();
        String user_email = walletExternalDTO.getEmail();

        if (utility.checkStringIsValid(password)) {

            if (!new PasswordValidatorConstraint().isValid(password, null)) {
                genericResponseDTO.setMessage("Password must be 8 or more characters in length.Password must contain 1 or more uppercase characters.Password must contain 1 or more lowercase characters ");
                genericResponseDTO.setCode("Failed");
                return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } else {
            password = generatedPassword;
            walletExternalDTO.setPassword(password);
        }

        if (!checkPasswordLength(password)) {
            genericResponseDTO.setMessage("Password Cannot be Empty");
            genericResponseDTO.setCode("Failed");
            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
        }

        if (!HumanManagerSchemeid.equalsIgnoreCase(walletExternalDTO.getScheme())) {
            if (utility.checkStringIsNotValid(walletExternalDTO.getLastName())) {
                genericResponseDTO.setMessage("Last name cannot be Empty");
                genericResponseDTO.setCode("Failed");
                return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
            }
        }

        if (Strings.isEmpty(walletExternalDTO.getPhoneNumber())) {
            genericResponseDTO.setMessage("Phone Number Cannot be Empty");
            genericResponseDTO.setCode("Failed");
            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
        }

        String schemeId = walletExternalDTO.getScheme();

        if (WARD366_SCHEME.equalsIgnoreCase(schemeId)) {
            walletExternalDTO.setPin(generatedPin);
        }

        Scheme bySchemeID = schemeService.findBySchemeID(schemeId);

        if (bySchemeID == null) {
            genericResponseDTO.setCode("fail");
            genericResponseDTO.setMessage("Invalid scheme");
            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
        }
        if (walletExternalDTO.getPhoneNumber().length() < 11 || walletExternalDTO.getPhoneNumber().length() > 15) {
            genericResponseDTO.setMessage("Phone number must be between 11 to 13 digits");
            genericResponseDTO.setCode("Failed");
            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
        }

        Optional<User> byLogin = userService.findByLogin(phoneNumber);
        if (!byLogin.isPresent()) {

            //CREATE PIN
            if (Strings.isEmpty(walletExternalDTO.getPin())) {
                genericResponseDTO.setMessage("User pin cannot be empty");
                genericResponseDTO.setCode("Failed");
                genericResponseDTO.setData(null);
                return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
            }

            RegisteredUserDTO registeredUserDTO = convertToRegisterUserDTO(walletExternalDTO);
            out.println("registeredUserDTO ===> " + registeredUserDTO);

            User user = null;
            try {
                user = userService.registerUser(registeredUserDTO, registeredUserDTO.getPassword());

                out.println("user ===> " + user);

            } catch (UsernameAlreadyUsedException e) {
                genericResponseDTO.setMessage("User Already exist");
                genericResponseDTO.setCode("Failed");
                genericResponseDTO.setData(null);
                return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }

            if (MCPHERSON_SCHEME.equalsIgnoreCase(walletExternalDTO.getScheme()) && walletExternalDTO.getBusinessName()!= null) {
                try {
                    Authority authority = new Authority();
                    authority.setName("ROLE_MERCHANT");
                    Set<Authority> auths = new HashSet<>();
                    auths.add(authority);
                    user.setAuthorities(auths);
                    User save = userRepository.save(user);

                    log.debug("Saved USer after ROLE " + save);

                } catch (Exception ep) {
                    ep.printStackTrace();
                }
            }

            if (!StringUtils.isEmpty(walletExternalDTO.getPhoto())) {
                try {
                    String encodedString = walletExternalDTO.getPhoto();
                    String photoName = "photo" + System.currentTimeMillis() + ".jpg";
                    String outputFileName = imageUrl + photoName;
                    byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
                    File file = new File(outputFileName);
                    log.debug("Path name = " + outputFileName);
                    log.debug("File absolute path = " + file.getAbsolutePath());
                    FileUtils.writeByteArrayToFile(file, decodedBytes);

                    user.setImageUrl(photoName);
                    User save = userRepository.save(user);

                    log.debug("Saved USer after Image " + save);

                } catch (IOException e) {
                    e.printStackTrace();
                    log.debug(e.getLocalizedMessage());
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                } catch (Exception ep) {
                    ep.printStackTrace();
                }
            }

            if (session != null) {
                session = manageRegistrationSession(session, registeredUserDTO, authenticationManagerBuilder, tokenProvider);
            }

            //CREATE PROFILE
            Profile profile = profileService.findByPhoneNumber(walletExternalDTO.getPhoneNumber());
            if (profile != null) {
                profile.setProfileID("1");
                profile.setDeviceNotificationToken(walletExternalDTO.getDeviceNotificationToken());

                String gender = walletExternalDTO.getGender();
                if (!StringUtils.isEmpty(gender)) {
                    try {
                        Gender gender1 = Gender.valueOf(gender.toUpperCase());
                        profile.setGender(gender1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                profile.setDateOfBirth(walletExternalDTO.getDateOfBirth());
                profile.setTotalBonus(0.0);

                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setAddress(walletExternalDTO.getAddress());
                addressDTO.setLatitude(walletExternalDTO.getLatitude());
                addressDTO.setLongitude(walletExternalDTO.getLongitude());
                addressDTO.setLocalGovt(walletExternalDTO.getLocalGovt());
                addressDTO.setState(walletExternalDTO.getState());
                addressDTO.setAddressType(walletExternalDTO.getAddressType());
                addressDTO.setCity(walletExternalDTO.getCity());

                profile.setAddress(walletExternalDTO.getAddress());
                profile.setNin(walletExternalDTO.getNin());

                String encryptedPin = passwordEncoder.encode(walletExternalDTO.getPin());
                profile.setPin(encryptedPin);
                profile.setProfileID("4");

                if (StringUtils.isEmpty(walletExternalDTO.getPhoto())) {
                    profile.setProfileID("2");
                }

                if (WARD366_SCHEME.equalsIgnoreCase(schemeId)) {
                    profile.setProfileID("3");
                }

                if (utility.checkStringIsValid(profile.getBvn())) {
                    profile.setKyc(kyclevelService.findByKycLevel(2));
                } else {
                    profile.setKyc(kyclevelService.findByKycLevel(1));
                }

                if (HUMAN_MANAGER_SCHEME.equalsIgnoreCase(walletExternalDTO.getScheme())) {
                    profile.setKyc(kyclevelService.findByKycLevel(31));
                }

                if (utility.checkStringIsValid(String.valueOf(walletExternalDTO.getKyc())) && walletExternalDTO.isAgent()) {
                    profile.setKyc(kyclevelService.findByKycLevel(walletExternalDTO.getKyc()));
                }

                Profile savedProfile = profileService.save(profile);

                savedProfile.setUser(user);

                log.info("SAVED PROFILE " + savedProfile);

                addressDTO.setAddressOwner(savedProfile);
                addressService.save(addressDTO);

                //CREATE WALLET
                ResponseEntity<GenericResponseDTO> walletForExternal = createWalletForExternal(walletExternalDTO, genericResponseDTO, bySchemeID, savedProfile, true);

                if (HttpStatus.OK.equals(walletForExternal.getStatusCode())) {
                    GenericResponseDTO body = walletForExternal.getBody();

                    String loginUrl = "mywallet.remita.net/ward366/login";

                    if (environment.acceptsProfiles(Profiles.of("staging", "dev"))){
                        loginUrl = "http://154.113.17.252:8085/ward366/login";
                    }

//                    List<WalletAccount> oneWalletAccount = findByPhoneNumberAndScheme(phoneNumber, WARD366_SCHEME);
                    List<WalletAccount> oneWalletAccount = (List<WalletAccount>) body.getData();
                    if (!oneWalletAccount.isEmpty()) {
                        String accountNumber = oneWalletAccount.get(0).getAccountNumber();

                        if (!walletForExternal.getStatusCode().isError() && WARD366_SCHEME.equalsIgnoreCase(schemeId)) {
                            String subject = "Ward366";
                            String smsMessage = "Dear " + firstName + ",\n \n" +
                                "A ward366 account has been created for you with the following details: \n"
                                + "Username: " + phoneNumber + "\n" + "Password: " + generatedPassword + "\n"
                                + "Pin: " + generatedPin + "\n"
                                + "Account Number: " + accountNumber + "\n \n"
                                + "Log on to " + loginUrl + " to access your wallet." +
                                "\nAfter logging in, please change your password and pin.";

                            String emailMessage = "Dear " + firstName + ",<br> <p>" +
                                "A ward366 account has been created for you with the following details: <br> <p>"
                                + "Username: " + phoneNumber + "<br>" + "Password: " + generatedPassword + "<br>"
                                + "Pin: " + generatedPin + "<br>"
                                + "Account Number: " + accountNumber + "<br> <p>"
                                + "Log on to " + loginUrl + " to access your wallet. <br>" +
                                "After logging in, please change your password and pin.";

                            utility.sendToNotificationConsumer(null, null, null, subject,
                                emailMessage, smsMessage, user_email, phoneNumber);
                        }
                    }
                }
                return walletForExternal;

            } else {
                genericResponseDTO.setMessage("Invalid profile!");
                genericResponseDTO.setCode("Failed");
                genericResponseDTO.setData(null);
                return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
            }

        }

        Optional<ProfileDTO> optionalProfile = profileService.findOneByPhoneNumber(phoneNumber);
        out.println("Process external registraion as  "+walletExternalDTO.getProcess_as());
        if (optionalProfile.isPresent()) {
            switch (walletExternalDTO.getProcess_as().toUpperCase()) {
                case "NEW":
                    genericResponseDTO.setMessage("Profile for User with Phone No. Already exist");
                    genericResponseDTO.setCode("Failed");
                    genericResponseDTO.setData(null);
                    return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);

                case "UPDATE":
                    Profile profile = profileService.findByPhoneNumber(phoneNumber);
                    out.println("Retrieved profile " + profile);
                    ResponseEntity<GenericResponseDTO> walletForExternal = createWalletForExternal(walletExternalDTO, genericResponseDTO, bySchemeID, profile, true);
                    return walletForExternal;
            }
        }
        genericResponseDTO.setMessage("Service Error: Creating Wallet");
        genericResponseDTO.setCode("Failed");
        genericResponseDTO.setData(null);
        return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> createCorporateWalletExternal(WalletExternalDTO walletExternalDTO,
                                                                            HttpSession session) {
        out.println("WalletExternalDTO ===> " + walletExternalDTO);

        GenericResponseDTO genericResponseDTO = new GenericResponseDTO();
        ResponseCorpDTO responseCorpDTO = new ResponseCorpDTO();
        User user = null;
        CorporateProfile corporate = null;
        String phoneNumber = utility.formatPhoneNumber(walletExternalDTO.getPhoneNumber());
        walletExternalDTO.setPhoneNumber(phoneNumber);

        if (Strings.isEmpty(walletExternalDTO.getPhoneNumber())) {
            genericResponseDTO.setMessage("Phone Number Cannot be Empty");
            genericResponseDTO.setCode("Failed");
            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
        }

        String schemeId = walletExternalDTO.getScheme();
        Scheme bySchemeID = schemeService.findBySchemeID(schemeId);

        if (bySchemeID == null) {
            genericResponseDTO.setCode("fail");
            genericResponseDTO.setMessage("Invalid scheme");
            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
        }

        if (walletExternalDTO.getPhoneNumber().length() < 11 || walletExternalDTO.getPhoneNumber().length() > 15) {
            genericResponseDTO.setMessage("Phone number must be between 11 to 13 digits");
            genericResponseDTO.setCode("Failed");
            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
        }

        if (!checkPasswordLength(walletExternalDTO.getPassword())) {
            genericResponseDTO.setMessage("Password Cannot be Empty");
            genericResponseDTO.setCode("Failed");
            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
        }

        Optional<User> byLogin = userService.findByLogin(phoneNumber);

        if (!byLogin.isPresent()) {
            //CREATE PIN
            if (Strings.isEmpty(walletExternalDTO.getPin())) {
                genericResponseDTO.setMessage("User pin cannot be empty");
                genericResponseDTO.setCode("Failed");
                genericResponseDTO.setData(null);
                return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
            }

            String firstName = walletExternalDTO.getFirstName();
            String lastName = walletExternalDTO.getLastName();

            if (utility.checkStringIsNotValid(lastName)) {
                if (utility.checkStringIsValid(firstName)) {
                    String trim = firstName.trim();
                    int indexOfSpace = trim.indexOf(" ");
                    if (indexOfSpace > 0) {
                        firstName = trim.substring(0, indexOfSpace).trim();
                        lastName = trim.substring(indexOfSpace).trim();
                        walletExternalDTO.setFirstName(firstName);
                        walletExternalDTO.setLastName(lastName);
                    }
                }
            }

            RegisteredCorporateUserDTO registeredUserDTO = convertToRegisterCorporateUserDTO(walletExternalDTO);
            out.println("registeredUserDTO ===> " + registeredUserDTO);

            try {
                genericResponseDTO = userService.registerCorporateUserExternal(registeredUserDTO, registeredUserDTO.getPassword());
                if (genericResponseDTO.getCode().equals("99")) {
                    return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                }
                CreateCorporate createCorporate = (CreateCorporate) genericResponseDTO.getData();
                user = createCorporate.getUser();
                corporate = createCorporate.getCorporateProfile();
                out.println("user ===> " + user);
            } catch (UsernameAlreadyUsedException e) {
                genericResponseDTO.setMessage("User Already exist");
                genericResponseDTO.setCode("Failed");
                genericResponseDTO.setData(null);
                return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }

            if (!StringUtils.isEmpty(walletExternalDTO.getPhoto())) {
                try {
                    String encodedString = walletExternalDTO.getPhoto();
                    String photoName = "photo" + System.currentTimeMillis() + ".jpg";
                    String outputFileName = imageUrl + photoName;
                    byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
                    File file = new File(outputFileName);
                    log.debug("Path name = " + outputFileName);
                    log.debug("File absolute path = " + file.getAbsolutePath());
                    FileUtils.writeByteArrayToFile(file, decodedBytes);

                    user.setImageUrl(photoName);
                    User save = userRepository.save(user);

                    log.debug("Saved USer after Image " + save);

                } catch (IOException e) {
                    e.printStackTrace();
                    log.debug(e.getLocalizedMessage());
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                } catch (Exception ep) {
                    ep.printStackTrace();
                }
            }

            if (session != null) {
                session = manageRegistrationSession(session, registeredUserDTO, authenticationManagerBuilder, tokenProvider);
            }
            //CREATE PROFILE
            Profile profile = profileService.findByPhoneNumber(walletExternalDTO.getPhoneNumber());
            if (profile != null) {
                profile.setProfileID("1");
                profile.setDeviceNotificationToken(walletExternalDTO.getDeviceNotificationToken());

                String gender = walletExternalDTO.getGender();
                if (!StringUtils.isEmpty(gender)) {
                    try {
                        Gender gender1 = Gender.valueOf(gender.toUpperCase());
                        profile.setGender(gender1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                profile.setDateOfBirth(walletExternalDTO.getDateOfBirth());
                profile.setTotalBonus(0.0);

                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setAddress(walletExternalDTO.getAddress());
                addressDTO.setLatitude(walletExternalDTO.getLatitude());
                addressDTO.setLongitude(walletExternalDTO.getLongitude());
                addressDTO.setLocalGovt(walletExternalDTO.getLocalGovt());
                addressDTO.setState(walletExternalDTO.getState());
                addressDTO.setAddressType(walletExternalDTO.getAddressType());
                addressDTO.setCity(walletExternalDTO.getCity());

                profile.setAddress(walletExternalDTO.getAddress());
                profile.setNin(walletExternalDTO.getNin());

                String encryptedPin = passwordEncoder.encode(walletExternalDTO.getPin());
                profile.setPin(encryptedPin);
                profile.setProfileID("4");

                if (StringUtils.isEmpty(walletExternalDTO.getPhoto())) {
                    profile.setProfileID("2");
                }

                if (utility.checkStringIsValid(profile.getBvn())) {
                    profile.setKyc(kyclevelService.findByKycLevel(2));
                } else {
                    profile.setKyc(kyclevelService.findByKycLevel(1));
                }
                if (HUMAN_MANAGER_SCHEME.equalsIgnoreCase(walletExternalDTO.getScheme())) {
                    profile.setKyc(kyclevelService.findByKycLevel(31));
                }

                if (utility.checkStringIsValid(String.valueOf(walletExternalDTO.getKyc())) && walletExternalDTO.isAgent()) {
                    profile.setKyc(kyclevelService.findByKycLevel(walletExternalDTO.getKyc()));
                }

                Profile savedProfile = profileService.save(profile);
                savedProfile.setUser(user);

                log.info("SAVED PROFILE " + savedProfile);

                addressDTO.setAddressOwner(savedProfile);
                addressService.save(addressDTO);

                ResponseEntity<GenericResponseDTO> walletCreatedResponse = createWalletForExternal(walletExternalDTO, genericResponseDTO, bySchemeID, profile, true);
                if (walletCreatedResponse.getStatusCode() == HttpStatus.OK) {
                    GenericResponseDTO g = walletCreatedResponse.getBody();
                    List<WalletAccountDTO> createdWallet = (List<WalletAccountDTO>) g.getData();
                    responseCorpDTO.setWalletAccount(createdWallet);
                    responseCorpDTO.setCorporateProfile(corporate);
                    walletCreatedResponse.getBody().setData(responseCorpDTO);
                }
                return walletCreatedResponse;
            } else {
                genericResponseDTO.setMessage("Invalid profile!");
                genericResponseDTO.setCode("Failed");
                genericResponseDTO.setData(null);
                return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
            }
        }

        Profile profile = profileService.findByPhoneNumber(phoneNumber);
        out.println("Retrieved profile " + profile);
        ResponseEntity<GenericResponseDTO> walletCreatedResponse = createWalletForExternal(walletExternalDTO, genericResponseDTO, bySchemeID, profile, true);
        if (walletCreatedResponse.getStatusCode() == HttpStatus.OK) {
            GenericResponseDTO g = walletCreatedResponse.getBody();
            List<WalletAccountDTO> createdWallet = (List<WalletAccountDTO>) g.getData();
            responseCorpDTO.setWalletAccount(createdWallet);
            responseCorpDTO.setCorporateProfile(corporate);
            walletCreatedResponse.getBody().setData(responseCorpDTO);
        }
        return walletCreatedResponse;
    }


    @Override
    public List<WalletAccount> findAllByAccountNumberAndAccountNameAndSchemeId(String accountNumber, String accountName, Long schemeId) {
        return walletAccountRepository.findAllByAccountNumberAndAccountNameAndSchemeId(accountNumber, accountName, schemeId);
    }

    @Override
    public List<WalletAccount> findAllBySchemeIDAndAccountOwnerIsNotNullAndSearch(String schemeId, String key) {
        Scheme scheme = schemeService.findBySchemeID(schemeId);
        return walletAccountRepository.findAllByAccountOwnerIsNotNullSearchKeywordAndSchemeWithoutPage(scheme.getSchemeID(), key);
    }

    @Override
    public Optional<WalletAccount> findOneByAccountOwnerAndBonusPointAccountType(String profilePhoneNumber) {
        return walletAccountRepository.findByAccountOwner_PhoneNumberAndWalletAccountType_AccountypeID(profilePhoneNumber, 4L);
    }

    @Override
    public WalletAccount getCustomerBonusAccount(Profile profile) {

        Optional<WalletAccount> bonusPointAccountType = findOneByAccountOwnerAndBonusPointAccountType(profile.getPhoneNumber());
        return bonusPointAccountType.orElseGet(() -> createBonusPointWalletAccount(profile));

    }

    @Override
    public ResponseEntity<GenericResponseDTO> addSpecialAccount(String accountName, String accountNumber) throws URISyntaxException {

        if (!utility.checkStringIsValid(accountName, accountNumber)) {
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid parameter"), HttpStatus.BAD_REQUEST);
        }

        WalletAccount account = findOneByAccountNumber(accountNumber);
        if (account != null) {
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Wallet account already exists"), HttpStatus.BAD_REQUEST);

        }

        WalletAccountDTO accountDTO = new WalletAccountDTO();
        accountDTO.setAccountNumber(accountNumber);
        accountDTO.setAccountName(accountName);
        accountDTO.setDateOpened(LocalDate.now());
        accountDTO.setCurrentBalance(0.00);
        accountDTO.setSchemeId(1L);
        accountDTO.setActualBalance(accountDTO.getCurrentBalance());
        accountDTO.setStatus(AccountStatus.ACTIVE);
        accountDTO.setWalletAccountTypeId(3L);
        WalletAccountDTO result = null;
        result = save(accountDTO);

        return ResponseEntity
            .created(new URI("/api/wallet-accounts/" + result.getId())).headers(HeaderUtil
                .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(new GenericResponseDTO("00", HttpStatus.CREATED, "success", result));
    }

    @Override
    public ResponseEntity<GenericResponseDTO> addSpecialAccount(String accountName, String accountNumber, String scheme) throws URISyntaxException {

        Scheme scheme1 = schemeService.findBySchemeID(scheme);

        if (!utility.checkStringIsValid(accountName, accountNumber)) {
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid parameter"), HttpStatus.BAD_REQUEST);
        }

        WalletAccount account = findOneByAccountNumber(accountNumber);
        if (account != null) {
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Wallet account already exists"), HttpStatus.BAD_REQUEST);

        }

        WalletAccountDTO accountDTO = new WalletAccountDTO();
        accountDTO.setAccountNumber(accountNumber);
        accountDTO.setAccountName(accountName);
        accountDTO.setDateOpened(LocalDate.now());
        accountDTO.setCurrentBalance(0.00);
//        accountDTO.setSchemeId(1L);
        accountDTO.setActualBalance(accountDTO.getCurrentBalance());
        accountDTO.setStatus(AccountStatus.ACTIVE);
        accountDTO.setWalletAccountTypeId(3L);
        accountDTO.setSchemeId(scheme1.getId());
        accountDTO.setSchemeName(scheme1.getScheme());
        WalletAccountDTO result = null;
        result = save(accountDTO);

        return ResponseEntity
            .created(new URI("/api/wallet-accounts/" + result.getId())).headers(HeaderUtil
                .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(new GenericResponseDTO("00", HttpStatus.CREATED, "success", result));
    }
    @Override
    public ResponseEntity<GenericResponseDTO> renameSpecialAccount(String accountName, String accountNumber) {

        if (!utility.checkStringIsValid(accountName, accountNumber)) {
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid parameter"), HttpStatus.BAD_REQUEST);
        }

        WalletAccount account = findOneByAccountNumber(accountNumber);
        if (account == null) {
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Wallet account cannot be found"), HttpStatus.BAD_REQUEST);
        }

        account.setAccountName(accountName);
        WalletAccount save = save(account);

        log.info("Rename wallet Account ===> " + save);

        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", save), HttpStatus.OK);

    }

    @Override
    public List<WalletAccount> findAllByTrackingRefNotNull() {
        return walletAccountRepository.findAllByTrackingRefNotNull();
    }

    @Override
    public List<WalletAccount> findAllByTrackingRefIsNotNullAndNubanAccountNoIsNull() {
        return walletAccountRepository.findAllByTrackingRefIsNotNullAndNubanAccountNoIsNull();
    }

    @Override
    public List<WalletAccount> findByNubanAccountNo(String nubanAccountNo) {

        return walletAccountRepository.findByNubanAccountNo(nubanAccountNo);
    }

    @Override
    public List<WalletAccountDTO> findAgentPrimaryAccount(String phoneNumber, Long accountTypeId) {

        log.debug("Request to get all WalletAccounts of a user");

        return walletAccountRepository
            .findByAccountOwner_PhoneNumberAndWalletAccountTypeAccountypeID(
                utility.formatPhoneNumber(phoneNumber),
                accountTypeId)
            .stream().map(walletAccountMapper::toDto)
            .collect(Collectors.toList());

    }

    private WalletAccount createBonusPointWalletAccount(Profile profile) {
        Long accountNumber = utility.getUniqueAccountNumber();

        Optional<WalletAccountTypeDTO> byAccountTypeId = walletAccountTypeService.findByAccountTypeId(4L);

        WalletAccountDTO walletAccountDTO = new WalletAccountDTO();
        walletAccountDTO.setAccountNumber(String.valueOf(accountNumber));
        walletAccountDTO.setAccountOwnerPhoneNumber(profile.getPhoneNumber());
        walletAccountDTO.setAccountOwnerId(profile.getId());
        walletAccountDTO.setAccountName(profile.getUser().getFirstName() + " - Bonus Point");
        walletAccountDTO.setDateOpened(LocalDate.now());
        walletAccountDTO.setCurrentBalance(0.0);
        walletAccountDTO.setActualBalance(0.0);
        walletAccountDTO.setSchemeId(1L);
        walletAccountDTO.setStatus(AccountStatus.ACTIVE);
        byAccountTypeId.ifPresent(walletAccountTypeDTO -> walletAccountDTO.setWalletAccountTypeId(walletAccountTypeDTO.getId()));

        WalletAccountDTO save = save(walletAccountDTO);

        System.out.println("Newly created bonusPointWalletAccount ===> " + save);

        return walletAccountMapper.toEntity(save);

    }

    public ResponseEntity<GenericResponseDTO> createWalletForExternal(@RequestBody @Valid WalletExternalDTO walletExternalDTO, GenericResponseDTO genericResponseDTO, Scheme bySchemeID, Profile profile, boolean createWallet) {
        Long accountNumber = utility.getUniqueAccountNumber();
        String phoneNumber = utility.formatPhoneNumber(walletExternalDTO.getPhoneNumber());

        WalletAccountDTO walletAccountDTO = new WalletAccountDTO();
        walletAccountDTO.setAccountNumber(String.valueOf(accountNumber));
        walletAccountDTO.setAccountOwnerPhoneNumber(phoneNumber);
        walletAccountDTO.setAccountOwnerId(profile.getId());
        walletAccountDTO.setAccountName(walletExternalDTO.getAccountName());
        walletAccountDTO.setDateOpened(LocalDate.now());
        walletAccountDTO.setCurrentBalance(walletExternalDTO.getOpeningBalance());
        walletAccountDTO.setActualBalance(walletAccountDTO.getCurrentBalance());
        walletAccountDTO.setSchemeId(bySchemeID.getId());
        walletAccountDTO.setWalletAccountTypeId(1L);
        if (walletExternalDTO.getWalletAccountTypeId() != null && walletExternalDTO.getWalletAccountTypeId() > 1) {
            walletAccountDTO.setWalletAccountTypeId(walletExternalDTO.getWalletAccountTypeId());
        }
        walletAccountDTO.setStatus(AccountStatus.ACTIVE);
        walletAccountDTO.setWalletLimit(String.valueOf(walletExternalDTO.getWalletLimit()));

        WalletAccountDTO wallet = null;

        try {
            List<WalletAccount> accountOptional = findByAccountOwnerPhoneNumberAndAccountNameAndScheme_SchemeID(phoneNumber, walletAccountDTO.getAccountName(), bySchemeID.getSchemeID());
            if (!accountOptional.isEmpty()) {
                WalletAccount walletAccount = accountOptional.get(0);
                log.info("Wallet found " + walletAccount);
                walletAccountMapper.toDto(walletAccount);
                genericResponseDTO.setCode("failed");
                genericResponseDTO.setMessage("You already have a wallet with the same name(" + walletExternalDTO.getAccountName() + ")");
                genericResponseDTO.setStatus(HttpStatus.BAD_REQUEST);
                genericResponseDTO.setData("You already have a wallet with the same name(" + walletExternalDTO.getAccountName() + ")");
                genericResponseDTO.setMetadata(new HashMap<>());

                return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);

            } else {
                wallet = save(walletAccountDTO);

                if (utility.checkStringIsNotValid(wallet.getTrackingRef()) || utility.checkStringIsNotValid(wallet.getNubanAccountNo())) {

                    Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
                    if (asyncExecutor != null) {
                        asyncExecutor.execute(() -> {
                            try {
                                out.println("Creating NUBAN account for new User ===> ");
                                utility.createPolarisAccount(phoneNumber, bySchemeID.getSchemeID());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            genericResponseDTO.setCode("failed");
            genericResponseDTO.setStatus(HttpStatus.BAD_REQUEST);
            genericResponseDTO.setMessage("You already have a wallet with the same name(" + walletExternalDTO.getAccountName() + ")");
            genericResponseDTO.setData(e.getLocalizedMessage());
            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
        }

        genericResponseDTO.setCode("success");
        genericResponseDTO.setMessage("Wallet created successfully");
        List<WalletAccount> save = new ArrayList<>();
        save.add(walletAccountMapper.toEntity(wallet));

        genericResponseDTO.setData(save);

        return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
    }

    @Override
    public List<WalletAccount> findAllByNubanAccountNoIsNull() {
        return walletAccountRepository.findAllByNubanAccountNoIsNull();
    }

    @Override
    public List<WalletAccount> findAllByTrackingRefIsNull() {
        return walletAccountRepository.findAllByTrackingRefIsNull();
    }

    public InlineResponseDatum verifyTransRef(String transRef) {

        InlineResponseDatum inlineResponseDatum = null;

        String hash = new DigestUtils("SHA-512").digestAsHex(transRef.trim() + secretKey);

        log.error("SECRET KEY " + secretKey);
        log.error("HASH KEY " + hash);
        log.error("SECRET KEY " + secretKey);
        log.error("HASH KEY " + hash);
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, String> params = new HashMap<String, String>();
            params.put("transId", transRef.trim());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("publicKey", inlinePmtStatusPublicKey);
            headers.set("TXN_HASH", hash);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            log.error("PUBLIC KEY " + inlinePmtStatusPublicKey);
            log.error("REMITDEMO LIVE URL " + remitaLiveUrl);

            HttpEntity<String> entity = new HttpEntity<String>(headers);

            //HttpEntity<String> exchange = restTemplate.exchange("http://login.remita.net/payment/v1/payment/query/{transId}", HttpMethod.GET, entity, String.class, params);


            ResponseEntity<InlineStatusResponse> response = restTemplate.exchange(remitaLiveUrl, HttpMethod.GET, entity, InlineStatusResponse.class, params);
            log.debug("response============================================= " + response);
            log.debug("response============================================= " + new ObjectMapper().writeValueAsString(response.getBody()));
            log.debug("InlineStatusResponse============================================= " + response.getBody().toString());
           /* log.debug(response.getHeaders().toString());
            log.debug(response.toString());*/
            log.debug(params.get("transId"));

//            InlineStatusResponse body = response.getBody();
            /* log.error("INLINE VERIFY BODY " + body);*/

            if (response.getBody() != null && "00".equalsIgnoreCase(response.getBody().getResponseCode()) && response.getBody().getResponseData() != null) {
                List<InlineResponseDatum> responseData = response.getBody().getResponseData();
                if (responseData != null && responseData.get(0).getPaymentState().equalsIgnoreCase("SUCCESSFUL")) {
                    if (!responseData.isEmpty() && responseData.get(0) != null) {
                        inlineResponseDatum = responseData.get(0);
                    }
                } else {
                    if (responseData.get(0) != null) {
                        String accountNumber = responseData.get(0).getCustomerId();
                        Double amount = responseData.get(0).getAmount();
                        WalletAccount desAccount = findOneByAccountNumber(accountNumber);

                        String phoneNumber = responseData.get(0).getPhoneNumber();
                        phoneNumber = utility.formatPhoneNumber(phoneNumber);

                        String destinationName = desAccount != null && desAccount.getAccountOwner() != null ? desAccount.getAccountOwner().getFullName() : desAccount.getAccountName();

                        FundDTO fundDTO = new FundDTO();
                        fundDTO.setAmount(amount);
                        fundDTO.setChannel("BankToWallet");
                        fundDTO.setTransRef(transRef);
                        fundDTO.setNarration(responseData.get(0).getNarration());
                        fundDTO.setPhoneNumber(phoneNumber);
                        fundDTO.setSpecificChannel(responseData.get(0).getProcessorId());
                        fundDTO.setRrr(responseData.get(0).getPaymentReference());
                        fundDTO.setStatus(TransactionStatus.INCOMPLETE);
                        fundDTO.setBeneficiaryName(destinationName);
                        fundDTO.setSourceAccountName(responseData.get(0).getFirstName() + " " + responseData.get(0).getLastName());

                        transactionLogService.save(fundDTO);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return inlineResponseDatum;
    }

    @Override
    public PaymentResponseDTO sendMoney(FundDTO sendMoneyDTO) {

        PaymentResponseDTO responseDTO = new PaymentResponseDTO();

        if (sendMoneyDTO.getAmount() <= 0.0) {
            responseDTO.setError(true);//
            responseDTO.setCode(INVALID_AMOUNT.getCode());
            responseDTO.setMessage(INVALID_AMOUNT.getDescription());
            return responseDTO;
        }

        if (!utility.checkStringIsValid(sendMoneyDTO.getAccountNumber())) {
            String phoneNumber = utility.formatPhoneNumber(sendMoneyDTO.getPhoneNumber());
            List<WalletAccount> walletAccounts = findByAccountOwnerPhoneNumber(phoneNumber);
            if (walletAccounts.isEmpty()) {
                responseDTO.setError(true);//
                responseDTO.setCode("failed");
                responseDTO.setMessage("Invalid Phone Number");
                return responseDTO;
            }
            WalletAccount walletAccount = walletAccounts.get(0);
            sendMoneyDTO.setAccountNumber(walletAccount.getAccountNumber());
        }
        boolean isNotKycTransaction = false;

        if (sendMoneyDTO.getRrr() != null) {
            isNotKycTransaction = sendMoneyDTO.getRrr().contains("fee");
        }

        ValidTransactionResponse validTransaction = utility.isValidTransaction(sendMoneyDTO.getChannel(),
            sendMoneyDTO.getSourceAccountNumber(), sendMoneyDTO.getAccountNumber(), sendMoneyDTO.getAmount(),
            sendMoneyDTO.getBonusAmount(), isNotKycTransaction);
        if (validTransaction.isValid()) {
            if(StringUtils.isBlank(sendMoneyDTO.getSpecificChannel())) {
                sendMoneyDTO.setSpecificChannel(SpecificChannel.SEND_MONEY_INTRA.getName());
            }
            try {
                log.info("SEND MONEY DTO " + new ObjectMapper().writeValueAsString(sendMoneyDTO));
//                if(!StringUtils.isBlank(sendMoneyDTO.getSpecificChannel())) {
//                    if (sendMoneyDTO.getRrr().equalsIgnoreCase("MCU")) {
//                        sendMoneyDTO.setSpecificChannel(SpecificChannel.WALLENCY_SCHOOLS_FEES_PAYMENT.getName());
//                    }
//                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            out.println("SEND MONEY DTO after Specific channel set ====>  " + sendMoneyDTO);
            //PaymentTransactionDTO paymentTransactionDTO = new PaymentTransactionDTO();
            WalletAccount account = walletAccountRepository.findOneByAccountNumber(sendMoneyDTO.getSourceAccountNumber());

            if (account == null) {
                responseDTO.setError(true);//
                responseDTO.setCode("failed");
                responseDTO.setMessage("Source account not found!");
                return responseDTO;
            }

            Double charges = 0.0;
            if ("WalletToBank".equalsIgnoreCase(sendMoneyDTO.getChannel())) {
                User currentUser = utility.getCurrentUser();
                if (currentUser != null) {
                    sendMoneyDTO.setPhoneNumber(currentUser.getLogin());
                }
                charges = accountingService.getTransactionFee(sendMoneyDTO.getAmount(),
                    SpecificChannel.SEND_MONEY.getName(), null);
            }

            Profile sourceAccountOwner = null;
            sourceAccountOwner = account.getAccountOwner();

            if (sourceAccountOwner != null) {
                double totalBonus = sourceAccountOwner.getTotalBonus();

                if (totalBonus < sendMoneyDTO.getBonusAmount() && sendMoneyDTO.getBonusAmount() > 0) {
                    responseDTO.setError(true);//
                    responseDTO.setCode(IN_SUFFICIENT_FUNDS.getCode());
                    responseDTO.setMessage("Insufficient Amount in the BonusPot");
                    return responseDTO;
                }
            }

            if (Double.parseDouble(account.getActualBalance()) < sendMoneyDTO.getAmount() + charges) {
                responseDTO.setError(true);//
                responseDTO.setCode(IN_SUFFICIENT_FUNDS.getCode());
                responseDTO.setMessage(IN_SUFFICIENT_FUNDS.getDescription());
                return responseDTO;
            }

            if (sendMoneyDTO.isToBeSaved()) {

                Optional<Beneficiary> beneficiaryOptional;
                if (sourceAccountOwner != null) {
                    if (sendMoneyDTO.isWalletAccount()) {

                        beneficiaryOptional = beneficiaryService.findByAccountOwnerAndAccountNumber(sourceAccountOwner, sendMoneyDTO.getAccountNumber());
                    } else {
                        beneficiaryOptional = beneficiaryService.findByAccountOwnerAndBankCode(sourceAccountOwner, sendMoneyDTO.getAccountNumber(), sendMoneyDTO.getDestBankCode());
                    }
                    if (!beneficiaryOptional.isPresent()) {
                        Beneficiary beneficiary = new Beneficiary();

                        beneficiary.setAccountOwner(sourceAccountOwner);

                        beneficiary.setAccountNumber(sendMoneyDTO.getAccountNumber());
                        beneficiary.setName(sendMoneyDTO.getBeneficiaryName());
                        beneficiary.setBankCode(sendMoneyDTO.getDestBankCode());

                        if (!beneficiary.isWallet()) {
                            String destBankCode = sendMoneyDTO.getDestBankCode();
                            if (!StringUtils.isEmpty(destBankCode)) {

                                Optional<Bank> bankDTOOptional = bankService.findByBankCode(destBankCode);
                                bankDTOOptional.ifPresent(bankDTO -> beneficiary.setBankName(bankDTO.getBankName()));
                            }
                        } else {
                            beneficiary.setWallet(sendMoneyDTO.isWalletAccount());
                            beneficiary.setBankName("Pouchii");
                        }

                        Beneficiary save = beneficiaryService.save(beneficiary);
                        log.info("SAVED BENEFICIARY ==> " + save);
                    }
                }
            }

            if ("wallettobank".equalsIgnoreCase(sendMoneyDTO.getChannel())) {
                sendMoneyDTO.setSpecificChannel(SpecificChannel.SEND_MONEY.getName());
                String sourceAccountOwnerName = sourceAccountOwner != null ? sourceAccountOwner.getFullName() : account.getAccountName();

                sendMoneyDTO.setSourceAccountName(sourceAccountOwnerName);

                sendMoneyDTO = transactionLogService.save(sendMoneyDTO);

                out.println("Funddto {} ====> " + sendMoneyDTO);

                producer.send(sendMoneyDTO);

                responseDTO.setCode("00");
                responseDTO.setError(false);//
                responseDTO.setMessage("Fund Successfully Sent for processing");

                if (account.getAccountOwner() != null) {
                    Profile accountOwner = account.getAccountOwner();
                    PushNotificationRequest request = new PushNotificationRequest();
                    request.setMessage("You have sent the sum of ₦" + sendMoneyDTO.getAmount() + " to account number : " + sendMoneyDTO.getAccountNumber());
                    request.setTitle("Send money");
                    request.setToken(accountOwner.getDeviceNotificationToken());
//                    pushNotificationService.sendPushNotificationToToken(request);

                    String phoneNumber = "";

                    User currentUser = utility.getCurrentUser();
                    if (currentUser != null) {
                        phoneNumber = currentUser.getLogin();
                    }


                }
                return responseDTO;
            }

            WalletAccount destinationAccount = null;

            destinationAccount = walletAccountRepository.findOneByAccountNumber(sendMoneyDTO.getAccountNumber());


            if (destinationAccount == null) {
                responseDTO.setError(true);//
                responseDTO.setCode("failed");
                responseDTO.setMessage("destination account not found!");
                return responseDTO;
            }

            Profile destinationAccountOwner = null;
            destinationAccountOwner = destinationAccount.getAccountOwner();
            String destinationAccountName = destinationAccountOwner != null ? destinationAccountOwner.getFullName() : destinationAccount.getAccountName();
            sendMoneyDTO.setBeneficiaryName(destinationAccountName);

            String sourceAccountName = sourceAccountOwner != null ? sourceAccountOwner.getFullName() : account.getAccountName();
            sendMoneyDTO.setSourceAccountName(sourceAccountName);

            responseDTO.setCode("00");
            responseDTO.setMessage("Fund Successfully Sent");

            sendMoneyDTO = transactionLogService.save(sendMoneyDTO);

            out.println("Funddto {} ====> " + sendMoneyDTO);

            producer.send(sendMoneyDTO);

            if (sourceAccountOwner != null && destinationAccountOwner != null) {
                PushNotificationRequest request = new PushNotificationRequest();
                request.setMessage("The Sum of ₦" + sendMoneyDTO.getAmount() + " has been sent to you from " + sourceAccountOwner.getFullName());
                request.setTitle("Send Money");
                request.setToken(destinationAccountOwner.getDeviceNotificationToken());
//                pushNotificationService.sendPushNotificationToToken(request);

            }

            String phoneNumber = "";

            User currentUser = utility.getCurrentUser();
            if (currentUser != null) {
                phoneNumber = currentUser.getLogin();
            }


            return responseDTO;
        }
        responseDTO.setError(true);//
        responseDTO.setCode("failed");
        responseDTO.setMessage(validTransaction.getMessage());
        return responseDTO;
    }

    @Override
    public PaymentResponseDTO sendMoneyBulkTransaction(FundDTO sendMoneyDTO) {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO();

        if (sendMoneyDTO.getAmount() <= 0.0) {
            responseDTO.setError(true);//
            responseDTO.setCode(INVALID_AMOUNT.getCode());
            responseDTO.setMessage(INVALID_AMOUNT.getDescription());
            return responseDTO;
        }

        boolean isNotKycTransaction = false;

        if (sendMoneyDTO.getRrr() != null) {
            isNotKycTransaction = sendMoneyDTO.getRrr().contains("fee");
        }

        ValidTransactionResponse validTransaction = utility.isValidTransaction(sendMoneyDTO.getChannel(),
            sendMoneyDTO.getSourceAccountNumber(), sendMoneyDTO.getBulkAccountNos(), sendMoneyDTO.getAmount(),
            sendMoneyDTO.getBonusAmount(), isNotKycTransaction);
        if (validTransaction.isValid()) {
            if (utility.checkStringIsNotValid(sendMoneyDTO.getSpecificChannel())) {
                sendMoneyDTO.setSpecificChannel(SpecificChannel.SEND_MONEY_INTRA.getName());
                if ("walletToBanks".equalsIgnoreCase(sendMoneyDTO.getChannel())) {
                    sendMoneyDTO.setSpecificChannel(SpecificChannel.SEND_MONEY_TO_BANKS.getName());
                }
            }
            try {
                log.info("SEND MONEY DTO " + new ObjectMapper().writeValueAsString(sendMoneyDTO));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            WalletAccount account = walletAccountRepository.findOneByAccountNumber(sendMoneyDTO.getSourceAccountNumber());

            if (account == null) {
                responseDTO.setError(true);//
                responseDTO.setCode("failed");
                responseDTO.setMessage("Source account not found!");
                return responseDTO;
            }

            Double charges = 0.0;
/*            if ("WalletToBank".equalsIgnoreCase(sendMoneyDTO.getChannel())) {
                User currentUser = utility.getCurrentUser();
                if (currentUser != null) {
                    sendMoneyDTO.setPhoneNumber(currentUser.getLogin());
                }
                charges = accountingService.getTransactionFee(sendMoneyDTO.getAmount(), SpecificChannel.SEND_MONEY.getName());
            }*/

            Profile sourceAccountOwner = null;
            sourceAccountOwner = account.getAccountOwner();

            if (sourceAccountOwner != null) {
                double totalBonus = sourceAccountOwner.getTotalBonus();

                if (totalBonus < sendMoneyDTO.getBonusAmount() && sendMoneyDTO.getBonusAmount() > 0) {
                    responseDTO.setError(true);//
                    responseDTO.setCode(IN_SUFFICIENT_FUNDS.getCode());
                    responseDTO.setMessage("Insufficient Amount in the BonusPot");
                    return responseDTO;
                }
            }

            if (Double.parseDouble(account.getActualBalance()) < sendMoneyDTO.getAmount() + charges) {
                responseDTO.setError(true);//
                responseDTO.setCode(IN_SUFFICIENT_FUNDS.getCode());
                responseDTO.setMessage(IN_SUFFICIENT_FUNDS.getDescription());
                return responseDTO;
            }

        /*    if (sendMoneyDTO.isToBeSaved()) {

                Optional<Beneficiary> beneficiaryOptional;
                if (sourceAccountOwner != null) {
                    if (sendMoneyDTO.isWalletAccount()) {

                        beneficiaryOptional = beneficiaryService.findByAccountOwnerAndAccountNumber(sourceAccountOwner, sendMoneyDTO.getAccountNumber());
                    } else {
                        beneficiaryOptional = beneficiaryService.findByAccountOwnerAndBankCode(sourceAccountOwner, sendMoneyDTO.getAccountNumber(), sendMoneyDTO.getDestBankCode());
                    }
                    if (!beneficiaryOptional.isPresent()) {
                        Beneficiary beneficiary = new Beneficiary();

                        beneficiary.setAccountOwner(sourceAccountOwner);

                        beneficiary.setAccountNumber(sendMoneyDTO.getAccountNumber());
                        beneficiary.setName(sendMoneyDTO.getBeneficiaryName());
                        beneficiary.setBankCode(sendMoneyDTO.getDestBankCode());

                        if (!beneficiary.isWallet()) {
                            String destBankCode = sendMoneyDTO.getDestBankCode();
                            if (!StringUtils.isEmpty(destBankCode)) {

                                Optional<Bank> bankDTOOptional = bankService.findByBankCode(destBankCode);
                                bankDTOOptional.ifPresent(bankDTO -> beneficiary.setBankName(bankDTO.getBankName()));
                            }
                        } else {
                            beneficiary.setWallet(sendMoneyDTO.isWalletAccount());
                            beneficiary.setBankName("Pouchii");
                        }

                        Beneficiary save = beneficiaryService.save(beneficiary);
                        log.info("SAVED BENEFICIARY ==> " + save);
                    }
                }
            }*/

      /*      if ("wallettobank".equalsIgnoreCase(sendMoneyDTO.getChannel())) {
                sendMoneyDTO.setSpecificChannel(SpecificChannel.SEND_MONEY.getName());
                String sourceAccountOwnerName = sourceAccountOwner != null ? sourceAccountOwner.getFullName() : account.getAccountName();

                sendMoneyDTO.setSourceAccountName(sourceAccountOwnerName);

                producer.send(sendMoneyDTO);

                responseDTO.setCode("00");
                responseDTO.setError(false);//
                responseDTO.setMessage("Fund Successfully Sent for processing");

                if (account.getAccountOwner() != null) {
                    Profile accountOwner = account.getAccountOwner();
                    PushNotificationRequest request = new PushNotificationRequest();
                    request.setMessage("You have sent the sum of ₦" + sendMoneyDTO.getAmount() + " to account number : " + sendMoneyDTO.getAccountNumber());
                    request.setTitle("Send money");
                    request.setToken(accountOwner.getDeviceNotificationToken());
//                    pushNotificationService.sendPushNotificationToToken(request);

                    String phoneNumber = "";

                    User currentUser = utility.getCurrentUser();
                    if (currentUser != null) {
                        phoneNumber = currentUser.getLogin();
                    }


                }
                return responseDTO;
            }*/

//            WalletAccount destinationAccount = null;

//            if (utility.checkStringIsValid(sendMoneyDTO.getAccountNumber())) {
//            destinationAccount = walletAccountRepository.findOneByAccountNumber(sendMoneyDTO.getAccountNumber());
//            }

            /*else {
                String phoneNumber = utility.formatPhoneNumber(sendMoneyDTO.getPhoneNumber());

                List<WalletAccount> walletAccountList = findByAccountOwnerPhoneNumber(phoneNumber);
                if (!walletAccountList.isEmpty()) {
                    destinationAccount = walletAccountList.get(0);
                }
            }*/

/*            if (destinationAccount == null) {
                responseDTO.setError(true);//
                responseDTO.setCode("failed");
                responseDTO.setMessage("destination account not found!");
                return responseDTO;
            }*/

/*            Profile destinationAccountOwner = null;
            destinationAccountOwner = destinationAccount.getAccountOwner();
            String destinationAccountName = destinationAccountOwner != null ? destinationAccountOwner.getFullName() : destinationAccount.getAccountName();
            sendMoneyDTO.setBeneficiaryName(destinationAccountName);
*/
            String sourceAccountName = sourceAccountOwner != null ? sourceAccountOwner.getFullName() : account.getAccountName();
            sendMoneyDTO.setSourceAccountName(sourceAccountName);

            responseDTO.setCode("00");
            responseDTO.setMessage("Fund Successfully Sent");

            producer.send(sendMoneyDTO);

            return responseDTO;
        }
        responseDTO.setError(true);//
        responseDTO.setCode("failed");
        responseDTO.setMessage(validTransaction.getMessage());
        return responseDTO;
    }

    @Override
    public PaymentResponseDTO sendBulkTransaction(FundDTO sendBulkMoneyDTO) {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO();

        Double amount = sendBulkMoneyDTO.getAmount();
        if (amount <= 0.0) {
            responseDTO.setError(true);//
            responseDTO.setCode(INVALID_AMOUNT.getCode());
            responseDTO.setMessage(INVALID_AMOUNT.getDescription());
            return responseDTO;
        }
        double sumOfSalary = sendBulkMoneyDTO.getBulkAccountNos().stream().mapToDouble(BulkBeneficiaryDTO::getAmount).sum();

        if (amount != sumOfSalary) {
            responseDTO.setError(true);//
            responseDTO.setCode("99");
            responseDTO.setMessage("Amount must be equal to the sum of beneficiaries salary");
            return responseDTO;
        }

        boolean isNotKycTransaction = false;

        if (sendBulkMoneyDTO.getRrr() != null) {
            isNotKycTransaction = sendBulkMoneyDTO.getRrr().contains("fee");
        }

        ValidTransactionResponse validTransaction = utility.isValidTransaction(sendBulkMoneyDTO.getChannel(),
            sendBulkMoneyDTO.getSourceAccountNumber(), sendBulkMoneyDTO.getBulkAccountNos(), amount,
            sendBulkMoneyDTO.getBonusAmount(), isNotKycTransaction);
        if (validTransaction.isValid()) {
            if (utility.checkStringIsNotValid(sendBulkMoneyDTO.getSpecificChannel())) {
                sendBulkMoneyDTO.setSpecificChannel(SpecificChannel.FUND_WALLET_INTRA.getName());
                if ("walletToBanks".equalsIgnoreCase(sendBulkMoneyDTO.getChannel())) {
                    if (utility.checkStringIsValid(sendBulkMoneyDTO.getSpecificChannel())) {
                        sendBulkMoneyDTO.setSpecificChannel(SpecificChannel.HM_PAYROLL.getName());
                    }
                }
            }
            try {
                log.info("SEND MONEY DTO " + new ObjectMapper().writeValueAsString(sendBulkMoneyDTO));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            WalletAccount sourceAccountNumber = walletAccountRepository.findOneByAccountNumber(sendBulkMoneyDTO.getSourceAccountNumber());

            if (sourceAccountNumber == null) {
                responseDTO.setError(true);//
                responseDTO.setCode("failed");
                responseDTO.setMessage("Source account not found!");
                return responseDTO;
            }

            Double charges = 0.0;
            if ("WalletToBanks".equalsIgnoreCase(sendBulkMoneyDTO.getChannel())) {
                User currentUser = utility.getCurrentUser();
                if (currentUser != null) {
                    sendBulkMoneyDTO.setPhoneNumber(currentUser.getLogin());
                }
                charges = accountingService.getTransactionFee(amount, SpecificChannel.SEND_MONEY.getName(), sendBulkMoneyDTO);
            }

            Profile sourceAccountOwner = null;
            sourceAccountOwner = sourceAccountNumber.getAccountOwner();

            if (sourceAccountOwner != null) {
                double totalBonus = sourceAccountOwner.getTotalBonus();

                if (totalBonus < sendBulkMoneyDTO.getBonusAmount() && sendBulkMoneyDTO.getBonusAmount() > 0) {
                    responseDTO.setError(true);//
                    responseDTO.setCode(IN_SUFFICIENT_FUNDS.getCode());
                    responseDTO.setMessage("Insufficient Amount in the BonusPot");
                    return responseDTO;
                }
            }

            if (Double.parseDouble(sourceAccountNumber.getActualBalance()) < amount + charges) {
                responseDTO.setError(true);//
                responseDTO.setCode(IN_SUFFICIENT_FUNDS.getCode());
                responseDTO.setMessage(IN_SUFFICIENT_FUNDS.getDescription());
                return responseDTO;
            }

            String sourceAccountName = sourceAccountOwner != null ? sourceAccountOwner.getFullName() : sourceAccountNumber.getAccountName();
            sendBulkMoneyDTO.setSourceAccountName(sourceAccountName);

            producer.send(sendBulkMoneyDTO);
            responseDTO.setCode("00");
            responseDTO.setMessage("Fund Successfully Sent");
            return responseDTO;
        }
        responseDTO.setError(true);//
        responseDTO.setCode("failed");
        responseDTO.setMessage(validTransaction.getMessage());
        return responseDTO;
    }

    @Override
    public PaymentResponseDTO sendMoneyWithCorrespondence(String sourceAccountNumber, String accountNumber, double amount) {

        if (amount <= 0.0) {
            responseDTO.setError(true);//
            responseDTO.setCode(INVALID_AMOUNT.getCode());
            responseDTO.setMessage(INVALID_AMOUNT.getDescription());
            return responseDTO;
        }

        FundDTO fundDTO = new FundDTO();
        fundDTO.setAccountNumber(accountNumber);
        fundDTO.setSourceAccountNumber(sourceAccountNumber);


        fundDTO.setSpecificChannel(SpecificChannel.SEND_MONEY_INTRA.getName());
        fundDTO.setAmount(amount);

        PaymentResponseDTO responseDTO = new PaymentResponseDTO();

        //PaymentTransactionDTO paymentTransactionDTO = new PaymentTransactionDTO();
        WalletAccount destinationAccount = walletAccountRepository.findOneByAccountNumber(accountNumber);

        WalletAccount sourceAccount = walletAccountRepository.findOneByAccountNumber(sourceAccountNumber);

        if (destinationAccount == null) {
            responseDTO.setError(true);//
            responseDTO.setCode("failed");
            responseDTO.setMessage("destination account not found!");
            return responseDTO;
        }

        if (sourceAccount == null) {
            responseDTO.setError(true);//
            responseDTO.setCode("failed");
            responseDTO.setMessage("Source account not found!");
            return responseDTO;
        }

        Profile destinationAccountOwner = destinationAccount.getAccountOwner();
        String destinationName = destinationAccount.getAccountOwner() != null ? destinationAccount.getAccountOwner().getFullName() : destinationAccount.getAccountName();
        fundDTO.setBeneficiaryName(destinationName);

        Profile sourceAccountOwner = sourceAccount.getAccountOwner();
        String sourceAccountName = sourceAccount.getAccountOwner() != null ? sourceAccount.getAccountOwner().getFullName() : sourceAccount.getAccountName();
        fundDTO.setSourceAccountName(sourceAccountName);

        fundDTO.setNarration("Transfer of " + fundDTO.getAmount() + " from " + sourceAccountName + " to " + destinationName);

        if ("Correspondent Account ".equalsIgnoreCase(sourceAccountName)) {
            fundDTO.setNarration("Funding of wallet (" + accountNumber + ") with the sum of " + utility.formatMoney(fundDTO.getAmount()));
        } else if ("Correspondent Account ".equalsIgnoreCase(destinationName)) {
            fundDTO.setNarration("Deduction of the sum of " + utility.formatMoney(fundDTO.getAmount()) + " from wallet (" + accountNumber + ")");
        } else {
            fundDTO.setNarration("Transfer of the sum of " + utility.formatMoney(fundDTO.getAmount()) + " from wallet (" + sourceAccountNumber + ") to wallet (" + accountNumber + ").");
        }

        if (sourceAccountOwner != null) {
            fundDTO.setPhoneNumber(sourceAccountOwner.getPhoneNumber());
        } else if (destinationAccountOwner != null) {
            fundDTO.setPhoneNumber(destinationAccountOwner.getPhoneNumber());
        }

        fundDTO.setChannel("walletToWallet");
        fundDTO.setTransRef(utility.getUniqueTransRef());
        fundDTO = utility.sanitize(fundDTO);
        responseDTO.setCode("00");
        responseDTO.setMessage("Fund Successfully Sent");

        fundDTO = transactionLogService.save(fundDTO);

        out.println("Funddto {} ====> " + fundDTO);

        producer.send(fundDTO);

        if (destinationAccountOwner != null) {
            PushNotificationRequest request = new PushNotificationRequest();
            request.setMessage("The Sum of ₦" + fundDTO.getAmount() + " has been credited into your wallet (" + fundDTO.getAmount() + ")");
            request.setTitle("Send Money");
            request.setToken(destinationAccountOwner.getDeviceNotificationToken());
//            pushNotificationService.sendPushNotificationToToken(request);

        }
        return responseDTO;
    }

    @Override
    public PaymentResponseDTO sendMoneyWithCorrespondence(String sourceAccountNumber, String accountNumber,
                                                          double amount, String narration) {

        if (amount <= 0.0) {
            responseDTO.setError(true);//
            responseDTO.setCode(INVALID_AMOUNT.getCode());
            responseDTO.setMessage(INVALID_AMOUNT.getDescription());
            return responseDTO;
        }

        FundDTO fundDTO = new FundDTO();
        fundDTO.setAccountNumber(accountNumber);
        fundDTO.setSourceAccountNumber(sourceAccountNumber);


        fundDTO.setSpecificChannel(SpecificChannel.SEND_MONEY_INTRA.getName());
        fundDTO.setAmount(amount);

        PaymentResponseDTO responseDTO = new PaymentResponseDTO();

        //PaymentTransactionDTO paymentTransactionDTO = new PaymentTransactionDTO();
        WalletAccount destinationAccount = walletAccountRepository.findOneByAccountNumber(accountNumber);

        WalletAccount sourceAccount = walletAccountRepository.findOneByAccountNumber(sourceAccountNumber);

        if (destinationAccount == null) {
            responseDTO.setError(true);//
            responseDTO.setCode("failed");
            responseDTO.setMessage("destination account not found!");
            return responseDTO;
        }

        if (sourceAccount == null) {
            responseDTO.setError(true);//
            responseDTO.setCode("failed");
            responseDTO.setMessage("Source account not found!");
            return responseDTO;
        }

        Profile destinationAccountOwner = destinationAccount.getAccountOwner();
        String destinationName = destinationAccount.getAccountOwner() != null ? destinationAccount.getAccountOwner().getFullName() : destinationAccount.getAccountName();
        fundDTO.setBeneficiaryName(destinationName);

        Profile sourceAccountOwner = sourceAccount.getAccountOwner();
        String sourceAccountName = sourceAccount.getAccountOwner() != null ? sourceAccount.getAccountOwner().getFullName() : sourceAccount.getAccountName();
        fundDTO.setSourceAccountName(sourceAccountName);

        fundDTO.setNarration(narration);

        if ("Correspondent Account ".equalsIgnoreCase(sourceAccountName)) {
            fundDTO.setNarration("Funding of wallet (" + accountNumber + ") with the sum of " + fundDTO.getAmount());
        } else if ("Correspondent Account ".equalsIgnoreCase(destinationName)) {
            fundDTO.setNarration("Deduction of the sum of " + fundDTO.getAmount() + " from wallet (" + accountNumber + ")");
        }

        if (sourceAccountOwner != null) {
            fundDTO.setPhoneNumber(sourceAccountOwner.getPhoneNumber());
        } else if (destinationAccountOwner != null) {
            fundDTO.setPhoneNumber(destinationAccountOwner.getPhoneNumber());
        }

        fundDTO.setChannel("walletToWallet");
        fundDTO.setTransRef(utility.getUniqueTransRef());

        responseDTO.setCode("00");
        responseDTO.setMessage("Fund Successfully Sent");

        fundDTO = transactionLogService.save(fundDTO);

        out.println("Funddto {} ====> " + fundDTO);

        producer.send(fundDTO);

        if (destinationAccountOwner != null) {
            PushNotificationRequest request = new PushNotificationRequest();
            request.setMessage("The Sum of ₦" + fundDTO.getAmount() + " has been credited into your wallet (" + fundDTO.getAmount() + ")");
            request.setTitle("Send Money");
            request.setToken(destinationAccountOwner.getDeviceNotificationToken());
        }
        return responseDTO;
    }

    @Override
    public PaymentResponseDTO sendMoneyToBankWithCorrespondence(FundDTO fundDTO) {

        if (fundDTO.getAmount() <= 0.0) {
            responseDTO.setError(true);//
            responseDTO.setCode(INVALID_AMOUNT.getCode());
            responseDTO.setMessage(INVALID_AMOUNT.getDescription());
            return responseDTO;
        }

        ValidTransactionResponse validTransaction = utility.isValidTransaction(fundDTO.getChannel(),
            fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber(), fundDTO.getAmount(),
            fundDTO.getBonusAmount(), false);
        if (!validTransaction.isValid()) {
            throw new GenericException("Error processing funding: {}" + validTransaction.getMessage());
        }

        fundDTO.setSpecificChannel(SpecificChannel.SEND_MONEY.getName());
        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
        WalletAccount sourceAccount = walletAccountRepository.findOneByAccountNumber(fundDTO.getSourceAccountNumber());
        if (sourceAccount == null) {
            responseDTO.setError(true);//
            responseDTO.setCode("failed");
            responseDTO.setMessage("Source account not found!");
            return responseDTO;
        }
        String sourceAccountName = sourceAccount.getAccountOwner() != null ? sourceAccount.getAccountOwner().getFullName() : sourceAccount.getAccountName();
        fundDTO.setSourceAccountName(sourceAccountName);
        fundDTO.setChannel("walletToBank");
        fundDTO.setTransRef(utility.getUniqueTransRef());
        responseDTO.setCode("00");
        responseDTO.setMessage("Fund Successfully Sent");
        fundDTO = transactionLogService.save(fundDTO);
        out.println("Funddto {} ====> " + fundDTO);
        producer.send(fundDTO);
        return responseDTO;
    }


    @Override
    public List<WalletAccount> findByPhoneNumberAndScheme(String phoneNumber, String scheme) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        return walletAccountRepository.findAllByAccountOwnerPhoneNumberAndSchemeSchemeID(phoneNumber, scheme);
    }

    @Override
    public PaymentResponseDTO requestMoney(FundDTO requestMoneyDTO) {

        if (requestMoneyDTO.getAmount() <= 0.0) {
            responseDTO.setError(true);//
            responseDTO.setCode(INVALID_AMOUNT.getCode());
            responseDTO.setMessage(INVALID_AMOUNT.getDescription());
            return responseDTO;
        }

        PaymentResponseDTO responseDTO = new PaymentResponseDTO();

        requestMoneyDTO.setSpecificChannel(SpecificChannel.REQUEST_MONEY.getName());
        String requesterName = null;

        this.theUser = utility.getCurrentUser();

        WalletAccount requesterAccount = walletAccountRepository.findOneByAccountNumber(requestMoneyDTO.getAccountNumber());
        if (requesterAccount == null) {
            responseDTO.setCode("022");
            responseDTO.setError(true);
            responseDTO.setMessage("The Owner of the Request is Unknown!!");
            return responseDTO;
        }

        Profile requesterProfile = requesterAccount.getAccountOwner();

        if (requesterProfile == null) {
            responseDTO.setCode("022");
            responseDTO.setError(true);
            responseDTO.setMessage("Invalid profile");
            return responseDTO;
        }
        requesterName = requesterProfile.getFullName();

       /* WalletAccount requestAccount = walletAccountRepository.findOneByAccountNumber(requestMoneyDTO.getSourceAccountNumber());

        if (requestAccount == null) {
            responseDTO.setCode("022");
            responseDTO.setError(true);
            responseDTO.setMessage("Source Account Owner Unknown!");
            return responseDTO;
        }
        Profile requestProfile = requestAccount.getAccountOwner();

        if (requestProfile == null) {
            responseDTO.setCode("022");
            responseDTO.setError(true);
            responseDTO.setMessage("Invalid source account profile");
            return responseDTO;
        }*/

        WalletAccount sender = null;

        if (utility.checkStringIsNotValid(requestMoneyDTO.getPhoneNumber(), requestMoneyDTO.getAccountNumber())) {
            responseDTO.setCode("023");
            responseDTO.setError(true);
            responseDTO.setMessage("Beneficiary Account not set !!");
            return responseDTO;
        }

        //Request using phone Number...
        if (utility.checkStringIsValid(requestMoneyDTO.getPhoneNumber())) {
            List<WalletAccount> destinationAccounts =
                walletAccountRepository.findByAccountOwnerPhoneNumber(requestMoneyDTO.getPhoneNumber());
            if (destinationAccounts.isEmpty()) {
                responseDTO.setCode("022");
                responseDTO.setError(true);
                responseDTO.setMessage("The Funding Wallet Owner Has No Wallet !!");
                return responseDTO;
            }
            sender = destinationAccounts.get(0);
            requestMoneyDTO.setSourceAccountNumber(sender.getAccountNumber());
        } else {
            sender = walletAccountRepository.findOneByAccountNumber(requestMoneyDTO.getSourceAccountNumber());
        }

        System.out.println(" The full requestMoneyDTO ===" + requestMoneyDTO);
        System.out.println(" destinationAccount=====" + sender.getAccountNumber() + "requestMoneyDTO.getAccountNumber()  ");

        if (sender == null) {
            responseDTO.setCode("fail");
            responseDTO.setError(true);
            responseDTO.setMessage("Invalid sender account number");
            return responseDTO;
        }

        if (sender.getAccountNumber().equals(requestMoneyDTO.getAccountNumber())) {
            responseDTO.setCode("fail");
            responseDTO.setError(true);
            responseDTO.setMessage("You cannot send to self");
            return responseDTO;
        }

        if (sender.getAccountOwner().equals(requesterProfile)) {
            responseDTO.setCode("fail");
            responseDTO.setError(true);
            responseDTO.setMessage("You cannot request from self");
            return responseDTO;
        }
        System.out.println("  Account Number sent=== " + requestMoneyDTO.getSourceAccountNumber() + "  destinationAccount=== " + sender);

        responseDTO.setCode("00");
        responseDTO.setError(false);
        responseDTO.setMessage("Money Successfully Requested");
        Profile profile = sender.getAccountOwner();
        PushNotificationRequest request = new PushNotificationRequest();
        String senderMessageContent = requesterProfile.getFullName() + " has requested a sum of " + utility.formatMoney(requestMoneyDTO.getAmount()) + " from you";
        if (requesterName != null) {
            System.out.println(" The loginPhoneNumber user name here ==============================" + requesterName);
            request.setMessage(senderMessageContent);
        }
//        request.setMessage("The Sum of " + requestMoneyDTO.getAmount() + " is requested from " + destinationAccount.getAccountOwner().getUser().getLastName());
        request.setTitle("Request Money");
        request.setToken(profile.getDeviceNotificationToken());

        System.out.println(" The full request for money at this stage /n/n/n/n/n====" + requestMoneyDTO);

        ValidTransactionResponse validTransaction = utility.isValidTransaction(requestMoneyDTO.getChannel(), requestMoneyDTO.getSourceAccountNumber(), requestMoneyDTO.getAccountNumber(), requestMoneyDTO.getAmount(), requestMoneyDTO.getBonusAmount(), true);
        if (validTransaction.isValid()) {
            requestMoneyDTO.setBeneficiaryName(requesterName);

            String senderAccountName = sender.getAccountOwner() != null ? sender.getAccountOwner().getFullName() : sender.getAccountName();
            requestMoneyDTO.setSourceAccountName(senderAccountName);

            producer.send(requestMoneyDTO);

            HashMap<String, Object> mapValue = new HashMap<String, Object>();
            /*
             * pushNotificationService.sendPushNotificationToToken(request);
             * if (theUser != null) { utility.sendEmail(theUser.getEmail(),
             * requestMoneySubject, requestMoneyContent + " " + profile.getFullName()); }
             * utility.sendEmail("akinrinde@justjava.com.ng", requestMoneySubject,
             * requestMoneyContent + " " + profile.getFullName());
             *
             * SendSMSDTO sendSMSDTO = new SendSMSDTO();
             * sendSMSDTO.setSmsMessage("The Sum of N" + requestMoneyDTO.getAmount() +
             * " is requested from " + requesterName);
             * sendSMSDTO.setMobileNumber(profile.getPhoneNumber());
             *
             */

            LocalDateTime transDateTime = LocalDateTime.now();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mma");
            String transDate = transDateTime.format(dateFormatter);
            String transTime = transDateTime.format(timeFormatter);

            String msg =
                "Date : " + transDate
                    + "<br/>"
                    + "Time : " + transTime
                    + "<p>"
                    + "<p>"
                    + "<p>";

            String senderEmailContent = "Dear " + profile.getUser().getFirstName() + "," +
                "<br/>" +
                "<br/>" +
                msg +
                senderMessageContent;

            mapValue.put("title", request.getTitle());
            mapValue.put("token", request.getToken());
            mapValue.put("deviceMessage", request.getMessage());
            mapValue.put("request", request);

            mapValue.put("subject", requestMoneySubject);
            mapValue.put("content", senderEmailContent);
            mapValue.put("email", profile.getUser().getEmail());


            mapValue.put("sms", senderMessageContent);
            mapValue.put("phoneNumber", profile.getPhoneNumber());

            notificationProducer.send(mapValue);

            String message = requestMoneyContent + utility.formatMoney(requestMoneyDTO.getAmount()) + " from " + profile.getFullName();

            String emailContent = "Dear " + requesterProfile.getUser().getFirstName() + "," +
                "<br/>" +
                "<br/>" +
                msg +
                message;

            String phoneNumber = requesterProfile.getPhoneNumber();
            String email = requesterProfile.getUser().getEmail();

            utility.sendToNotificationConsumer(requesterProfile.getDeviceNotificationToken(), message, "Request money", requestMoneySubject, emailContent, message, email, phoneNumber);

            return responseDTO;
        }

        responseDTO.setCode("fail");
        responseDTO.setError(true);
        responseDTO.setMessage(validTransaction.getMessage());
        return responseDTO;

    }

    @Override
    public WalletAccount findOneByAccountNumber(String accountNumber) {
        return walletAccountRepository.
            findOneByAccountNumber(accountNumber);
    }

    @Override
    public WalletAccount findByAccountNumber(String accountNumber) {
        return walletAccountRepository.
            findByAccountNumber(accountNumber);
    }

    @Override
    public WalletAccount save(WalletAccount walletAccount) {
        return walletAccountRepository.save(walletAccount);
    }

    @Override
    public List<WalletAccount> saveAll(List<WalletAccount> walletAccounts) {
        return walletAccountRepository.saveAll(walletAccounts);
    }

    @Override
    public List<WalletAccount> findByAccountOwnerPhoneNumber(String phoneNumber) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        return walletAccountRepository.findByAccountOwnerPhoneNumber(phoneNumber);
    }

    @Override
    public List<WalletAccount> findAgentWallet(String phoneNumber, Long accountTypeId) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        return walletAccountRepository.findByAccountOwnerPhoneNumberAndStatusNotAndWalletAccountType_AccountypeID(phoneNumber, AccountStatus.INACTIVE, accountTypeId);
    }

    @Override
    public List<WalletAccount> findSubAgentWalletsBySuperAgentPhoneNumber(String superAgentPhoneNumber, Long accountTypeId) {
        List<WalletAccount> superAgentWalletAccounts = findAgentWallet(superAgentPhoneNumber, 2L);

        log.debug("SuperAgent Wallet list=== " + superAgentWalletAccounts);
        if (!superAgentWalletAccounts.isEmpty()) {
            WalletAccount walletAccount = superAgentWalletAccounts.get(0);
            System.out.println("Super Agent Wallet ==> " + walletAccount);

            List<WalletAccount> subWallets = walletAccountRepository.findAllByParentAndWalletAccountType_AccountypeID(walletAccount, accountTypeId);
            System.out.println("Sub Wallets List ==> " + subWallets);
//            return new ArrayList<>(walletAccount.getSubWallets());
            return new ArrayList<>(subWallets);
        }

        return null;
    }

    @Override
    public Optional<WalletAccount> findByAccountOwnerPhoneNumberAndAccountName(String phoneNumber, String accountName) {
        return walletAccountRepository.findByAccountOwnerPhoneNumberAndAccountName(phoneNumber, accountName);
    }

    @Override
    public List<WalletAccount> findByAccountOwnerPhoneNumberAndAccountNameAndScheme_SchemeID(String phoneNumber, String accountName, String schemeID) {
        return walletAccountRepository.findByAccountOwnerPhoneNumberAndAccountNameAndScheme_SchemeID(utility.formatPhoneNumber(phoneNumber), accountName, schemeID);
    }

    @Override
    public List<WalletAccount> findByAccountOwnerPhoneNumberAndScheme_SchemeID(String phoneNumber, String schemeID) {
        return walletAccountRepository.findByAccountOwnerPhoneNumberAndScheme_SchemeID(phoneNumber, schemeID);
    }

    @Override
    public PaymentResponseDTO treatInvoice(InvoiceDTO invoiceDTO) {

        PaymentResponseDTO responseDTO = new PaymentResponseDTO();

        if ("approve".equalsIgnoreCase(invoiceDTO.getAction())) {

            Double actualBalance = 0.0;
            Double debitAmount = 0.0;

            List<JournalLine> journalLines = journalLineService.findByJounalReference(invoiceDTO.getReference());
            System.out.println("Lines outside == >" + journalLines);

            /*List<WalletAccount> accountList = journalLines
                .stream()
                .filter(journalLine -> journalLine.getCredit() > 0 && journalLine.getWalletAccount() != null && journalLine.getWalletAccount().getAccountOwner() != null)
                .map(JournalLine::getWalletAccount)
                .collect(Collectors.toList());
            WalletAccount creditAccount = null;
            if (!accountList.isEmpty()){
                creditAccount = accountList.get(0);
            }*/

            for (JournalLine journalLine : journalLines) {
                WalletAccount walletAccount1 = journalLine.getWalletAccount();
                if (journalLine.getDebit() > 0 && walletAccount1 != null && walletAccount1.getAccountOwner() != null) {
                    debitAmount = journalLine.getDebit();

                    actualBalance = Double.valueOf(walletAccount1.getActualBalance());

                    if (!StringUtils.isEmpty(invoiceDTO.getAccountNumber())) {
                        WalletAccount walletAccount = findOneByAccountNumber(invoiceDTO.getAccountNumber());
                        System.out.println("Wallet Account retrieved ===> " + walletAccount);
                        if (walletAccount != null) {
                            actualBalance = Double.valueOf(walletAccount.getActualBalance());

                            ValidTransactionResponse validTransaction = utility.isValidTransaction("walletToBank", invoiceDTO.getAccountNumber(), "", debitAmount, 0.0, false);
                            if (!validTransaction.isValid()) {
                                responseDTO.setMessage(validTransaction.getMessage());
                                responseDTO.setCode("99");
                                responseDTO.setError(true);

                                return responseDTO;
                            }

                            System.out.println("updated journalLine ==> " + journalLine);
                        }
                    }

                }
            }

            log.info("CURRENT BAL : " + actualBalance + " AMOUNT : " + debitAmount);

            if (actualBalance < debitAmount) {
                responseDTO.setCode(IN_SUFFICIENT_FUNDS.getCode());
                responseDTO.setMessage(IN_SUFFICIENT_FUNDS.getDescription());
                return responseDTO;
            }
        }

        producer.send(invoiceDTO);

        FundDTO requestMoneyLog = transactionLogService.findByTransRef(invoiceDTO.getReference());

        if ("approve".equalsIgnoreCase(invoiceDTO.getAction())) {
            log.info("About to trigger agentRef ===> ");
            if (requestMoneyLog != null) {
                String agentRef = requestMoneyLog.getAgentRef();
                if (!StringUtils.isEmpty(agentRef)) {
                    FundDTO fundDTO = transactionLogService.findByTransRef(agentRef);
                    producer.send(fundDTO);
                }
            }
        }

        if ("approve".equalsIgnoreCase(invoiceDTO.getAction())) {
            if (requestMoneyLog != null) {
                responseDTO.setMessage("You have approved a request for " + utility.formatMoney(requestMoneyLog.getAmount()));
                responseDTO.setCode("00");

            } else {
                responseDTO.setMessage("Fund approved Successfully");
                responseDTO.setCode("00");

            }

        } else {
            if (requestMoneyLog != null) {
                responseDTO.setMessage("You have rejected a request for " + utility.formatMoney(requestMoneyLog.getAmount()));
                responseDTO.setCode("00");

            } else {
                responseDTO.setMessage("Fund rejected successfully");
                responseDTO.setCode("00");

            }
        }

        return responseDTO;
    }

    @Override
    public NewWalletAccountResponse openNewWalletAccountByForeignEndPoint(NewWalletAccountDTO newWalletAccountDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        NewWalletAccountResponse newWalletAccountResponse = null;

        String openAccountUrl = "https://login.remita.net/remita/exapp/api/v1/wallet/services/core-banking/v1/account/open";

        try {
            RestTemplate restTemplate = new RestTemplate();


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> clientRequest =
                new HttpEntity<String>(objectMapper.writeValueAsString(newWalletAccountDTO), headers);

            newWalletAccountResponse = restTemplate.postForObject(openAccountUrl, clientRequest, NewWalletAccountResponse.class, NewWalletAccountResponse.class);

            log.debug("Wallet Account Response  = " + objectMapper.writeValueAsString(newWalletAccountResponse));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return newWalletAccountResponse;
    }

    @Override
    public Optional<WalletAccount> findByAccountName(String accountName) {
        return walletAccountRepository.findByAccountNameAndStatusNot(accountName, AccountStatus.INACTIVE);
    }

    public String createWalletExternalForUssd(@Valid @RequestBody WalletExternalDTO walletExternalDTO) {

        RegisteredUserDTO registeredUserDTO = convertToRegisterUserDTO(walletExternalDTO);

        User user;

        Long bySchemeID = 1L;
        try {
            user = userService.registerUser(registeredUserDTO, registeredUserDTO.getPassword());

        } catch (UsernameAlreadyUsedException e) {
            user = userRepository.findByLogin(registeredUserDTO.getPhoneNumber());

            Profile profile = profileService.findByPhoneNumber(registeredUserDTO.getPhoneNumber());
            if (profile != null) {
                return createWalletForExternalUssd(walletExternalDTO, bySchemeID, profile);
            }
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            registeredUserDTO.getPhoneNumber(), registeredUserDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = true;
        String jwt = tokenProvider.createToken(authentication, rememberMe);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        //CREATE PROFILE
        Profile profile = profileService.findByPhoneNumber(walletExternalDTO.getPhoneNumber());
        profile.setPhoneNumber(walletExternalDTO.getPhoneNumber());
        profile.setProfileID("4");
        profile.setDeviceNotificationToken(walletExternalDTO.getDeviceNotificationToken());
        profile.setTotalBonus(0.0);

        String gender = walletExternalDTO.getGender();
        Gender gender1 = Gender.valueOf(gender.toUpperCase());
        profile.setGender(gender1);
        profile.setDateOfBirth(walletExternalDTO.getDateOfBirth());

        String encryptedPin = passwordEncoder.encode(walletExternalDTO.getPin());
        profile.setPin(encryptedPin);
        profile.setKyc(kyclevelService.findByKycLevel(1));

        Profile savedProfile = profileService.save(profile);

        savedProfile.setUser(user);

        log.info("SAVED PROFILE " + savedProfile);

        //CREATE WALLET
//        long accountNumber = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);
        return createWalletForExternalUssd(walletExternalDTO, bySchemeID, savedProfile);

    }

    private RegisteredUserDTO convertToRegisterUserDTO(WalletExternalDTO walletExternalDTO) {
        RegisteredUserDTO registeredUserDTO = new RegisteredUserDTO();
        registeredUserDTO.setFirstName(walletExternalDTO.getFirstName());
        registeredUserDTO.setLastName(walletExternalDTO.getLastName());
        registeredUserDTO.setPhoneNumber(walletExternalDTO.getPhoneNumber());
        registeredUserDTO.setPassword(walletExternalDTO.getPassword());
        registeredUserDTO.setEmail(walletExternalDTO.getEmail());
        String deviceNotificationToken = String.valueOf(UUID.randomUUID());
        registeredUserDTO.setDeviceNotificationToken(deviceNotificationToken);
        return registeredUserDTO;
    }

    private RegisteredCorporateUserDTO convertToRegisterCorporateUserDTO(WalletExternalDTO walletExternalDTO) {
        RegisteredCorporateUserDTO registeredUserDTO = new RegisteredCorporateUserDTO();
        registeredUserDTO.setFirstName(walletExternalDTO.getFirstName());
        registeredUserDTO.setLastName(walletExternalDTO.getLastName());
        registeredUserDTO.setPhoneNumber(walletExternalDTO.getPhoneNumber());
        registeredUserDTO.setPassword(walletExternalDTO.getPassword());
        registeredUserDTO.setEmail(walletExternalDTO.getEmail());
        String deviceNotificationToken = String.valueOf(UUID.randomUUID());
        registeredUserDTO.setDeviceNotificationToken(deviceNotificationToken);
        registeredUserDTO.setBusinessName(walletExternalDTO.getBusinessName());
        registeredUserDTO.setBusinessAddress(walletExternalDTO.getBusinessAddress());
        registeredUserDTO.setCategory(walletExternalDTO.getCategory());
        registeredUserDTO.setBusinessPhoneNo(walletExternalDTO.getBusinessPhoneNo());
        registeredUserDTO.setRcNO(walletExternalDTO.getRcNO());
        registeredUserDTO.setRegType(walletExternalDTO.getRegType());
        registeredUserDTO.setTin(walletExternalDTO.getTin());
        registeredUserDTO.setCacCertificate(walletExternalDTO.isCacCertificate());
        registeredUserDTO.setCacCo7(walletExternalDTO.isCacCo7());
        registeredUserDTO.setCacCo2(walletExternalDTO.isCacCo2());
        registeredUserDTO.setCorporateDocuments(walletExternalDTO.getCorporateDocuments());
        return registeredUserDTO;
    }

    private String createWalletForExternalUssd(WalletExternalDTO walletExternalDTO,
                                               Long bySchemeID, Profile profile) {

        Long accountNumber = utility.getUniqueAccountNumber();

        Optional<SchemeDTO> one = schemeService.findOne(bySchemeID);
        if (!one.isPresent()) {
            return "invalid scheme";
        }

        WalletAccountDTO walletAccountDTO = new WalletAccountDTO();
        walletAccountDTO.setAccountNumber(String.valueOf(accountNumber));
        walletAccountDTO.setAccountOwnerPhoneNumber(walletExternalDTO.getPhoneNumber());
        walletAccountDTO.setAccountOwnerId(profile.getId());
        walletAccountDTO.setAccountName(walletExternalDTO.getAccountName());
        walletAccountDTO.setDateOpened(LocalDate.now());
        walletAccountDTO.setCurrentBalance(walletExternalDTO.getOpeningBalance());
        walletAccountDTO.setActualBalance(walletAccountDTO.getCurrentBalance());
        walletAccountDTO.setSchemeId(bySchemeID);
        walletAccountDTO.setWalletAccountTypeId(1L);
        walletAccountDTO.setStatus(AccountStatus.ACTIVE);

        WalletAccountDTO wallet = null;

        try {
            List<WalletAccount> accountOptional = findByAccountOwnerPhoneNumberAndAccountNameAndScheme_SchemeID(walletExternalDTO.getPhoneNumber(), walletAccountDTO.getAccountName(), one.get().getSchemeID());
            if (!accountOptional.isEmpty()) {
                WalletAccount walletAccount = accountOptional.get(0);
                log.info("Wallet found " + walletAccount);

                return "You already have a wallet with the same name(" + walletExternalDTO.getAccountName() + ")";

            } else {
                wallet = save(walletAccountDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "You already have a wallet with the same name(" + walletExternalDTO.getAccountName() + ").";
        }

        if (wallet != null) {
//            String result = "You have successfully registered for a Tier 1 Wallet, your Wallet Id is (" + wallet.getAccountNumber() + ")";
            Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
            if (asyncExecutor != null) {
                WalletAccountDTO finalWallet = wallet;
                asyncExecutor.execute(() -> {
                    try {
                        out.println("Creating NUBAN account for new User ===> "
                            + walletAccountDTO.getAccountOwnerPhoneNumber());

                        Long schemeId = finalWallet.getSchemeId();
                        Optional<Scheme> optionalScheme = schemeService.findSchemeId(schemeId);
                        if (optionalScheme.isPresent()) {
                            Scheme scheme = optionalScheme.get();
                            String schemeID = scheme.getSchemeID();

                            out.println("Inside polaris ===>");

                            try {
                                utility.createPolarisAccount(profile.getPhoneNumber(), schemeID);
                            } catch (Exception e) {
                                e.printStackTrace();
                                out.println("Failed to create nuban for user " + profile.getPhoneNumber() + " " +
                                    "scheme" +
                                    " =" + schemeID);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            String result = "Your Tier 1 Account with ID " + wallet.getAccountNumber() + " is now open.";

            asyncExecutor = asyncConfiguration.getAsyncExecutor();
            if (asyncExecutor != null) {
                asyncExecutor.execute(() -> {
                    SendSMSDTO sendSMSDTO = new SendSMSDTO();
                    sendSMSDTO.setMobileNumber(profile.getPhoneNumber());
                    sendSMSDTO.setSmsMessage(result);
                    try {
                        externalOTPRESTClient.sendSMS(sendSMSDTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            return result;
        }

        return "failed";
    }

    public String sendUSSDFundWalletLink(String message, Profile profile) {
        SendSMSDTO sendSMSDTO = new SendSMSDTO();
        sendSMSDTO.setMobileNumber(profile.getPhoneNumber());
        sendSMSDTO.setSmsMessage(message);
        try {
            externalOTPRESTClient.sendSMS(sendSMSDTO);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    @Override
    public List<WalletAccount> findAllByNubanAccountNoNotNull() {
        ArrayList<String> exceptions = new ArrayList<>();
        exceptions.add("");
        exceptions.add(null);
        exceptions.add("YJIoHUi9jpgngZ+GyiApyQ==");
        return walletAccountRepository.findWalletAccountsByNubanAccountNoNotInAndAccountOwnerNotNullAndNubanAccountNoIsNotNull(exceptions);
    }

    @Override
    public List<WalletAccountDTO> findAllByNubanAccountNoNotNullWithBalances() {
        ArrayList<String> exceptions = new ArrayList<>();
        exceptions.add("");
        exceptions.add(null);
        exceptions.add("YJIoHUi9jpgngZ+GyiApyQ==");
        List<WalletAccount> walletAccounts = walletAccountRepository.findWalletAccountsByNubanAccountNoNotInAndAccountOwnerNotNullAndNubanAccountNoIsNotNull(exceptions);
        return walletAccounts.stream()
            .map(walletAccount -> {
                WalletAccountDTO walletAccountDTO = walletAccountMapper.toDto(walletAccount);
                String accountOwnerPhoneNumber = walletAccountDTO.getAccountOwnerPhoneNumber();
                double totalBalance = utility.getTotalWalletsBalance(accountOwnerPhoneNumber);
                walletAccountDTO.setTotalCustomerBalances(totalBalance);
                return walletAccountDTO;
            })
            .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<GenericResponseDTO> retrieveNuban(String phoneNumber) {

        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        Optional<WalletAccount> walletAccountOptional = utility.getPrimaryWalletByPhoneNumber(phoneNumber);

        out.println("0 retrieveNuban method walletAccountOptional = "+walletAccountOptional);
        if (walletAccountOptional.isPresent()) {
            WalletAccount walletAccount = walletAccountOptional.get();
            out.println("1 retrieveNuban method walletAccount = "+walletAccount);


            if (utility.checkStringIsNotValid(walletAccount.getTrackingRef())
                    || walletAccount.getTrackingRef().length() < 20) {

                Profile profile = profileService.findByPhoneNumber(phoneNumber);

                out.println("2 retrieveNuban method profile = "+profile);
                List<AddressDTO> addressDTOList = addressService.findByAddressOwner(phoneNumber);
                List<Address> addresses = addressMapper.toEntity(addressDTOList);

                out.println("3 retrieveNuban method addresses = "+addresses);
                List<WalletAccount> walletAccounts = findByAccountOwnerPhoneNumber(phoneNumber);

                out.println("4 retrieveNuban method walletAccounts = "+walletAccounts);
                List<Scheme> allSchemes = schemeService.findAllSchemes();

                out.println("5 retrieveNuban method allSchemes = "+allSchemes);
                for (Scheme scheme : allSchemes) {
                    List<WalletAccount> schemeWallets = walletAccounts.stream().filter(
                        walletAccount1 -> walletAccount1.getScheme().getSchemeID().equalsIgnoreCase(scheme.getSchemeID())
                    ).collect(Collectors.toList());

                    out.println("6 retrieveNuban method schemeWallets = "+schemeWallets);
                    try {

                        utility.createPolarisAccount(profile.getUser(), profile, addresses, schemeWallets, scheme);
                        out.println("after createPolarisAccount");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", null),
                        HttpStatus.OK);
                }

            } else if (utility.checkStringIsValid(walletAccount.getNubanAccountNo())) {

                return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", walletAccount.getNubanAccountNo()), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(new GenericResponseDTO("99", "Could not retrieve nuban account", null), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> retrieveNubanByScheme(String phoneNumber, String schemeId) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        utility.createPolarisAccount(phoneNumber, schemeId);

        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", null),
            HttpStatus.OK);
    }

    public boolean containsValidAccount(List<BulkBeneficiaryDTO> accountNumbers) {
        boolean containsValid = true;
        for (BulkBeneficiaryDTO b : accountNumbers) {
            boolean check = walletAccountRepository.existsByAccountNumber(b.getAccountNumber());
            if (!check) {
                containsValid = check;
                break;
            }
        }
        return containsValid;
    }


    @Override
    public WalletAccount resetBalances(double resetBalance, String accountNumber){
        WalletAccount walletAccount = walletAccountRepository.findByAccountNumber(accountNumber);
        walletAccount.setActualBalance(String.valueOf(resetBalance));
        walletAccount.setCurrentBalance(String.valueOf(resetBalance));
        WalletAccount save = walletAccountRepository.save(walletAccount);
        return save;
    }

    @Override
    public WalletAccount syncBalances(String accountNumber){
        WalletAccount walletAccount = walletAccountRepository.findByAccountNumber(accountNumber);
        log.info("Wallet account to sync {} ", walletAccount);
        log.info("Wallet Actual Balance {}", walletAccount.getActualBalance());
        log.info("Wallet Current Balance {}", walletAccount.getCurrentBalance());
        walletAccount.setActualBalance(walletAccount.getCurrentBalance());
        WalletAccount save = walletAccountRepository.save(walletAccount);
        log.info("Wallet account after sync {} ", save);
        return save;
    }

    @Override
    public List<WalletAccountDTO> findAllCurrentBalanceNotEqualActualBalance() {
        List<WalletAccountDTO> walletAccounts = walletAccountRepository.findWalletAccountsByCurrentBalanceIsNotActualBalance().stream().map(walletAccountMapper::toDto).collect(Collectors.toList());
        return walletAccounts;
    }

    @Override
    public void deleteByAccountNumber(String accountNumber) {
        walletAccountRepository.deleteByAccountNumber(accountNumber);
    }

}
