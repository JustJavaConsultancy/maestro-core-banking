package ng.com.systemspecs.apigateway.service.kafka.consumer;

import io.vavr.control.Either;
import ng.com.systemspecs.apigateway.client.ExternalRESTClient;
import ng.com.systemspecs.apigateway.config.KafkaProperties;
import ng.com.systemspecs.apigateway.domain.Journal;
import ng.com.systemspecs.apigateway.domain.WalletAccount;
import ng.com.systemspecs.apigateway.domain.enumeration.TransactionStatus;
import ng.com.systemspecs.apigateway.service.JournalLineService;
import ng.com.systemspecs.apigateway.service.JournalService;
import ng.com.systemspecs.apigateway.service.TransactionLogService;
import ng.com.systemspecs.apigateway.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.accounting.AccountingService;
import ng.com.systemspecs.apigateway.service.dto.BulkBeneficiaryDTO;
import ng.com.systemspecs.apigateway.service.dto.FundDTO;
import ng.com.systemspecs.apigateway.service.kafka.GenericConsumer;
import ng.com.systemspecs.apigateway.service.kafka.deserializer.DeserializationError;
import ng.com.systemspecs.apigateway.util.Utility;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransConsumer extends GenericConsumer<Object> {
//    @Autowired
    ExternalRESTClient externalRESTClient;

    private final AccountingService accountingService;
    private final Utility utility;
    private final TransactionLogService transactionLogService;
    private final JournalLineService journalLineService;
    private final JournalService journalService;
    private final WalletAccountService walletAccountService;
    private final Logger log = LoggerFactory.getLogger(TransConsumer.class);

    public TransConsumer(@Value("${kafka.consumer.trans.name}") final String topicName,
                         final KafkaProperties kafkaProperties, AccountingService accountingService, Utility utility, TransactionLogService transactionLogService, JournalLineService journalLineService, JournalService journalService, WalletAccountService walletAccountService) {
        super(topicName, kafkaProperties.getConsumer().get("trans"), kafkaProperties.getPollingTimeout());
        this.accountingService = accountingService;
        this.utility = utility;
        this.transactionLogService = transactionLogService;
        this.journalLineService = journalLineService;
        this.journalService = journalService;
        this.walletAccountService = walletAccountService;
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
                log.info("TransConsumer map====================================================== " + map);

                if (map.containsKey("reference")) {
                    log.info("map 222/n/n/n/n/n====================================================== " + map);
                    accountingService.treatInvoice(String.valueOf(map.get("reference")), String.valueOf(map.get("action")), String.valueOf(map.get("account_number")));
                } else if (map.containsKey("channel")) {
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
                    String originalRef = fundDTO.getTransRef();
                    double originalAmt = fundDTO.getAmount();
                    String originalBeneficary = fundDTO.getBeneficiaryName();

                    log.info("FUND DTO MAP " + map);
                    log.info("FUND DTO CREATED FROM MAP " + fundDTO);

                    Optional<Journal> byReference = journalService.findByReference(fundDTO.getTransRef().trim().toUpperCase());
                    log.info("Reference in journal exist ==> " + byReference.isPresent());
                    if (!byReference.isPresent()) {
                        String response = processMessage(fundDTO, originalRef, originalAmt, originalBeneficary, byReference);
                        log.info("Process message Response ===> " + response);
                    } else {
                        System.out.println("Reference number already exists in journal ===> " + byReference);
                    }
                }
                log.info("Handling record: {}", map);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(" error failure because ===>" + e.getLocalizedMessage());
            e.printStackTrace();
        }

        // TODO: Here is where you can handle your messages
    }

    private String processMessage(
        FundDTO fundDTO, String originalRef, double originalAmt,
        String originalBeneficary, Optional<Journal> byReference
    ) throws Exception {
        log.info("Journal by reference is not present");
        FundDTO byTransRef = transactionLogService.findByTransRef(fundDTO.getTransRef().trim());
        String bulkAccounts = "";
        log.info("Transaction by reference ==> " + byReference);

        if (byTransRef != null) {
            System.out.println("Retrieved fundDTO =======> " + byTransRef);
            return accountingService.fundWallet(byTransRef);
        }

        log.info("Transaction log by reference does not exist");
        log.info("Is Transaction Bulk Tnx?  :::>   " + fundDTO.isBulkTrans());
        if (fundDTO.isBulkTrans()) {
            if (fundDTO.isMultipleCredit() && fundDTO.getBulkAccountNos().size() > 0) {
                log.info("Transaction is bulk");
                log.info("Level Two:::> " + fundDTO.getBulkAccountNos());
                for (BulkBeneficiaryDTO beneficiary : fundDTO.getBulkAccountNos()) {

                }
//           fundDTO.setRrr(bulkRefs); //todo set bulk reference as rrr
                fundDTO.setTransRef(originalRef);
                fundDTO.setAmount(originalAmt);
                fundDTO.setBeneficiaryName(originalBeneficary);
                FundDTO save = transactionLogService.saveBulkTrans(fundDTO);
                save.setBulkAccountNos(fundDTO.getBulkAccountNos());
                return accountingService.fundWallet(save);

            } else if (!fundDTO.isMultipleCredit() /*This means bulk transaction is multiple debit*/
                && fundDTO.getBulkAccountNos().size() > 0) {
                log.info("Transaction is bulk but debits");

                for (BulkBeneficiaryDTO b : fundDTO.getBulkAccountNos()) {
                    WalletAccount w = walletAccountService.findOneByAccountNumber(b.getAccountNumber());
                    String newRef = utility.getUniqueTransRef();
                    fundDTO.setRrr(originalRef + "||" + newRef);
                    fundDTO.setDestBankCode(b.getBankCode());
                    fundDTO.setTransRef(newRef);
                    fundDTO.setStatus(TransactionStatus.START);
                    fundDTO.setAccountNumber(b.getAccountNumber());
                    fundDTO.setAmount(b.getAmount());
                    fundDTO.setBeneficiaryName(w.getAccountFullName());
                    FundDTO save = transactionLogService.saveBulkTrans(fundDTO);
                    System.out.println("Saved Bulk fundDTO =======> " + save);
                }
                fundDTO.setTransRef(originalRef);
                fundDTO.setAmount(originalAmt);
                fundDTO.setBeneficiaryName(originalBeneficary);

                return accountingService.fundWallet(fundDTO);
            }
        } else {
            log.info("Transaction is new one and not bulk ===> ");
            fundDTO.setStatus(TransactionStatus.START);
            log.info("About to save a new transaction");
            try {
                FundDTO save = transactionLogService.save(fundDTO);
                System.out.println("Saved Single Transaction fundDTO =======> " + save);
                return accountingService.fundWallet(save);
            } catch (Exception e) {
                log.info("Exception saving new funddto because " + e.getLocalizedMessage());
                e.printStackTrace();
                FundDTO retrievedFundDto = transactionLogService.findByTransRef(fundDTO.getTransRef());
                if (retrievedFundDto != null) {
                    return accountingService.fundWallet(retrievedFundDto);
                } else {
                    log.info("Could not retrieve nor save");
                }
            }
        }
        return "Failed ";
    }

    @Bean
    public void executeKafkaTransRunner() {
        new SimpleAsyncTaskExecutor().execute(this);
    }
}
