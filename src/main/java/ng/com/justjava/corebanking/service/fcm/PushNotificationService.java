package ng.com.justjava.corebanking.service.fcm;

import ng.com.justjava.corebanking.service.NotificationService;
import ng.com.justjava.corebanking.service.dto.NotificationDTO;
import ng.com.justjava.corebanking.service.dto.PushNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class PushNotificationService {

    private final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);
    private final FCMService fcmService;
    private final NotificationService notificationService;

    public PushNotificationService(FCMService fcmService, NotificationService notificationService) {
        this.fcmService = fcmService;
        this.notificationService = notificationService;
    }

    public void sendPushNotification(PushNotificationRequest request) {
        try {
            fcmService.sendMessage(getSamplePayloadData(), request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    public void sendPushNotificationWithoutData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageWithoutData(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
            try{
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setContent(request.getMessage());
            notificationDTO.setTitle(request.getTitle());
            notificationDTO.setTime(Instant.now());
            notificationDTO.setRecipient(request.getRecipient());

            NotificationDTO save = notificationService.save(notificationDTO);
            logger.info("Push Notification saved ====> " + save);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private Map<String, String> getSamplePayloadData() {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("messageId", "msgid");
        pushData.put("name", "kazeem");
        pushData.put("user", "akinrinde kazeem");
        return pushData;
    }

    public void sendNotificationToToken(String message, String title, String deviceToken) {
        PushNotificationRequest request = new PushNotificationRequest();
        request.setMessage(message);
        request.setTitle(title);
        request.setToken(deviceToken);
        sendPushNotificationToToken(request);
    }
}
