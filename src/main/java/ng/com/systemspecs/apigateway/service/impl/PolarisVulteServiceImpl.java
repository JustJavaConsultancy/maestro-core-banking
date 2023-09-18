package ng.com.systemspecs.apigateway.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ng.com.systemspecs.apigateway.domain.Journal;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.Scheme;
import ng.com.systemspecs.apigateway.domain.User;
import ng.com.systemspecs.apigateway.domain.enumeration.*;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.systemspecs.apigateway.service.exception.GenericException;
import ng.com.systemspecs.apigateway.service.kafka.producer.TransProducer;
import ng.com.systemspecs.apigateway.util.PolarisUtils;
import ng.com.systemspecs.apigateway.util.PolarisVulteUtils;
import ng.com.systemspecs.apigateway.util.Utility;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.lang.System.out;

@Service
public class PolarisVulteServiceImpl implements PolarisVulteService {

    private static final String SUCCESSFUL = "Successful";
    private static final String FAILED = "Failed";
    private static final String WAITING_FOR_OTP = "WaitingForOTP";
    private static final String PENDING_VALIDATION = "PendingValidation";
    private final Utility utility;
    private final PolarisVulteUtils polarisVulteUtils;
    private final ProfileService profileService;
    private final WalletAccountService walletAccountService;
    private final JournalService journalService;
    private final TransProducer producer;
    private final SchemeService schemeService;
    private  final PolarisUtils polarisUtils;

    @Value("${app.constants.dfs.correspondence-account}")
    private String CORRESPONDENCE_ACCOUNT;
    @Value("${app.vulte.mock-mode}")
    private String mockMode;
    @Value("${app.vulte.service_url}")
    private String serviceUrl;
    @Value("${app.vulte.validate_url}")
    private String validateUrl;
    @Value("${app.polaris-card.collection-account}")
    private String collectionAccounturl;

    public PolarisVulteServiceImpl(Utility utility, PolarisVulteUtils polarisVulteUtils, ProfileService profileService, WalletAccountService walletAccountService, JournalService journalService, TransProducer producer, SchemeService schemeService, PolarisUtils polarisUtils) {
        this.utility = utility;
        this.polarisVulteUtils = polarisVulteUtils;
        this.profileService = profileService;
        this.walletAccountService = walletAccountService;
        this.journalService = journalService;
        this.producer = producer;
        this.schemeService = schemeService;
        this.polarisUtils = polarisUtils;
    }

    @Override
    public GenericResponseDTO getBalance(String accountNumber, String schemeId) throws Exception {
        String uniqueTransRef = utility.getUniqueTransRef();

        Scheme scheme = getSchemeOrThrow(schemeId);

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, scheme.getApiKey(), scheme.getSecretKey());

        Optional<ProfileDTO> currentUserOptional = profileService.findByUserIsCurrentUser();
        if (!currentUserOptional.isPresent()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid current user profile");
        }

        PolarisVulteCustomer customer = buildPolarisCustomerFromProfile(currentUserOptional.get());

        String authSecure = polarisVulteUtils.generateAuthSecure(accountNumber, scheme.getSecretKey());
        String requestType = PolarisVulteRequestType.GET_BALANCE.getName();
        String authType = PolarisVulteAuthType.BANK_ACCOUNT.getName();
        String transactionDescription = "Get balance";

        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, authSecure, requestType, authType,
            transactionDescription, 0.00, customer, null, "Polaris");

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);

        return buildResponse(genericResponseDTO);
    }

    @NotNull
    private Scheme getSchemeOrThrow(String schemeId) {
        Scheme scheme = schemeService.findBySchemeID(schemeId);

        if (scheme == null) {
            throw new GenericException("Invalid SchemeId");
        }
        return scheme;
    }

    @Override
    public GenericResponseDTO getStatement(String accountNumber, String startDate, String endDate, String schemeId) throws Exception {
        String uniqueTransRef = utility.getUniqueTransRef();

        Scheme scheme = getSchemeOrThrow(schemeId);

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, scheme.getApiKey(), scheme.getSecretKey());

        Optional<ProfileDTO> currentUserOptional = profileService.findByUserIsCurrentUser();
        if (!currentUserOptional.isPresent()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid current user profile");
        }

        PolarisVulteCustomer customer = buildPolarisCustomerFromProfile(currentUserOptional.get());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(startDate, dateTimeFormatter);
        } catch (Exception e) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid start date format " + startDate, null);
        }
        try {
            LocalDate.parse(endDate, dateTimeFormatter);
        } catch (Exception e) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid end date format " + endDate, null);
        }
        PolarisGetStatementDetails details = new PolarisGetStatementDetails(startDate, endDate);

        String authSecure = polarisVulteUtils.generateAuthSecure(accountNumber, scheme.getSecretKey());
        String requestType = PolarisVulteRequestType.GET_STATEMENT.getName();
        String authType = PolarisVulteAuthType.BANK_ACCOUNT.getName();
        String transactionDescription = "Get Statement";

        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, authSecure, requestType, authType,
            transactionDescription, 0.00, customer, details, "Polaris");

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);

        if (!HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
            return buildResponse(genericResponseDTO);
        }

        PolarisVulteResponseDTO responseData = (PolarisVulteResponseDTO) genericResponseDTO.getData();
        out.println("Get Account statements responseData ==> " + responseData);

        if ("Successful".equalsIgnoreCase(responseData.getStatus())) {
            PolarisVulteData data = responseData.getData();
            out.println("Get Account statements PolarisVulteData ==> " + data);
            Object providerResponseObject = data.getProviderResponse();

            if (providerResponseObject != null) {
                out.println("Get Account statements providerResponseObject==>" + providerResponseObject);

                String s = new ObjectMapper().writeValueAsString(providerResponseObject);
                out.println("Get Account statements providerResponseString ==> " + s);

                PolarisGetStatementsResponse providerResponse = new ObjectMapper().readValue(s,
                    PolarisGetStatementsResponse.class);
                out.println("Get Account statements providerResponse ==> " + providerResponse);

                return new GenericResponseDTO("00", HttpStatus.OK, "success", providerResponse);
            }
        }

        return genericResponseDTO;
    }

    private PolarisVulteCustomer buildPolarisCustomerFromProfile(ProfileDTO profileDTO) {
        User user = profileDTO.getUser();
        String sourceAccountFirstName = null, sourceAccountSurname = null, email = null, customerRef = null;
        if (user != null) {
            sourceAccountSurname = user.getLastName();
            sourceAccountFirstName = user.getFirstName();
            email = user.getEmail();
            customerRef = String.valueOf(user.getId());
        }
        PolarisVulteCustomer customer = buildPolarisVulteCustomer(profileDTO.getPhoneNumber(),
            sourceAccountFirstName, sourceAccountSurname, email, customerRef);
        return customer;
    }

    @Override
    public GenericResponseDTO fundTransfer(PolarisVulteFundTransferDTO transferDTO) throws Exception {
        System.out.println("Entering Polaris fundTransfer endpoint ==> " + transferDTO);
        String uniqueTransRef = utility.getUniqueTransRef();
        System.out.println("Polaris fundTransfer uniqueTransRef ==> " + uniqueTransRef);

        String secretKey = transferDTO.getSecretKey();
        String apiKey = transferDTO.getApiKey();

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, apiKey, secretKey);

        Double amount = transferDTO.getAmount();
        String destinationAccountNumber = transferDTO.getDestinationAccountNumber();
        String sourceAccountNumber = transferDTO.getAccountNumber();
        String sourceAccountFirstName = transferDTO.getSourceAccountFirstName();
        String sourceAccountSurname = transferDTO.getSourceAccountLastName();
        String destinationBankCode = transferDTO.getDestinationBankCode();
        String email = transferDTO.getEmail();
        String phoneNumber = transferDTO.getPhoneNumber();

        String authSecure = polarisVulteUtils.generateAuthSecure(sourceAccountNumber, secretKey);
        System.out.println("Polaris fundTransfer authSecure ==> " + authSecure);

        String requestType = PolarisVulteRequestType.FUND_TRANSFER.getName();
        String authType = PolarisVulteAuthType.BANK_ACCOUNT.getName();
        String transactionDescription = "Fund transfer";

        PolarisVulteCustomer customer = buildPolarisVulteCustomer(phoneNumber,
            sourceAccountFirstName, sourceAccountSurname, email, transferDTO.getCustomerRef());
        System.out.println("Polaris fundTransfer customer ==> " + customer);

        DestinationAccountDetails details = buildDestinationAccountDetails(destinationBankCode, destinationAccountNumber);

        System.out.println("Polaris fundTransfer details ==> " + details);

        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, authSecure, requestType, authType,
            transactionDescription, amount, customer, details, "Polaris");

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);

        GenericResponseDTO response = buildResponse(genericResponseDTO);
        if (response.getStatus().isError()) {
            utility.sendEmail(
                "moronkola@systemspecs.com",
                "Polaris Fund Transfer failure",
                "Message ===> " + genericResponseDTO
            );
        }
        return response;
    }

    @Override
    public GenericResponseDTO disburse(PolarisVulteFundTransferDTO transferDTO) throws Exception {
        System.out.println("Entering Polaris disburse endpoint ==> " + transferDTO);
        String uniqueTransRef = transferDTO.getTransRef();
        System.out.println("Polaris Disburse uniqueTransRef " + uniqueTransRef);

        String secretKey = transferDTO.getSecretKey();
        String apiKey = transferDTO.getApiKey();

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, apiKey, secretKey);

        String destinationAccountNumber = transferDTO.getDestinationAccountNumber();
        String sourceAccountFirstName = transferDTO.getSourceAccountFirstName();
        String sourceAccountSurname = transferDTO.getSourceAccountLastName();
        String phoneNumber = transferDTO.getPhoneNumber();
        Double amount = transferDTO.getAmount();
        String destinationBankCode = transferDTO.getDestinationBankCode();
        String email = transferDTO.getEmail();

        String authSecure = null;
        System.out.println("Polaris disburse Auth secure ===> " + authSecure);

        String requestType = PolarisVulteRequestType.DISBURSE.getName();
        String transactionDescription = transferDTO.getTransactionDescription();

        PolarisVulteCustomer customer = buildPolarisVulteCustomer(phoneNumber,
            sourceAccountFirstName, sourceAccountSurname, email, transferDTO.getCustomerRef());

        System.out.println("Polaris Disburse customer ==> " + customer);

        DestinationAccountDetails details = buildDestinationAccountDetails(destinationBankCode, destinationAccountNumber);
        System.out.println("Polaris Disburse details ==> " + details);

        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, null, requestType, null,
            transactionDescription, amount, customer, details, "Polaris");
        System.out.println("Polaris Disburse request ==> " + request);

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);
        System.out.println("Polaris Disburse genericResponseDTO ==> " + genericResponseDTO);

        GenericResponseDTO response = buildResponse(genericResponseDTO);

        if (response.getStatus().isError()) {
            utility.sendEmail(
                "temire@systemspecs.com.ng",
                "Polaris Disburse failure",
                "Message ===> " + genericResponseDTO +"<br> Request ===> "+request
            );
        }
        return response;
    }

    @Override
    public GenericResponseDTO collect(PolarisVulteFundTransferDTO transferDTO) throws Exception {
        String uniqueTransRef = utility.getUniqueTransRef();

        String secretKey = transferDTO.getSecretKey();
        String apiKey = transferDTO.getApiKey();

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, apiKey, secretKey);

        String requestType = PolarisVulteRequestType.COLLECT.getName();
        String transactionDescription = "Collect";

        String sourceAccountFirstName = transferDTO.getSourceAccountFirstName();
        String sourceAccountSurname = transferDTO.getSourceAccountLastName();
        String sourceAccountNumber = transferDTO.getAccountNumber();
        String customerRef = transferDTO.getCustomerRef();
        String phoneNumber = transferDTO.getPhoneNumber();
        Double amount = transferDTO.getAmount();
        String email = transferDTO.getEmail();

        PolarisVulteCustomer customer = buildPolarisVulteCustomer(phoneNumber,
            sourceAccountFirstName, sourceAccountSurname, email, customerRef);

        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, null, requestType, null,
            transactionDescription, amount, customer, null, "Polaris");

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);

        return buildResponse(genericResponseDTO);

    }

    @Override
    public GenericResponseDTO bvnLookupMin(ValidateBVNDTO validateBVNDTO, String schemeId) throws Exception {

        return bvnLookup(validateBVNDTO, PolarisVulteRequestType.LOOKUP_BVN_MIN.getName(), "Lookup bvn min", schemeId);
    }

    @Override
    public GenericResponseDTO bvnLookupMid(ValidateBVNDTO validateBVNDTO, String schemeId) throws Exception {
        return bvnLookup(validateBVNDTO, PolarisVulteRequestType.LOOKUP_BVN_MID.getName(), "Lookup bvn mid", schemeId);
    }


    @Override
    public GenericResponseDTO bvnLookupMax(ValidateBVNDTO validateBVNDTO, String schemeId) throws Exception {

        return bvnLookup(validateBVNDTO, PolarisVulteRequestType.LOOKUP_BVN_MAX.getName(), "Lookup bvn max", schemeId);
    }

    @Override
    public GenericResponseDTO lookupAccountMax(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception {
        return lookupAccount(lookupAccountDTO, PolarisVulteRequestType.LOOKUP_ACCOUNT_MAX.getName(), "Lookup account " +
            "max");
    }


    @Override
    public GenericResponseDTO lookupAccountMid(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception {
        return lookupAccount(lookupAccountDTO, PolarisVulteRequestType.LOOKUP_ACCOUNT_MID.getName(), "Lookup account " +
            "mid");
    }

    @Override
    public GenericResponseDTO lookupAccountMin(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception {
        return lookupAccount(lookupAccountDTO, PolarisVulteRequestType.LOOKUP_ACCOUNT_MIN.getName(), "Lookup account " +
            "min");
    }

    @Override
    public GenericResponseDTO getAccountsMin(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception {
        return getAccounts(lookupAccountDTO, PolarisVulteRequestType.GET_ACCOUNT_MIN.getName(), "Get accounts min");
    }

    @Override
    public GenericResponseDTO getAccountsMid(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception {
        return getAccounts(lookupAccountDTO, PolarisVulteRequestType.GET_ACCOUNT_MID.getName(), "Get accounts mid");
    }

    @Override
    public GenericResponseDTO getAccountsMax(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception {
        return getAccounts(lookupAccountDTO, PolarisVulteRequestType.GET_ACCOUNT_MAX.getName(), "Get accounts max");
    }

    @Override
    public GenericResponseDTO lookUpNuban(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception {
        String uniqueTransRef = utility.getUniqueTransRef();

        Scheme scheme = schemeService.findBySchemeID(lookupAccountDTO.getSchemeId());

        String secretKey = scheme.getSecretKey();
        String apiKey = scheme.getApiKey();

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, apiKey, secretKey);

        String phoneNumber = lookupAccountDTO.getPhoneNumber();
        String sourceAccountFirstName = lookupAccountDTO.getSourceAccountFirstName();
        String sourceAccountLastName = lookupAccountDTO.getSourceAccountLastName();
        String email = lookupAccountDTO.getEmail();
        String accountNumber = lookupAccountDTO.getAccountNumber();

        String requestType = PolarisVulteRequestType.LOOKUP_NUBAN.getName();
        String transactionDescription = "Lookup nuban";

        String authSecure = polarisVulteUtils.generateAuthSecure(accountNumber, secretKey);
        String authType = PolarisVulteAuthType.BANK_ACCOUNT.getName();

        PolarisVulteCustomer customer = buildPolarisVulteCustomer(phoneNumber,
            sourceAccountFirstName, sourceAccountLastName, email, lookupAccountDTO.getCustomerRef());

        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, authSecure, requestType, authType,
            transactionDescription, 0.00, customer, null, "Polaris");

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);

        return buildResponse(genericResponseDTO);
    }

    @Override
    public GenericResponseDTO getBanks(String schemeId, HttpSession session) throws Exception {
        String uniqueTransRef = utility.getUniqueTransRef();

        Scheme scheme = getSchemeOrThrow(schemeId);

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, scheme.getApiKey(), scheme.getSecretKey());

        String requestType = PolarisVulteRequestType.GET_BANKS.getName();

        String phoneNumber = (String) session.getAttribute("phoneNumber");

        Profile profile = profileService.findByPhoneNumber(phoneNumber);
        User user = profile.getUser();

        PolarisVulteCustomer customer = buildPolarisVulteCustomer(phoneNumber,
            user.getFirstName(), user.getLastName(), user.getEmail(), phoneNumber);

        String description = "Get List of Banks";
        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, null, requestType, null,
            description, 0.00, customer, null, "Polaris");

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);

        return buildResponse(genericResponseDTO);
    }

    @Override
    public GenericResponseDTO getBranch(String schemeId, HttpSession session) throws Exception {
        String uniqueTransRef = utility.getUniqueTransRef();

        Scheme scheme = getSchemeOrThrow(schemeId);

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, scheme.getApiKey(), scheme.getSecretKey());

        String requestType = PolarisVulteRequestType.GET_BRANCH.getName();

        String phoneNumber = (String) session.getAttribute("phoneNumber");

        Profile profile = profileService.findByPhoneNumber(phoneNumber);
        User user = profile.getUser();

        PolarisVulteCustomer customer = buildPolarisVulteCustomer(phoneNumber,
            user.getFirstName(), user.getLastName(), user.getEmail(), phoneNumber);

        String description = "Get List of Branch";
        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, null, requestType, null,
            description, 0.00, customer, null, "Polaris");

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);

        return buildResponse(genericResponseDTO);
    }

    @Override
    public GenericResponseDTO openAccount(PolarisVulteOpenAccountDTO accountDTO) throws Exception {
        return openNewAccount(accountDTO, PolarisVulteRequestType.OPEN_ACCOUNT.getName(), "Open new account");
    }

    @Override
    public GenericResponseDTO openVirtualAccount(PolarisVulteOpenAccountDTO accountDTO) throws Exception {
        System.out.println("Entering Polaris openVirtualAccount  ===> " + accountDTO);
        String uniqueTransRef = utility.getUniqueTransRef();
        System.out.println("Polaris openVirtualAccount uniqueTransRef ===> " + uniqueTransRef);

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, accountDTO.getApiKey(), accountDTO.getSecretKey());
        System.out.println("Polaris openVirtualAccount headers ===> " + headers);

        String phoneNumber = accountDTO.getPhoneNumber();
        String firstname = accountDTO.getFirstname();
        String surname = accountDTO.getLastName();
        String email = accountDTO.getEmail();
        String customerRef = accountDTO.getCustomerRef();
        String description = "Open new virtual account";
        String requestType = PolarisVulteRequestType.OPEN_ACCOUNT.getName();

        String authType = null;
        String authSecure = null;

        PolarisVulteCustomer customer = buildPolarisVulteCustomer(phoneNumber,
            firstname, surname, email, customerRef);

        System.out.println("Polaris openVirtualAccount customer ===> " + customer);

        PolarisVulteOpenAccountDetails details = buildOpenAccountDetails(accountDTO);
        System.out.println("Polaris openVirtualAccount details ===> " + details);

        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, authSecure, requestType, authType,
            description, 0.0, customer, details, "PolarisVirtual");
        System.out.println("Polaris openVirtualAccount request ===> " + request);

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);
        System.out.println("Polaris openVirtualAccount response ===> " + genericResponseDTO);

        GenericResponseDTO response = buildResponse(genericResponseDTO);

        if (response.getStatus().isError()) {
            utility.sendEmail(
                "moronkola@systemspecs.com",
                "Polaris Open virtual account failure",
                "Message ===> " + genericResponseDTO
            );
        }
        return response;
    }

    @Override
    public GenericResponseDTO openWallet(PolarisVulteOpenAccountDTO accountDTO) throws Exception {
        return openNewAccount(accountDTO, PolarisVulteRequestType.OPEN_WALLET.getName(), "Open new wallet");
    }

    @Override
    public GenericResponseDTO webHook(PolarisVulteWebHookRequest request) throws Exception {

        out.println("Notification request data ==++++> " + request);

        FundDTO fundDTO = new FundDTO();
        if (request != null) {

            WebHookDetails details = request.getDetails();
            double amount = details.getAmountInKobo() / 100;
            String customerFirstName = details.getCustomerFirstName();
            String customerMobileNo = utility.formatPhoneNumber(details.getCustomerMobileNo());
            String customerSurnameName = details.getCustomerSurnameName();
            String transactionRef = details.getTransactionRef();
            String customerRef = details.getCustomerRef();
            String transactionType = details.getTransactionType();
            String requestType = request.getRequestType();
            String mock_mode = request.getMock_mode();
            Object data = request.getData();

            if ("transaction_notification".equalsIgnoreCase(requestType)
                && "Live".equalsIgnoreCase(mock_mode)
                && "collect".equalsIgnoreCase(transactionType)
            ) {
                WalletAccountDTO walletAccountDto;
                try {
                    Optional<WalletAccountDTO> walletAccountOptional = walletAccountService.findOne(Long.valueOf(customerRef));
                    walletAccountDto = walletAccountOptional.orElseGet(() -> walletAccountService.findAllByAccountOwnerPhoneNumber(customerMobileNo).get(0));
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("Could not convert customerRef");
                    walletAccountDto = walletAccountService.findAllByAccountOwnerPhoneNumber(customerMobileNo).get(0);
                }

                if (walletAccountDto != null) {
                    String destinationAccountNumber = walletAccountDto.getAccountNumber();
                    fundDTO = utility.buildFundDTO(
                        CORRESPONDENCE_ACCOUNT,
                        destinationAccountNumber,
                        amount,
                        customerSurnameName + " " + customerFirstName,
                        "Polaris Notification", customerMobileNo,
                        SpecificChannel.FUND_WALLET_POLARIS.getName(),
                        "BankToWallet");
                    fundDTO.setNarration("Funding of wallet " + destinationAccountNumber + " with the sum of " + utility.formatMoney(amount));

                    List<Journal> byExternalRef = journalService.findByExternalRef(transactionRef);
                    if (byExternalRef.isEmpty()) {
                        fundDTO.setRrr(transactionRef);
                        out.println("Sending "+fundDTO+" \n to Kafka inside polaris webhook");
                        producer.send(fundDTO);
                    }
                }
            }
        }
        return new GenericResponseDTO("00", HttpStatus.OK, "success", null);
    }

    @Override
    public GenericResponseDTO validateTransaction(String otp, String requestType, String transactionRef, String schemeId) throws Exception {
        String uniqueTransRef = utility.getUniqueTransRef();

        Scheme scheme = getSchemeOrThrow(schemeId);

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, scheme.getApiKey(), scheme.getSecretKey());

        String authSecure = polarisVulteUtils.generateAuthSecure(otp, scheme.getSecretKey());

        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, authSecure, requestType, null,
            null, 0.00, null, null, "Polaris");

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);

        return buildResponse(genericResponseDTO);
    }

    @Override
    public GenericResponseDTO queryTransaction(String requestType, String transactionRef, String schemeId) throws Exception {
        String uniqueTransRef = utility.getUniqueTransRef();

        Scheme scheme = getSchemeOrThrow(schemeId);

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, scheme.getApiKey(), scheme.getSecretKey());

        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, null, requestType, null,
            null, 0.00, null, null, "Polaris");

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);

        return buildResponse(genericResponseDTO);
    }

    private GenericResponseDTO openNewAccount(PolarisVulteOpenAccountDTO accountDTO, String name, String description) throws Exception {
        String uniqueTransRef = utility.getUniqueTransRef();

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, accountDTO.getApiKey(), accountDTO.getSecretKey());

        String phoneNumber = accountDTO.getPhoneNumber();
        String firstname = accountDTO.getFirstname();
        String surname = accountDTO.getLastName();
        String email = accountDTO.getEmail();
        String customerRef = accountDTO.getCustomerRef();
        String authType = PolarisVulteAuthType.BVN.getName();
        String authSecure = polarisVulteUtils.generateAuthSecure(accountDTO.getBvn(), accountDTO.getSecretKey());

        PolarisVulteCustomer customer = buildPolarisVulteCustomer(phoneNumber,
            firstname, surname, email, customerRef);

        PolarisVulteOpenAccountDetails details = buildOpenAccountDetails(accountDTO);

        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, authSecure, name, authType,
            description, 0.00, customer, details, "Polaris");

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);

        GenericResponseDTO response = buildResponse(genericResponseDTO);
        if (response.getStatus().isError()) {
            utility.sendEmail(
                "temire@systemspecs.com.ng",
                "Polaris Disburse failure",
                "Message ===> " + genericResponseDTO +"<br> Request ===> "+request
            );
        }
        return response;
    }


    private GenericResponseDTO getAccounts(PolarisVulteFundTransferDTO lookupAccountDTO, String requestType, String description) {
        String uniqueTransRef = utility.getUniqueTransRef();


        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, lookupAccountDTO.getApiKey(),
            lookupAccountDTO.getSecretKey());

        String phoneNumber = lookupAccountDTO.getPhoneNumber();
        String sourceAccountFirstName = lookupAccountDTO.getSourceAccountFirstName();
        String sourceAccountLastName = lookupAccountDTO.getSourceAccountLastName();
        String email = lookupAccountDTO.getEmail();

        PolarisVulteCustomer customer = buildPolarisVulteCustomer(phoneNumber,
            sourceAccountFirstName, sourceAccountLastName, email, lookupAccountDTO.getCustomerRef());

        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, null, requestType, null,
            description, 0.00, customer, null, "Polaris");

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);

        return buildResponse(genericResponseDTO);
    }

    private GenericResponseDTO lookupAccount(PolarisVulteFundTransferDTO lookupAccountDTO,
                                             String requestType, String description) throws Exception {
        String uniqueTransRef = utility.getUniqueTransRef();

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, lookupAccountDTO.getApiKey(), lookupAccountDTO.getSecretKey());

        String accountNumber = lookupAccountDTO.getAccountNumber();
        String authSecure = polarisVulteUtils.generateAuthSecure(accountNumber, lookupAccountDTO.getSecretKey());
        String authType = PolarisVulteAuthType.BANK_ACCOUNT.getName();

        String phoneNumber = lookupAccountDTO.getPhoneNumber();
        String sourceAccountFirstName = lookupAccountDTO.getSourceAccountFirstName();
        String sourceAccountLastName = lookupAccountDTO.getSourceAccountLastName();
        String email = lookupAccountDTO.getEmail();

        PolarisVulteCustomer customer = buildPolarisVulteCustomer(phoneNumber,
            sourceAccountFirstName, sourceAccountLastName, email, lookupAccountDTO.getCustomerRef());

        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, authSecure, requestType, authType,
            description, 0.00, customer, null, "Polaris");

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);

        return buildResponse(genericResponseDTO);
    }

    private GenericResponseDTO bvnLookup(ValidateBVNDTO validateBVNDTO, String requestType, String description, String schemeId) throws Exception {
        String uniqueTransRef = utility.getUniqueTransRef();

        Scheme scheme = getSchemeOrThrow(schemeId);

        HttpHeaders headers = polarisVulteUtils.getHeaders(uniqueTransRef, scheme.getApiKey(), scheme.getSecretKey());

        String bvn = validateBVNDTO.getBvn();
        String dateOfBirth = validateBVNDTO.getDateOfBirth();
        String phoneNumber = validateBVNDTO.getPhoneNumber();
        String firstName = validateBVNDTO.getFirstName();
        String lastName = validateBVNDTO.getLastName();


        String authSecure = polarisVulteUtils.generateAuthSecure(bvn, scheme.getSecretKey());
        String authType = PolarisVulteAuthType.BVN.getName();

        DateOfBirthDetails details = new DateOfBirthDetails();
        details.setDob(dateOfBirth);

        PolarisVulteCustomer customer = buildPolarisVulteCustomer(phoneNumber,
            firstName, lastName, null, null);

        PolarisVulteRequestDTO request = buildPolarisVulteRequest(uniqueTransRef, authSecure, requestType, authType,
            description, 0.00, customer, details, "Polaris");

        GenericResponseDTO genericResponseDTO = polarisVulteUtils.invokeVulteApi(request, headers, serviceUrl);

        return buildResponse(genericResponseDTO);

    }

    private String getDestinationAccountNumber(String channel, String accountNumber) {
        String destinationAccountNumber;
        if ("WalletToWallet".equalsIgnoreCase(channel)) {
            destinationAccountNumber = getNubanAccountNumberOrThrow(accountNumber);
        } else {
            destinationAccountNumber = accountNumber;
        }
        return destinationAccountNumber;
    }

    private DestinationAccountDetails buildDestinationAccountDetails(String destBankCode,
                                                                     String destinationAccountNumber) {
        DestinationAccountDetails details = new DestinationAccountDetails();
        details.setDestinationAccount(destinationAccountNumber);
        details.setDestinationBankCode(destBankCode);
        details.setOtpOverride(true);
        return details;
    }

    private String getNubanAccountNumberOrThrow(String accountNumber) {
        Optional<String> accountNumberOptional = utility.getWalletAccountNubanByAccountNumber(accountNumber);

        if (!accountNumberOptional.isPresent()) {
            throw new GenericException("Invalid source account number");
        }

        return accountNumberOptional.get();
    }

    private PolarisVulteCustomer buildPolarisVulteCustomer(String phoneNumber,
                                                           String sourceAccountFirstName,
                                                           String sourceAccountSurname,
                                                           String email, String customerRef) {
        PolarisVulteCustomer customer = new PolarisVulteCustomer();
        if (utility.checkStringIsValid(customerRef)) {
            customer.setCustomerRef(customerRef);
        } else {
            customer.setCustomerRef(phoneNumber);
        }
        customer.setFirstname(sourceAccountFirstName);
        customer.setSurname(sourceAccountSurname);
        customer.setEmail(email);
        customer.setMobileNo(phoneNumber);
        return customer;
    }

    private PolarisVulteRequestDTO buildPolarisVulteRequest(
        String uniqueTransRef,
        String authSecure,
        String requestType,
        String authType,
        String transactionDescription,
        Double amount,
        PolarisVulteCustomer customer,
        Object details,
        String provider
    ) {
        double amountInKobo = amount * 100;
        BigDecimal stripTrailingZerosAmountInKobo = BigDecimal.valueOf(amountInKobo).stripTrailingZeros();


        PolarisVulteAuth auth = new PolarisVulteAuth();
        auth.setAuthProvider(provider);
        auth.setSecure(authSecure);
        auth.setType(authType);
        auth.setRouteMode(null);

        PolarisVulteMeta polarisVulteMeta = new PolarisVulteMeta();
        polarisVulteMeta.setAccountCurrency("NGN");

        PolarisVulteTransaction transaction = new PolarisVulteTransaction();
        transaction.setMockMode(mockMode);
        transaction.setTransactionRef(utility.getUniqueTransRef());
        transaction.setTransactionDesc(transactionDescription);
        transaction.setTransactionRefParent(null);
        transaction.setAmount(stripTrailingZerosAmountInKobo);
        transaction.setCustomer(customer);
        transaction.setMeta(polarisVulteMeta);
        transaction.setDetails(details);

        PolarisVulteRequestDTO request = new PolarisVulteRequestDTO();
        request.setRequestRef(uniqueTransRef);
        request.setRequestType(requestType);
        request.setAuth(auth);
        request.setTransaction(transaction);
        return request;
    }

    private GenericResponseDTO buildResponse(GenericResponseDTO genericResponseDTO) {
        if (genericResponseDTO != null) {
            if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                try {
                    PolarisVulteResponseDTO response = (PolarisVulteResponseDTO) genericResponseDTO.getData();
                    if (response != null) {
                        String status = response.getStatus();
                        PolarisVulteData data = response.getData();

                        if (SUCCESSFUL.equalsIgnoreCase(status) && "00".equalsIgnoreCase(data.getProviderRespondeCode())) {
                            return new GenericResponseDTO("00", HttpStatus.OK, response.getMessage(), response.getProviderResponse());
                        } else if (WAITING_FOR_OTP.equalsIgnoreCase(status)) {
                            return new GenericResponseDTO("00", HttpStatus.OK, response.getMessage(), null);
                        } else if (FAILED.equalsIgnoreCase(status)) {
                            Object error = data.getError();
                            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, response.getMessage(), error);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("buildResponse ==> " + e.getMessage());

                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Failed");
                }
            }
            return genericResponseDTO;
        } else {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Failed");
        }
    }

    private PolarisVulteOpenAccountDetails buildOpenAccountDetails(PolarisVulteOpenAccountDTO accountDTO) {
        String nameOnAccount = (accountDTO.getFirstname() + " " + accountDTO.getLastName()).trim();
        if (utility.checkStringIsValid(accountDTO.getMiddleName())) {
            nameOnAccount = nameOnAccount + " " + accountDTO.getMiddleName();
        }
        String title = "Mr";
        if (accountDTO.getGender() != null) {
            if (Gender.FEMALE.getName().equalsIgnoreCase(accountDTO.getGender().getName())) {
                title = "Mrs";
            }
        }

        PolarisVulteOpenAccountDetails details = new PolarisVulteOpenAccountDetails();
        details.setNameOnAccount(nameOnAccount);
        details.setMiddlename(accountDTO.getMiddleName());
        details.setDob(accountDTO.getDateOfBirth());
        if (accountDTO.getGender() != null) {
            details.setGender(accountDTO.getGender().getName());
        } else {
            details.setGender(Gender.MALE.getName());
        }
        details.setTitle(title);
        details.setAddressLine1(accountDTO.getAddressLine1());
        details.setAddressLine2(accountDTO.getAddressLine2());
        details.setCity(accountDTO.getCity());
        details.setState(accountDTO.getState());
        details.setCountry(accountDTO.getCountry());

        return details;
    }

    @Override
    public GenericResponseDTO openCollectionAccount(PolarisCollectionAccountRequestDTO polarisCollectionAccountRequestDTO) throws Exception {

        GenericResponseDTO genericResponseDTO = polarisUtils.callPolarisApi(collectionAccounturl, polarisCollectionAccountRequestDTO, HttpMethod.POST);
        return genericResponseDTO;
    }

    @Override
    public GenericResponseDTO getAuth() throws Exception {

        GenericResponseDTO genericResponseDTO = polarisUtils.getAuth();
        return genericResponseDTO;
    }


}
