package ng.com.systemspecs.apigateway.web.rest.remitastp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.systemspecs.apigateway.domain.enumeration.SpecificChannel;
import ng.com.systemspecs.apigateway.domain.enumeration.TransactionType;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.systemspecs.apigateway.service.dto.stp.DefaultApiResponse;
import ng.com.systemspecs.apigateway.service.dto.stp.*;
import ng.com.systemspecs.apigateway.service.kafka.producer.TransProducer;
import ng.com.systemspecs.apigateway.service.mapper.WalletAccountMapper;
import ng.com.systemspecs.apigateway.service.stp.RequestHelper;
import ng.com.systemspecs.apigateway.service.ussd.*;
import ng.com.systemspecs.apigateway.util.Utility;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static ng.com.systemspecs.apigateway.service.dto.stp.ExtendedConstants.ResponseCode.*;

@RestController
@Validated
@RequestMapping("/api")
public class PostController {

    final static Logger LOG = LoggerFactory.getLogger(PostController.class);
    private final Gson gson = new Gson();
    String defaultBankCode = "011";
    @Autowired
    WalletAccountService walletAccountService;
    @Autowired
    WalletAccountMapper walletAccountMapper;
    @Autowired
    JournalLineService journalLineService;
    @Autowired
    JournalService journalService;
    @Autowired
    Utility utility;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ProfileService profileService;
    @Autowired
    IdempotentService idempotentService;
    @Autowired
    PaymentTransactionService paymentTransactionService;
    @Autowired
    private RequestHelper requestHelper;
    @Autowired
    private TransProducer producer;

    @Autowired
    private TransactionController transactionController;

    @Autowired
    private BillerTransactionService billerTransactionService;

    @Autowired
    private BankService bankService;

    @Autowired
    private CoralPayService coralPayService;

    @Autowired
    private BillerService billerService;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/pay/post/{requestID}", method = RequestMethod.POST)
    public EncodedMessage postPayment(@PathVariable String requestID, @RequestBody EncodedMessage ftEncoded) throws JsonProcessingException {
        String json = ftEncoded.getPayload();
        LOG.info(">>>>> process encoded {}", json);
        byte[] decodedPayload = Base64.decodeBase64(json);
        LOG.info(">>>>> process decoded {}", new String(decodedPayload));
        PaymentPostRequestPayload plr = gson.fromJson(new String(decodedPayload), PaymentPostRequestPayload.class);
        StpRequestDetailsUploadList paymentRequest = gson.fromJson(plr.getPaymentDetail(), StpRequestDetailsUploadList.class);
        FTResponse[] response = doPostPayment(paymentRequest);

        LOG.info("FUND TRANSFER RESPONSE " + new ObjectMapper().writeValueAsString(response));
        EncodedMessage responseEncoded = new EncodedMessage();

        String res = new Gson().toJson(response);
        LOG.info("Processing request {}", res);
        responseEncoded.setPayload(new String(Base64.encodeBase64(res.getBytes())));
        return responseEncoded;
    }


    /*@SuppressWarnings("unchecked")
    @RequestMapping(value = "/pay/post/{requestID}", method = RequestMethod.POST)
    public EncodedMessage postPayment(@Valid @PathVariable String requestID, @RequestHeader(value = "Idempotent-key", required = true) String idempotentKey, @RequestBody EncodedMessage ftEncoded) throws Exception, InvalidWalletOperatorException {
        Optional<Idempotent> idempotentOptional = idempotentService.findByIdempotentKey(idempotentKey);
        if (idempotentOptional.isPresent()) {
            String response = idempotentOptional.get().getResponse();
            FTResponse[] ftResponse = {};
            try {
                ftResponse = (FTResponse[]) utility.deserializeIdempotentResponse(response, ftResponse);
                if (ftResponse != null) {
                    EncodedMessage responseEncoded = new EncodedMessage();

                    String res = new Gson().toJson(ftResponse);
                    LOG.info("Processing request {}", res);
                    responseEncoded.setPayload(new String(Base64.encodeBase64(res.getBytes())));
                    return responseEncoded;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new EncodedMessage(String.valueOf(ftResponse));
            }
        }

        String json = ftEncoded.getPayload();
        LOG.info(">>>>> process encoded {}", json);
        byte[] decodedPayload = Base64.decodeBase64(json);
        LOG.info(">>>>> process decoded {}", new String(decodedPayload));
        PaymentPostRequestPayload plr = gson.fromJson(new String(decodedPayload), PaymentPostRequestPayload.class);
        StpRequestDetailsUploadList paymentRequest = gson.fromJson(plr.getPaymentDetail(), StpRequestDetailsUploadList.class);
        FTResponse[] response = doPostPayment(paymentRequest);
        LOG.info("FUND TRANSFER RESPONSE " + new ObjectMapper().writeValueAsString(response));
        EncodedMessage responseEncoded = new EncodedMessage();
        try {
            String idempotentResponse = utility.serializeIdempotentResponse(response);
            Idempotent idempotent = new Idempotent();
            idempotent.setIdempotentKey(idempotentKey);
            idempotent.setResponse(idempotentResponse);
            idempotentService.save(idempotent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String res = new Gson().toJson(response);
        LOG.info("Processing request {}", res);
        responseEncoded.setPayload(new String(Base64.encodeBase64(res.getBytes())));
        return responseEncoded;
    }*/

    private FTResponse[] doPostPayment(StpRequestDetailsUploadList paymentRequest) throws JsonProcessingException {

        LOG.info("PAYMENT  REQUEST " + new ObjectMapper().writeValueAsString(paymentRequest));

        FTResponse[] ftResponses = new FTResponse[paymentRequest.size()];

        for (int i = 0, paymentRequestSize = paymentRequest.size(); i < paymentRequestSize; i++) {

            ftResponses = new FTResponse[paymentRequestSize];
            StpRequestDetailsInstantUpload upload = paymentRequest.get(i);
            FundDTO fundDTO = new FundDTO();
            fundDTO.setTransRef(String.valueOf(upload.getTranRefNo()));
            fundDTO.setAmount(upload.getAmount());
            fundDTO.setSourceAccountNumber(upload.getDebitAccountNo());
            fundDTO.setSourceBankCode(upload.getBankCode());
            fundDTO.setAccountNumber(upload.getCreditAccountNo());
            fundDTO.setDestBankCode(upload.getBankCode());
            fundDTO.setSpecificChannel(SpecificChannel.STP.getName());
            fundDTO.setNarration(upload.getNarration());

            WalletAccount creditAccountNumber = walletAccountService.findOneByAccountNumber(upload.getCreditAccountNo());
            WalletAccount debitAccountNumber = walletAccountService.findOneByAccountNumber(upload.getDebitAccountNo());

//            PaymentResponseDTO paymentResponseDTO = null;

            ResponseEntity<PaymentResponseDTO> fund = null;

            FTResponse ftResponse;

            if (debitAccountNumber != null && creditAccountNumber == null) {
                fundDTO.setChannel("WalletToBank");
                fundDTO.setSpecificChannel(SpecificChannel.STP.getName());
                fund = walletAccountService.fundSTP(fundDTO);
            }

            if (creditAccountNumber != null && debitAccountNumber == null) {
                fundDTO.setChannel("BankToWallet");
                fundDTO.setSpecificChannel(SpecificChannel.THIRD_PARTY.getName());
                fund = walletAccountService.fundSTP(fundDTO);
            }

            if (debitAccountNumber != null && creditAccountNumber != null) {
                fundDTO.setChannel("WalletToWallet");
                fundDTO.setSpecificChannel(SpecificChannel.FUND_WALLET_INTRA.getName());
                fund = walletAccountService.fundSTP(fundDTO);
            }

            LOG.info("FUND DTO ===> " + new ObjectMapper().writeValueAsString(fundDTO));

            LOG.info("FUND response ===> " + new ObjectMapper().writeValueAsString(fund));

            if (fund != null && fund.getStatusCode().equals(HttpStatus.OK)) {
                ftResponse = getFtResponse(fundDTO, paymentRequest.get(i));

                ftResponses[i] = ftResponse;
            } else if (fund != null && !fund.getStatusCode().equals(HttpStatus.OK)) {
                ftResponse = new FTResponse();
                ftResponse.setResponseCode("99");
                ftResponse.setRetryCount(paymentRequest.get(i).getRetryCount());
                ftResponse.setProcessOrder(paymentRequest.get(i).getProcessOrder());
                ftResponse.setResponseDescription(fund.getBody().getMessage());
                if ("Insufficient balance".equalsIgnoreCase(fund.getBody().getMessage())) {
                    ftResponse.setResponseCode("52");
                }
                ftResponse.setStatus(FTResponse.StatusCode.FAILED_CLOSED);
                ftResponses[i] = ftResponse;
            }

           /* else if (paymentResponseDTO != null && paymentResponseDTO.getError()) {
                ftResponse = new FTResponse();
                ftResponse.setResponseCode("99");
                ftResponse.setRetryCount(paymentRequest.get(i).getRetryCount());
                ftResponse.setProcessOrder(paymentRequest.get(i).getProcessOrder());
                ftResponse.setResponseDescription(paymentResponseDTO.getMessage());
                ftResponse.setStatus(FTResponse.StatusCode.FAILED_CLOSED);
                ftResponses[i] = ftResponse;
            } else if (paymentResponseDTO != null && !paymentResponseDTO.getError()) {
                ftResponse = getFtResponse(fundDTO, paymentRequest.get(i));
                ftResponses[i] = ftResponse;
            }*/

            else {

                ftResponses = new FTResponse[1];
                ftResponse = new FTResponse();
                ftResponse.setResponseCode("99");
                ftResponse.setRetryCount(paymentRequest.get(i).getRetryCount());
                ftResponse.setProcessOrder(paymentRequest.get(i).getProcessOrder());
                ftResponse.setResponseDescription("Account number(s) not found");
                ftResponse.setStatus(FTResponse.StatusCode.FAILED_CLOSED);

                ftResponses[i] = ftResponse;
            }
        }
        LOG.info("FT Responses ===> " + new ObjectMapper().writeValueAsString(ftResponses));

        return ftResponses;

    }

    private FTResponse getFtResponse(FundDTO fundDTO, StpRequestDetailsInstantUpload paymentRequest) {
        FTResponse ftResponse = new FTResponse();
        ftResponse.setAmount(fundDTO.getAmount());
        ftResponse.setBankCode(fundDTO.getSourceBankCode());
        ftResponse.setCreditAccountNo(fundDTO.getAccountNumber());
        ftResponse.setCurrency("NGN");
        ftResponse.setDebitAccountNo(fundDTO.getSourceAccountNumber());
        ftResponse.setNarration(fundDTO.getNarration());
        ftResponse.setTranRefNo(fundDTO.getTransRef());
        ftResponse.setToBankCode(fundDTO.getDestBankCode());
        ftResponse.setStatus(FTResponse.StatusCode.SUCCESS);
        ftResponse.setResponseCode("00");
        ftResponse.setTransitToMirror(String.valueOf(paymentRequest.getTransitToMirror()));
        ftResponse.setBankCode(fundDTO.getSourceBankCode());
        ftResponse.setTransDate(String.valueOf(LocalDateTime.now()));
        ftResponse.setRequestCode(paymentRequest.getRequestId());
        ftResponse.setRetryCount(paymentRequest.getRetryCount());
        ftResponse.setProcessOrder(paymentRequest.getProcessOrder());
        ftResponse.setOrigDebitRefNo(paymentRequest.getOrigDebitRefNo());
        ftResponse.setOrgPayerAccount(paymentRequest.getOrgPayerAccount());
        ftResponse.setResponseDescription("Transaction successful");
        return ftResponse;
    }

    @RequestMapping(value = {"/payment/status"}, method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getPaymentStatus(@RequestBody EncodedMessage message) throws Exception {
        String json = message.getPayload();
        byte[] decodedPayload = Base64.decodeBase64(json);
        StatusRequest statusRequest = gson.fromJson(new String(decodedPayload), StatusRequest.class);
        String transRef = statusRequest.getTransRef();

        StpRequestDetailsUpload upload = new StpRequestDetailsUpload();
        PaymentResponse response = new PaymentResponse();

        Optional<Journal> journalOptional = journalService.findByReference(transRef);
        if (journalOptional.isPresent()) {
            Journal journal = journalOptional.get();
            LOG.info("JOURNAL  ===> " + new ObjectMapper().writeValueAsString(journal));

            JournalLine journalLine = null;
            List<JournalLine> journalLines = journalLineService.findByJounalReference(transRef);
            LOG.info("JOURNALINE  ===> " + new ObjectMapper().writeValueAsString(journalLines));

            if (!journalLines.isEmpty()) {
                journalLine = journalLines.get(0);
            }
            upload.setNarration(journal.getDisplayMemo());
            assert journalLine != null;
            upload.setAmount(journalLine.getAmount());
            upload.setTransDate(String.valueOf(journal.getDueDate()));
            response = new PaymentResponse();
            response.setTransRef(transRef);
            response.setResponseCode(StpConstants.PROCESSED_OK);
            response.setResponseMessage(ExtendedConstants.ResponseStatus.API_SUCCESS_STATUS.getCode());
            response.setDetail(upload);
            LOG.info("PAYMENT RESPONSE  ===> " + new ObjectMapper().writeValueAsString(response));

            return requestHelper.encodeResponse(gson.toJson(response));
        }

        response.setResponseCode(ExtendedConstants.ResponseCode.UNKNOWN.getCode());
        response.setResponseMessage(ExtendedConstants.ResponseCode.UNKNOWN.getDescription());

        LOG.info("PAYMENT RESPONSE  ===> " + new ObjectMapper().writeValueAsString(response));

        return requestHelper.encodeResponse(gson.toJson(response));

    }

    @RequestMapping(value = {"/account/statement"}, method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getAccountStatement(@RequestBody EncodedMessage message) throws Exception {
        String json = message.getPayload();
        LOG.info(">>>>> process encoded {}", json);
        byte[] decodedPayload = Base64.decodeBase64(json);
        LOG.info(">>>>> process decoded {}", new String(decodedPayload));
        AccountStatementRequest accountStatementRequest = gson.fromJson(new String(decodedPayload), AccountStatementRequest.class);

        AccountStatementResponse response = new AccountStatementResponse();
        String accountNumber = accountStatementRequest.getAccountNumber();
        String statementDateStr = accountStatementRequest.getStatementDate();

        if (statementDateStr != null && !statementDateStr.isEmpty() && accountNumber != null && !accountNumber.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate statementLocalDate = LocalDate.parse(statementDateStr, formatter);
            LocalDateTime statementDate = LocalDateTime.of(statementLocalDate, LocalTime.MIDNIGHT);

            List<JournalLine> journalLines = journalLineService.findAllByWalletAccountAccountNumberAndJounalTransDateBetween(accountNumber, statementDate, LocalDateTime.now());
            response.setResponseCode(StpConstants.PROCESSED_OK);
            response.setResponseMessage(ExtendedConstants.ResponseStatus.API_SUCCESS_STATUS.getCode());
            List<StatementLine> statementLines = new ArrayList<>();
            for (JournalLine journalLine : journalLines) {
                StatementLine statementLine = new StatementLine();
                statementLine.setAmount(String.valueOf(journalLine.getAmount()));
                statementLine.setCurrency("NGN");
                statementLine.setNarration(journalLine.getMemo());
                statementLine.setTransactionDate(journalLine.getTransactionDate());
                statementLine.setCRDR(journalLine.getCreditDebit());

                statementLines.add(statementLine);
            }
            response.setStatementLines(statementLines);

            LOG.info("ACCOUNT STATEMENT  ===> " + new ObjectMapper().writeValueAsString(response));

            return requestHelper.encodeResponse(gson.toJson(response));

        }
        response.setResponseCode(ExtendedConstants.ResponseCode.FAILED.getCode());
        response.setResponseMessage(ExtendedConstants.ResponseStatus.API_FAIL_STATUS.getCode());

        LOG.info("ERROR ACCOUNT STATEMENT  ===> " + new ObjectMapper().writeValueAsString(response));

        return requestHelper.encodeResponse(gson.toJson(response));
    }

    @PostMapping("/account/name")
    public ResponseEntity<NameEnquiryResponse> getAccountName(@RequestBody BaseAccountRequestDto encodedMessage) throws Exception {

        String accountNumber = encodedMessage.getAccountNumber();

        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
        NameEnquiryResponse nameEnquiryResponse = new NameEnquiryResponse();

        if (walletAccount != null) {
            nameEnquiryResponse.setAccountName(walletAccount.getAccountOwner().getFullName());
            nameEnquiryResponse.setAccountNumber(walletAccount.getAccountNumber());
            nameEnquiryResponse.setResponseCode(StpConstants.PROCESSED_OK);
            nameEnquiryResponse.setResponseMessage(ExtendedConstants.ResponseStatus.API_SUCCESS_STATUS.getCode());

            String res = new Gson().toJson(nameEnquiryResponse);
            LOG.info("Processing request {}", res);

            LOG.info("ACCOUNT NAME RESPONSE  ===> " + new ObjectMapper().writeValueAsString(nameEnquiryResponse));

            return new ResponseEntity<>(nameEnquiryResponse, HttpStatus.OK);
        }

        nameEnquiryResponse.setResponseCode(ExtendedConstants.ResponseCode.INVALID_ACCOUNT.getCode());
        nameEnquiryResponse.setResponseMessage(ExtendedConstants.ResponseCode.INVALID_ACCOUNT.getDescription());

        LOG.info("ACCOUNT NAME FAIL RESPONSE  ===> " + new ObjectMapper().writeValueAsString(nameEnquiryResponse));

        return new ResponseEntity<>(nameEnquiryResponse, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/account/balance/{bankCode}/{accountNumber}")
    public ResponseEntity<BalanceEnquiryResponse> getAccountBalance(@PathVariable String bankCode,
                                                                    @PathVariable String accountNumber) throws Exception {

        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
        LOG.info("WALLET ACCOUNT  ===> " + walletAccount);

        BalanceEnquiryResponse balanceEnquiryResponse = new BalanceEnquiryResponse();

        if (walletAccount != null) {
            balanceEnquiryResponse.setResponseCode(StpConstants.PROCESSED_OK);
            balanceEnquiryResponse.setResponseMessage(ExtendedConstants.ResponseStatus.API_SUCCESS_STATUS.getCode());
            balanceEnquiryResponse.setAccountName(walletAccount.getAccountOwner().getFullName());
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String format = now.format(formatter);
            balanceEnquiryResponse.setBalanceDate(format);
            balanceEnquiryResponse.setAmount(walletAccount.getActualBalance());
            balanceEnquiryResponse.setCurrency("NGN");

            LOG.info("ACCOUNT BALANCE RESPONSE  ===> " + new ObjectMapper().writeValueAsString(balanceEnquiryResponse));

            return new ResponseEntity<>(balanceEnquiryResponse, HttpStatus.OK);
        }
        balanceEnquiryResponse.setResponseCode(ExtendedConstants.ResponseCode.INVALID_ACCOUNT.getCode());
        balanceEnquiryResponse.setResponseMessage(ExtendedConstants.ResponseCode.INVALID_ACCOUNT.getDescription());

        LOG.info("ACCOUNT BALANCE FAIL RESPONSE  ===> " + new ObjectMapper().writeValueAsString(balanceEnquiryResponse));

        return new ResponseEntity<>(balanceEnquiryResponse, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/request/otp")
    public String requestOTP(@RequestBody EncodedMessage encodedMessage) throws Exception {

        String json = encodedMessage.getPayload();
        LOG.info(">>>>> process encoded {}", json);
        byte[] decodedPayload = Base64.decodeBase64(json);
        LOG.info(">>>>> process decoded {}", new String(decodedPayload));

        NameEnquiryRequest nameEnquiryRequest = gson.fromJson(new String(decodedPayload), NameEnquiryRequest.class);

        GenerateOTPResponse generateOtpResponse = new GenerateOTPResponse();

        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(nameEnquiryRequest.getAccountNumber());
        LOG.info("WALLET ACCOUNT QUERY RESPONSE  ===> " + new ObjectMapper().writeValueAsString(walletAccount));

        if (walletAccount != null && walletAccount.getAccountOwner() != null) {
            String phoneNumber = walletAccount.getAccountOwner().getPhoneNumber();
            phoneNumber = utility.returnPhoneNumberFormat(phoneNumber);
            generateOtpResponse.setResponseCode(StpConstants.PROCESSED_OK);
            generateOtpResponse.setResponseMessage(ExtendedConstants.ResponseStatus.API_SUCCESS_STATUS.getCode());
            generateOtpResponse.setPhoneNumber(phoneNumber);
            WalletAccountType walletAccountType = walletAccount.getWalletAccountType();
            if (walletAccountType != null) {
                generateOtpResponse.setAccountClass(walletAccountType.getWalletAccountType());
            }

            userService.sendOTP(phoneNumber);

            LOG.info("REQUEST OTP RESPONSE  ===> " + new ObjectMapper().writeValueAsString(generateOtpResponse));


            return requestHelper.encodeResponse(gson.toJson(generateOtpResponse));
        }
        generateOtpResponse.setResponseCode(ExtendedConstants.ResponseCode.FAILED.getCode());
        generateOtpResponse.setResponseMessage(ExtendedConstants.ResponseStatus.API_FAIL_STATUS.getCode());

        LOG.info("REQUEST OTP FAIL RESPONSE  ===> " + new ObjectMapper().writeValueAsString(generateOtpResponse));

        return requestHelper.encodeResponse(gson.toJson(generateOtpResponse));

    }

    @PostMapping("/authenticate/otp/{accountNumber}/{phoneNumber}/{otp}")
    public Boolean authenticateOTP(@PathVariable String accountNumber,
                                   @PathVariable String phoneNumber, @PathVariable String otp) throws Exception {

        Boolean result = false;

        if (accountNumber != null && otp != null) {
            WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
            LOG.info("WALLET ACCOUNT QUERY RESPONSE  ===> " + walletAccount);

            if (walletAccount != null && walletAccount.getAccountOwner() != null) {
                String ownerPhoneNumber = walletAccount.getAccountOwner().getPhoneNumber();
                if (ownerPhoneNumber != null) {
                    ResponseEntity<Boolean> booleanResponseEntity = userService.verifyOTP(Long.valueOf(otp), ownerPhoneNumber);
                    result = booleanResponseEntity.getBody();
                }
                LOG.info("AUTHENTICATE OTP RESPONSE  ===> " + result);

                return result;

            }
        }

        return result;

    }

    @PostMapping("/authenticate/otp")
    public GenericStpResponse authenticateOTP(@Valid @RequestBody AuthenticateOTPRequest request) throws Exception {

        LOG.info("INCOMING PAYLOAD  ===> " + request);

        Boolean result = Boolean.FALSE;

        String accountNumber = request.getAccountNumber();
        String dob = request.getDob();
        String mobileNumber = utility.formatPhoneNumber(request.getMobileNumber().trim());
        LOG.info("REFORMATTED PHONE NUMBER ===> " + mobileNumber);
        String otp = request.getOtp();
        String pin = request.getPin();

        GenericStpResponse genericStpResponse = new GenericStpResponse();

        if (accountNumber != null && otp != null) {
            WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
            LOG.info("WALLET ACCOUNT QUERY RESPONSE  ===> " + walletAccount);
            if (walletAccount != null && walletAccount.getAccountOwner() != null) {
                String ownerPhoneNumber = utility.formatPhoneNumber(walletAccount.getAccountOwner().getPhoneNumber());
                LOG.info("OWNER PHONE NUMBER ===> " + ownerPhoneNumber);
                if (ownerPhoneNumber != null && ownerPhoneNumber.equalsIgnoreCase(mobileNumber)) {
                    ResponseEntity<Boolean> booleanResponseEntity = userService.verifyOTP(Long.valueOf(otp), ownerPhoneNumber);
                    result = booleanResponseEntity.getBody();
                    LOG.info("VERIFY OTP result ====> " + result);
                    if (!result) {
                        genericStpResponse.setResponseCode(INVALID_OTP.getCode());
                        genericStpResponse.setResponseMessage(INVALID_OTP.getDescription());
                        return genericStpResponse;
                    } else if (validateProfile(walletAccount, pin, dob)) {
                        LOG.info("AUTHENTICATE OTP RESPONSE  ===> " + result);
                        genericStpResponse.setResponseCode("00");
                        genericStpResponse.setResponseMessage("successful");
                        return genericStpResponse;
                    }
                }
            }
        }

        genericStpResponse.setResponseCode(CUSTOMER_ACCOUNT_RECORD_MISMATCH.getCode());
        genericStpResponse.setResponseMessage(CUSTOMER_ACCOUNT_RECORD_MISMATCH.getDescription());

        return genericStpResponse;

    }

    private boolean validateProfile(WalletAccount walletAccount, String pin, String dob) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dobDate = LocalDate.parse(dob, formatter);

        Profile profile = walletAccount.getAccountOwner();
        LOG.info("PROFILE ===> " + profile);
        // Profile profile = profileService.
        String currentEncryptedPin = profile.getPin();
        if (passwordEncoder.matches(pin, currentEncryptedPin)) {
            LocalDate dateOfBirth = profile.getDateOfBirth();
            return dateOfBirth.isEqual(dobDate);
        }
        return false;
    }

    @PostMapping(value = "/validate/account/{accountNumber}/{phoneNumber}")
    public Boolean validateAccount(@PathVariable String accountNumber,
                                   @PathVariable String phoneNumber) throws JsonProcessingException {

        boolean isValid = false;
        WalletAccount account = walletAccountService.findOneByAccountNumber(accountNumber);
        isValid = account != null;
        if (isValid) {
            String profilePhoneNumber = account.getAccountOwner().getPhoneNumber();
            profilePhoneNumber = utility.returnPhoneNumberFormat(profilePhoneNumber);
            userService.sendOTP(profilePhoneNumber);
        }
        LOG.info("WALLET ACCOUNT RESPONSE  ===> " + account + " isValid== " + isValid);
        return isValid;

    }

    @PostMapping(value = "/remita/notification", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<GenericResponseDTO> processPaymentNotification(@RequestBody
                                                                             RemitaPaymentNotificationList notification) {
        LOG.info(" Remita notofcation from Remita===> " + notification);

        for (RemitaBillerNotification payment : notification) {

            String transactiondate = payment.getTransactiondate();
            LOG.info("TRANSDATE ==> " + transactiondate);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime transDate = LocalDateTime.parse(transactiondate, formatter);

            PaymentTransactionDTO paymentTransaction = new PaymentTransactionDTO();
            paymentTransaction.setAmount(BigDecimal.valueOf(payment.getAmount()));
            paymentTransaction.setChannel(payment.getChannel());
            paymentTransaction.setCurrency("NGN");
            paymentTransaction.setDestinationAccountBankCode(payment.getBank());
            //paymentTransaction.setDestinationAccountName(payment.getBillerName());
            //paymentTransaction.setSourceAccountName(payment.getPayerName());
            paymentTransaction.setTransactionDate(transDate);
            //paymentTransaction.setTransactionOwnerPhoneNumber(payment.getPayerPhoneNumber());
            //paymentTransaction.setTransactionRef(payment.getOrderRef());
            paymentTransaction.setTransactionType(TransactionType.BANK_ACCOUNT_TRANSFER);
            paymentTransaction.setDestinationAccount("");
            paymentTransaction.setSourceAccount("");
            paymentTransaction.setDestinationNarration("");
            paymentTransaction.setSourceNarration("");
            paymentTransaction.setSourceAccountBankCode("");

            PaymentTransactionDTO save = paymentTransactionService.save(paymentTransaction);

            //Payment processing...
            String accountNumber = null;

            List<Customfielddata> customfielddataList = payment.getCustomfielddata();

            if (customfielddataList.isEmpty()) {
                return new ResponseEntity<>(new GenericResponseDTO(SUCCESS.getCode(), "Funding successful", new ArrayList<>()), HttpStatus.OK);
            }
            for (Customfielddata data : customfielddataList) {
                if (data.getDescription().equalsIgnoreCase("Walletid")) {
                    accountNumber = data.getColval();

                    FundDTO fundDTO = new FundDTO();
                    fundDTO.setAccountNumber(accountNumber);
                    fundDTO.setAmount(payment.getAmount());
                    fundDTO.setChannel("BankToWallet");
                    fundDTO.setTransRef(payment.getOrderref());
                    fundDTO.setPhoneNumber(payment.getPayerphonenumber());
                    fundDTO.setSpecificChannel(SpecificChannel.STP.getName());
                    fundDTO.setNarration(payment.getPaymentdescription());
                    fundDTO.setRrr(payment.getRrr());

                    LOG.info("FUND DTO Remita notification " + fundDTO);

                    return walletAccountService.fundWalletStp(fundDTO);
                }
            }

        }

        return new ResponseEntity<>(new GenericResponseDTO(FAILED.getCode(), FAILED.getDescription(), new ArrayList<String>()), HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/customer/accounts/{bankCode}/{phoneNumber}")
    public ResponseEntity<DefaultApiResponse> getCustomerWallets(@PathVariable String bankCode,
                                                                 @PathVariable String phoneNumber) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        List<WalletAccount> wallets = walletAccountService.findByAccountOwnerPhoneNumber(phoneNumber);

        DefaultApiResponse defaultApiResponse = new DefaultApiResponse();

        if (wallets.isEmpty()) {
            defaultApiResponse.setStatus(CUSTOMER_ACCOUNT_NOT_FOUND.getCode());
            defaultApiResponse.setMessage(CUSTOMER_ACCOUNT_NOT_FOUND.getDescription());
            defaultApiResponse.setData(new ArrayList<String>());
            return new ResponseEntity<>(defaultApiResponse, HttpStatus.BAD_REQUEST);
        }

        List<WalletREsponseDTO> walletResponse = new ArrayList<>();

        for (WalletAccount walletAccount : wallets) {
            WalletREsponseDTO walletREsponseDTO = new WalletREsponseDTO();
            walletREsponseDTO.setAccountNumber(walletAccount.getAccountNumber());
            walletREsponseDTO.setAccountName(walletAccount.getAccountName());
            walletREsponseDTO.setAccountOpeningDate(String.valueOf(walletAccount.getDateOpened()));
            if (walletREsponseDTO.getAccountType() != null) {
                walletREsponseDTO.setAccountType(walletAccount.getWalletAccountType().getWalletAccountType());
            }
            if (walletAccount.getAccountOwner() != null) {
                walletREsponseDTO.setBvn(walletAccount.getAccountOwner().getBvn());
                walletREsponseDTO.setCustomerId(walletAccount.getAccountOwner().getPhoneNumber());
                walletREsponseDTO.setFullName(walletAccount.getAccountOwner().getFullName());
                walletREsponseDTO.setPhoneNumber(walletAccount.getAccountOwner().getPhoneNumber());
                if (walletAccount.getAccountOwner().getUser() != null) {
                    walletREsponseDTO.setEmail(walletAccount.getAccountOwner().getUser().getEmail());
                }
                walletREsponseDTO.setAvailableBalance(new BigDecimal(walletAccount.getCurrentBalance()));
            }

            walletREsponseDTO.setCurrency("NGN");
            walletREsponseDTO.setStatus("ACTIVE");
            walletResponse.add(walletREsponseDTO);

        }

        HashMap<String, Integer> metaMap = new HashMap<>();
        metaMap.put("totalNumberOfRecords", walletResponse.size());

        defaultApiResponse.setStatus(SUCCESS.getCode());
        defaultApiResponse.setMessage("The process was completed successfully");
        defaultApiResponse.setData(walletResponse);
        defaultApiResponse.set_meta(metaMap);

        return new ResponseEntity<>(defaultApiResponse, HttpStatus.OK);
    }

	@PostMapping(value = "/ussd/wallet")
	public DirectUSSDResponseDTO processUSSD(@Valid @RequestBody DirectUSSDRequestDTO directUSSDRequestDTO, HttpSession session)
			throws Exception {
        USSDRequestDTO ussdRequestDTO = new USSDRequestDTO();
        ussdRequestDTO.setMsisdn(directUSSDRequestDTO.getMsisdn());
        ussdRequestDTO.setNetwork(directUSSDRequestDTO.getNetwork());
        ussdRequestDTO.setSessionId(directUSSDRequestDTO.getSessionid());
        ussdRequestDTO.setTimeStamp(directUSSDRequestDTO.getDatetime());
        ussdRequestDTO.setUssdContent(directUSSDRequestDTO.getInput());
		String ussdContent = "";
		String messageType = "1";
		String requestUssdContent = ussdRequestDTO.getUssdContent();
		String phoneNumber = "+234" + ussdRequestDTO.getMsisdn().substring(ussdRequestDTO.getMsisdn().length() - 10);
		String separator="#";
		if("99".equalsIgnoreCase(requestUssdContent)) {
            separator = "";
            requestUssdContent = "0";
        }else if ("98".equalsIgnoreCase(requestUssdContent)){
            separator = "";
            requestUssdContent = "";
        }

		if("95".equalsIgnoreCase(requestUssdContent)) {
            separator = "";
            requestUssdContent = "";
        }
		if ("96".equalsIgnoreCase(requestUssdContent)){
			session.getServletContext().removeAttribute(phoneNumber);
			requestUssdContent="*7000#";
        }

		if("*7000#".equalsIgnoreCase(requestUssdContent)) {
			String savedContent=(String) session.getServletContext().getAttribute(phoneNumber);
			if(savedContent!=null) {
				USSDResponseDTO ussdResponseDTO = new USSDResponseDTO();
		        DirectUSSDResponseDTO directUSSDResponseDTO = new DirectUSSDResponseDTO();
				ussdResponseDTO.setTimeStamp(LocalDateTime.now().toString());
				ussdResponseDTO.setMsgType("1");
				ussdResponseDTO.setMsisdn(phoneNumber);
				ussdResponseDTO.setNetwork(ussdRequestDTO.getNetwork());
				ussdResponseDTO.setSessionId(ussdRequestDTO.getSessionId());
				ussdResponseDTO.setUssdContent("You did not conclude last request\n"
                    + "95. Continue \n"
                    + "96. Start New Request");
                LOG.info("0 ussdRequestDTO ==> " + ussdRequestDTO);
                LOG.info("1 ussdResponseDTO ==> " + ussdResponseDTO);
                directUSSDResponseDTO.setResponse(ussdResponseDTO.getUssdContent());
                String freeFlow = "2".equalsIgnoreCase(messageType) ? "FB":"FC";
                directUSSDResponseDTO.setFreeflow(freeFlow);
                return directUSSDResponseDTO;
			}
		}
        Profile profile = profileService.findByPhoneNumber(phoneNumber);

        System.out.println(" The Session ID sent === " +phoneNumber);

        LOG.info("1 requestUssdContent==== " + requestUssdContent);

        requestUssdContent = requestUssdContent.replace("#", "");
        requestUssdContent = (session.getServletContext().getAttribute(phoneNumber) == null ? ""
            : session.getServletContext().getAttribute(phoneNumber) + separator)
            + requestUssdContent;

        LOG.info("2 requestUssdContent==== " + requestUssdContent);

        session.getServletContext().setAttribute(phoneNumber, requestUssdContent);

        LOG.info("3 session==== " + session.getServletContext().getAttribute(phoneNumber));

        /*
         * Enumeration<String> attributes =
         * session.getServletContext().getAttributeNames(); List<String>
         * listedAttributes =Collections.list(attributes); for (String attribute :
         * listedAttributes) {
         * System.out.println(" The application context attribute===="+attribute); }
         */
        String[] tokens = requestUssdContent.split("#");
        /*
		 * for (String token : tokens) { LOG.info(" The Split Token " + token); }
		 */

        if ("98".equalsIgnoreCase(ussdRequestDTO.getUssdContent())){
            requestUssdContent = StringUtils.substring(requestUssdContent, 0, requestUssdContent.length() - 1);
            session.getServletContext().setAttribute(phoneNumber, requestUssdContent);
            LOG.info("2 requestUssdContent after 98 ==== " + requestUssdContent);
        }

        int option = -1;
        try {
            if(tokens!=null && tokens.length>=2 && profile != null) {
                option = Integer.parseInt(tokens[1]);
            }
            if(profile == null) {
                option = 0;
            }
        } catch (NumberFormatException e) {
            option=99;
            e.printStackTrace();
        }

        LOG.info(" option " + option+ "   requestUssdContent==="+requestUssdContent + " phoneNumber===="+phoneNumber);

        switch (option) {//
            case -1:
                Home home = new Home(requestUssdContent, profile);
                ussdContent = home.getResponse().getContent();
                messageType = home.getResponse().getMsgType();
                break;
            case 0:
                CreateWalletUssd createWalletUssd = new CreateWalletUssd(requestUssdContent, utility,
                    walletAccountService, phoneNumber);
                LOG.info("creating wallet");
                ussdContent = createWalletUssd.getResponse().getContent();
                messageType = createWalletUssd.getResponse().getMsgType();
                break;
            case 1:
                ussdContent = "Your Account Balance(s):\n";
                List<WalletAccount> accounts = walletAccountService.findByAccountOwnerPhoneNumber(phoneNumber);
                StringBuilder ussdContentBuilder = new StringBuilder(ussdContent);
                for (WalletAccount walletAccount : accounts) {
                    ussdContentBuilder.append(walletAccount.getAccountNumber()).append(" : ").append(utility.formatMoney(Double.valueOf(walletAccount.getActualBalance()))).append("\n");
                    messageType = "2";
                }
                ussdContent = ussdContentBuilder.toString();
                break;
            case 2:
                SendMoneyToWallet sendMoneyToWallet = new SendMoneyToWallet(requestUssdContent,
                    walletAccountService, profile, session, passwordEncoder, phoneNumber, utility);
                ussdContent = sendMoneyToWallet.getResponse().getContent();
                messageType = sendMoneyToWallet.getResponse().getMsgType();
                break;
            case 3:
                SendMoneyToBank sendMoneyToBank = new SendMoneyToBank(requestUssdContent, profile,
                    bankService, walletAccountService, session, phoneNumber,
                    passwordEncoder, utility);
                ussdContent = sendMoneyToBank.getResponse().getContent();
                messageType = sendMoneyToBank.getResponse().getMsgType();
                break;

            case 4:
                FundWallet fundWallet = new FundWallet(requestUssdContent, profile, session, walletAccountService, phoneNumber, utility, bankService, coralPayService);
                ussdContent = fundWallet.getResponse().getContent();
                messageType = fundWallet.getResponse().getMsgType();
                break;

            case 5:
                BillsPayment billsPayment = new BillsPayment(requestUssdContent, profile, bankService,
                    billerTransactionService, billerService, utility,
                    walletAccountService, session, phoneNumber, passwordEncoder);
                ussdContent = billsPayment.getResponse().getContent();
                messageType = billsPayment.getResponse().getMsgType();
                break;

            case 6:
                PayRRR payRRR = new PayRRR(requestUssdContent, profile, billerTransactionService,
                    walletAccountService, session, phoneNumber, passwordEncoder, utility);
                ussdContent = payRRR.getResponse().getContent();
                messageType = payRRR.getResponse().getMsgType();
                break;

            case 7:
                CashOut cashOut = new CashOut(requestUssdContent,
                    walletAccountService, profile, session, passwordEncoder, phoneNumber, utility);
                ussdContent = cashOut.getResponse().getContent();
                messageType = cashOut.getResponse().getMsgType();

                break;

            case 8:
                BuyAirtimeOrData buyAirtimeOrData = new BuyAirtimeOrData(requestUssdContent, profile, billerTransactionService,
                    billerService, utility,
                    walletAccountService, session, phoneNumber, passwordEncoder, ussdRequestDTO.getNetwork());
                ussdContent = buyAirtimeOrData.getResponse().getContent();
                messageType = buyAirtimeOrData.getResponse().getMsgType();

                break;

            case 9:
                RequestMoney requestMoney = new RequestMoney(requestUssdContent, walletAccountService, profile, utility);
                ussdContent = requestMoney.getResponse().getContent();
                messageType = requestMoney.getResponse().getMsgType();
                break;
            case 10:
                ApproveRequestMoney approveRequestMoney = new ApproveRequestMoney(requestUssdContent,
                    journalLineService, profile, walletAccountService, session, phoneNumber, utility);
                ussdContent = approveRequestMoney.getResponse().getContent();
                messageType = approveRequestMoney.getResponse().getMsgType();
                break;
            case 11:
                Settings settings = new Settings(requestUssdContent, profile, journalLineService, session, userService, passwordEncoder, profileService);
                ussdContent = settings.getResponse().getContent();
                messageType = settings.getResponse().getMsgType();
                break;
            default:
                ussdContent = "Invalid Option selected";
                messageType = "2";
                break;
        }

        USSDResponseDTO ussdResponseDTO = new USSDResponseDTO();
        DirectUSSDResponseDTO directUSSDResponseDTO = new DirectUSSDResponseDTO();
        ussdResponseDTO.setTimeStamp(LocalDateTime.now().toString());
        ussdResponseDTO.setMsgType(messageType);
        ussdResponseDTO.setMsisdn(phoneNumber);
        ussdResponseDTO.setNetwork(ussdRequestDTO.getNetwork());
        ussdResponseDTO.setSessionId(phoneNumber);
        ussdResponseDTO.setUssdContent(ussdContent);
        if("2".equalsIgnoreCase(messageType)) {
            session.getServletContext().removeAttribute(phoneNumber);
        }
        LOG.info("0 ussdRequestDTO ==> " + ussdRequestDTO);
        LOG.info("1 ussdResponseDTO ==> " + ussdResponseDTO);
        directUSSDResponseDTO.setResponse(ussdResponseDTO.getUssdContent());
        String freeFlow = "2".equalsIgnoreCase(messageType) ? "FB":"FC";
        directUSSDResponseDTO.setFreeflow(freeFlow);
        return directUSSDResponseDTO;
    }


}
