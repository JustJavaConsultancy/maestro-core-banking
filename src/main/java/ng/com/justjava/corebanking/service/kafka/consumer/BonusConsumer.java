package ng.com.justjava.corebanking.service.kafka.consumer;

import io.vavr.control.Either;
import ng.com.justjava.corebanking.service.accounting.AccountingService;
import ng.com.justjava.corebanking.service.kafka.deserializer.DeserializationError;
import ng.com.justjava.corebanking.config.KafkaProperties;
import ng.com.justjava.corebanking.service.JournalLineService;
import ng.com.justjava.corebanking.service.JournalService;
import ng.com.justjava.corebanking.service.TransactionLogService;
import ng.com.justjava.corebanking.service.kafka.GenericConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class BonusConsumer extends GenericConsumer<Object> {

    private final AccountingService accountingService;
    private final TransactionLogService transactionLogService;
    private final JournalLineService journalLineService;
    private final JournalService journalService;
    private final Logger log = LoggerFactory.getLogger(BonusConsumer.class);

    public BonusConsumer(@Value("${kafka.consumer.bonus.name}") final String topicName,
                         final KafkaProperties kafkaProperties, AccountingService accountingService, TransactionLogService transactionLogService, JournalLineService journalLineService, JournalService journalService) {
        super(topicName, kafkaProperties.getConsumer().get("bonus"), kafkaProperties.getPollingTimeout());
        this.accountingService = accountingService;
        this.transactionLogService = transactionLogService;
        this.journalLineService = journalLineService;
        this.journalService = journalService;
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
                log.info("Handling record: {}", map);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // TODO: Here is where you can handle your messages
    }

    @Bean
    public void executeKafkaTransRunner() {
        new SimpleAsyncTaskExecutor().execute(this);
    }
}
