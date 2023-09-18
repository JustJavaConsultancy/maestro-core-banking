package ng.com.systemspecs.apigateway.service.kafka.producer;

import ng.com.systemspecs.apigateway.config.KafkaProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class BonusProducer {

    private final Logger log = LoggerFactory.getLogger(BonusProducer.class);

    private final KafkaProducer<String, Object> kafkaProducer;

    private final String topicName;

    public BonusProducer(@Value("${kafka.producer.bonus.name}") final String topicName, final KafkaProperties kafkaProperties) {
        this.topicName = topicName;
        this.kafkaProducer = new KafkaProducer<>(kafkaProperties.getProducer().get("bonus"));
    }

    @PostConstruct
    public void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public void send(final Object message) {
        final ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, message);
        try {
            log.info("Sending asynchronously a Object record to topic: '" + topicName + "'");
            kafkaProducer.send(record);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void shutdown() {
        log.info("Shutdown Kafka producer");
        kafkaProducer.close();
    }
}
