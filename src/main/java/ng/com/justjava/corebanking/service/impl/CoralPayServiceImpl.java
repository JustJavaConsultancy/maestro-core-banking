package ng.com.justjava.corebanking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ng.com.justjava.corebanking.service.kafka.producer.TransProducer;
import ng.com.justjava.corebanking.client.ExternalCgateRESTClient;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.SpecificChannel;
import ng.com.justjava.corebanking.domain.enumeration.TransactionStatus;
import ng.com.justjava.corebanking.service.CoralPayService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.CgateDetailsRequestDTO;
import ng.com.justjava.corebanking.service.dto.CgateDetailsResponseDTO;
import ng.com.justjava.corebanking.service.dto.CgatePaymentNotificationRequestDTO;
import ng.com.justjava.corebanking.service.dto.CgatePaymentNotificationResponseDTO;
import ng.com.justjava.corebanking.service.dto.CoralPayTransactionStatusDTO;
import ng.com.justjava.corebanking.service.dto.CoralPayTransactionStatusResponseDTO;
import ng.com.justjava.corebanking.service.dto.FundDTO;
import ng.com.justjava.corebanking.service.dto.InvokeReferenceRequestDTO;
import ng.com.justjava.corebanking.service.dto.InvokeReferenceRequestDetailsDTO;
import ng.com.justjava.corebanking.service.dto.InvokeReferenceResponseDTO;
import ng.com.justjava.corebanking.service.dto.InvokeReferenceResponseDetailsDTO;
import ng.com.justjava.corebanking.service.dto.RequestHeaderDTO;
import ng.com.justjava.corebanking.service.dto.ValidTransactionResponse;
import ng.com.justjava.corebanking.util.CoralPayUtils;
import ng.com.justjava.corebanking.util.PGPUtils;
import ng.com.justjava.corebanking.util.Utility;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Service
@Transactional
public class CoralPayServiceImpl implements CoralPayService {

    final static Logger log = LoggerFactory.getLogger(CoralPayService.class);
    private final Gson gson = new Gson();

    @Value("${app.coral.mid}")
    private String mid;

    @Value("${app.coral.tid}")
    private String tid;

    @Value("${app.serm}")
    private String serm;

    @Value("${app.dough}")
    private String dough;

    private final String basic = "Basic UmVtaXRhOjA4NDEwMTEwMjFAMDA2IzE=";
    //        private static final String CORALPAY_INVOKE_URL_TEST = "https://testdev.coralpay.com/cgateproxy/api/invokereference";
    private static final String CORALPAY_INVOKE_URL = "https://cgateweb.coralpay.com:567/api/InvokeReference";
    //    private static final String CORALPAY_STATUS_QUERY_URL = "https://testdev.coralpay.com/cgateproxy/api/statusquery";
    private static final String CORALPAY_STATUS_QUERY_URL = "https://cgateweb.coralpay.com:567/api/StatusQuery";
    //    private static final String CORALPAY_REFUND_URL = "https://testdev.coralpay.com/cgateproxy/api/refund";
    private static final String CORALPAY_REFUND_URL = "https://cgateweb.coralpay.com:567/api/refund";


    private final ExternalCgateRESTClient externalCgateRESTClient;
    private final PGPUtils pgpUtils;
    private final CoralPayUtils coralPayUtils;
    private final WalletAccountService walletAccountService;
    private final Utility utility;
    private final TransProducer producer;

    public CoralPayServiceImpl(ExternalCgateRESTClient externalCgateRESTClient, PGPUtils pgpUtils, CoralPayUtils coralPayUtils, WalletAccountService walletAccountService, Utility utility, TransProducer producer) {
        this.externalCgateRESTClient = externalCgateRESTClient;
        this.pgpUtils = pgpUtils;
        this.coralPayUtils = coralPayUtils;
        this.walletAccountService = walletAccountService;
        this.utility = utility;
        this.producer = producer;
    }

    @Override
    public CgatePaymentNotificationResponseDTO processPaymentNotification(CgatePaymentNotificationRequestDTO notificationRequest) {

        log.info("coralPay Payment Notification ==> " + notificationRequest);
        CgatePaymentNotificationResponseDTO response = new CgatePaymentNotificationResponseDTO();

        String walletId = notificationRequest.getCustomerRef();

        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(walletId);
        if (walletAccount != null) {

            double amount = notificationRequest.getAmount();
            String mobileNumber = utility.formatPhoneNumber(notificationRequest.getMobileNumber());
            String paymentReference = notificationRequest.getPaymentReference();
            String transactionDate = notificationRequest.getTransactionDate();
            String traceId = notificationRequest.getTraceId();
            String beneficiaryName = walletAccount.getAccountOwner() != null ? walletAccount.getAccountOwner().getFullName() : walletAccount.getAccountName();

            FundDTO fundDTO = new FundDTO();
            fundDTO.setChannel("BankToWallet");
            fundDTO.setRrr(paymentReference);
            fundDTO.setSourceAccountName("CoralPay");
            fundDTO.setSpecificChannel(SpecificChannel.CORAL_PAY.getName());
            fundDTO.setBeneficiaryName(beneficiaryName);
            fundDTO.setAccountNumber(walletId);
            fundDTO.setAmount(amount);
            fundDTO.setPhoneNumber(mobileNumber);
            fundDTO.setTransRef(utility.getUniqueTransRef());
            fundDTO.setNarration("Funding of wallet(" + walletId + ") with the sum of " + utility.formatMoney(amount));
            fundDTO.setStatus(TransactionStatus.PROCESSING);

            ValidTransactionResponse validTransaction = utility.isValidTransaction(fundDTO.getChannel(), fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber(), fundDTO.getAmount(), fundDTO.getBonusAmount(), false);
            if (validTransaction.isValid()) {

                producer.send(fundDTO);

                response.setResponseCode("00");
                response.setResponseMessage("Successful");
                return response;
            } else {

                response.setResponseCode("99");
                response.setResponseMessage(validTransaction.getMessage());
                return response;
            }
        }

        response.setResponseCode("01");
        response.setResponseMessage("Invalid Account Number (" + walletId + ")");
        return response;

    }

    @Override
    public CgateDetailsResponseDTO getPaymentDetails(CgateDetailsRequestDTO request) {

        String accountNumber = request.getCustomerRef();
        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);

        CgateDetailsResponseDTO response = new CgateDetailsResponseDTO();
        response.setTraceId(utility.getUniqueTransRef());

        if (walletAccount != null && walletAccount.getAccountOwner() != null) {
            response.setCustomerName(walletAccount.getAccountOwner().getFullName());
            response.setAmount(0.00);
            response.setDisplayMessage("Wallet Topup");
            response.setResponseCode("00");
            return response;
        }
        response.setResponseCode("01");
        response.setDisplayMessage("Failed");
        return response;

    }

    @Override
    public String invokeReference(String payload) {
        byte[] encryptedData = pgpUtils.createEncryptedData(payload.getBytes());
        log.info("ENCRYPTED_DATA ARRAY " + encryptedData);
        System.out.println("========================");
        log.info("ENCRYPTED_DATA STRING " + Strings.fromByteArray(encryptedData));
        System.out.println("========================");
        log.info("ENCRYPTED_DATA STRING CONVERSION " + new String(encryptedData, StandardCharsets.UTF_8));
        System.out.println("========================++");
        byte[] bytes = Hex.encode(encryptedData);
        System.out.println(org.apache.commons.codec.binary.Hex.encodeHex(encryptedData));
        System.out.println("====+++====== " + new String(bytes));
        String s = externalCgateRESTClient.invokeReference(basic, new String(bytes));
        if (s != null) {
            log.info("RESPONSE " + s);
            byte[] decode = Hex.decode(s);
            String decryptedResponse = pgpUtils.extractPlainTextData(decode);
            log.info("DECRYPTED_DATA " + decryptedResponse);
            return decryptedResponse;
        }
        return null;

    }

    @Override
    public String invokeExp(String payload) {
        String s = externalCgateRESTClient.invokeReference(basic, payload);
        System.out.println("RESPONSE " + s);
        byte[] decode = Hex.decode(s);
        String decryptedResponse = pgpUtils.extractPlainTextData(decode);
        System.out.println("DECRYPTED_DATA " + decryptedResponse);
        return decryptedResponse;
    }

    public String convertToCoralPayInvokeReferencePayload(InvokeReferenceRequestDTO invokeReferenceRequestDTO) {
        log.info("coralPay InvokeReference ==> " + invokeReferenceRequestDTO);

        String payload = new Gson().toJson(invokeReferenceRequestDTO);
        log.info("InvokeReference payload as string ==> " + payload);
        return payload;
    }

    public InvokeReferenceResponseDTO convertBackCoralPayInvokeReferenceResponse(String response) {
        log.info("coralPay InvokeReference response ==> " + response);

        try {
            InvokeReferenceResponseDTO invokeReferenceResponseDTO = new ObjectMapper().readValue(response, InvokeReferenceResponseDTO.class);

            log.info("InvokeReference response as string ==> " + invokeReferenceResponseDTO);
            return invokeReferenceResponseDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InvokeReferenceResponseDTO callInvokeReference(InvokeReferenceRequestDTO invokeReferenceRequestDTO) {

        RequestHeaderDTO requestHeader = invokeReferenceRequestDTO.getRequestHeader();
        requestHeader.setUsername(serm);
        requestHeader.setPassword(dough);
        invokeReferenceRequestDTO.setRequestHeader(requestHeader);

        InvokeReferenceRequestDetailsDTO requestDetails = invokeReferenceRequestDTO.getRequestDetails();
        requestDetails.setMerchantId(mid);
        requestDetails.setTerminalId(tid);
        requestDetails.setSubMerchantName("Pouchii");
        invokeReferenceRequestDTO.setRequestDetails(requestDetails);

        String s = convertToCoralPayInvokeReferencePayload(invokeReferenceRequestDTO);
        String invokeReferenceResponse = coralPayInvokeReference(s);
        return convertBackCoralPayInvokeReferenceResponse(invokeReferenceResponse);
    }

    @Override
    public String getTransactionReference(String walletNumber, Double amount) {

        //Todo invoke reference
        InvokeReferenceRequestDTO invokeReferenceRequestDTO = new InvokeReferenceRequestDTO();

        InvokeReferenceRequestDetailsDTO invokeReferenceRequestDetailsDTO = new InvokeReferenceRequestDetailsDTO();
        invokeReferenceRequestDetailsDTO.setTraceID(walletNumber);
        invokeReferenceRequestDetailsDTO.setAmount(amount);
        invokeReferenceRequestDetailsDTO.setSubMerchantName("Pouchii");

        invokeReferenceRequestDTO.setRequestDetails(invokeReferenceRequestDetailsDTO);
        RequestHeaderDTO requestHeaderDTO = new RequestHeaderDTO();
        invokeReferenceRequestDTO.setRequestHeader(requestHeaderDTO);

        String transactionReference = null;

        InvokeReferenceResponseDTO invokeReferenceResponseDTO = callInvokeReference(invokeReferenceRequestDTO);
        if (invokeReferenceResponseDTO != null) {
            CgatePaymentNotificationResponseDTO responseHeader = invokeReferenceResponseDTO.getResponseHeader();
            if (responseHeader != null) {
                if ("00".equalsIgnoreCase(responseHeader.getResponseCode())) {
                    InvokeReferenceResponseDetailsDTO responseDetails = invokeReferenceResponseDTO.getResponseDetails();
                    if (responseDetails != null) {
                        transactionReference = responseDetails.getReference();
                    }
                }
            }
        }

        return transactionReference;
    }

    @Override
    public String coralPayInvokeReference(String payload) {
        log.info("InvokeReference payload as string ==> " + payload);
        String encryptedMessage = coralPayUtils.encrypt(payload);
        log.info("InvokeReference EncryptedMessage ==> " + encryptedMessage);
//        String response = externalCgateRESTClient.invokeReference(basic, encryptedMessage);
        String response = coralPayUtils.invokeCoralPay(CORALPAY_INVOKE_URL, encryptedMessage);
        log.info("Invoke reference Response ==> " + response);
        if (response != null) {
            String decrypt = coralPayUtils.decrypt(response);
            log.info("decrypted Invoke reference Response ==> " + decrypt);
            return decrypt;
        }
        return null;
    }

    @Override
    public String coralPayPaymentNotification(String payload) {

        CoralPayTransactionStatusResponseDTO response = new CoralPayTransactionStatusResponseDTO();

        log.info("coralPay PaymentNotification payload ==> " + payload);

        String decryptedMsg = coralPayUtils.decrypt(payload);
        decryptedMsg = decryptedMsg.replace("TraceID", "traceID");
        decryptedMsg = decryptedMsg.replace("TransactionID", "transactionID");
        decryptedMsg = decryptedMsg.replace("UserID", "userID");

        log.info("coralPay PaymentNotification DecryptedMessage in json ==> " + decryptedMsg);
//        String response = externalCgateRESTClient.invokeReference(basic, encryptedMessage);
        CoralPayTransactionStatusDTO request = gson.fromJson(decryptedMsg, CoralPayTransactionStatusDTO.class);
        log.info("coralPay PaymentNotification request ==> " + request);

        Double amount = request.getAmount();
        if (amount > 0) {
            String customerMobile = request.getCustomerMobile();
            String merchantId = request.getMerchantId();
            String reference = request.getReference();
            //        String walletId = request.getUserID();
            String walletId = request.getTraceID();

            WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(walletId);
            if (walletAccount != null) {

                String beneficiaryName = walletAccount.getAccountOwner() != null ? walletAccount.getAccountOwner().getFullName() : walletAccount.getAccountName();

                double fundingAmount = amount - 0.0;

                FundDTO fundDTO = new FundDTO();
                fundDTO.setChannel("BankToWallet");
                fundDTO.setRrr(reference);
                fundDTO.setSourceAccountName("CoralPay");
                fundDTO.setSpecificChannel(SpecificChannel.CORAL_PAY.getName());
                fundDTO.setBeneficiaryName(beneficiaryName);
                fundDTO.setAccountNumber(walletId);
                fundDTO.setAmount(fundingAmount);
                fundDTO.setPhoneNumber(customerMobile);
                fundDTO.setTransRef(utility.getUniqueTransRef());
                fundDTO.setNarration("Funding of wallet(" + walletId + ") with the sum of " + utility.formatMoney(fundingAmount));
                fundDTO.setStatus(TransactionStatus.PROCESSING);

                producer.send(fundDTO);

                response.setResponseCode("00");
                response.setResponseMessage("Successful");

                String res = new Gson().toJson(response);
                log.info("coralPay PaymentNotification json response ==> " + res);

                if (res != null) {
                    return coralPayUtils.encrypt(res);
                }

            }
        }

        response.setResponseCode("01");
        response.setResponseMessage("Failed");

        String res = new Gson().toJson(response);
        log.info("coralPay PaymentNotification json response ==> " + res);

        if (res != null) {
            return coralPayUtils.encrypt(res);
        }

        return null;

    }

    @Override
    public String statusQuery(String payload) {
        log.info("StatusQuery payload ==> " + payload);
        String encryptedMessage = coralPayUtils.encrypt(payload);
        log.info("Status EncryptedMessage ==> " + encryptedMessage);
//        String response = externalCgateRESTClient.invokeReference(basic, encryptedMessage);
        String response = coralPayUtils.invokeCoralPay(CORALPAY_STATUS_QUERY_URL, encryptedMessage);
        log.info("Invoke reference Response ==> " + response);
        if (response != null) {
            String decrypt = coralPayUtils.decrypt(response);
            log.info("decrypted Invoke reference Response ==> " + decrypt);
            return decrypt;
        }
        return null;
    }

    @Override
    public String refund(String payload) {
        log.info("Refund payload ==> " + payload);
        String encryptedMessage = coralPayUtils.encrypt(payload);
        log.info("Refund EncryptedMessage ==> " + encryptedMessage);
//        String response = externalCgateRESTClient.invokeReference(basic, encryptedMessage);
        String response = coralPayUtils.invokeCoralPay(CORALPAY_REFUND_URL, encryptedMessage);
        log.info("Refund Response ==> " + response);
        if (response != null) {
            String decrypt = coralPayUtils.decrypt(response);
            log.info("decrypted Refund Response ==> " + decrypt);
            return decrypt;
        }
        return null;
    }

    private class Test {
        public Test() {
            String payload = "{" +
                "\"RequestHeader\": {" +
                "\"Username\": \"Remita\"," +
                "\"Password\": \"0841011021@006#1\"" +
                "}," +
                "\"RequestDetails\": {" +
                "\"TerminalId\": \"1057TT01\"" +
                "\"Channel\": \"USSD\"," +
                "\"Amount\": 2000.0," +
                "\"MerchantId\": \"1057TT010000001\"," +
                "\"TransactionType\": \"0\",\n" +
                "\"SubMerchantName\": \"DFS Wallet\"," +
                "\"TraceID\": \"09283474729\"" +
                "}" +
                "}";

            String s = invokeReference(payload);
            log.info("Test Response" + s);
        }
    }
}
