package ng.com.justjava.corebanking.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ng.com.justjava.corebanking.service.kafka.producer.TransProducer;
import ng.com.justjava.corebanking.domain.enumeration.TransactionType;
import ng.com.justjava.corebanking.service.PaymentTransactionService;
import ng.com.justjava.corebanking.service.RITSService;
import ng.com.justjava.corebanking.service.dto.PaymentResponseDTO;
import ng.com.justjava.corebanking.service.dto.PaymentTransactionDTO;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnqiryRequest;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnquiryResponse;
import ng.com.systemspecs.remitarits.bankenquiry.GetActiveBankResponse;
import ng.com.systemspecs.remitarits.bulkpayment.BulkPaymentRequest;
import ng.com.systemspecs.remitarits.bulkpayment.BulkPaymentResponse;
import ng.com.systemspecs.remitarits.bulkpaymentstatus.BulkPaymentStatusRequest;
import ng.com.systemspecs.remitarits.bulkpaymentstatus.BulkPaymentStatusResponse;
import ng.com.systemspecs.remitarits.configuration.Credentials;
import ng.com.systemspecs.remitarits.service.RemitaRITSService;
import ng.com.systemspecs.remitarits.service.impl.RemitaRITSServiceImpl;
import ng.com.systemspecs.remitarits.singlepayment.SinglePaymentRequest;
import ng.com.systemspecs.remitarits.singlepayment.SinglePaymentResponse;
import ng.com.systemspecs.remitarits.singlepaymentstatus.PaymentStatusRequest;
import ng.com.systemspecs.remitarits.singlepaymentstatus.PaymentStatusResponse;
import ng.com.systemspecs.remitarits.util.EnvironmentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class RITSServiceImpl implements RITSService {

    private final Logger log = LoggerFactory.getLogger(RITSServiceImpl.class);

    private static final long Lower_Bond = 10000000000L;
    private static final long Upper_Bond = 90000000000L;
    Credentials credentials = null;
    @Value("${app.constants.remita.rits.merchant-id}")
    private String MERCHANT_ID;

    @Value("${app.constants.remita.rits.rits-api-token}")
    private String RITS_API_TOKEN;

    @Value("${app.constants.remita.rits.rits-secret-key}")
    private String RITS_SECRET_KEY;

    @Value("${app.constants.remita.rits.rits-secret-key-iv}")
    private String RITS_SECRET_KEY_IV;

    @Value("${app.constants.remita.rits.rits-api-key}")
    private String RITS_API_KEY;

    private final TransProducer producer;
    private final PaymentTransactionService paymentTransactionService;

    public RITSServiceImpl(TransProducer producer, PaymentTransactionService paymentTransactionService) {
        this.producer = producer;
        this.paymentTransactionService = paymentTransactionService;
    }

    @Override
    public RemitaRITSService getRemitaRITSService(String requestId) {


        if (requestId == null) {
            requestId = String.valueOf(System.currentTimeMillis());
        }

        credentials = new Credentials();
        credentials.setMerchantId(MERCHANT_ID);
        credentials.setApiKey(RITS_API_KEY);
        credentials.setApiToken(RITS_API_TOKEN);
        credentials.setSecretKey(RITS_SECRET_KEY);
        credentials.setSecretKeyIv(RITS_SECRET_KEY_IV);
        credentials.setRequestId(requestId);
//	       credentials.setEnvironment(EnvironmentType.DEMO);
        credentials.setEnvironment(EnvironmentType.LIVE);

        log.info("REMITA RITS Service Credentials " + credentials);

        log.info("REMITA RITS MERCHANT_ID  ==> " + MERCHANT_ID);
        log.info("REMITA RITS RITS_API_KEY  ==> " + RITS_API_KEY);
        log.info("REMITA RITS RITS_API_TOKEN  ==> " + RITS_API_TOKEN);
        log.info("REMITA RITS RITS_SECRET_KEY  ==> " + RITS_SECRET_KEY);
        log.info("REMITA RITS RITS_SECRET_KEY_IV  ==> " + RITS_SECRET_KEY_IV);



        log.info("REMITA RITS Service Credentials " + credentials);

        log.info("REMITA RITS MERCHANT_ID  ==> " + MERCHANT_ID);
        log.info("REMITA RITS RITS_API_KEY  ==> " + RITS_API_KEY);
        log.info("REMITA RITS RITS_API_TOKEN  ==> " + RITS_API_TOKEN);
        log.info("REMITA RITS RITS_SECRET_KEY  ==> " + RITS_SECRET_KEY);
        log.info("REMITA RITS RITS_SECRET_KEY_IV  ==> " + RITS_SECRET_KEY_IV);

        return new RemitaRITSServiceImpl(credentials);

    }

    @Override
    public SinglePaymentResponse singlePayment(SinglePaymentRequest singleRequest) {

        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
        PaymentTransactionDTO paymentTransactionDTO = new PaymentTransactionDTO();

        String transRef = singleRequest.getTransRef();

        if (transRef == null) {
            transRef = String.valueOf(System.currentTimeMillis());
        }

        RemitaRITSService ritsService = getRemitaRITSService(transRef);

        singleRequest.setTransRef(transRef);
        log.info("request_id  = " + credentials.getRequestId());
        String amount = singleRequest.getAmount();
        String creditAccount = singleRequest.getCreditAccount();
        String debitAccount = singleRequest.getDebitAccount();
         String fromBank = singleRequest.getFromBank();
         String toBank = singleRequest.getToBank();

         try {
             String s = new ObjectMapper().writeValueAsString(singleRequest);
             log.info("SINGLE PAYMENT REQUEST +== " + s);

         } catch (JsonProcessingException e) {
             e.printStackTrace();
         }

         SinglePaymentResponse singlePaymentResponse = ritsService.singlePayment(singleRequest);

         log.info("SINGLE PAYMENT RESPONSE +== " + singlePaymentResponse);

         responseDTO.setCode((singlePaymentResponse.getData()).getResponseCode());
         // responseDTO.setMessage((singlePaymentResponse.getData()).getResponseMsg());

         if ((singlePaymentResponse.getData()).getResponseCode().equals("00")) {
             paymentTransactionDTO.setAmount(new BigDecimal(amount));
             paymentTransactionDTO.setChannel("BANK");
             paymentTransactionDTO.setDestinationAccount(String.valueOf(singleRequest.getCreditAccount()));
             paymentTransactionDTO.setSourceAccount(debitAccount);
             paymentTransactionDTO.setSourceNarration("Bank transfer  from ( " + debitAccount + " )");
             paymentTransactionDTO.setDestinationNarration("Bank Transfer into ( " + creditAccount + " )");
             paymentTransactionDTO.setPaymenttransID(System.currentTimeMillis());
             paymentTransactionDTO.setCurrency("NGN");
             paymentTransactionDTO.setSourceAccountBankCode(fromBank);
             paymentTransactionDTO.setDestinationAccountBankCode(toBank);
             paymentTransactionDTO.setTransactionRef((singlePaymentResponse.getData()).getTransRef());
             paymentTransactionDTO.setTransactionType(TransactionType.BANK_ACCOUNT_TRANSFER);

             PaymentTransactionDTO save = paymentTransactionService.save(paymentTransactionDTO);


             log.debug("creditAccount = " + creditAccount);
             responseDTO.setPaymentTransactionDTO(paymentTransactionDTO);
//             producer.send(responseDTO);
             responseDTO.setMessage("successful");
         } else {
		    		responseDTO.setMessage("failed");
		    	}
		     return  singlePaymentResponse;
		    }

	  @Override
	  public BulkPaymentResponse postBulkPayment(BulkPaymentRequest bulkPaymentRequest) {
          RemitaRITSService ritsService = getRemitaRITSService(bulkPaymentRequest.getBulkPaymentInfo().getBatchRef());
          return ritsService.bulkPayment(bulkPaymentRequest);

      }

	  @Override
	    public PaymentStatusResponse singlePaymentStatus(PaymentStatusRequest paymentStatusRequest) {
          RemitaRITSService ritsService = getRemitaRITSService(paymentStatusRequest.getTransRef());
          return ritsService.singlePaymentStatus(paymentStatusRequest);

      }


	  @Override
	    public BulkPaymentStatusResponse bulkPaymentStatus(BulkPaymentStatusRequest bulkPaymentStatusRequest) {
          RemitaRITSService ritsService = getRemitaRITSService(bulkPaymentStatusRequest.getBatchRef());
          return ritsService.bulkPaymentStatus(bulkPaymentStatusRequest);
      }

	  @Override
	    public AccountEnquiryResponse getAccountEnquiry(AccountEnqiryRequest accountEnqiryRequest) {
          RemitaRITSService ritsService = getRemitaRITSService(String.valueOf(System.currentTimeMillis()));
          return ritsService.accountEnquiry(accountEnqiryRequest);
      }


	  @Override
	    public  GetActiveBankResponse  getActiveBanks() {
          RemitaRITSService ritsService = getRemitaRITSService(String.valueOf(System.currentTimeMillis()));
          return  ritsService.getActiveBanks();

      }
}
