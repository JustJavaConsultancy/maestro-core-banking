package ng.com.justjava.corebanking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ng.com.justjava.corebanking.service.BillerTransactionService;
import ng.com.justjava.corebanking.service.HealthCheckService;
import ng.com.justjava.corebanking.service.RITSService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.justjava.corebanking.util.Utility;
import ng.com.systemspecs.remitabillinggateway.util.APiResponseCode;
import ng.com.systemspecs.remitabillinggateway.validate.CustomField;
import ng.com.systemspecs.remitabillinggateway.validate.ValidateRequest;
import ng.com.systemspecs.remitabillinggateway.validate.ValidateResponse;
import ng.com.systemspecs.remitabillinggateway.validate.Value;
import ng.com.systemspecs.remitarits.singlepayment.SinglePayment;
import ng.com.systemspecs.remitarits.singlepayment.SinglePaymentRequest;
import ng.com.systemspecs.remitarits.singlepayment.SinglePaymentResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class HealthCheckServiceImpl implements HealthCheckService {

    @org.springframework.beans.factory.annotation.Value("${app.constants.inline.secret-key}")
    private String secretKey;
    @org.springframework.beans.factory.annotation.Value("${app.constants.inline.inline-pmt-status-public-key}")
    private String inlinePmtStatusPublicKey;
    @org.springframework.beans.factory.annotation.Value("${app.constants.inline.remita-live-url}")
    private String remitaLiveUrl;

    private final BillerTransactionService billerTransactionService;
    private final WalletAccountService walletAccountService;
    private final RITSService ritsService;
    private final Utility utility;

    public HealthCheckServiceImpl(BillerTransactionService billerTransactionService, WalletAccountService walletAccountService, RITSService ritsService, Utility utility) {
        this.billerTransactionService = billerTransactionService;
        this.walletAccountService = walletAccountService;
        this.ritsService = ritsService;
        this.utility = utility;
    }


    @Override
    public List<StatusCheckDTO> checkExternalServicesStatus() {
//        StatusCheckDTO DSTVStatus = checkStatus("DSTV", "12345789", "246272782");
        StatusCheckDTO bankTransferStatus = checkRitsService();
        StatusCheckDTO inlineVerificationStatus = checkInlineVerification();
        StatusCheckDTO startimesStatus = checkStatus("Startimes", "2020072935003", "6464246753", "Startimes biller service");
        StatusCheckDTO DSTVStatus = checkStatus("DSTV", "2020072929000", "6272607722", "DSTV biller service");
        StatusCheckDTO GOTVStatus = checkStatus("GOTV", "2020072922000", "6272607695", "GOTV biller service");
        StatusCheckDTO JEDCStatus = checkStatus("JEDC", "2997091595", "2997091330", "JEDC biller service");
        StatusCheckDTO EKDCStatus = checkStatus("EKDC", "3310524079", "3310531523", "EKDC biller service");
        StatusCheckDTO IBEDCStatus = checkStatus("IBEDC", "4111452350", "4127916939", "IBEDC biller service");
        StatusCheckDTO EEDCCStatus = checkStatus("EEDC", "4111465504", "4127916944", "EEDC biller service");
        StatusCheckDTO AEDCStatus = checkStatus("AEDC", "2237112949", "", "AEDC biller service");
        StatusCheckDTO IKEDCStatus = checkStatus("IKEDC", "4111432182", "4127916933", "IKEDC biller service");
        StatusCheckDTO SMILEStatus = checkStatus("SMILE", "2020072936004", "6114998704", "SMILE biller service");
        StatusCheckDTO MTNAirtimeStatus = checkStatus("MTN", "1273219412", "", "MTN biller service");
        StatusCheckDTO GLOAirtimeStatus = checkStatus("GLO", "1273219257", "", "GLO biller service");
        StatusCheckDTO NineMobileAirtimeStatus = checkStatus("9 MOBILE", "1273219173", "", "9 mobile biller service");
        StatusCheckDTO AirtelAirtimeStatus = checkStatus("AIRTEL", "20200307001008", "", "Airtel biller service");

        List<StatusCheckDTO> listOfStatuses = new ArrayList<>();
        listOfStatuses.add(bankTransferStatus);
        listOfStatuses.add(inlineVerificationStatus);
        listOfStatuses.add(DSTVStatus);
        listOfStatuses.add(GOTVStatus);
        listOfStatuses.add(JEDCStatus);
        listOfStatuses.add(EKDCStatus);
        listOfStatuses.add(IBEDCStatus);
        listOfStatuses.add(EEDCCStatus);
        listOfStatuses.add(AEDCStatus);
        listOfStatuses.add(IKEDCStatus);
        listOfStatuses.add(MTNAirtimeStatus);
        listOfStatuses.add(GLOAirtimeStatus);
        listOfStatuses.add(NineMobileAirtimeStatus);
        listOfStatuses.add(AirtelAirtimeStatus);
        listOfStatuses.add(SMILEStatus);
        listOfStatuses.add(startimesStatus);
        return listOfStatuses;
    }

    private StatusCheckDTO checkInlineVerification() {
        String transRef = utility.getUniqueTransRef();
        StatusCheckDTO statusCheckDTO = new StatusCheckDTO();
        String hash = new DigestUtils("SHA-512").digestAsHex(transRef + secretKey);

        System.out.println("SECRET KEY " + secretKey);
        System.out.println("HASH KEY " + hash);
        System.out.println("SECRET KEY " + secretKey);
        System.out.println("HASH KEY " + hash);
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, String> params = new HashMap<String, String>();
            params.put("transId", transRef);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("publicKey", inlinePmtStatusPublicKey);
            headers.set("TXN_HASH", hash);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            System.out.println("PUBLIC KEY " + inlinePmtStatusPublicKey);
            System.out.println("REMITDEMO LIVE URL " + remitaLiveUrl);

            HttpEntity<String> entity = new HttpEntity<String>(headers);

            //HttpEntity<String> exchange = restTemplate.exchange("http://login.remita.net/payment/v1/payment/query/{transId}", HttpMethod.GET, entity, String.class, params);


            ResponseEntity<InlineStatusResponse> response = restTemplate.exchange(remitaLiveUrl, HttpMethod.GET, entity, InlineStatusResponse.class, params);
            System.out.println("response============================================= " + response.toString());
            System.out.println("response============================================= " + new ObjectMapper().writeValueAsString(response.getBody()));
            System.out.println("InlineStatusResponse============================================= " + response.getBody().toString());
            System.out.println(params.get("transId"));

            statusCheckDTO.setService("Inline verification");
            String systemDescription = "Inline verification API for fund Wallet";
            statusCheckDTO.setDescription(systemDescription);

            if (response.getBody() != null && "99".equalsIgnoreCase(response.getBody().getResponseCode()) && response.getBody().getResponseData() != null) {
                if (response.getBody().getResponseData() != null && !response.getBody().getResponseData().isEmpty()) {
                    InlineResponseDatum inlineResponseDatum = response.getBody().getResponseData().get(0);
                    if (inlineResponseDatum.getPaymentState().equalsIgnoreCase("UNKNOWN_TRANSACTION")) {
                        statusCheckDTO.setStatus("UP");
                        return statusCheckDTO;
                    } else {
                        statusCheckDTO.setMessage(inlineResponseDatum.getMessage());
                        statusCheckDTO.setStatus("DOWN");
                        statusCheckDTO.setTransRef(transRef);
                        return statusCheckDTO;
                    }
                } else if (response.getBody() != null) {
                    statusCheckDTO.setMessage(response.getBody().getResponseMsg());
                    statusCheckDTO.setStatus("DOWN");
                    statusCheckDTO.setTransRef(transRef);
                    return statusCheckDTO;
                }

            }
        } catch (Exception e) {
            System.out.println(e);
            statusCheckDTO.setStatus("DOWN");
            statusCheckDTO.setMessage(e.getLocalizedMessage());
            statusCheckDTO.setTransRef(transRef);

            return statusCheckDTO;
        }

        statusCheckDTO.setStatus("DOWN");
        statusCheckDTO.setMessage("Failure");
        statusCheckDTO.setTransRef(transRef);
        return statusCheckDTO;
    }

    private StatusCheckDTO checkRitsService() {
        SinglePaymentRequest request = new SinglePaymentRequest();
        request.setAmount(String.valueOf(0.0));
        request.setCreditAccount("012398000");
        request.setDebitAccount("2033196815");
        request.setFromBank("011");
        request.setToBank("050");
        request.setNarration("Send Money from Wallet Account To " + request.getCreditAccount());
        String systemDescription = "RITS Service for bank transfer";

        StatusCheckDTO statusCheckDTO = new StatusCheckDTO();
        statusCheckDTO.setService("Bank Transfer");
        statusCheckDTO.setDescription(systemDescription);

        String transRef = utility.getUniqueTransRef();
        request.setTransRef(transRef);
        SinglePaymentResponse singlePaymentResponse = ritsService.singlePayment(request);
        if (singlePaymentResponse.getData() != null) {
            SinglePayment singlePayment = singlePaymentResponse.getData();
            if (singlePayment != null &&
                singlePayment.getResponseCode().equalsIgnoreCase("54") &&
                singlePayment.getResponseDescription().equalsIgnoreCase("INVALID AMOUNT")) {
                statusCheckDTO.setStatus("UP");
                return statusCheckDTO;
            } else if (singlePayment != null) {
                statusCheckDTO.setMessage(singlePayment.getResponseDescription());
                statusCheckDTO.setStatus("DOWN");
                statusCheckDTO.setTransRef(transRef);
                return statusCheckDTO;
            }
        }

        statusCheckDTO.setStatus("DOWN");
        statusCheckDTO.setTransRef(transRef);
        statusCheckDTO.setMessage(singlePaymentResponse.getStatus());
        return statusCheckDTO;
    }

    private StatusCheckDTO checkStatus(String billerName, String billId, String customFieldId, String systemDescription) {
        List<CustomFieldDTO> customFields = new ArrayList<>();
        String transRef = utility.getUniqueTransRef();
        CustomFieldDTO customFieldDTO = new CustomFieldDTO(customFieldId, "1234567678");
        customFields.add(customFieldDTO);
        ValidateRequest validateRequest = buildValidateRequest(customFields, billId);
        GenericResponseDTO genericResponseDTO = billerTransactionService.validateCheck(validateRequest);
        StatusCheckDTO statusCheckDTO = new StatusCheckDTO();
        statusCheckDTO.setService(billerName);
        statusCheckDTO.setDescription(systemDescription);

        if (genericResponseDTO.getStatus().equals(HttpStatus.OK)) {
            statusCheckDTO.setStatus("UP");
        } else if (genericResponseDTO.getData() != null) {
            ValidateResponse data = (ValidateResponse) genericResponseDTO.getData();
            if (data.getResponseCode().equalsIgnoreCase(APiResponseCode.ERROR_WHILE_CONNECTING.getCode())) {
                statusCheckDTO.setStatus("DOWN");
                statusCheckDTO.setMessage(data.getResponseMsg());
                statusCheckDTO.setTransRef(transRef);
            }
        } else {
            statusCheckDTO.setStatus("DOWN");
            statusCheckDTO.setMessage(genericResponseDTO.getMessage());
            statusCheckDTO.setTransRef(transRef);

        }

        return statusCheckDTO;
    }

    private StatusCheckDTO checkStartimesStatus() {
        List<CustomFieldDTO> customFields = new ArrayList<>();
        String transRef = utility.getUniqueTransRef();
        CustomFieldDTO customFieldDTO = new CustomFieldDTO("6464246753", "1234567678");
        customFields.add(customFieldDTO);
        String billId = "2020072935003";
        ValidateRequest validateRequest = buildValidateRequest(customFields, billId);
        GenericResponseDTO genericResponseDTO = billerTransactionService.validateCheck(validateRequest);
        StatusCheckDTO statusCheckDTO = new StatusCheckDTO();
        statusCheckDTO.setService("startimes");

        if (genericResponseDTO.getStatus().equals(HttpStatus.OK)) {
            statusCheckDTO.setStatus("UP");
        } else if (genericResponseDTO.getData() != null) {
            ValidateResponse data = (ValidateResponse) genericResponseDTO.getData();
            if (data.getResponseCode().equalsIgnoreCase(APiResponseCode.ERROR_WHILE_CONNECTING.getCode())) {
                statusCheckDTO.setStatus("DOWN");
                statusCheckDTO.setTransRef(transRef);
                statusCheckDTO.setMessage(data.getResponseMsg());

            }
        } else {
            statusCheckDTO.setStatus("DOWN");
            statusCheckDTO.setTransRef(transRef);
            statusCheckDTO.setMessage(genericResponseDTO.getMessage());
        }

        return statusCheckDTO;

    }

    private ValidateRequest buildValidateRequest(List<CustomFieldDTO> customFields, String billId) {

        ValidateRequest validateRequest = new ValidateRequest();

        List<CustomField> customFieldList = new ArrayList<>();

        for (CustomFieldDTO customFieldDTO : customFields) {
            List<Value> valueList = new ArrayList<>();
            Value value = new Value();
            CustomField customField = new CustomField();
            customField.setId(customFieldDTO.getCustomFieldId());

            value.setQuantity(0);
            value.setValue(customFieldDTO.getValue());
            valueList.add(value);

            customField.setValues(valueList);
            customFieldList.add(customField);

        }

        validateRequest.setCustomFields(customFieldList);

        validateRequest.setAmount(BigDecimal.valueOf(0.0));
        validateRequest.setBillId(billId);
        validateRequest.setCurrency("NGN");

        validateRequest.setPayerEmail("system@wallet.remita.net");
        validateRequest.setPayerPhone("07064907683");
        validateRequest.setPayerName("System System");

        return validateRequest;
    }
}
