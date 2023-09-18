package ng.com.systemspecs.apigateway.service.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import io.vavr.control.Either;
import ng.com.systemspecs.apigateway.config.AsyncConfiguration;
import ng.com.systemspecs.apigateway.config.KafkaProperties;
import ng.com.systemspecs.apigateway.domain.Scheme;
import ng.com.systemspecs.apigateway.domain.WalletAccount;
import ng.com.systemspecs.apigateway.domain.enumeration.SpecificChannel;
import ng.com.systemspecs.apigateway.domain.enumeration.TransactionStatus;
import ng.com.systemspecs.apigateway.service.SchemeService;
import ng.com.systemspecs.apigateway.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.BulkBeneficiaryDTO;
import ng.com.systemspecs.apigateway.service.dto.FundDTO;
import ng.com.systemspecs.apigateway.service.dto.PayFeesNotificationDTO;
import ng.com.systemspecs.apigateway.service.dto.TransactionCallBackDTO;
import ng.com.systemspecs.apigateway.service.kafka.GenericConsumer;
import ng.com.systemspecs.apigateway.service.kafka.deserializer.DeserializationError;
import ng.com.systemspecs.apigateway.util.Utility;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.System.out;

@Service
public class CallbackConsumer extends GenericConsumer<Object>{
    private final Logger log = LoggerFactory.getLogger(CallbackConsumer.class);
    private final WalletAccountService walletAccountService;
    private final SchemeService schemeService;
    private final Utility utility;
    private final AsyncConfiguration asyncConfiguration;

    public CallbackConsumer(@Value("${kafka.consumer.callback.name}") final String topicName,
                            final KafkaProperties kafkaProperties, WalletAccountService walletAccountService, SchemeService schemeService, Utility utility, AsyncConfiguration asyncConfiguration) {
        super(topicName, kafkaProperties.getConsumer().get("callback"), kafkaProperties.getPollingTimeout());
        this.walletAccountService = walletAccountService;
        this.schemeService = schemeService;
        this.utility = utility;
        this.asyncConfiguration = asyncConfiguration;
    }


    @Override
    protected void handleMessage(final ConsumerRecord<String, Either<DeserializationError, Object>> record) {
        final Either<DeserializationError, Object> value = record.value();

        try {
            if (value.isLeft()) {
                log.error("Deserialization record failure: {}", value.getLeft());
            } else {

                LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) value.get();
                LinkedHashMap<String, Object> dto = null;
                log.info("CallbackConsumer map====================================================== " + map);

                FundDTO fundDTO = new FundDTO();
                fundDTO.setId((Integer) map.get("id"));
                fundDTO.setAccountNumber(String.valueOf(map.get("account_number")));
                fundDTO.setAmount(Double.parseDouble(String.valueOf(map.get("amount"))));
                fundDTO.setChannel(String.valueOf(map.get("channel")));
                fundDTO.setDestBankCode(String.valueOf(map.get("dest_bank_code")));
                fundDTO.setSourceAccountNumber(String.valueOf(map.get("source_account_number")));
                fundDTO.setSourceBankCode(String.valueOf(map.get("source_bank_code")));
                fundDTO.setTransRef(String.valueOf(map.get("trans_ref")));
                fundDTO.setNarration(String.valueOf(map.get("narration")));
                fundDTO.setSpecificChannel(String.valueOf(map.get("specific_channel")));
                fundDTO.setPhoneNumber(String.valueOf(map.get("phone_number")));
                fundDTO.setBeneficiaryName(String.valueOf(map.get("beneficiary_name")));
                fundDTO.setRrr(String.valueOf(map.get("rrr")));
                fundDTO.setShortComment(String.valueOf(map.get("short_comment")));
                fundDTO.setWalletAccount((Boolean.parseBoolean(String.valueOf(map.get("is_wallet_account")))));
                fundDTO.setToBeSaved((Boolean.parseBoolean(String.valueOf(map.get("to_be_ saved")))));
                fundDTO.setSourceAccountName((String.valueOf(map.get("source_account_name"))));
                fundDTO.setAgentRef((String.valueOf(map.get("agent_ref"))));
                fundDTO.setRedeemBonus(Boolean.parseBoolean((String.valueOf(map.get("redeem_bonus")))));
                fundDTO.setBonusAmount(Double.parseDouble((String.valueOf(map.get("bonus_amount")))));
                fundDTO.setCharges(Double.parseDouble((String.valueOf(map.get("charges")))));
                fundDTO.setBulkTrans(Boolean.parseBoolean((String.valueOf(map.get("bulk_trans")))));
                fundDTO.setMultipleCredit(Boolean.parseBoolean((String.valueOf(map.get("multiple_credit")))));

                List<Object> array = (List<Object>) map.get("bulk_account_nos");
                log.info("Bulk Accounts Array ====> " + array);
                List<BulkBeneficiaryDTO> bulkDTOS = new ArrayList<>();

                if (!Objects.isNull(array)) {
                    for (Object o : array) {
                        LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) o;
                        BulkBeneficiaryDTO bulk = new BulkBeneficiaryDTO();
                        bulk.setAccountNumber(String.valueOf(obj.get("account_number")));
                        bulk.setAmount(Double.parseDouble(String.valueOf(obj.get("amount"))));
                        bulk.setBankCode(String.valueOf(obj.get("bank_code")));
                        bulkDTOS.add(bulk);
                    }
                }
                fundDTO.setBulkAccountNos(bulkDTOS);
                fundDTO.setStatus(TransactionStatus.valueOf(String.valueOf(map.get("status"))));

                log.info("CallbackConsumer MAP " + map);
                log.info("CallbackConsumer FundDTO " + fundDTO);

                String response = processMessage(fundDTO);
                log.info("Process message Response CallbackConsumer:: >>===> " + response);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("CallbackConsumer failed!");
        }
    }


public String processMessage(FundDTO fundDTO){
    AtomicReference<String> response = new AtomicReference<>("No URL to process!");
    TransactionCallBackDTO transactionCallBackDTO = buildTransactionCallBackDTO(fundDTO.getStatus(), fundDTO);

    WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(fundDTO.getSourceAccountNumber());

    if ("bankToWallet".equalsIgnoreCase(fundDTO.getChannel())
        || SpecificChannel.WALLENCY_SCHOOLS_FEES_PAYMENT.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())) {
        walletAccount = walletAccountService.findOneByAccountNumber(fundDTO.getAccountNumber());
    }
    Scheme scheme = walletAccount.getScheme();

    Executor executor = asyncConfiguration.getAsyncExecutor();
    if (executor != null) {
        executor.execute(() -> {
            Object payload = transactionCallBackDTO;
            if (SpecificChannel.WALLENCY_SCHOOLS_FEES_PAYMENT.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())) {
                payload = buildNotificationDTO(fundDTO);
            }
            if (scheme != null && utility.checkStringIsValid(scheme.getCallbackUrl())) {
                //send Call back to scheme url
                response.set(sendCallBack(scheme.getCallbackUrl(), payload));
                out.println("Callback response " + response);
            } else {
                //send Call back to scheme url
                schemeService.findSchemeId(1L).ifPresent(scheme1 -> {
                    if (utility.checkStringIsValid(scheme1.getCallbackUrl())) {
                        response.set(sendCallBack(scheme1.getCallbackUrl(), transactionCallBackDTO));
                        out.println("Callback response " + response);
                    }
                });
            }
        });
    }
    return response.get();
}

    private TransactionCallBackDTO buildTransactionCallBackDTO(TransactionStatus status, FundDTO fundDTO) {
        TransactionCallBackDTO transactionCallBackDTO = new TransactionCallBackDTO();

        transactionCallBackDTO.setDestination(fundDTO.getAccountNumber());
        transactionCallBackDTO.setDestinationAccountName(fundDTO.getBeneficiaryName());
        transactionCallBackDTO.setExternalRef(fundDTO.getRrr());
        transactionCallBackDTO.setNarration(fundDTO.getNarration());
        transactionCallBackDTO.setSource(fundDTO.getSourceAccountNumber());
        transactionCallBackDTO.setSourceAccountName(fundDTO.getSourceAccountName());
        transactionCallBackDTO.setStatus(status.toString());
        transactionCallBackDTO.setReference(transactionCallBackDTO.getReference());
        transactionCallBackDTO.setTransType(fundDTO.getChannel());
        transactionCallBackDTO.setAmount(fundDTO.getAmount());
        transactionCallBackDTO.setPhoneNumber(fundDTO.getPhoneNumber());
        transactionCallBackDTO.setSpecificTransType(fundDTO.getSpecificChannel());
        return transactionCallBackDTO;
    }

    private PayFeesNotificationDTO buildNotificationDTO(FundDTO fundDTO) {
        PayFeesNotificationDTO payFeesNotificationDTO = new Gson().fromJson(fundDTO.getAgentRef(), PayFeesNotificationDTO.class);
        return payFeesNotificationDTO;
    }

    private String sendCallBack(String url, Object payload) {
        System.out.println("Callback initial request payload ====> " + payload);

        if (payload == null) {
            return "fail";
        }

        try {
            String requestBody = getObjectAsJsonString(payload);
            System.out.println("Get obj requestBody ==>" + requestBody);

            HttpResponse<JsonNode> httpResponse = postCallBackURL(url, requestBody);
            out.println("Post call back response body " + httpResponse.getBody());
            out.println("Post call back response status text " + httpResponse.getStatusText());
            out.println("Post call back response status " + httpResponse.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    //    private ResponseEntity<String> postCallBackURL(String url, HttpEntity httpEntity) throws InterruptedException {
    private HttpResponse<JsonNode> postCallBackURL(String url, String body) throws InterruptedException, IllegalAccessException {
        out.println("Url ===> " + url);
        if (utility.checkStringIsNotValid(url)) {
            throw new IllegalAccessException("Invalid url configured " + url);
        }
        boolean isFailing = true;
//        ResponseEntity<String> responseEntity = null;
        HttpResponse<JsonNode> httpResponse = null;
        int count = 0;
        do {
            try {
                httpResponse = Unirest
                    .post(url)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asJson();
                System.out.println("Callback response entity ====> " + httpResponse);
                System.out.println("Callback response entity status text ====> " + httpResponse.getStatusText());
                System.out.println("Callback response entity status ====> " + httpResponse.getStatus());
                System.out.println("Callback response entity body ====> " + httpResponse.getBody());
                System.out.println("Callback response entity headers ====> " + httpResponse.getHeaders());
                if (200 == httpResponse.getStatus()) {
                    String responseBody = httpResponse.getBody().toString();
                    System.out.println("Callback response Body from scheme endpoint ====> " + responseBody);
                    if (responseBody != null) {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        System.out.println("Callback response from scheme endpoint ==>" + jsonObject);
                    }
                    isFailing = false;
                } else {
                    out.println("post Call back failed!! ===> " + httpResponse.getStatus());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isFailing) {
                Thread.sleep(300000); //try again in 5 mins if ailed
            }
            count++;
        } while (isFailing && count < 10);

        out.println("Final callBack response entity ====> " + httpResponse);

//        return responseEntity;
        return httpResponse;
    }


    private String getObjectAsJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @Bean
    public void executeKafkaCallbackRunner() {
        new SimpleAsyncTaskExecutor().execute(this);
    }
}
