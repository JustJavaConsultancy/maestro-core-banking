package ng.com.justjava.corebanking.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.coobird.thumbnailator.Thumbnails;
import ng.com.justjava.corebanking.client.ExternalEmailRESTClient;
import ng.com.justjava.corebanking.config.AsyncConfiguration;
import ng.com.justjava.corebanking.domain.*;
import ng.com.justjava.corebanking.domain.enumeration.*;
import ng.com.justjava.corebanking.repository.UserRepository;
import ng.com.justjava.corebanking.repository.WalletAccountRepository;
import ng.com.justjava.corebanking.security.SecurityUtils;
import ng.com.justjava.corebanking.service.*;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.dto.stp.EncodedMessage;
import ng.com.justjava.corebanking.service.dto.stp.ExtendedConstants;
import ng.com.justjava.corebanking.service.exception.GenericException;
import ng.com.justjava.corebanking.service.kafka.producer.NotificationProducer;
import ng.com.justjava.corebanking.service.mapper.AddressMapper;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.systemspecs.apigateway.domain.enumeration.*;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

@Component
public class Utility {

    @Value("${app.percentage.vat}")
    private double vatFeePercentage;

    final static Logger LOG = LoggerFactory.getLogger(Utility.class);
    private static final long Lower_Bond = 1000000000L;
    private static final long Upper_Bond = 9000000000L;
    @Value("${app.constants.dfs.itex-electricity-acct}")
    private static String ITEX_ELELECTRICTY_ACCT;
    private final Logger log = LoggerFactory.getLogger(Utility.class);
    private final WalletAccountRepository walletAccountRepository;
    private final ExternalEmailRESTClient externalEmailRESTClient;
    private final AsyncConfiguration asyncConfiguration;
    private final UserRepository userRepository;
    private final ApprovalRequestService approvalRequestService;
    private final UserService userService;
    private final NotificationProducer notificationProducer;
    private final SchemeService schemeService;
    private final AddressService addressService;
    private final AddressMapper addressMapper;
    private final TransactionLogService transactionLogService;
    private final WalletAccountService walletAccountService;
    private final ProfileService profileService;
    private final MyDeviceService myDeviceService;
    private final CashConnectService cashConnectService;
    private final WalletAccountTypeService walletAccountTypeService;
    private final JournalService journalService;
    private final SessionData sessionData;
    private final PasswordEncoder passwordEncoder;
    private final PolarisVulteService polarisVulteService;
    @Value("${app.uri.webservices.callID}")
    private String callID;
    @Value("${app.uri.webservices.password}")
    private String password;
    private User theUser;
    @Value("${app.scheme.systemspecs}")
    private String SYSTEMSPECS_SCHEME;
    @Value("${app.email.addresses.Kazeem}")
    private String Kazeem;
    @Value("${app.email.addresses.Bolaji}")
    private String Bolaji;
    @Value("${app.email.addresses.Tunji}")
    private String Tunji;
    @Value("${app.email.addresses.Demola}")
    private String Demola;
    @Value("${app.email.addresses.Ameze}")
    private String Ameze;
    @Value("${app.email.addresses.Maryam}")
    private String Maryam;
    @Value("${app.email.addresses.Seun}")
    private String Seun;
    @Value("${app.email.addresses.Chijioke}")
    private String Chijioke;
    @Value("${app.email.addresses.Olakunle}")
    private String Olakunle;
    @Value("${app.email.addresses.Ebube}")
    private String Ebube;
    @Value("${app.email.addresses.Adeibukun}")
    private String Adeibukun;
    @Value("${app.email.addresses.Omolara}")
    private String Omolara;
    @Value("${app.email.addresses.Chioma}")
    private String Chioma;
    @Value("${app.email.addresses.Temire}")
    private String Temire;
    @Value("${app.email.addresses.olamide}")
    private String Olamide;
    @Value("${app.constants.dfs.cashconnect-bvn-acct}")
    private String CASH_CONNECT_BVN_ACCT;
    @Value("${app.constants.dfs.rpsl-nin-validation-acct}")
    private String RPSL_NIN_VALIDATION_ACCT;
    @Value("${app.constants.dfs.cashconnect-interbank-services-acct}")
    private String CASH_CONNECT_INTERBANK_SERVICES_ACCT;
    @Value("${app.constants.dfs.rpsl-biller-pay-rrr-acct}")
    private String RSPL_BILLER_PAY_RRR_ACCT;
    @Value("${app.constants.dfs.rpsl-biller-electricity-acct}")
    private String RSPL_BILLER_PAY_ELECTRICITY_ACCT;
    @Value("${app.constants.dfs.rpsl-biller-cable-tv-acct}")
    private String RSPL_BILLER_PAY_TV_SUBSCRIPTUION_ACCT;
    @Value("${app.constants.dfs.itex-cable-tv-acct}")
    private String ITEX_CABLE_TV_ACCT;
    @Value("${app.constants.dfs.itex-data-acct}")
    private String ITEX_DATA_ACCT;
    @Value("${app.constants.dfs.itex-airtime-acct}")
    private String ITEX_AIRTIME_ACCT;
    @Value("${app.constants.dfs.itex-internet-acct}")
    private String ITEX_INTERNET_ACCT;
    @Value("${app.constants.dfs.remita-wallency-expense-acct}")
    private String REMITA_WALLENCY_EXPENSE_ACCT;
    @Value("${app.constants.dfs.introducer-account}")
    private String INTRODUCER_ACCT;
    @Value("${app.constants.dfs.cashconnect-commission-acct}")
    private String CASH_CONNECT_COMMISSION_ACCT;
    @Value("${app.constants.dfs.rpsl-inline-acct}")
    private String RSPL_INLINE_ACCT;
    @Value("${app.constants.dfs.telco-mtn-session-fee-acct}")
    private String TELCO_MTN_SESSION_FEE_ACCT;
    @Value("${app.constants.dfs.telco-9mobile-session-fee-acct}")
    private String TELCO_9MOBILE_SESSION_FEE_ACCT;
    @Value("${app.constants.dfs.telco-airtel-session-fee-acct}")
    private String TELCO_AIRTEL_SESSION_FEE_ACCT;
    @Value("${app.constants.dfs.telco-glo-session-fee-acct}")
    private String TELCO_GLO_SESSION_FEE_ACCT;
    @Value("${app.constants.dfs.ci-ussd-charge-acct}")
    private String CI_USSD_CHARGE_ACCT;
    @Value("${app.constants.dfs.mutual-benefits-income-acct}")
    private String MUTUAL_BENEFITS_INCOME_ACCT;
    @Value("${app.constants.dfs.coral-income-account}")
    private String CORAL_PAY_USSD_FUNDING_ACCT;
    @Value("${app.constants.dfs.remita-income-account}")
    private String RPSL_INTERBANK_SERVICES_ACCT;
    @Value("${app.constants.dfs.vat-account}")
    private String VAT_ACCOUNT;
    @Value("${app.constants.dfs.charge-account}")
    private String REMITA_WALLENCY_INCOME_ACCT;
    @Value("${app.cashconnect.transaction-ref}")
    private String transRefPrefix;
    @Value("${app.scheme.ibile}")
    private String IBILE_SCHEME;

    public Utility(WalletAccountRepository walletAccountRepository, ExternalEmailRESTClient externalEmailRESTClient,
                   AsyncConfiguration asyncConfiguration, UserRepository userRepository,
                   @Lazy ApprovalRequestService approvalRequestService, @Lazy UserService userService,
                   NotificationProducer notificationProducer, SchemeService schemeService, AddressService addressService,
                   AddressMapper addressMapper, TransactionLogService transactionLogService, @Lazy WalletAccountService walletAccountService,
                   ProfileService profileService, MyDeviceService myDeviceService, @Lazy CashConnectService cashConnectService,
                   PasswordEncoder passwordEncoder, WalletAccountTypeService walletAccountTypeService,
                   @Lazy JournalService journalService, SessionData sessionData,
                   @Lazy PolarisVulteService polarisVulteService) {

        this.walletAccountRepository = walletAccountRepository;
        this.externalEmailRESTClient = externalEmailRESTClient;
        this.asyncConfiguration = asyncConfiguration;
        this.userRepository = userRepository;
        this.approvalRequestService = approvalRequestService;
        this.userService = userService;
        this.notificationProducer = notificationProducer;
        this.schemeService = schemeService;
        this.addressService = addressService;
        this.addressMapper = addressMapper;
        this.transactionLogService = transactionLogService;
        this.walletAccountService = walletAccountService;
        this.profileService = profileService;
        this.myDeviceService = myDeviceService;
        this.cashConnectService = cashConnectService;
        this.passwordEncoder = passwordEncoder;
        this.walletAccountTypeService = walletAccountTypeService;
        this.journalService = journalService;
        this.sessionData = sessionData;
        this.polarisVulteService = polarisVulteService;
    }

    /**
     * Applies the specified mask to the card number.
     *
     * @param cardNumber The card number in plain format card number at that
     *                   position, use x to skip the digit at that position
     * @return The masked card number
     */
    public static String maskCardNumber(String cardNumber) {
        String mask;
        if (cardNumber.length() > 16) {
            mask = "xxxx-xxxx-xxxx-xxxx-####";
        } else {
            mask = "xxxx-xxxx-xxxx-####";
        }

        // format the number
        int index = 0;
        StringBuilder maskedNumber = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '#') {
                maskedNumber.append(cardNumber.charAt(index));
                index++;
            } else if (c == 'x') {
                maskedNumber.append(c);
                index++;
            } else {
                maskedNumber.append(c);
            }
        }

        // return the masked number
        return maskedNumber.toString();
    }

    public static void main(String[] args) {

//        out.println(generatePassword(10));
//        out.println( formatMoney(75000000D));

//        decodePayload("eyJwYXltZW50RGV0YWlsIjogIlt7IFwiZGViaXRBY2NvdW50Tm9cIjogNTU4NDI1MzUxMDAsIFwiYW1vdW50XCI6IDEwMCwgXCJkZXN0QmFua0NvZGVcIjogXCJTUEVDU1wiLCBcInRyYW5SZWZOb1wiOiBcInR5dHl5NTY3ODl5eXl5XCIsXCJuYXJyYXRpb25cIjogXCJmdW5kIHdhbGxldFwiLFwiY3JlZGl0QWNjb3VudE5vXCI6IFwiNTMxMDEzMjM5MjNcIiwgXCJzb3VyY2VCYW5rQ29kZVwiOiBcIkFCQ1wifV0iLCAiYmFua0NvZGUiOiAiQUJDIn0\\u003d");
//        decodePayload("\"W3siZGViaXRBY2NvdW50Tm8iOiI1NTg0MjUzNTEwMCIsImNyZWRpdEFjY291bnRObyI6IjUzMTAxMzIzOTIzIiwidHJhblJlZk5vIjoidHl0eXk1Njc4OXl5eXkiLCJjdXJyZW5jeSI6Ik5HTiIsInJldHJ5Q291bnQiOjAsInJlcXVlc3RDb2RlIjoiUE9TVF9QQVlNRU5UIiwicHJvY2Vzc09yZGVyIjowLCJhbW91bnQiOjEwMC4wfV0=\"");
//        decodePayload("W3siZGViaXRBY2NvdW50Tm8iOiIxMzE1Mjk2ODMwNiIsImNyZWRpdEFjY291bnRObyI6IjQ3NTQ4NzQ0NDkiLCJ0cmFuUmVmTm8iOiJGUkc1Njc4OTU2N0RGR0giLCJjdXJyZW5jeSI6Ik5HTiIsInJldHJ5Q291bnQiOjAsInJlcXVlc3RDb2RlIjoiUE9TVF9QQVlNRU5UIiwicHJvY2Vzc09yZGVyIjowLCJhbW91bnQiOjIwLjB9XQ==");
//        encodePayload("{\\\"paymentDetail\\\": \\\"[{ \\\\\\\"debitAccountNo\\\\\\\": 55842535100, \\\\\\\"amount\\\\\\\": 100, \\\\\\\"destBankCode\\\\\\\": \\\\\\\"SPECS\\\\\\\", \\\\\\\"tranRefNo\\\\\\\": \\\\\\\"tytyy56MMK9yyyy\\\\\\\",\\\\\\\"narration\\\\\\\": \\\\\\\"fund wallet\\\\\\\",\\\\\\\"creditAccountNo\\\\\\\": \\\\\\\"53101323923\\\\\\\", \\\\\\\"sourceBankCode\\\\\\\": \\\\\\\"ABC\\\\\\\"}]\\\", \\\"bankCode\\\": \\\"ABC\\\"}");
//        encodePayload("{\"paymentDetail\":\"[{\\\"debitAccountNo\\\":\\\"55842535100\\\",\\\"creditAccountNo\\\":\\\"5970000000000\\\",\\\"narration\\\":\\\"R-270442710810/C0000263:MTN Airtime Topup for Muji\\\",\\\"tranRefNo\\\":8442757970,\\\"amount\\\":100.0,\\\"sessionNumber\\\":-100,\\\"transDate\\\":\\\"22-12-2020 09:47.42\\\",\\\"retryCount\\\":0,\\\"transitToMirror\\\":false,\\\"processOrder\\\":1,\\\"bulkPosting\\\":false,\\\"bankCode\\\":\\\"597\\\"}]\",\"requestCode\":\"POST_PAYMENT\",\"controlRequestId\":\"93308988-d9dc-47ba-b0df-ae6463bb18cb\",\"bank\":\"597\"}");
//        encodePayload("{\"paymentDetail\": \"[{ \\\"debitAccountNo\\\": 55842535100, \\\"amount\\\": 100, \\\"destBankCode\\\": \\\"SPECS\\\", \\\"tranRefNo\\\": \\\"tytyy56789yyyy\\\",\\\"narration\\\": \\\"fund wallet\\\",\\\"creditAccountNo\\\": \\\"53101323923\\\", \\\"sourceBankCode\\\": \\\"ABC\\\"}]\", \"bankCode\": \"ABC\"}");
//        encodePayload("{\"paymentDetail\": \"[{ \\\"debitAccountNo\\\": \\\"\\\", \\\"amount\\\": 100, \\\"destBankCode\\\": \\\"SPECS\\\", \\\"tranRefNo\\\": \\\"MMMNGHTYGG45678\\\",\\\"narration\\\": \\\"fund wallet\\\",\\\"creditAccountNo\\\": \\\"53101323923\\\", \\\"sourceBankCode\\\": \\\"ABC\\\"}]\", \"bankCode\": \"ABC\"}"); //Todo Bank to Wallet
//        encodePayload("{\"paymentDetail\": \"[{ \\\"debitAccountNo\\\": 13152968306, \\\"amount\\\": 20, \\\"destBankCode\\\": \\\"SPECS\\\", \\\"tranRefNo\\\": \\\"FRG56789567DFGH\\\",\\\"narration\\\": \\\"funding wallet\\\",\\\"creditAccountNo\\\": \\\"4754874449\\\", \\\"sourceBankCode\\\": \\\"ABC\\\"}]\", \"bankCode\": \"ABC\"}");//TODO my wallet to wallet
//        encodePayload("{\"paymentDetail\": \"[{ \\\"debitAccountNo\\\": 55842535100, \\\"amount\\\": 20, \\\"destBankCode\\\": \\\"SPECS\\\", \\\"tranRefNo\\\": \\\"FRG56789567DFGH\\\",\\\"narration\\\": \\\"funding wallet\\\",\\\"creditAccountNo\\\": \\\"479106\\\", \\\"sourceBankCode\\\": \\\"050\\\"}]\", \"bankCode\": \"050\"}");//TODO my wallet to bank

        out.println(generateSecurePassword());
    }

    private static void encodePayload(String res) {
        // String res = "{\"paymentDetail\": \"[{ \\\"debitAccountNo\\\": 55842535100,
        // \\\"amount\\\": 100, \\\"destBankCode\\\": \\\"SPECS\\\", \\\"tranRefNo\\\":
        // \\\"tytyy56789yyyy\\\",\\\"narration\\\": \\\"fund
        // wallet\\\",\\\"creditAccountNo\\\": \\\"53101323923\\\",
        // \\\"sourceBankCode\\\": \\\"ABC\\\"}]\", \"bankCode\": \"ABC\"}";
        String json = new Gson().toJson(res);
        EncodedMessage encodedMessage = new EncodedMessage();

        out.println("PAYLOAD " + new String(Base64.encodeBase64(json.getBytes())));

        String s = new String(Base64.encodeBase64(res.getBytes()));
        encodedMessage.setPayload(s);

        out.println("ENCODED " + new Gson().toJson(encodedMessage));
    }

    public static void decodePayload(String json) {
//        String json = ftEncoded.getPayload();
        LOG.info(">>>>> process encoded {}", json);
        final Gson gson = new Gson();
        byte[] decodedPayload = Base64.decodeBase64(json);
        LOG.info(">>>>> process decoded {}", new String(decodedPayload));
    }
    /*
     * public ClientAuth getClientAuth() {
     *
     * ClientAuth clientAuth = new ClientAuth(); JAXBElement<String> callIdValue =
     * new JAXBElement<String>(clientAuth.getCallID().getName(), String.class,
     * callID); JAXBElement<String> passwordValue = new
     * JAXBElement<String>(clientAuth.getCallID().getName(), String.class,
     * password); clientAuth.setCallID(callIdValue);
     * clientAuth.setCallPassword(passwordValue); return clientAuth;
     *
     * }
     */

    public static String generatePassword(int n) {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();

//        return RandomStringUtils.randomAlphabetic(10);
    }

    public static String generateSecurePassword() {
        // chose a Character random from this String
        String capitalLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String smallLetters = "abcdefghijklmnopqrstuvxyz";
        String numberString = "0123456789";
        String specialCharacters = "$#@%&";

        // create StringBuffer size of capitalLetters
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            if (i % 10 == 0) {
                int index = (int) (specialCharacters.length() * Math.random());
                sb.append(specialCharacters.charAt(index));
            } else if (i % 4 == 0) {
                int index = (int) (numberString.length() * Math.random());
                sb.append(numberString.charAt(index));
            } else if (i == 7) {
                int index = (int) (capitalLetters.length() * Math.random());
                sb.append(capitalLetters.charAt(index));
            } else {
                int index = (int) (smallLetters.length() * Math.random());
                sb.append(smallLetters.charAt(index));
            }
        }

        return sb.toString();
    }

    public static String generateSecurePin() {
        // chose a Character random from this String
        String pin = null;

        ThreadLocalRandom random = ThreadLocalRandom.current();
        long pinNum = random.nextLong(100_00L);
        pin = Long.toString(pinNum);

        return pin;
    }


    public List<String> getWalletToWalletSpecificChannels() {

        ArrayList<String> specificChannels = new ArrayList<>();

        specificChannels.add(SpecificChannel.PAY_AEDC_ELECTRICITY_ITEX.getName().toLowerCase());
        specificChannels.add(SpecificChannel.PAY_PHEDC_ELECTRICITY_ITEX.getName().toLowerCase());
        specificChannels.add(SpecificChannel.PAY_PHEDC_ELECTRICITY_ITEX.getName().toLowerCase());
        specificChannels.add(SpecificChannel.PAY_IBEDC_ELECTRICITY_ITEX.getName().toLowerCase());
        specificChannels.add(SpecificChannel.PAY_IKEDC_ELECTRICITY_ITEX.getName().toLowerCase());
        specificChannels.add(SpecificChannel.PAY_EEDC_ELECTRICITY_ITEX.getName().toLowerCase());
        specificChannels.add(SpecificChannel.PAY_EKEDC_ELECTRICITY_ITEX.getName().toLowerCase());
        specificChannels.add(SpecificChannel.PAY_KEDCO_ELECTRICITY_ITEX.getName().toLowerCase());
        specificChannels.add(SpecificChannel.BUY_DATA_ITEX_AIRTEL.getName().toLowerCase());
        specificChannels.add(SpecificChannel.BUY_AIRTIME_ITEX_MTN.getName().toLowerCase());
        specificChannels.add(SpecificChannel.BUY_AIRTIME_ITEX_GLO.getName().toLowerCase());
        specificChannels.add(SpecificChannel.BUY_AIRTIME_ITEX_AIRTEL.getName().toLowerCase());
        specificChannels.add(SpecificChannel.BUY_AIRTIME_ITEX_9MOBILE.getName().toLowerCase());
        specificChannels.add(SpecificChannel.BUY_DATA_ITEX_MTN.getName().toLowerCase());
        specificChannels.add(SpecificChannel.BUY_DATA_ITEX_GLO.getName().toLowerCase());
        specificChannels.add(SpecificChannel.BUY_DATA_ITEX_9MOBILE.getName().toLowerCase());
        specificChannels.add(SpecificChannel.PAY_CABLE_TV_ITEX.getName().toLowerCase());
        specificChannels.add(SpecificChannel.STARTIMES_TV.getName().toLowerCase());
        specificChannels.add(SpecificChannel.PAY_INTERNET_ITEX.getName().toLowerCase());
        specificChannels.add(SpecificChannel.IBILE_PAY.getName().toLowerCase());
        specificChannels.add(SpecificChannel.INSURANCE.getName().toLowerCase());
//        specificChannels.add(SpecificChannel.LIBERTY.toString());

        return specificChannels;
    }

    public User getCurrentUser() {

        /*
         * SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).
         * ifPresent(user -> { log.info("User object == > " + user); this.theUser =
         * user; });
         *
         * log.info("User object == > " + theUser);
         *
         * return theUser;
         */

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {

            String username = null;
            UserDetails principal = (UserDetails) auth.getPrincipal();

            if (principal != null) {
                username = principal.getUsername();
            }

            if (username != null) {
                Optional<User> userOptional = userService.findByLogin(username);
                log.info("Current login user ====> " + userOptional.toString());

                if (userOptional.isPresent()) {
                    return userOptional.get();
                }
            }
        }

        throw new GenericException("Invalid current user, please login");
    }

    public NewWalletAccountResponse getNewWalletAccount(NewWalletAccountDTO newWalletAccountDTO) {

        String url = "https://login.remita.net/remita/exapp/api/v1/wallet/services/core-banking/v1/account/open";
        try {
            RestTemplate restTemplate = new RestTemplate();
            log.error("REQUEST WALLET " + newWalletAccountDTO);
            log.error(new ObjectMapper().writeValueAsString(newWalletAccountDTO));

            HttpEntity<NewWalletAccountDTO> request = new HttpEntity<>(newWalletAccountDTO);
            ResponseEntity<NewWalletAccountResponse> response = restTemplate.exchange(url, HttpMethod.POST, request,
                NewWalletAccountResponse.class);
            log.error(new ObjectMapper().writeValueAsString(response.getBody()));
            return response.getBody();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public NewWalletAccountResponse getAdditionalWalletAccount(AdditionalWalletAccountDTO additionalWalletAccountDTO) {

        String url = "https://login.remita.net/remita/exapp/api/v1/wallet/services/core-banking/v1/account/addNew";
        try {
            RestTemplate restTemplate = new RestTemplate();
            log.error("REQUEST WALLET " + additionalWalletAccountDTO);
            log.error(new ObjectMapper().writeValueAsString(additionalWalletAccountDTO));

            HttpEntity<AdditionalWalletAccountDTO> request = new HttpEntity<>(additionalWalletAccountDTO);
            ResponseEntity<NewWalletAccountResponse> response = restTemplate.exchange(url, HttpMethod.POST, request,
                NewWalletAccountResponse.class);
            log.error(new ObjectMapper().writeValueAsString(response.getBody()));
            return response.getBody();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String formatMoney(Double amount) {
        if (amount == null) {
            return null;
        }

        Locale locale = new Locale("en", "NG");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(amount);
    }

    /*
     * public Object deserializeIdempotentResponse(String response, Object obj)
     * throws IOException { return new ObjectMapper().readValue(response,
     * obj.getClass()); }
     */

    public String serializeIdempotentResponse(Object response) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(response);
    }

    public Object deserializeIdempotentResponse(String response, Object obj) throws IOException {
        return new ObjectMapper().readValue(response, obj.getClass());
    }

    public boolean searchAccountNumber(String accountNumber) {
        return walletAccountRepository.existsByAccountNumber(accountNumber);
    }

    public Long getUniqueAccountNumber() {
        long accountNo = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);
        boolean b = walletAccountRepository.existsByAccountNumber(String.valueOf(accountNo));
        if (b) {
            getUniqueAccountNumber();
        }
        return accountNo;
    }

    public String maskAccountNumber(String number) {
        int i = 0;
        StringBuffer temp = new StringBuffer();
        while (i < (number.length())) {
            if (i > number.length() - 5) {
                temp.append(number.charAt(i));
            } else {
                temp.append("*");
            }
            i++;
        }
        out.println(temp);
        return temp.toString();
    }

    public String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }

        phoneNumber = phoneNumber.trim();
        if (phoneNumber.length() < 10 || phoneNumber.length() > 14) {
            return phoneNumber;
        }

        return phoneNumber = "+234" + phoneNumber.substring(phoneNumber.length() - 10);

    }

    public byte[] resizeImage(byte[] imageArray) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(imageArray);
        BufferedImage originalImage = ImageIO.read(bis);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(originalImage)
            .size(100, 150)
            .keepAspectRatio(true)
            .outputFormat("JPEG")
            .outputQuality(1)
            .toOutputStream(outputStream);
        return outputStream.toByteArray();
    }

    public Map<String, String> getEmailMap() {
        Map<String, String> emails = new HashMap<>();
        emails.put("Kazeem", Kazeem);
//        emails.put("Tunji", Tunji);
        emails.put("Bolaji", Bolaji);
        emails.put("Demola", Demola);
        emails.put("Ameze", Ameze);
        emails.put("Maryam", Maryam);
        emails.put("Seun", Seun);
        emails.put("Chijioke", Chijioke);
        emails.put("Olakunle", Olakunle);
        emails.put("Adeibukun", Adeibukun);
        emails.put("Chioma", Chioma);
//        emails.put("Temire", Temire);
        emails.put("Olamide", Olamide);
        emails.put("Customer support", "pouchii@systemspecs.com.ng");

        return emails;
    }

    /*
     * public static void main(String[] args) throws IOException {
     *
     * String encodedString = ""; byte[] decodedBytes =
     * Base64.getDecoder().decode(encodedString); ByteArrayInputStream bis = new
     * ByteArrayInputStream(decodedBytes); BufferedImage originalImage =
     * ImageIO.read(bis); ByteArrayOutputStream outputStream = new
     * ByteArrayOutputStream();
     *
     * Thumbnails.of(originalImage) .size(100, 150) .keepAspectRatio(true)
     * .outputFormat("JPEG") .outputQuality(1) .toOutputStream(outputStream); byte[]
     * bytes = outputStream.toByteArray();
     *
     * File file = new File(outputFileName);
     *
     * FileUtils.writeByteArrayToFile(file, decodedBytes);
     *
     * }
     */

    public Map<String, String> getContactEmailMap() {
        Map<String, String> emails = new HashMap<>();
        emails.put("Customer support", "pouchii@systemspecs.com.ng");
        emails.put("Kazeem", Kazeem);
        emails.put("Bolaji", Bolaji);
        emails.put("Demola", Demola);
        emails.put("Ameze", Ameze);
        emails.put("Maryam", Maryam);
        emails.put("Seun", Seun);
        emails.put("Chioma", Chioma);
        emails.put("Adeibukun", Adeibukun);
        emails.put("Olamide", Olamide);

        return emails;
    }

    public Map<String, String> getDoubleEntryValidationEmailMap() {
        Map<String, String> emails = new HashMap<>();
        emails.put("Kazeem", Kazeem);
        emails.put("Temire", Temire);


        return emails;
    }

    public Map<String, String> getNotificationEmailMap() {
        Map<String, String> emails = getEmailMap();
        emails.put("Omolara", Omolara);

        return emails;
    }

    public String returnPhoneNumberFormat(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        phoneNumber = phoneNumber.trim();

        if (phoneNumber.length() > 9) {
            phoneNumber = "0" + phoneNumber.substring(phoneNumber.length() - 10);
        }
        return phoneNumber;
    }

    public String maskBalance(String formattedBalance) {
        LOG.info("FORMATTED BALANCE " + formattedBalance);
        int i = 0;
        StringBuffer temp = new StringBuffer();
        while (i < (formattedBalance.length())) {
            if (i > formattedBalance.length() - 4) {
                temp.append(formattedBalance.charAt(i));
            } else if (i == 0 || i == 1 || i == 2) {
                temp.append(formattedBalance.charAt(i));
            } else if (i == 3 && formattedBalance.startsWith("-")) {
                temp.append(formattedBalance.charAt(i));
            } else {
                temp.append("x");
            }
            i++;
        }
        out.println("MASKED BAL " + temp);
        return temp.toString();
    }

    public boolean checkStringIsValid(String string) {
        return StringUtils.isNotEmpty(string);
    }

    public boolean checkStringIsValid(String... string) {

        for (String s : string) {
            boolean notEmpty = StringUtils.isNotEmpty(s);
            if (!notEmpty) {
                return false;
            }
        }

        return true;
    }

    public boolean checkStringIsNotValid(String string) {
        return StringUtils.isEmpty(string);
    }

    public boolean checkStringIsNotValid(String... string) {
        for (String s : string) {
            boolean isEmpty = StringUtils.isEmpty(s);
            if (!isEmpty) {
                return false;
            }
        }
        return true;
    }

    public void sendEmail(String email, String subject, String content) {

        boolean isValid = checkStringIsValid(email, subject, content);

        if (isValid) {
            sendToNotificationConsumer("", "", "", subject, content, "", email, "");
        }
    }

    public void sendEmailByConsumer(String email, String subject, String content) {
        log.info("Inside the sendEmail method ===> " + email);
        log.info("Inside the sendEmail method ===> " + subject);
        log.info("Inside the sendEmail method ===> " + content);

        boolean isValid = checkStringIsValid(email, subject, content);

        if (isValid) {
            try {
                MessageMap messageMap = new MessageMap();
                messageMap.setProductName("Pouchii");
                messageMap.setSubject(subject);
                messageMap.setToEmail(email);
                messageMap.setMAILPARAMMAP(new MAILPARAMMAP(content));
                Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
                if (asyncExecutor != null) {
                    asyncExecutor.execute(() -> {
                        externalEmailRESTClient.sendEmail(new SendEmailDTO(messageMap));
                    });
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendEmail(List<String> emails, String subject, String content) {
        for (String email : emails) {
            sendEmail(email, subject, content);
        }
    }

    public void sendEmail(Map<String, String> emails, String subject, String content) {
        try {
            Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
            if (asyncExecutor != null) {
                asyncExecutor.execute(
                    () -> {
                        for (Map.Entry<String, String> entry : emails.entrySet()) {
                            boolean isValid = checkStringIsValid(entry.getValue(), subject, content);
                            if (isValid) {
                                sendEmailByConsumer(entry.getValue(), subject, content);
                            }
                        }
                    }
                );
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Scheme getSchemeFromSession(HttpSession session) {
        String schemeId = (String) session.getAttribute("scheme");

        if (checkStringIsNotValid(schemeId)) {
            String phoneNumber = (String) session.getAttribute("phoneNumber");
            if (checkStringIsNotValid(phoneNumber)) {
                phoneNumber = profileService.findByUserIsCurrentUser().map(ProfileDTO::getPhoneNumber).orElse("null");
            }
            schemeId =
                (String) session.getServletContext().getAttribute("scheme-" + phoneNumber);
            log.info("Utility Retrieved scheme context session ==> " + schemeId);
            log.info("Utility Retrieved context session ID ==> " + session.getId());
        }
        if (checkStringIsNotValid(schemeId)) {
            schemeId = SYSTEMSPECS_SCHEME;
        }
        log.info("Utility Retrieved scheme finally ==> " + schemeId);
        return schemeService.findBySchemeID(schemeId);
    }

    public synchronized String getUniqueTransRef() {
        String l = String.valueOf(currentTimeMillis());
        String transRef = l.substring(l.length() - 11);
        FundDTO byTransRef = transactionLogService.findByTransRef(transRef);
        Optional<Journal> byReference = journalService.findByReference(transRef);
        if (byTransRef != null || byReference.isPresent()) {
            getUniqueTransRef();
        }
        return transRef;
    }

    public String getUniqueCashConnectTransRef() {
        String l = String.valueOf(currentTimeMillis());
        return transRefPrefix + l.substring(l.length() - 9);
    }

    public String getUniqueRequestId() {
        String l = String.valueOf(currentTimeMillis());
        String transRef = l.substring(l.length() - 11);
        ApprovalRequest approvalRequest = approvalRequestService.findByRequestId(transRef);
        if (approvalRequest != null) {
            getUniqueRequestId();
        }
        return transRef;
    }

    public String formatMoneyDecimal(Double amount) {
        if (amount == null) {
            return null;
        }
        return String.format("%.2f", amount);
    }

    public Page<?> toPage(List<?> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        if (start > list.size())
            return new PageImpl<>(new ArrayList<>(), pageable, list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());

        /*
         * int total = list.size(); int start = toIntExact(pageable.getOffset()); int
         * end = Math.min((start + pageable.getPageSize()), total);
         *
         * List<?> output = new ArrayList<>();
         *
         * if (start <= end) { output = list.subList(start, end); }
         *
         * return new PageImpl<>( output, pageable, total );
         */

        /*
         * int pageNum = pageable.getPageNumber(); int size = pageable.getPageSize();
         *
         * if(pageNum<1){ pageNum = 1; } if(size < 1){ size = 10; } //spring page starts
         * with 0 Pageable pageable2 = PageRequest.of(pageNum-1, size); //when pageNum *
         * size is too big(bigger than list.size()), totalRecords.subList() will throw a
         * exception, we need to fix this if(pageable.getOffset() > list.size()){
         * pageable2 = PageRequest.of(0, size); } List<?> pageRecords =
         * list.subList((int)pageable2.getOffset(), (int)Math.min(pageable2.getOffset()
         * + pageable2.getPageSize(), list.size()));
         *
         * Page springPage = new PageImpl<>(pageRecords, pageable, list.size()); return
         * springPage;
         */

    }

    public WalletAccount getWalletAccountOrCreateNew(String accountNumber, String accountNamePrefix,
                                                     Long accountTypeId) {
        WalletAccount retrievedWalletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
        if (retrievedWalletAccount != null) {
            log.info("Wallet Account retrieved " + retrievedWalletAccount);
            return retrievedWalletAccount;
        }

        Optional<WalletAccountType> one = walletAccountTypeService.findOneById(accountTypeId);
        Scheme scheme = schemeService.findBySchemeID("53797374656d73706563732077616c6c6574");

        WalletAccount walletAccount = new WalletAccount();
        walletAccount.setCurrentBalance("0.00");
        walletAccount.setActualBalance("0.00");
        walletAccount.setDateOpened(LocalDate.now());
        walletAccount.setAccountName(accountNamePrefix + " - " + accountNumber);
        walletAccount.setAccountNumber(String.valueOf(accountNumber));
        walletAccount.setStatus(AccountStatus.ACTIVE);
        one.ifPresent(walletAccount::setWalletAccountType);
        walletAccount.setScheme(scheme);
        WalletAccount save = walletAccountService.save(walletAccount);
        log.info("Wallet Account created " + save);
        return save;

    }

    public void sendToNotificationConsumer(String token, String deviceMessage, String title, String subject,
                                           String content, String sms, String email, String phoneNumber) {
        HashMap<String, Object> mapValue = new HashMap<String, Object>();

        mapValue.put("title", title);
        mapValue.put("token", token);
        mapValue.put("deviceMessage", deviceMessage);

        mapValue.put("subject", subject);
        mapValue.put("content", content);
        mapValue.put("sms", sms);
        mapValue.put("email", email);
        mapValue.put("phoneNumber", phoneNumber);

        notificationProducer.send(mapValue);
    }

    public boolean saveImage(String encodedString, String outputFileName) {
        try {
            log.debug("ABOUT TO DECODE");
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(encodedString);
            log.debug("AFTER DECODE");

            File file = new File(outputFileName);
            log.debug("Path name = " + outputFileName);
            log.debug("File absolute path = " + file.getAbsolutePath());
            FileUtils.writeByteArrayToFile(file, decodedBytes);

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            log.debug(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public FundDTO buildFundDTO(String sourceAccountNumber, String accountNumber, Double amount, String beneficiaryName,
                                String sourceAccountName, String profilePhoneNumber, String specificChannel, String channel) {
        FundDTO fundDTO = new FundDTO();
        fundDTO.setSourceAccountNumber(sourceAccountNumber);
        fundDTO.setAccountNumber(accountNumber);
        fundDTO.setChannel(channel);
        fundDTO.setSpecificChannel(specificChannel);
        fundDTO.setBeneficiaryName(beneficiaryName);
        fundDTO.setAmount(amount);
        fundDTO.setPhoneNumber(profilePhoneNumber);
        fundDTO.setTransRef(getUniqueTransRef());
        fundDTO.setSourceAccountName(sourceAccountName);
        return fundDTO;
    }

    public Optional<FundDTO> buildFundDTO(String sourceAccountNumber, String accountNumber, Double amount,
                                          String profilePhoneNumber, String specificChannel, String channel, String transRef) {

        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
        WalletAccount sourceAccount = walletAccountService.findOneByAccountNumber(sourceAccountNumber);

        if (sourceAccount == null || walletAccount == null) {
            return Optional.empty();
        }

        String beneficiaryName = walletAccount.getAccountFullName();
        String sourceAccountName = sourceAccount.getAccountFullName();

        FundDTO fundDTO = new FundDTO();
        fundDTO.setSourceAccountNumber(sourceAccountNumber);
        fundDTO.setAccountNumber(accountNumber);
        fundDTO.setChannel(channel);
        fundDTO.setSpecificChannel(specificChannel);
        fundDTO.setBeneficiaryName(beneficiaryName);
        fundDTO.setAmount(amount);
        fundDTO.setPhoneNumber(profilePhoneNumber);
        if (checkStringIsNotValid(transRef)) {
            fundDTO.setTransRef(getUniqueTransRef());
        } else
            fundDTO.setTransRef(transRef);
        fundDTO.setSourceAccountName(sourceAccountName);
        return Optional.of(fundDTO);
    }

    public int getPinFailureCount(HttpSession session) {
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

    public ResponseEntity<GenericResponseDTO> buildPinAttemptExceededResponse(GenericResponseDTO response,
                                                                              HttpSession session) {

        response.setMessage("Maximum failed pin reached, account deactivated");
        response.setCode("52");
        this.theUser.setActivated(false);
        this.theUser.setStatus(UserStatus.DEACTIVATE_CUSTOMER_PIN.getName());
        userRepository.save(this.theUser);

        session.removeAttribute("pinFailureCount");

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<GenericResponseDTO> buildPinErrorResponse(HttpSession session, int pinFailureCount) {
        GenericResponseDTO response;
        session.setAttribute("pinFailureCount", ++pinFailureCount);

        response = new GenericResponseDTO();
        response.setMessage("Invalid / wrong PIN entered");
        response.setCode("51");
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<GenericResponseDTO> checkTransactionPin(HttpSession session, String transactionPin) {
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            this.theUser = user;
        });

        if (theUser == null) {
            return new ResponseEntity<>(
                new GenericResponseDTO("64", HttpStatus.BAD_REQUEST, "User session expired", null),
                HttpStatus.BAD_REQUEST);
        }

        GenericResponseDTO response = new GenericResponseDTO();

        int pinFailureCount = getPinFailureCount(session);

        if (pinFailureCount >= 3) {
            return buildPinAttemptExceededResponse(response, session);
        }

        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());

        String currentEncryptedPin = profile.getPin();
        if (!passwordEncoder.matches(transactionPin, currentEncryptedPin) || StringUtils.isEmpty(transactionPin)) {
            return buildPinErrorResponse(session, pinFailureCount);
        }

        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", null), HttpStatus.OK);

    }

    public GenericResponseDTO checkSufficientFunds(Double amount, Double bonusAmount, String sourceAccountNumber, Double charges) {
        if (bonusAmount == null) bonusAmount = 0.0;
        WalletAccount sourceAccount = walletAccountService.findOneByAccountNumber(sourceAccountNumber);
        if (sourceAccount != null) {
            Profile sourceAccountOwner = sourceAccount.getAccountOwner();

            if (sourceAccountOwner != null) {
                double totalBonus = sourceAccountOwner.getTotalBonus();

                if (totalBonus < bonusAmount && bonusAmount > 0) {

                    return new GenericResponseDTO(ExtendedConstants.ResponseCode.IN_SUFFICIENT_FUNDS.getCode(), HttpStatus.BAD_REQUEST,
                        "Insufficient Amount in the BonusPot", totalBonus);
                }

            }

            double vatFee = getVatFee(charges);

            if (Double.parseDouble(sourceAccount.getActualBalance()) < amount + charges + vatFee) {
                return new GenericResponseDTO(ExtendedConstants.ResponseCode.IN_SUFFICIENT_FUNDS.getCode(), HttpStatus.BAD_REQUEST,
                    ExtendedConstants.ResponseCode.IN_SUFFICIENT_FUNDS.getDescription(), sourceAccount.getActualBalance());
            }

            return new GenericResponseDTO("00", HttpStatus.OK, "success", sourceAccount.getActualBalance());
        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid source account");

    }

    public ValidTransactionResponse isValidTransaction(String channel, String sourceAccountNumber, String accountNumber,
                                                       Double amount, Double bonusAmount, boolean isRequestMoney) {
        out.println("Entering isValid transaction");

        if (!isRequestMoney) {
            WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(sourceAccountNumber);
            if (walletAccount != null) {
                Profile accountOwner = walletAccount.getAccountOwner();
                if (accountOwner != null) {
                    String accountOwnerPhoneNumber = accountOwner.getPhoneNumber();
                    Boolean aBoolean = profileService.canSpendOnAccount(accountOwnerPhoneNumber, sourceAccountNumber,
                        amount);
                    if (Boolean.FALSE.equals(aBoolean)) {
                        return new ValidTransactionResponse(false, "Daily limit exceeded");
                    }

                }
            }
        }

        if ("walletToBank".equalsIgnoreCase(channel)) {
            boolean canBeDebited = walletAccountService.checkAccountCanBeDebited(sourceAccountNumber, amount);
            if (!canBeDebited) {
                return new ValidTransactionResponse(canBeDebited, "Forbidden transaction, account cannot be debited");
            }
        } else if ("bankToWallet".equalsIgnoreCase(channel)) {
            boolean canBeCredited = walletAccountService.checkAccountCanBeCredited(accountNumber, amount + bonusAmount,
                sourceAccountNumber);

            if (!canBeCredited) {
                return new ValidTransactionResponse(canBeCredited,
                    "Forbidden transaction, account cannot be credited/Exceeded limit");
            }

        } else if ("walletToWallet".equalsIgnoreCase(channel) || "INVOICE".equalsIgnoreCase(channel)) {
            boolean canBeDebited = walletAccountService.checkAccountCanBeDebited(sourceAccountNumber, amount);
            boolean canBeCredited = walletAccountService.checkAccountCanBeCredited(accountNumber, amount + bonusAmount,
                sourceAccountNumber);
            if (!canBeDebited) {
                return new ValidTransactionResponse(canBeDebited, "Forbidden transaction, account cannot be debited");
            }
            if (!canBeCredited) {
                return new ValidTransactionResponse(canBeCredited,
                    "Forbidden transaction, account cannot be credited/Exceeded limit");
            }
        }

        else if ("Card Request".equalsIgnoreCase(channel)) {
            boolean canBeDebited = walletAccountService.checkAccountCanBeDebited(sourceAccountNumber, amount);
            boolean canBeCredited = walletAccountService.checkAccountCanBeCredited(accountNumber, amount + bonusAmount,
                sourceAccountNumber);
            if (!canBeDebited) {
                return new ValidTransactionResponse(canBeDebited, "Forbidden transaction, account cannot be debited");
            }
            if (!canBeCredited) {
                return new ValidTransactionResponse(canBeCredited,
                    "Forbidden transaction, account cannot be credited/Exceeded limit");
            }
        }

        return new ValidTransactionResponse(true, "success");
    }

    public ValidTransactionResponse isValidTransaction(String channel, String sourceAccountNumber, List<BulkBeneficiaryDTO> accountNumbers,
                                                       Double amount, Double bonusAmount, boolean isRequestMoney) {
        out.println("Entering isValid transaction");

        if (!isRequestMoney) {
            WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(sourceAccountNumber);
            if (walletAccount != null) {
                Profile accountOwner = walletAccount.getAccountOwner();
                if (accountOwner != null) {
                    String accountOwnerPhoneNumber = accountOwner.getPhoneNumber();
                    Boolean aBoolean = profileService.canSpendOnAccount(accountOwnerPhoneNumber, sourceAccountNumber,
                        amount);
                    if (Boolean.FALSE.equals(aBoolean)) {
                        return new ValidTransactionResponse(false, "Daily limit exceeded");
                    }

                }
            }
        }

       if ("walletToBanks".equalsIgnoreCase(channel)) {
            boolean canBeDebited = walletAccountService.checkAccountCanBeDebited(sourceAccountNumber, amount);
            if (!canBeDebited) {
                return new ValidTransactionResponse(canBeDebited, "Forbidden transaction, account cannot be debited");
            }
        }
        if ("walletToWallets".equalsIgnoreCase(channel)) {
            boolean canBeDebited = walletAccountService.checkAccountCanBeDebited(sourceAccountNumber, amount);
            boolean canBeCredited = walletAccountService.checkAccountCanBeCredited(accountNumbers, amount + bonusAmount,
                sourceAccountNumber);
            if (!canBeDebited) {
                return new ValidTransactionResponse(canBeDebited, "Forbidden transaction, account cannot be debited");
            }
            if (!canBeCredited) {
                return new ValidTransactionResponse(canBeCredited,
                    "Forbidden transaction, One account or more accounts are Inactive or cannot be credited/Exceeded limit");
            }
        }

        return new ValidTransactionResponse(true, "success");
    }


    public Optional<WalletAccount> getPrimaryWallet(List<WalletAccount> walletAccountOptional) {

        if (walletAccountOptional.isEmpty()) {
            return Optional.empty();
        }

        WalletAccount walletAccount = walletAccountOptional.get(0);
        Profile accountOwner = walletAccount.getAccountOwner();
        if (accountOwner != null) {
            User user = accountOwner.getUser();
            if (user != null) {
                String firstName = user.getFirstName().trim();
                for (WalletAccount walletAccount1 : walletAccountOptional) {
                    if (walletAccount1.getAccountName().trim().equalsIgnoreCase(firstName)) {
                        return Optional.of(walletAccount1);
                    }
                }
            }
        }
        return Optional.empty();

    }

    public Optional<WalletAccount> getPrimaryWalletByPhoneNumber(String phoneNumber) {

        phoneNumber = formatPhoneNumber(phoneNumber);
        if (checkStringIsNotValid(phoneNumber)) {
            return Optional.empty();
        }

        Profile profile = profileService.findByPhoneNumber(phoneNumber);
        if (profile != null) {
            User user = profile.getUser();
            if (user != null) {
                String firstName = user.getFirstName().trim();

                List<WalletAccount> walletAccountList = walletAccountService.findByAccountOwnerPhoneNumber(phoneNumber);
                for (WalletAccount walletAccount : walletAccountList) {
                    if (walletAccount.getAccountName().trim().equalsIgnoreCase(firstName)) {
                        return Optional.of(walletAccount);
                    }
                }
            }
        }
        return Optional.empty();

    }

    public Optional<WalletAccount> getPrimaryWalletByPhoneNumberAndScheme(String phoneNumber, String schemeId) {

        phoneNumber = formatPhoneNumber(phoneNumber);
        if (checkStringIsNotValid(phoneNumber)) {
            return Optional.empty();
        }

        Profile profile = profileService.findByPhoneNumber(phoneNumber);
        if (profile != null) {
            User user = profile.getUser();
            if (user != null) {
                String firstName = user.getFirstName().trim();

                List<WalletAccount> walletAccountList =
                    walletAccountService.findByAccountOwnerPhoneNumberAndScheme_SchemeID(phoneNumber, schemeId);
                out.println("Wallet ACcount retrieved ===>>>  "+walletAccountList);
                for (WalletAccount walletAccount : walletAccountList) {
                    if (walletAccount.getAccountName().trim().equalsIgnoreCase(firstName)) {
                        return Optional.of(walletAccount);
                    }
                }
            }
        }
        return Optional.empty();

    }

    public Optional<String> getCashConnectRefNo(String phoneNumber) {

        out.println("TrackingRef Account Owner phoneNumber ==> " + phoneNumber);

        Optional<WalletAccount> primaryWallet = getPrimaryWalletByPhoneNumber(phoneNumber);

        out.println("Account Owner primary wallet ==> " + primaryWallet);

        if (primaryWallet.isPresent()) {

            Profile profile = profileService.findByPhoneNumber(phoneNumber);

            WalletAccount walletAccount = primaryWallet.get();
            out.println("WalletAccount for TrackingRef  ==> " + walletAccount);

            out.println("BVN is valid ==> " + profile.getBvn());

            CashConnectAccountRequestDTO requestDTO = new CashConnectAccountRequestDTO();

            requestDTO.setAccountType("individualSavings");
            String address = "Ikeja, Lagos state";
            String state = "Lagos";

            if (StringUtils.isEmpty(profile.getAddress())) {
                address = profile.getAddress();
                out.println("address ====== " + address);

            }

            List<AddressDTO> addresses = addressService.findByAddressOwner(profile.getPhoneNumber());

            if (!addresses.isEmpty()) {
                log.info("Getting Nuban account Addresses ==> " + addresses);

                if (!addresses.isEmpty()) {
                    address = addresses.get(0).getAddress();
                    state = addresses.get(0).getState();

                    log.info("Getting nuban account State ==> " + state);

                }
            }

            requestDTO.setAddress(address);
            requestDTO.setBvn(profile.getBvn());
            requestDTO.setDateOfBirth(String.valueOf(profile.getDateOfBirth()));
            if (profile.getUser() != null) {

                requestDTO.setEmail(profile.getUser().getEmail());
                requestDTO.setFullname(profile.getFullName());
                if (Gender.MALE.equals(profile.getGender())) {
                    requestDTO.setGender(0);
                } else if (Gender.FEMALE.equals(profile.getGender())) {
                    requestDTO.setGender(1);
                }

                requestDTO.setIsControlAccount(0);
                requestDTO.setLastname(profile.getUser().getLastName());
                requestDTO.setOthernames(profile.getUser().getFirstName());

                requestDTO.setPhoneNo(profile.getPhoneNumber());

                out.println("requestDTO ===> " + requestDTO);

                if (StringUtils.isNotEmpty(state)) {
                    requestDTO.setState(state);

                    out.println("REQUEST_DTO ====> " + requestDTO);
                    try {

                        log.info("Before API call ===> ");

                        if (checkStringIsValid(walletAccount.getTrackingRef())) {
                            requestDTO.setTransactionTrackingRef(walletAccount.getTrackingRef());
                        } else {

                            String transactionTrackingRef = getUniqueCashConnectTransRef();

                            requestDTO.setTransactionTrackingRef(transactionTrackingRef);

                            walletAccount.setTrackingRef(transactionTrackingRef);
                            WalletAccount save1 = walletAccountService.save(walletAccount);

                            log.info("Updated Wallet Account ===>  " + save1);
                        }

                        ResponseEntity<GenericResponseDTO> responseEntity = cashConnectService
                            .CreateNewAccount(requestDTO);

                        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                            GenericResponseDTO body = responseEntity.getBody();

                            log.info("After API call ===> ");

                            if (body != null) {

                                String data = (String) body.getData();
                                log.info("data ++++++++++ " + data);
                                CashConnectAccountResponseDTO cashConnectAccountResponseDTO = new ObjectMapper()
                                    .readValue(data, CashConnectAccountResponseDTO.class);

                                log.info("CashConnectAccountResponseDTO ++++++++++ " + cashConnectAccountResponseDTO);
                                if (cashConnectAccountResponseDTO != null) {
                                    boolean isSuccessful = cashConnectAccountResponseDTO.getIsSuccessful();
                                    if (isSuccessful) {
                                        String transactionTrackingRef = cashConnectAccountResponseDTO
                                            .getTransactionTrackingRef();
                                        log.info("Account TransRef ==> " + transactionTrackingRef);

                                        walletAccount.setTrackingRef(transactionTrackingRef);
                                        WalletAccount save = walletAccountService.save(walletAccount);
                                        log.info("Updated account with transRef ==> " + save);

                                        return Optional.of(transactionTrackingRef);

                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                        out.println("Error Nuban Acct API CAll " + e.getLocalizedMessage());

                    }
                }

            }

            log.info("TransRef is not null  ====> " + walletAccount.getTrackingRef());

        }

        return Optional.empty();

    }

    public Optional<String> retrieveCashConnectAccountNo(String phoneNumber) {

        out.println("NUBAN Account Owner phoneNumber ==> " + phoneNumber);

        Optional<WalletAccount> primaryWallet = getPrimaryWalletByPhoneNumber(phoneNumber);

        out.println("NUBAN Account Owner primary wallet ==> " + primaryWallet);

        if (primaryWallet.isPresent()) {

            Profile profile = profileService.findByPhoneNumber(phoneNumber);

            WalletAccount walletAccount = primaryWallet.get();
            out.println("NUBAN Acct wallet ===> " + walletAccount);

            if (profile != null && checkStringIsValid(walletAccount.getTrackingRef())
                && checkStringIsNotValid(walletAccount.getNubanAccountNo())) {

                out.println("Retrieving account number from TrackingRef ====> " + walletAccount.getTrackingRef());

                try {

                    ResponseEntity<GenericResponseDTO> responseEntity = cashConnectService
                        .retrieveAccountNumber(walletAccount.getTrackingRef());

                    if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {

                        GenericResponseDTO body = responseEntity.getBody();
                        if (body != null) {

                            String data = (String) body.getData();
                            log.info("data ++++++++++ " + data);
                            CashConnectAccountNumberResponse cashConnectAccountNumberResponse = new ObjectMapper()
                                .readValue(data, CashConnectAccountNumberResponse.class);

                            log.info("cashConnectAccountNumberResponse ++++++++++ " + cashConnectAccountNumberResponse);
                            if (cashConnectAccountNumberResponse != null) {
                                String NUBAN = cashConnectAccountNumberResponse.getNuban();

                                if (checkStringIsValid(NUBAN)) {
                                    log.info("Account NUBAN ==> " + NUBAN);

                                    walletAccount.setNubanAccountNo(NUBAN);
                                    WalletAccount save = walletAccountService.save(walletAccount);
                                    log.info("Updated account with NubanAccountNo ==> " + save);

                                    return Optional.of(NUBAN);
                                }

                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("Error Nuban Acct API CAll " + e.getLocalizedMessage());
                }
            }

            walletAccount.setNubanAccountNo(null);
            WalletAccount save = walletAccountService.save(walletAccount);

            log.info("Reversed transactionRef " + save);
        }

        return Optional.empty();
    }

    public Optional<String> getWalletAccountNubanByAccountNumber(String accountNumber) {
        out.println("getWalletAccountNubanByAccountNumber  accountNumber ===> " + accountNumber);
        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
        out.println("Retrieved wallet account =====> + " + walletAccount);
        if (walletAccount != null) {
            Profile accountOwner = walletAccount.getAccountOwner();
            out.println("AccountOwner ===)+++> " + accountOwner);
            if (accountOwner != null) {
                return getWalletAccountNubanByPhoneNumber(accountOwner.getPhoneNumber());
            } else {
                return Optional.of(walletAccount.getNubanAccountNo());
            }
        }

        return Optional.empty();

    }

    public Optional<String> getWalletAccountNubanByPhoneNumber(String phoneNumber) {
        out.println("getWalletAccountNubanByPhoneNumber phonenumber --==+)))> " + phoneNumber);
        Optional<WalletAccount> walletAccountOptional = getPrimaryWalletByPhoneNumber(formatPhoneNumber(phoneNumber));
        out.println("getWalletAccountNubanByPhoneNumber walletAccountOptional ===> " + walletAccountOptional);
        if (walletAccountOptional.isPresent()) {
            WalletAccount primaryWallet = walletAccountOptional.get();
            out.println("getWalletAccountNubanByPhoneNumber primaryWallet ===<>  " + primaryWallet);
            if (checkStringIsValid(primaryWallet.getNubanAccountNo())) {
                return Optional.of(primaryWallet.getNubanAccountNo());
            }
        }

        return Optional.empty();
    }

    public void sendAlertEmailForAction(User user, String action, LocalDateTime dateTime) {

        Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
        if (asyncExecutor != null) {
            asyncExecutor.execute(() -> {
                try {

                    if (user != null) {
                        String phoneNumber = user.getLogin();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss a");
                        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
                        String format = dateTime.format(formatter);
                        String gmt = "(GMT+1)";
                        String format2 = dateTime.format(formatter2);

                        String date = format + ", " + gmt + " " + format2;
                        String tel = "+234 (0)16348010";

                        List<MyDevice> myDevices = myDeviceService.findByProfilePhoneNumber(phoneNumber);
                        String deviceName = "";
                        if (!myDevices.isEmpty()) {
                            MyDevice myDevice = myDevices.get(0);
                            deviceName = myDevice.getName();
                        }

                        String msg = "Dear " + user.getFirstName() + "," + "<br/>" + "<br/>" + "<br/>" + "The action " + action
                            + " just occurred on your Wallet at " + date;

                        if (checkStringIsValid(deviceName)) {
                            msg = msg + "from " + deviceName + ".";
                        }
                        msg = msg + "<br/>" + "<br/>" + "<br/>"
                            + "<p>If you did not initiate or request this, please call our customer service on +234 (0)16348010 or email pouchii@systemspecs.com.ng immediately.</p> "
                            + "<br/>" + "<br/>"
                            + "<p/>You can also restrict your account from any compromise by using *7000*13# from your registered phone number or *7000*registerednumber*13# from any other phone number.</p>"
                            + "<br/>" + "<br/>" + "<p>Thank you.</p>";

                        if (checkStringIsValid(user.getEmail())) {
                            sendEmail(user.getEmail(), action.toUpperCase(), msg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public double getTotalWalletsBalance(String accountOwnerPhoneNumber) {
        accountOwnerPhoneNumber = formatPhoneNumber(accountOwnerPhoneNumber);

        return walletAccountService.findByAccountOwnerPhoneNumber(accountOwnerPhoneNumber).stream()
            .mapToDouble(value -> Double.parseDouble(value.getActualBalance())).sum();

    }

    public void sendUnsuccessfulKYCEmailUpgrade(Profile profile, Integer currentKycLevel, Integer nextLevel, List<String> reasons) {
        User user = profile.getUser();
        if (user != null) {
            String email = user.getEmail();
            if (checkStringIsValid(email)) {
                StringBuilder msg = new StringBuilder();
                msg.append("Dear " + user.getFirstName() + "," + "<br/>" + "<br/>" + "<br/>" + "<br/>" + "<br/>"
                    + "This is to inform you that your KYC upgrade request from " + currentKycLevel + " to "
                    + nextLevel + " has been declined." + "<br/>" + "<br/>" + "<br/>"
                    + "Reason(s) for rejection: " + "<br/>" + "<ul>");

                for (String reason : reasons)
                    msg.append("<li> " + reason + "</li>");

                msg.append("</ul>" + "<br/>" + "<br/>" + "<br/>"
                    + "Kindly retry as applicable to enable you enjoy the benefits!" + "<br/>" + "<br/>" + "<br/>"
                    + "<b>" + "<i>"
                    + "You can also buy airtime, buy power, pay for TV subscription and pay other Billers with your wallet.</i></b>");

                log.info("Email to send: \n " + msg);

                try {
                    sendEmail(email, "KYC UPGRADE NOTIFICATION", msg.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendSuccessKYCEmailUpgrade(Profile profile, Integer currentKycLevel, Integer nextLevel) {
        log.debug("KYC Upgrade Successful Email");
        User user = profile.getUser();
        if (user != null) {
            String email = user.getEmail();
            if (checkStringIsValid(email)) {

                String msg = "Dear " + user.getFirstName() + "," + "<br/>" + "<br/>" + "<br/>" + "<br/>" + "<br/>"
                    + "Congratulations! You have been successfully upgraded from " + currentKycLevel + " to "
                    + nextLevel + "." + "<br/>" + "<br/>" + "<br/>" + "Now, you can enjoy the benefits! " + "<br/>"
                    + "<br/>" + "<br/>" + "<b>" + "<i>"
                    + "You can also buy airtime, buy power, pay for TV subscription and pay other Billers with your wallet.</i></b>";

                try {
                    sendEmail(email, "KYC UPGRADE NOTIFICATION", msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

//	public static EmailNotificationDTO sendEmailWithAttachment(EmailNotificationDTO e) {
//		ApplicationProperties a = new ApplicationProperties();
//		Properties props = new Properties();
//		props.put("mail.smtp.auth", "" + a.getMail().getStarttls());
//		props.put("mail.smtp.starttls.enable", "" + a.getMail().getStarttls());
//		props.put("mail.smtp.host", a.getMail().getHost());
//		props.put("mail.smtp.port", a.getMail().getPort());
//
//		// Get the Session object.
//		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(a.getMail().getUsername(), a.getMail().getPassword());
//			}
//		});
//
//		try {
//
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress(a.getMail().getFrom()));
//			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(e.getRecipient()));
//			message.setSubject(e.getTitle());
//
//			BodyPart messageBodyPart = new MimeBodyPart();
//			messageBodyPart.setContent(e.getContent(), "text/html");
//
//			Multipart multipart = new MimeMultipart();
//			multipart.addBodyPart(messageBodyPart);
//
//			for (String attachment : e.getAttachments()) {
//				MimeBodyPart attachmentPart = new MimeBodyPart();
//				attachmentPart.attachFile(new File(attachment));
//				multipart.addBodyPart(attachmentPart);
//			}
//			message.setContent(multipart);
//			// Send message
//			Transport.send(message);
//			e.setSent(true);
//		} catch (MessagingException | IOException ex) {
//			e.setSent(false);
//			throw new RuntimeException(ex);
//		}
//		return e;
//	}

    public EmailNotificationDTO sendEmailWithAttachment(EmailNotificationDTO e, String documentUrl) {
        log.debug("Sending Email with Attachments");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");

        LocalDateTime format = LocalDateTime.now();
        String now = format.format(dateFormatter);
        String time = format.format(timeFormatter);
        boolean saved = false;
        String documentName = "";
        String outputFileName = "";
        StringBuilder sb = new StringBuilder();
        log.debug("Sending Email with Attachments == 2");

        for (AttachmentDTO a : e.getAttachments()) {
            log.debug("Creating Attachment links");
            int i = 1;
            String uuid = UUID.randomUUID().toString();
            if (a.getDocFormat().equalsIgnoreCase(DocumentType.JPG.name())) {
                documentName = "customer-complaint-attachment-" + uuid + ".jpg";
            } else if (a.getDocFormat().equalsIgnoreCase(DocumentType.PDF.name())) {
                documentName = "customer-complaint-attachment-" + uuid + ".pdf";
            } else if (a.getDocFormat().equalsIgnoreCase(DocumentType.PNG.name())) {
                documentName = "customer-complaint-attachment-" + uuid + ".png";
            } else if (a.getDocFormat().equalsIgnoreCase(DocumentType.DOC.name())) {
                documentName = "customer-complaint-attachment-" + uuid + ".doc";
            } else if (a.getDocFormat().equalsIgnoreCase(DocumentType.DOCX.name())) {
                documentName = "customer-complaint-attachment-" + uuid + ".docx";
            }
            outputFileName = documentUrl + documentName;
            saved = saveImage(a.getEncodedString(), outputFileName);
            log.debug("Attachment:: " + outputFileName);
            if (saved)
                sb.append(
                    "<b><a href=https://wallet.remita.net/contact-us?docId=" + uuid + ">" + documentName + "</a></b><br>");
        }

        String msg = "<br/>" + "<br/>" + "<br/>" + "Date: " + now + "<br/>"
            + "Time: " + time + "<br/>" + "<br/>" + "<br/>" + "Customer Name: " + e.getCustomerName() + "<br/>"
            + "Email: " + e.getSender() + "<br/> Phone Number: " + e.getCustomerPhone() + "<br/>" + "Message: <br/>"
            + "" + e.getContent() + "<br/>" + "<br><br><br><br><br><br>"
            + "<b>======================================== FILE  ATTACHMENTS ===========================================</b><br><br><br>"
            + sb
            + "<b>=================================================================================================</b>";
        log.debug("Message::  " + msg);

        Map<String, String> emails = getContactEmailMap();

        for (Map.Entry<String, String> entry : emails.entrySet()) {
            sendEmail(entry.getValue(), "CUSTOMER " + e.getTitle(), msg);
        }

        e.setSent(true);
        return e;
    }

    public Optional<String> getPrimaryWalletId(List<WalletAccount> schemeWalletAccounts, User user) {
        return getCustomerRef(schemeWalletAccounts, user);
    }

    public Optional<String> getPrimaryWalletId(String phoneNumber, Scheme scheme, User user) {
        phoneNumber = formatPhoneNumber(phoneNumber);
        List<WalletAccount> schemeWalletAccounts =
            walletAccountRepository.findAllByAccountOwnerPhoneNumberAndSchemeAndStatusNot(
                phoneNumber,
                scheme,
                AccountStatus.ACTIVE
            );

        return getCustomerRef(schemeWalletAccounts, user);
    }

    @NotNull
    private Optional<String> getCustomerRef(List<WalletAccount> schemeWalletAccounts, User user) {
        List<WalletAccount> primaryWalletList = schemeWalletAccounts.stream()
            .filter(walletAccount -> walletAccount.getAccountName().equalsIgnoreCase(user.getFirstName().trim()))
            .collect(Collectors.toList());
        if (!primaryWalletList.isEmpty()) {
            Optional<WalletAccount> primaryWallet = getPrimaryWallet(primaryWalletList);
            if (primaryWallet.isPresent()) {
                WalletAccount walletAccount = primaryWallet.get();
                return Optional.of(String.valueOf(walletAccount.getId()));
            }
        } else if (!schemeWalletAccounts.isEmpty()) {
            return Optional.of(String.valueOf(schemeWalletAccounts.get(0).getId()));
        }

        return Optional.empty();
    }

    @Transactional
    public void createPolarisAccount(
        User user,
        Profile profile, List<Address> addresses,
        List<WalletAccount> schemeWalletAccounts,
        Scheme scheme
    ) {
        out.println("Inside createPolarisAccount");
        if (profile != null && !schemeWalletAccounts.isEmpty()) {

            Optional<String> customerRefOptional = getPrimaryWalletId(schemeWalletAccounts, user);
            Optional<WalletAccount> primaryWalletOptional = getPrimaryWallet(schemeWalletAccounts);
            WalletAccount primaryWallet;
            primaryWallet = primaryWalletOptional.orElseGet(() -> schemeWalletAccounts.get(0));
            out.println("1 Inside createPolarisAccount primaryWallet=="+primaryWallet );

            String trackingRef = primaryWallet.getTrackingRef();
            if (checkStringIsNotValid(trackingRef) || trackingRef.length() < 20) {
                PolarisVulteOpenAccountDTO accountDTO = new PolarisVulteOpenAccountDTO();
                if (customerRefOptional.isPresent()) {
                    out.println("2 Inside createPolarisAccount customerRefOptional=="+customerRefOptional );
                    accountDTO.setCustomerRef(customerRefOptional.get());
                    out.println("3 Inside createPolarisAccount accountDTO=="+accountDTO );
                } else {
                    accountDTO.setCustomerRef(String.valueOf(schemeWalletAccounts.get(0).getId()));
                    out.println("4 Inside createPolarisAccount accountDTO=="+accountDTO );
                }
                accountDTO.setFirstname(user.getFirstName());
                accountDTO.setLastName(user.getLastName());
                accountDTO.setBvn(profile.getBvn());
                accountDTO.setPhoneNumber(returnPhoneNumberFormat(user.getLogin()));
                accountDTO.setAddressLine1(profile.getAddress());
                accountDTO.setNameOnAccount(profile.getFullName());
                accountDTO.setEmail(user.getEmail());
                Gender gender = profile.getGender();
                if (gender != null) {
                    accountDTO.setGender(gender);
                }
                out.println("5a Inside createPolarisAccount accountDTO=="+accountDTO );
                if (profile.getDateOfBirth() != null) {
                    accountDTO.setDateOfBirth(profile.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    out.println("5b Inside createPolarisAccount accountDTO=="+accountDTO );
                }
                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    accountDTO.setAddressLine1(address.getAddress());
                    accountDTO.setAddressLine2(address.getLocalGovt());
                    accountDTO.setCity(address.getCity());
                    accountDTO.setState(address.getState());
                    accountDTO.setCountry("Nigeria");
                }
                out.println("6 Inside createPolarisAccount accountDTO=="+accountDTO );
                accountDTO.setApiKey(scheme.getApiKey());
                accountDTO.setSecretKey(scheme.getSecretKey());
                out.println("Login Open virtual account DTO ==> " + accountDTO);
                try {
                    GenericResponseDTO genericResponseDTO = polarisVulteService.openVirtualAccount(accountDTO);

                    out.println("Login Open virtual account DTO response ==> " + genericResponseDTO);

                    if (genericResponseDTO != null && HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                        try {
                            PolarisVulteResponseDTO responseData = (PolarisVulteResponseDTO) genericResponseDTO.getData();

                            out.println("Login Open virtual account DTO responseData ==> " + responseData);

                            if (responseData != null && "Successful".equalsIgnoreCase(responseData.getStatus())) {
                                PolarisVulteData data = responseData.getData();

                                out.println("Login Open virtual account DTO PolarisVulteData ==> " + data);

                                Object providerResponseObject = data.getProviderResponse();
                                if (providerResponseObject != null) {

                                    out.println("Login open virtual account providerResponseObject==>" + providerResponseObject);

                                    String s = new ObjectMapper().writeValueAsString(providerResponseObject);
                                    out.println("Login open virtual account providerResponseString ==> " + s);

                                    OpenAccountProviderResponse providerResponse = new ObjectMapper().readValue(s,
                                        OpenAccountProviderResponse.class);
                                    out.println("Login Open virtual account DTO providerResponse ==> " + providerResponse);

                                    String accountNumber = providerResponse.getAccountNumber();
                                    String accountReference = providerResponse.getAccountReference();

                                    out.println("Account number ===> " + accountNumber + " ==> Account reference " + accountReference);
                                    List<WalletAccount> newWalletAccounts = new ArrayList<>();
                                    for (WalletAccount walletAccount : schemeWalletAccounts) {
                                        out.println("Login Open virtual account Initial wallet ==> " + walletAccount);
                                        walletAccount.setTrackingRef(accountReference);
                                        walletAccount.setNubanAccountNo(accountNumber);
                                        out.println("Login Open virtual account Initial wallet b4 save ==> " + walletAccount);
                                        WalletAccount saved = walletAccountService.save(walletAccount);

                                        out.println("Login Open virtual account Saved wallet account ==> " + saved);
                                        newWalletAccounts.add(walletAccount);
                                    }
                                    out.println("Login Open virtual account newWalletAccounts ==> " + newWalletAccounts);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.println("error creating polaris virtual account");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("Exception openVirtualAccount ====> " + e.getLocalizedMessage());
                }
            }
        }
    }


    public void createPolarisAccount(String phoneNumber, String schemeId) {
        out.println("Inside createPolarisAccount");
        phoneNumber = formatPhoneNumber(phoneNumber);
        Profile profile = profileService.findByPhoneNumber(phoneNumber);

        List<WalletAccount> schemeWalletAccounts = walletAccountService.findByAccountOwnerPhoneNumberAndScheme_SchemeID(phoneNumber, schemeId);
        if (profile == null) {
            throw new GenericException("Profile does not exist");
        }
        User user = profile.getUser();

        List<AddressDTO> byAddressOwner = addressService.findByAddressOwner(phoneNumber);
        List<Address> addresses = addressMapper.toEntity(byAddressOwner);

        Scheme scheme = schemeService.findBySchemeID(schemeId);

        if (!schemeWalletAccounts.isEmpty() && user != null && scheme != null) {

            Optional<String> customerRefOptional = getPrimaryWalletId(schemeWalletAccounts, user);
            Optional<WalletAccount> primaryWalletOptional = getPrimaryWallet(schemeWalletAccounts);
            WalletAccount primaryWallet;
            if (primaryWalletOptional.isPresent()) {
                primaryWallet = primaryWalletOptional.get();
            } else {
                primaryWallet = primaryWalletOptional.orElseGet(() -> schemeWalletAccounts.get(0));
            }
            String trackingRef = primaryWallet.getTrackingRef();
            if (checkStringIsNotValid(trackingRef) || trackingRef.length() < 20) {
                PolarisVulteOpenAccountDTO accountDTO = new PolarisVulteOpenAccountDTO();
                if (customerRefOptional.isPresent()) {
                    accountDTO.setCustomerRef(customerRefOptional.get());
                } else {
                    accountDTO.setCustomerRef(String.valueOf(schemeWalletAccounts.get(0).getId()));
                }
                accountDTO.setFirstname(user.getFirstName());
                accountDTO.setLastName(user.getLastName());
                accountDTO.setBvn(profile.getBvn());
                accountDTO.setPhoneNumber(returnPhoneNumberFormat(user.getLogin()));
                accountDTO.setAddressLine1(profile.getAddress());
                accountDTO.setNameOnAccount(profile.getFullName());
                accountDTO.setEmail(user.getEmail());
                Gender gender = profile.getGender();
                if (gender != null) {
                    accountDTO.setGender(gender);
                }
                if (profile.getDateOfBirth() != null) {
                    accountDTO.setDateOfBirth(profile.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    accountDTO.setAddressLine1(address.getAddress());
                    accountDTO.setAddressLine2(address.getLocalGovt());
                    accountDTO.setCity(address.getCity());
                    accountDTO.setState(address.getState());
                    accountDTO.setCountry("Nigeria");
                }
                accountDTO.setApiKey(scheme.getApiKey());
                accountDTO.setSecretKey(scheme.getSecretKey());
                out.println("Login Open virtual account DTO ==> " + accountDTO);
                try {
                    GenericResponseDTO genericResponseDTO = polarisVulteService.openVirtualAccount(accountDTO);

                    out.println("Login Open virtual account DTO response ==> " + genericResponseDTO);

                    if (genericResponseDTO != null && HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                        try {
                            PolarisVulteResponseDTO responseData = (PolarisVulteResponseDTO) genericResponseDTO.getData();

                            out.println("Login Open virtual account DTO responseData ==> " + responseData);

                            if (responseData != null && "Successful".equalsIgnoreCase(responseData.getStatus())) {
                                PolarisVulteData data = responseData.getData();

                                out.println("Login Open virtual account DTO PolarisVulteData ==> " + data);

                                Object providerResponseObject = data.getProviderResponse();
                                if (providerResponseObject != null) {

                                    out.println("Login open virtual account providerResponseObject==>" + providerResponseObject);

                                    String s = new ObjectMapper().writeValueAsString(providerResponseObject);
                                    out.println("Login open virtual account providerResponseString ==> " + s);

                                    OpenAccountProviderResponse providerResponse = new ObjectMapper().readValue(s,
                                        OpenAccountProviderResponse.class);
                                    out.println("Login Open virtual account DTO providerResponse ==> " + providerResponse);

                                    String accountNumber = providerResponse.getAccountNumber();
                                    String accountReference = providerResponse.getAccountReference();

                                    out.println("Account number ===> " + accountNumber + " ==> Account reference " + accountReference);
                                    List<WalletAccount> newWalletAccounts = new ArrayList<>();
                                    for (WalletAccount walletAccount : schemeWalletAccounts) {
                                        out.println("Login Open virtual account Initial wallet ==> " + walletAccount);
                                        walletAccount.setTrackingRef(accountReference);
                                        walletAccount.setNubanAccountNo(accountNumber);
                                        out.println("Login Open virtual account Initial wallet b4 save ==> " + walletAccount);
                                        WalletAccount saved = walletAccountService.save(walletAccount);

                                        out.println("Login Open virtual account Saved wallet account ==> " + saved);
                                        newWalletAccounts.add(walletAccount);
                                    }
                                    out.println("Login Open virtual account newWalletAccounts ==> " + newWalletAccounts);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.println("error creating polaris virtual account");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("Exception openVirtualAccount ====> " + e.getLocalizedMessage());
                }
            }
        }
    }

    public double getVatFee(double charges) {
        return charges * vatFeePercentage;

    }

    public String getPolarisCardRequestUniqueRef() {

        String ref = null;

        try {

            ThreadLocalRandom random = ThreadLocalRandom.current();
            long code = random.nextLong(100_000_000L);
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String[] add = dateTime.format(dateTimeFormatter).split(" ");
            String[] date = add[0].split("-");
            String totalDate = date[0] + date[1] + date[2];

            String[] time = add[1].split(":");
            String totalTime = time[0] + time[1] + time[2];

            long totalDateTime = Long.parseLong(totalDate + totalTime);

            ref = "Remita-" + (code + totalDateTime);


        }catch (Exception e){
            e.printStackTrace();
        }

        return ref;

    }

    public String getUniqueLoanId() {

        String ref = null;

        try {

            ThreadLocalRandom random = ThreadLocalRandom.current();
            long code = random.nextLong(100_000_0L);
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String[] add = dateTime.format(dateTimeFormatter).split(" ");
            String[] date = add[0].split("-");
            String totalDate = date[0] + date[1] + date[2];

            String[] time = add[1].split(":");
            String totalTime = time[0] + time[1] + time[2];

            long totalDateTime = Long.parseLong(totalDate + totalTime);

            ref = Long.toString(code + totalDateTime);


        }catch (Exception e){
            e.printStackTrace();
        }

        return ref;

    }

    public FundDTO sanitize (FundDTO fundDTO){
        if(fundDTO.getAccountNumber().equalsIgnoreCase("4640983710")
            && fundDTO.getSourceBankCode().equalsIgnoreCase("SPECS")){
            fundDTO.setAccountNumber("2213523032");
        }
        return fundDTO;
    }

    public WalletAccountDTO walletAccountResponse(WalletAccount walletAccount){
        if (walletAccount == null){
            return null;
        }
        WalletAccountDTO walletAccountDTO = new WalletAccountDTO();
        walletAccountDTO.setAccountName(walletAccount.getAccountName());
        walletAccountDTO.setAccountNumber(walletAccount.getAccountNumber());
        walletAccountDTO.setActualBalance(Double.parseDouble(walletAccount.getActualBalance()));
        walletAccountDTO.setCurrentBalance(Double.parseDouble(walletAccount.getCurrentBalance()));
        walletAccountDTO.setNubanAccountNo(walletAccount.getNubanAccountNo());

        return walletAccountDTO;
    }

}
