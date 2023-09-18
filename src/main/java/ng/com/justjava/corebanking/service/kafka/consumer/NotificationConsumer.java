package ng.com.justjava.corebanking.service.kafka.consumer;

import com.google.common.base.CaseFormat;
import io.vavr.control.Either;
import ng.com.justjava.corebanking.service.kafka.deserializer.DeserializationError;
import ng.com.justjava.corebanking.client.ExternalOTPRESTClient;
import ng.com.justjava.corebanking.config.AsyncConfiguration;
import ng.com.justjava.corebanking.config.KafkaProperties;
import ng.com.justjava.corebanking.service.dto.PushNotificationRequest;
import ng.com.justjava.corebanking.service.dto.SendSMSDTO;
import ng.com.justjava.corebanking.service.fcm.PushNotificationService;
import ng.com.justjava.corebanking.service.kafka.GenericConsumer;
import ng.com.justjava.corebanking.util.Utility;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

@Service
public class NotificationConsumer extends GenericConsumer<Object>{
    private final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);
    private final PushNotificationService pushNotificationService;
    private final Utility utility;
    private final ExternalOTPRESTClient externalOTPRESTClient;
    private final AsyncConfiguration asyncConfiguration;

    public NotificationConsumer(@Value("${kafka.consumer.notification.name}") final String topicName,
                                final KafkaProperties kafkaProperties, Utility utility,
                                PushNotificationService pushNotificationService,
                                ExternalOTPRESTClient externalOTPRESTClient,
                                AsyncConfiguration asyncConfiguration) {

        super(topicName, kafkaProperties.getConsumer().get("notification"), kafkaProperties.getPollingTimeout());
        this.pushNotificationService = pushNotificationService;
        this.utility = utility;
        this.externalOTPRESTClient = externalOTPRESTClient;
        this.asyncConfiguration = asyncConfiguration;
    }

    public static <T> T toBean(Map<String, Object> beanPropMap, Class<T> type) {
        try {
            T beanInstance = type.getConstructor().newInstance();
            for (Object k : beanPropMap.keySet()) {


                String key = (String) k;

                key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);
                System.out.println(" The snake =====" + k + " the camel======" + key);
                Object value = beanPropMap.get(key);
                if (value != null) {
                    try {
                        Field field = type.getDeclaredField(key);
                        field.setAccessible(true);

                        System.out.println(" field name" + field.getName() + " value===" + value + "  k==" + k);
                        field.set(beanInstance, value);
                        field.setAccessible(false);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
            return beanInstance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void handleMessage(final ConsumerRecord<String, Either<DeserializationError, Object>> record) {
        try {
            System.out.println(" I'm inside notification Consumer...." + record.value());

            final Either<DeserializationError, Object> value = record.value();
            if (value.isLeft()) {
                log.error("Deserialization record failure: {}", value.getLeft());
            } else {
                HashMap mapValue = (HashMap) value.get();
                PushNotificationRequest request = new PushNotificationRequest();

                request.setMessage((String) mapValue.get("deviceMessage"));
                request.setToken((String) mapValue.get("token"));
                request.setTitle((String) mapValue.get("title"));
                String phoneNumber = (String) mapValue.get("phoneNumber");
                request.setRecipient(utility.formatPhoneNumber(phoneNumber));
                if (utility.checkStringIsValid(request.getRecipient(), request.getMessage(), request.getTitle(), request.getToken())) {
                    pushNotificationService.sendPushNotificationToToken(request);
                }


                String subject = (String) mapValue.get("subject");
                String content = (String) mapValue.get("content");
                String sms = (String) mapValue.get("sms");
                String email = (String) mapValue.get("email");

                if (utility.checkStringIsValid(email, subject, content)) {
                    utility.sendEmailByConsumer(email, subject, content);
                    utility.sendEmailByConsumer("akinrinde@justjava.com.ng", subject, content);
                }

                if (utility.checkStringIsValid(sms, phoneNumber)) {
                    SendSMSDTO sendSMSDTO = new SendSMSDTO();
                    sendSMSDTO.setSmsMessage(sms);
                    sendSMSDTO.setMobileNumber(phoneNumber);

                    try {
                        Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
                        if (asyncExecutor != null) {
                            asyncExecutor.execute(() -> {
                                externalOTPRESTClient.sendSMS(sendSMSDTO);
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(" The map here \n\n\n\n\n\n\n\n====mapValue==" + mapValue);
	    }
        }catch (Exception e){
            e.printStackTrace();
            log.error("NotificationConsumer failed!");
        }
    }
    @Bean
    public void executeKafkaLogRunner() {
        new SimpleAsyncTaskExecutor().execute(this);
    }
}
