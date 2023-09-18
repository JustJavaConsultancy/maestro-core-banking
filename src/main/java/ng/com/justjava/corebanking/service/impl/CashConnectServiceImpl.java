package ng.com.justjava.corebanking.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ng.com.justjava.corebanking.repository.ProfileRepository;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.kafka.producer.TransProducer;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.SpecificChannel;
import ng.com.justjava.corebanking.service.CashConnectService;
import ng.com.justjava.corebanking.service.JournalService;
import ng.com.justjava.corebanking.service.TransactionLogService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.justjava.corebanking.util.CashConnectUtils;
import ng.com.justjava.corebanking.util.RemitaCarmelUtils;
import ng.com.justjava.corebanking.util.Utility;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.lang.System.out;

@Service
@Transactional
public class CashConnectServiceImpl implements CashConnectService {

    private static final String CASH_CONNECT_BANK_CODE = "865";

    @Value("${app.cashconnect.account-number}")
    private String CASHCONNECT_VIRTUAL_ACCOUNT_NUMBER;
    @Value("${app.cashconnect.get-bvn-url}")
    private String getBvnURL;
    @Value("${app.cashconnect.base-url}")
    private String baseURL;
    private final CashConnectUtils cashConnectUtils;
    private final RestTemplate restTemplate;
    private final WalletAccountService walletAccountService;
    private final TransProducer producer;
    private final TransactionLogService transactionLogService;
    private final JournalService journalService;
    private final Utility utility;
    private final RemitaCarmelUtils remitaCarmelUtils;
    private final ProfileRepository profileRepository;

    private final Environment environment;
    @Value("${app.cashconnect.account-upgrade-url}")
    private String upgradeTierURL;
    @Value("${app.cashconnect.account-upgrade-kyc-url}")
    private String uploadKycURL;
    @Value("${app.cashconnect.account-statement-url}")
    private String accountStatementURL;
    @Value("${app.cashconnect.account-number-url}")
    private String getAccountNumberURL;
    @Value("${app.constants.dfs.correspondence-account}")
    private String CORRESPONDENCE_ACCOUNT;
    @Value("${app.cashconnect.account-summary-url}")
    private String getAccountSummaryURL;
    @Value("${app.cashconnect.account-info-url}")
    private String getAccountInfoURL;
    @Value("${app.cashconnect.get-bank-list-url}")
    private String getBankListURL;
    @Value("${app.cashconnect.new-account-url}")
    private String newAccountURL;
    @Value("${app.cashconnect.get-fund-intra-url}")
    private String getFundIntraURL;
    @Value("${app.cashconnect.get-fund-interbank-url}")
    private String getFundInterBankURL;
    @Value("${app.cashconnect.get-fund-transfer-status-url}")
    private String getFundTransferStatusURL;
    @Value("${app.cashconnect.loan-offer-url}")
    private String loanOfferURL;
    @Value("${app.cashconnect.accept-loan-url}")
    private String acceptLoanURL;
    @Value("${app.cashconnect.loan-payment-notification-url}")
    private String loanPaymentNotificationURL;
    @Value("${app.cashconnect.get-loan-status-url}")
    private String getLoanStatusURL;
    @Value("${app.cashconnect.get-credit-report-url}")
    private String getCreditReportURL;
    @Value("${app.cashconnect.get-credit-report-phone-url}")
    private String getCreditReportByPhoneURL;
    @Value("${app.cashconnect.register-webhook-url}")
    private String registerWebHookURL;
    @Value("${app.constants.dfs.customer-correspondence-acct}")
    private String CUSTOMER_CORRESPONDENCE_ACCT;

    public CashConnectServiceImpl(CashConnectUtils cashConnectUtils, RestTemplate restTemplate, @Lazy WalletAccountService walletAccountService, TransProducer producer, TransactionLogService transactionLogService, JournalService journalService, Utility utility,
                                  Environment environment, RemitaCarmelUtils remitaCarmelUtils, ProfileRepository profileRepository) {
        this.cashConnectUtils = cashConnectUtils;
        this.restTemplate = restTemplate;
        this.walletAccountService = walletAccountService;
        this.producer = producer;
        this.transactionLogService = transactionLogService;
        this.journalService = journalService;
        this.utility = utility;
        this.environment = environment;
        this.remitaCarmelUtils = remitaCarmelUtils;
        this.profileRepository = profileRepository;
    }

    @Override
    public ResponseEntity<GenericResponseDTO> getBvn(String bvnNumber, HttpSession session) {

        String phone = (String) session.getAttribute("phoneNumber");

        String phoneNumber = utility.formatPhoneNumber(phone);

        Profile profile = profileRepository.findOneByPhoneNumber(phoneNumber);

        out.println(profile);

        if (profile == null){
            new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User Profile not found"), HttpStatus.BAD_REQUEST);
        }

        User user = profile.getUser();
        out.println(user);

        if (user == null){
            new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User not found"), HttpStatus.BAD_REQUEST);
        }

        RemitaBVNRequest remitaBVNRequest = new RemitaBVNRequest();
        remitaBVNRequest.setRequestReference("{{transRef}}");
        remitaBVNRequest.setBvn(bvnNumber);

        RemitaBVNResponse response = remitaCarmelUtils.bvnResponse(remitaBVNRequest);
        RemitaBVNResponseData data = (RemitaBVNResponseData) response.getData();

        String firstName = data.getFirstName();
        String lastName = data.getLastName();
        String dob = data.getDateOfBirth();
        LocalDate parse;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        parse = LocalDate.parse(dob, formatter);

        LocalDate currentUserDateOfBirth;

        out.println("Comparing === \n " +
            "Firstname: " + firstName + " === " + user.getFirstName() +
            "\nLastname: " + lastName + " === " + user.getLastName() +
            "\nphoneNumber: " + phoneNumber + " === " + utility.formatPhoneNumber(user.getLogin()) +
            "\ndateOfBirth: " + parse + " === " + profile.getDateOfBirth());

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
        if (firstName.equalsIgnoreCase(user.getFirstName())) {
            count++;
        } else {
            msg = msg + ", firstname ";
        }
        if (lastName.equalsIgnoreCase(user.getLastName())) {
            count++;
        } else {
            msg = msg + ", lastname";
        }
        if (utility.formatPhoneNumber(phoneNumber)
            .equalsIgnoreCase(utility.formatPhoneNumber(user.getLogin()))) {
            count++;
        } else {
            msg = msg + ", phoneNumber";

        }
        if (count < 3) {
            return new ResponseEntity<>(new GenericResponseDTO("99", msg, null), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "BVN Verification Successful", null), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<GenericResponseDTO> validateBVN(ValidateBVNDTO validateBVNDTO) {

            RemitaBVNRequest remitaBVNRequest = new RemitaBVNRequest();
            remitaBVNRequest.setRequestReference("{{transRef}}");
            remitaBVNRequest.setBvn(validateBVNDTO.getBvn());

            RemitaBVNResponse response = remitaCarmelUtils.bvnResponse(remitaBVNRequest);


            if (response != null) {

                try {

                    RemitaBVNResponseData data = (RemitaBVNResponseData) response.getData();

                    out.println("data ===>" + data);

                    String firstName = data.getFirstName();
                    String lastName = data.getLastName();
                    String phoneNumber = data.getPhoneNumber1();
                    String dateOfBirth = data.getDateOfBirth();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                    String currentUserDateOfBirth = validateBVNDTO.getDateOfBirth();
                    LocalDate parse;
                    LocalDate userDateOfBirth;

                    try {
                        parse = LocalDate.parse(dateOfBirth, formatter2);
                        userDateOfBirth = LocalDate.parse(currentUserDateOfBirth, formatter);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred while validating BVN", false), HttpStatus.BAD_REQUEST);
                    }

                    int count = 0;

                    out.println("Comparing ==== \n " +
                        "Firstname: " + firstName + " === " + validateBVNDTO.getFirstName() +
                        "\nLastname: " + lastName + " === " + validateBVNDTO.getLastName() +
                        "\nphoneNumber: " + phoneNumber + " === " + validateBVNDTO.getPhoneNumber() +
                        "\ndateOfBirth: " + parse + " === " + userDateOfBirth
                    );

                    if (userDateOfBirth.compareTo(parse) == 0) {
                        count++;
                    }

                    if (firstName.equalsIgnoreCase(validateBVNDTO.getFirstName())) {
                        count++;
                    }

                    if (lastName.equalsIgnoreCase(validateBVNDTO.getLastName())) {
                        count++;
                    }
                    if (utility.formatPhoneNumber(phoneNumber).equalsIgnoreCase(utility.formatPhoneNumber(validateBVNDTO.getPhoneNumber()))) {
                        count++;
                    }

                    out.println("Count ===> " + count);

                    if (count < 3) {
                        return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid BVN", false), HttpStatus.BAD_REQUEST);
                    } else {
                        return new ResponseEntity<>(new GenericResponseDTO("00", "Valid BVN", true), HttpStatus.OK);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred while validating BVN", false), HttpStatus.BAD_REQUEST);
    }

    private String getObjectAsJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }


    @Override
    public ResponseEntity<GenericResponseDTO> CreateNewAccount(CashConnectAccountRequestDTO requestDTO) {
        return callCashConnectAPI(newAccountURL, requestDTO);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> upgradeAccountKyc(UpgradeTierKycDTO requestDTO) {
        return callCashConnectAPI(uploadKycURL, requestDTO);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> upgradeAccountTier(UpgradeTierDTO requestDTO) {
        return callCashConnectAPI(upgradeTierURL, requestDTO);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> getAccountStatement(String accountNumber, String fromDate, String toDate) {

        HashMap<String, String> params = new HashMap<>();
        params.put("accountNumber", accountNumber);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);

        return callCashConnectRequestParamAPI(accountStatementURL, params);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> retrieveAccountNumber(String TransactionTrackingRef) {
        HashMap<String, String> params = new HashMap<>();
        params.put("TransactionTrackingRef", TransactionTrackingRef);
        return callCashConnectRequestParamAPI(getAccountNumberURL, params);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> getAccountSummary(String accountNumber) {
        HashMap<String, String> params = new HashMap<>();
        params.put("accountNumber", accountNumber);
        return callCashConnectRequestParamAPI(getAccountSummaryURL, params);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> getAccountInfo(String accountNumber) {
        HashMap<String, String> params = new HashMap<>();
        params.put("accountNumber", accountNumber);
        return callCashConnectRequestParamAPI(getAccountInfoURL, params);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> getBankList() {
        return callCashConnectRequestParamAPI(getBankListURL, null);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> fundIntra(FundIntraDTO fundIntraDTO) {
        return callCashConnectAPI(getFundIntraURL, fundIntraDTO);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> fundInterBank(FundIntraDTO fundIntraDTO) {
        return callCashConnectAPI(getFundInterBankURL, fundIntraDTO);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> getFundTransferStatus(String TransactionTrackingRef) {
        HashMap<String, String> params = new HashMap<>();
        params.put("TransactionTrackingRef", TransactionTrackingRef);
        return callCashConnectRequestParamAPI(getFundTransferStatusURL, params);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> getLoanOffers(LoanOfferDTO loanOfferDTO) {
        loanOfferDTO.setTransactionTrackingRef(utility.getUniqueTransRef());
        return callCashConnectAPI(loanOfferURL, loanOfferDTO);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> acceptLoanOffer(AcceptLoanDTO acceptLoanDTO) {
        return callCashConnectAPI(acceptLoanURL, acceptLoanDTO);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> loanPaymentNotification(LoanPaymentNotificationDTO notificationDTO) {
        return callCashConnectAPI(loanPaymentNotificationURL, notificationDTO);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> getLoanStatus(String LoanId, String accountNumber, String PhoneNo) {
        HashMap<String, String> params = new HashMap<>();
        params.put("LoanId", LoanId);
        params.put("accountNumber", accountNumber);
        params.put("PhoneNo", PhoneNo);

        return callCashConnectRequestParamAPI(getLoanStatusURL, params);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> getCreditReport(String bvn) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bvn", bvn);

        return callCashConnectRequestParamAPI(getCreditReportURL, params);

    }

    @Override
    public ResponseEntity<GenericResponseDTO> getCreditReportByPhone(String PhoneNo) {
        HashMap<String, String> params = new HashMap<>();
        params.put("PhoneNo", PhoneNo);

        return callCashConnectRequestParamAPI(getCreditReportURL, params);
    }


    @Override
    public ResponseEntity<GenericResponseDTO> transactionPaymentNotification(CashConnectWebHookDTO notificationDTO) {

        CashConnectWebHookDataDTO data = notificationDTO.getData();

        out.println("Notification data ==++++> " + data);

        FundDTO fundDTO = new FundDTO();
        if (data != null) {

            String direction = data.getDirection();
            String sessionId = data.getSessionId();

            CashConnectAccountDTO originator = data.getOriginator();
            CashConnectAccountDTO beneficiary = data.getBeneficiary();
            if (beneficiary != null && originator != null) {
                String sourceAccountName = "";
                String accountNumber = beneficiary.getAccountNumber();

                String bankCode = null;
                sourceAccountName = originator.getAccountName();
                bankCode = originator.getBankCode();


                if ("inflow".equalsIgnoreCase(direction) && !CASH_CONNECT_BANK_CODE.equalsIgnoreCase(bankCode)) {

                    List<WalletAccount> walletAccountOptional = walletAccountService.findByNubanAccountNo(accountNumber);

                    if (!walletAccountOptional.isEmpty()) {
                        Optional<WalletAccount> primaryWallet = utility.getPrimaryWallet(walletAccountOptional);

                        if (primaryWallet.isPresent()) {

                            WalletAccount walletAccount = primaryWallet.get();

                            String amount = data.getAmount();
                            double amountInDouble;
                            try {
                                amountInDouble = Double.parseDouble(amount);
                            } catch (Exception e) {
                                return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid amount (" + amount + ")"), HttpStatus.BAD_REQUEST);
                            }

                            String phoneNumber = "";
                            Profile accountOwner = walletAccount.getAccountOwner();


                            if (accountOwner != null) {
                                phoneNumber = accountOwner.getPhoneNumber();

                                String destinationAccountNumber = walletAccount.getAccountNumber();
                                fundDTO = utility.buildFundDTO(CUSTOMER_CORRESPONDENCE_ACCT, destinationAccountNumber, amountInDouble,
                                    walletAccount.getAccountFullName(), sourceAccountName, phoneNumber, SpecificChannel.FUND_WALLET_CASHCONNECT.getName(), "BankToWallet");

//                        fundDTO.setNarration("Transfer of " + utility.formatMoney(amountInDouble) + " from " + sourceAccountName + " (" + CORRESPONDENCE_ACCOUNT + ") to "
//                            + walletAccount.getAccountFullName() + " (" + destinationAccountNumber + ")");
                                fundDTO.setNarration("Funding of wallet " + destinationAccountNumber + " with the sum of " + utility.formatMoney(amountInDouble));

                                FundDTO byRrr = transactionLogService.findByRrr(sessionId);
                                if (byRrr == null) {
                                    fundDTO.setRrr(sessionId);
                                    producer.send(fundDTO);
                                }

                                return new ResponseEntity<>(new GenericResponseDTO("00", "success", "Payment received successfully"), HttpStatus.OK);

                            }
                        }
                        return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid destination wallet account profile", null), HttpStatus.BAD_REQUEST);

                    }
                    return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid wallet account", null), HttpStatus.BAD_REQUEST);


                } else if ("outflow".equalsIgnoreCase(direction) && originator != null) {
                    //todo

               /* String originatorAccountNumber = originator.getAccountNumber();

                List<WalletAccount> walletAccountOptional = walletAccountService.findByNubanAccountNo(originatorAccountNumber);
                if (!walletAccountOptional.isEmpty()) {
                    Optional<WalletAccount> primaryWallet = utility.getPrimaryWallet(walletAccountOptional);

                    if (primaryWallet.isPresent()) {

                        WalletAccount walletAccount = primaryWallet.get();

                        String amount = data.getAmount();
                        double amountInDouble;
                        try {
                            amountInDouble = Double.parseDouble(amount);
                        } catch (Exception e) {
                            return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid amount (" + amount + ")"), HttpStatus.BAD_REQUEST);
                        }

                        String phoneNumber = "";
                        Profile accountOwner = walletAccount.getAccountOwner();


                        if (accountOwner != null) {
                            phoneNumber = accountOwner.getPhoneNumber();

                            String sourceAccountNumber = walletAccount.getAccountNumber();
                            fundDTO = utility.buildFundDTO(sourceAccountNumber, CUSTOMER_CORRESPONDENCE_ACCT, amountInDouble,
                                walletAccount.getAccountFullName(), sourceAccountName, phoneNumber, SpecificChannel.FUND_WALLET.getName(), "walletToWallet");

//                        fundDTO.setNarration("Transfer of " + utility.formatMoney(amountInDouble) + " from " + sourceAccountName + " (" + CORRESPONDENCE_ACCOUNT + ") to "
//                            + walletAccount.getAccountFullName() + " (" + destinationAccountNumber + ")");
                            fundDTO.setNarration("Fund Transfer of the sum of " + utility.formatMoney(amountInDouble) + " by " + sourceAccountName + " (" + sourceAccountNumber + ")");

                            FundDTO byRrr = transactionLogService.findByRrr(sessionId);
                            if (byRrr == null) {
                                fundDTO.setRrr(sessionId);
                                producer.send(fundDTO);
                            }

                            return new ResponseEntity<>(new GenericResponseDTO("00", "success", "Payment received successfully"), HttpStatus.OK);
                        }
                    }
                }

                return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid Originator account", null), HttpStatus.BAD_REQUEST);*/

                    return new ResponseEntity<>(new GenericResponseDTO("00", "success", "Payment received successfully"), HttpStatus.OK);


                }

            }
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid payment notification request data", null), HttpStatus.BAD_REQUEST);

                /*String amount = data.getAmount();
                double amountInDouble;
                try {
                    amountInDouble = Double.parseDouble(amount);
                } catch (Exception e) {
                    return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid amount (" + amount + ")"), HttpStatus.BAD_REQUEST);
                }

                String date = data.getDate();

                String sessionId = data.getSessionId();
                CashConnectAccountDTO beneficiary = data.getBeneficiary();
                CashConnectAccountDTO originator = data.getOriginator();
                if (beneficiary != null && originator != null && amountInDouble > 0) {
                    String sourceAccountName = originator.getAccountName();
                    String sourceAccountNumber = originator.getAccountNumber();

                    String accountOwnerPhoneNumber = null;

//                WalletAccount sourceWalletAccount = walletAccountService.findOneByAccountNumber(sourceAccountNumber);
                    WalletAccount sourceWalletAccount = null;
                    Optional<WalletAccount> walletAccountOptional = walletAccountService.findByNubanAccountNo(sourceAccountNumber);
                    if (walletAccountOptional.isPresent()) {
                        sourceWalletAccount = walletAccountOptional.get();
                        Profile accountOwner = sourceWalletAccount.getAccountOwner();
                        if (accountOwner != null) {
                            accountOwnerPhoneNumber = accountOwner.getPhoneNumber();
                        }
                    } else {
//                    /return new ResponseEntity<>(new GenericResponseDTO("99", "failed", "Invalid originator account number"), HttpStatus.BAD_REQUEST);
                    }

                    String beneficiaryAccountName = beneficiary.getAccountName();
                    String beneficiaryAccountNumber = beneficiary.getAccountNumber();

                    Optional<WalletAccount> beneficiaryWalletAccountOptional = walletAccountService.findByNubanAccountNo(beneficiaryAccountNumber);
                    WalletAccount beneficiaryWalletAccount = null;
                    if (!beneficiaryWalletAccountOptional.isPresent()) {
//                    return new ResponseEntity<>(new GenericResponseDTO("99", "failed", "Invalid beneficiary account number"), HttpStatus.BAD_REQUEST);
                    } else {
                        beneficiaryWalletAccount = beneficiaryWalletAccountOptional.get();
                    }

                    fundDTO.setSourceAccountName(sourceAccountName);
                    fundDTO.setRedeemBonus(false);
                    fundDTO.setStatus(TransactionStatus.START);
                    fundDTO.setNarration("Transfer of " + utility.formatMoney(amountInDouble) + " from " + sourceAccountName + " (" + sourceAccountNumber + ") to " + beneficiaryAccountName + " (" + beneficiaryAccountNumber + ")");
                    fundDTO.setTransRef(utility.getUniqueTransRef());
                    fundDTO.setPhoneNumber(accountOwnerPhoneNumber);
                    fundDTO.setAmount(amountInDouble);
                    if (beneficiaryWalletAccount != null) {
                        fundDTO.setAccountNumber(beneficiaryWalletAccount.getAccountNumber());
                    }
                    fundDTO.setBeneficiaryName(beneficiaryAccountName);
                    fundDTO.setSpecificChannel(SpecificChannel.THIRD_PARTY.getName());
                    fundDTO.setRrr(sessionId);
                    fundDTO.setChannel("walletToWallet");
                    if (sourceWalletAccount != null) {
                        fundDTO.setSourceAccountNumber(sourceWalletAccount.getAccountNumber());
                    }
                *//*fundDTO.setDestBankCode();
                fundDTO.setSourceBankCode();
                fundDTO.setAgentRef();
                fundDTO.setPin();
                fundDTO.setShortComment();*//*

//              producer.send(fundDTO);

                    return new ResponseEntity<>(new GenericResponseDTO("00", "success", "Payment received successfully"), HttpStatus.OK);
                }

            }
        }

        return new ResponseEntity<>(new GenericResponseDTO("99", "Failed", null), HttpStatus.BAD_REQUEST);*/

    }

    @Override
    public ResponseEntity<GenericResponseDTO> registerWebhook(RegisterWebHookDTO registerWebHook) {
        return callCashConnectAPI(registerWebHookURL, registerWebHook);
    }

    @Override
    public GenericResponseDTO sendMoneyToBank(FundDTO fundDTO/*, Double intraFee, Double vatFee*/) {

        Double amountIndDouble = fundDTO.getAmount() + fundDTO.getBonusAmount();
        String accountNumber = fundDTO.getAccountNumber();

        String uniqueCashConnectTransRef;

        String sourceAccountNo = fundDTO.getSourceAccountNumber();
        String narrationStr = fundDTO.getNarration();
        String narration = StringUtils.substring(narrationStr, 0, 98);


        Optional<String> nubanByAccountNumberOptional = utility.getWalletAccountNubanByAccountNumber(sourceAccountNo);

        out.println("Customer nuban Optional ==> " + nubanByAccountNumberOptional);

        if (nubanByAccountNumberOptional.isPresent()) {

            String sourceAccountNuban = nubanByAccountNumberOptional.get();

            uniqueCashConnectTransRef = utility.getUniqueCashConnectTransRef();
            out.println("TransactionTrackingRef ==> " + uniqueCashConnectTransRef);

            FundIntraDTO fundInterDTO = new FundIntraDTO();
            fundInterDTO.setTransactionTrackingRef(uniqueCashConnectTransRef);
            fundInterDTO.setAmount(String.valueOf(amountIndDouble));
            fundInterDTO.setSourceAccount(sourceAccountNuban);
            fundInterDTO.setDestinationAccount(accountNumber);
            fundInterDTO.setDestinationAccountName(fundDTO.getBeneficiaryName());
            fundInterDTO.setDestinationBankCode(fundDTO.getDestBankCode());
            fundInterDTO.setNarration(narration);

            out.println("FundInterDTO for Virtual acct to BAnk ==>" + fundInterDTO);
            try {
                ResponseEntity<GenericResponseDTO> responseEntity = validateTransferAPICall(fundInterDTO, true);

                out.println("FundInterDTO response for Virtual acct to BAnk ==>" + responseEntity);

                if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {

                    return new GenericResponseDTO("00", HttpStatus.OK, "success", fundInterDTO.getTransactionTrackingRef());

                } else {
                    return responseEntity.getBody();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), null);
            }
        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid nuban account", null);

    }

    private GenericResponseDTO getTransactionStatusUsingTrackRef(String uniqueCashConnectTransRef) {
        ResponseEntity<GenericResponseDTO> fundTransferStatus = getFundTransferStatus(uniqueCashConnectTransRef);

        if (HttpStatus.OK.equals(fundTransferStatus.getStatusCode())) {
            GenericResponseDTO genericResponseDTO = fundTransferStatus.getBody();

            if (genericResponseDTO != null) {

                try {
                    String data = new ObjectMapper().writeValueAsString(genericResponseDTO.getData());

                    CashConnectTransferStatus cashConnectTransferStatus = new ObjectMapper().readValue(data, CashConnectTransferStatus.class);

                    out.println("Converted String to CashConnect Transfer Status ==>" + cashConnectTransferStatus);

                    String status = cashConnectTransferStatus.getStatus();
                    String statusCode = cashConnectTransferStatus.getStatusCode();
                    if (cashConnectTransferStatus.getMessage() != null &&
                        cashConnectTransferStatus.getMessage().trim().equalsIgnoreCase("Unable To Locate Record")) {
                        status = "success";
                        statusCode = "00";
                    }

                    //if ("success".equalsIgnoreCase(status) && "00".equalsIgnoreCase(statusCode)) {
                    if ("success".equalsIgnoreCase(status) && "00".equalsIgnoreCase(statusCode)) {
                        return new GenericResponseDTO("00", HttpStatus.OK, "success", cashConnectTransferStatus);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public GenericResponseDTO intraTransfer(String sourceAccount, String destinationAccount, String destinationAccountName,
                                            double amount, String narration) {
        FundIntraDTO intraDTO;
        String uniqueCashConnectTransRef = utility.getUniqueCashConnectTransRef();


        narration = StringUtils.substring(narration, 0, 98);

        intraDTO = new FundIntraDTO();
        intraDTO.setAmount(String.valueOf(amount));
        intraDTO.setDestinationAccount(destinationAccount);
        intraDTO.setDestinationAccountName(destinationAccountName);
        intraDTO.setNarration(narration);
        intraDTO.setSourceAccount(sourceAccount);
        intraDTO.setTransactionTrackingRef(uniqueCashConnectTransRef);

        out.println("Source account ==>" + sourceAccount);
        out.println("Destination account ==>" + destinationAccount);

        out.println("intraDTO From SourceAccount to Destination nuban ==>" + intraDTO);

        ResponseEntity<GenericResponseDTO> responseEntity1 = validateTransferAPICall(intraDTO, false);

        out.println("Request txn ==> " + intraDTO);
        out.println("Reversal txn ==> " + responseEntity1);

        if (HttpStatus.OK.equals(responseEntity1.getStatusCode())) {

            return new GenericResponseDTO("00", HttpStatus.OK, "success", responseEntity1.getBody());
        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", responseEntity1.getBody());

    }

    public GenericResponseDTO balanceCashConnectAccount(FundDTO fundDTO, double charges) {

        out.println("BalanceCashConnectAccount ===> {} " + fundDTO + " ==> " + charges);

        String channel = fundDTO.getChannel();

        String narration = fundDTO.getNarration();
        narration = StringUtils.substring(narration, 0, 98);

        if (channel.equalsIgnoreCase("BankToWallet")) {

            String uniqueCashConnectTransRef = utility.getUniqueCashConnectTransRef();

            Double amountIndDouble = fundDTO.getAmount();

            String accountNumber = fundDTO.getAccountNumber();

            Optional<String> nubanByAccountNumberOptional = utility.getWalletAccountNubanByAccountNumber(accountNumber);

            if (nubanByAccountNumberOptional.isPresent()) {

                String accountNo = nubanByAccountNumberOptional.get();

                FundIntraDTO intraDTO = new FundIntraDTO();
                intraDTO.setAmount(String.valueOf(amountIndDouble));
                intraDTO.setDestinationAccount(accountNo);
                intraDTO.setDestinationAccountName(fundDTO.getSourceAccountName());
                intraDTO.setNarration(narration);
                intraDTO.setSourceAccount(CASHCONNECT_VIRTUAL_ACCOUNT_NUMBER);
                intraDTO.setTransactionTrackingRef(uniqueCashConnectTransRef);

                out.println("FundIntraDTO for BankToWallet ====> " + intraDTO);

                ResponseEntity<GenericResponseDTO> fundIntraResponse = validateTransferAPICall(intraDTO, false);

                out.println("FundIntra response for BankToWallet ====> " + fundIntraResponse);


                return fundIntraResponse.getBody();
            }

        } else if (channel.equalsIgnoreCase("WalletToWallet")) {

            out.println("Inside Cash connect wallet to wallet {}");

            String uniqueCashConnectTransRef = utility.getUniqueCashConnectTransRef();

            Double amountIndDouble = fundDTO.getAmount() + charges;

            String sourceAccountNo = fundDTO.getSourceAccountNumber();

            Optional<String> nubanBySourceAccountNumberOptional = utility.getWalletAccountNubanByAccountNumber(sourceAccountNo);

            out.println("nubanBySourceAccountNumberOptional " + nubanBySourceAccountNumberOptional);

            Optional<String> nubanByDestinationAccountNumberOptional = utility.getWalletAccountNubanByAccountNumber(fundDTO.getAccountNumber());

            out.println("nubanByDestinationAccountNumberOptional " + nubanByDestinationAccountNumberOptional);


            String sourceNubanAccount = null;
            String destinationNubanAccount = null;

            if (nubanBySourceAccountNumberOptional.isPresent()) {
                sourceNubanAccount = nubanBySourceAccountNumberOptional.get();
            }

            if (nubanByDestinationAccountNumberOptional.isPresent()) {
                destinationNubanAccount = nubanByDestinationAccountNumberOptional.get();
            }


            List<String> walletToWalletSpecificChannels = utility.getWalletToWalletSpecificChannels();
            out.println(" walletToWalletSpecificChannels  ==> " + walletToWalletSpecificChannels);
            String specificChannel = fundDTO.getSpecificChannel().toLowerCase();
            out.println(" specificChannel  ==> " + specificChannel);

            if (walletToWalletSpecificChannels.contains(specificChannel)) {
                destinationNubanAccount = CASHCONNECT_VIRTUAL_ACCOUNT_NUMBER;
            }

            out.println("sourceNubanAccount ==> " + sourceNubanAccount + " destinationNubanAccount ==> " + destinationNubanAccount);

            /*if (SpecificChannel.LIBERTY.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())) {
                sourceNubanAccount = CASHCONNECT_VIRTUAL_ACCOUNT_NUMBER;
            }*/


            if (utility.checkStringIsValid(sourceNubanAccount, destinationNubanAccount)) {

                FundIntraDTO intraDTO = new FundIntraDTO();
                intraDTO.setAmount(String.valueOf(amountIndDouble));
                intraDTO.setDestinationAccount(destinationNubanAccount);
                intraDTO.setDestinationAccountName(fundDTO.getSourceAccountName());
                intraDTO.setNarration(narration);
                intraDTO.setSourceAccount(sourceNubanAccount);
                intraDTO.setTransactionTrackingRef(uniqueCashConnectTransRef);

                out.println("FundIntraDTO for WalletToWallet ====> " + intraDTO);

                ResponseEntity<GenericResponseDTO> fundIntraResponse = validateTransferAPICall(intraDTO, false);

                out.println("FundIntra Response for WalletToWallet ====> " + fundIntraResponse);

                return fundIntraResponse.getBody();

            } else {
                out.println("Source Nuban Account and Destination Nuban Account not Valid");

            }

        } else if (channel.equalsIgnoreCase("WalletToBank")) {

        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed");
    }

    @Override
    public ResponseEntity<SimpleResponseDTO> interbankCallBack(InterBankCallBackDTO interBankCallBackDTO) {

        if (interBankCallBackDTO != null) {
            if (!"00".equalsIgnoreCase(interBankCallBackDTO.getCode()) && "Successful".equalsIgnoreCase(interBankCallBackDTO.getMessage())) {

                String trackingRef = interBankCallBackDTO.getTrackingRef().trim();
                FundDTO fundDTO = transactionLogService.findByRrr(trackingRef);
                if (fundDTO != null) {
                    ReverseDTO reverseDTO = new ReverseDTO();
                    reverseDTO.setStatus("Completed");
                    reverseDTO.setTransRef(fundDTO.getTransRef());

                    GenericResponseDTO genericResponseDTO = journalService.reverseTransaction(reverseDTO);

                    out.println("Interbank reversal response " + genericResponseDTO);
                }

                return new ResponseEntity<>(new SimpleResponseDTO("00", "Accepted"), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(new SimpleResponseDTO("00", "Accepted"), HttpStatus.BAD_REQUEST);

            }
        }

        return new ResponseEntity<>(new SimpleResponseDTO("99", "Rejected"), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<GenericResponseDTO> validateTransferAPICall(FundIntraDTO fundIntraDTO, Boolean isInterBank) {
        ResponseEntity<GenericResponseDTO> responseEntity = null;
        out.println(" The  isInterBank=== " + isInterBank + " and the fundDTO== " + fundIntraDTO);
        if (Boolean.TRUE.equals(isInterBank)) {
            responseEntity = fundInterBank(fundIntraDTO);
        } else {
            responseEntity = fundIntra(fundIntraDTO);
        }

        out.println("transfer response object ====> " + responseEntity);

        GenericResponseDTO body = responseEntity.getBody();

        if (body != null) {

            Object data = body.getData();

            JSONObject jsonObject = new JSONObject(data);

            out.println("jsonObject ====> " + jsonObject);

            if (jsonObject.has("errors")) {

                try {
                    JSONArray errors = jsonObject.getJSONArray("errors");
                    if (errors.length() > 0) {
                        String o = errors.getString(0);
                        if ("TransactionTrackingRef already used, use unique reference".equalsIgnoreCase(o)) {
                            fundIntraDTO.setTransactionTrackingRef(utility.getUniqueCashConnectTransRef());
                            validateTransferAPICall(fundIntraDTO, isInterBank);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        GenericResponseDTO transactionStatus = getTransactionStatusUsingTrackRef(fundIntraDTO.getTransactionTrackingRef());
        out.println("Transaction status for  ==> "
            + fundIntraDTO.getTransactionTrackingRef()
            + " ===> " + transactionStatus);

        return responseEntity;
    }

    private ResponseEntity<GenericResponseDTO> callCashConnectRequestParamAPI(String URL, HashMap<String, String> params) {
        out.println("params initial value ===> " + params);

        HttpHeaders headers = cashConnectUtils.getAuthToken();
        headers.set("Content-Type", MediaType.TEXT_PLAIN_VALUE);
        headers.set("Accept", MediaType.TEXT_PLAIN_VALUE);

        HttpEntity<String> httpEntity = null;

        try {
            httpEntity = new HttpEntity<>(headers);

            out.println("Get obj HTTP entity ===> " + httpEntity);
            ResponseEntity<String> responseEntity = null;
            try {
                if (params == null) {
                    responseEntity = restTemplate.exchange(baseURL + URL, HttpMethod.GET, httpEntity, String.class);
                } else {
                    responseEntity = restTemplate.exchange(baseURL + URL, HttpMethod.GET, httpEntity, String.class, params);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace()), HttpStatus.BAD_REQUEST);
            }

            out.println("ResponseEntity ====> " + responseEntity);
            out.println("ResponseEntity Body ====> " + responseEntity.getBody());

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {

                CashConnectResponse body = new Gson().fromJson(responseEntity.getBody(), CashConnectResponse.class);

                out.println("Body ====> " + body);
                if (body != null && !"failed".equalsIgnoreCase(body.getStatus())) {
//                    GetBvnResponseData data = (GetBvnResponseData)body.getData();
                    Object data = body.getData();
                    out.println("data ====> " + data);
                  /*  ObjectMapper objectMapper = new ObjectMapper();

                    GetBvnResponseData data1 = objectMapper.readValue(objectMapper.writeValueAsString(data), GetBvnResponseData.class);

                    System.out.println("data1 ====> " + data1);*/

                    if (data != null) {

                        String dataString = new ObjectMapper().writeValueAsString(data);
                        out.println("dataString ====> " + dataString);
                        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, body.getStatus(), dataString), HttpStatus.OK);
                    }
                }
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", new Gson().fromJson(responseEntity.getBody(), Object.class)), HttpStatus.BAD_REQUEST);

            }

            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", new Gson().fromJson(responseEntity.getBody(), Object.class)), HttpStatus.BAD_REQUEST);
//        return new ResponseEntity<>(new GenericResponseDTO("99", "failed", null), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace()), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<GenericResponseDTO> callCashConnectAPI(String URL, Object obj) {
        out.println("obj initial entity ===> " + obj);

        HttpHeaders headers = cashConnectUtils.getAuthToken();
        if (headers == null) {
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", "Cash connect authentication failed"), HttpStatus.BAD_REQUEST);
        }

        headers.set("Content-Type", MediaType.TEXT_PLAIN_VALUE);
        headers.set("Accept", MediaType.TEXT_PLAIN_VALUE);

        HttpEntity<String> httpEntity = null;
        try {
            if (obj != null) {
                String requestBody = getObjectAsJsonString(obj);
                out.println("Get obj requestBody ===> " + requestBody);

                httpEntity = new HttpEntity<>(requestBody, headers);
            } else {
                httpEntity = new HttpEntity<>(headers);
            }

            out.println("Get obj HTTP entity ===> " + httpEntity);
            ResponseEntity<String> responseEntity = null;
            try {
                responseEntity = restTemplate.postForEntity(baseURL + URL, httpEntity, String.class);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace()), HttpStatus.BAD_REQUEST);
            }
            out.println("ResponseEntity ====> " + responseEntity);
            out.println("ResponseEntity Body ====> " + responseEntity.getBody());

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                out.println("Generic Response Entity ===> " + responseEntity.getBody());
                CashConnectResponse body = new Gson().fromJson(responseEntity.getBody(), CashConnectResponse.class);
                out.println("Body ====> " + body);
                if (body != null && !"failed".equalsIgnoreCase(body.getStatus())) {
//                    GetBvnResponseData data = (GetBvnResponseData)body.getData();
                    Object data = body.getData();
                    out.println("data ====> " + data);

                  /*  ObjectMapper objectMapper = new ObjectMapper();

                    GetBvnResponseData data1 = objectMapper.readValue(objectMapper.writeValueAsString(data), GetBvnResponseData.class);

                    System.out.println("data1 ====> " + data1);*/

                    if (data != null) {
                        String dataString = new ObjectMapper().writeValueAsString(data);
                        out.println("dataString ====> " + dataString);

                        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, body.getStatus(), dataString), HttpStatus.OK);
                    }
                } else if (body != null) {
                    return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, body.getStatus(), body), HttpStatus.BAD_REQUEST);
                }

            }

            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", new ObjectMapper().readValue(responseEntity.getBody(), Object.class)), HttpStatus.BAD_REQUEST);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Failed", e.getStackTrace()), HttpStatus.BAD_REQUEST);
        }
    }

}
