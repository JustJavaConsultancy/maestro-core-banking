package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.AppNotification;
import ng.com.systemspecs.apigateway.domain.enumeration.AppNotificationType;
import ng.com.systemspecs.apigateway.service.dto.AppNotificationDTO;
import ng.com.systemspecs.apigateway.service.dto.AppNotificationResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.AppNotification}.
 */
public interface AppNotificationService {

    Optional<AppNotificationDTO> findById(Long id);

    AppNotificationDTO findOneById(Long id);

    List<AppNotificationDTO> findAllByScheme(String scheme);

    List<AppNotificationDTO> findAllByAudience(String audience);

    List<AppNotificationDTO> findAllByNotificationType(AppNotificationType notificationtype);

    List<AppNotificationDTO> findAllByCreatedDateBetween(Instant startDate, Instant endDate);

    List<AppNotificationDTO> findAllBySchemeAndCreatedDateBetween( String scheme, Instant startDate, Instant endDate);

    AppNotificationDTO save(AppNotificationDTO notificationDTO);

    Page<AppNotificationDTO> findAll(Pageable pageable);

    AppNotificationResponseDTO findCustomerAppNotifications(String scheme);

    List<AppNotification> findAllByNotificationTypeAndAudience(String notificationType, String audience);

    Optional<AppNotificationDTO> findOne(Long id);

    Page<AppNotificationDTO> findAllWithKeyword(Pageable pageable, String key);

    List<AppNotificationDTO> findAllByNotificationTypeAndScheme(AppNotificationType appNotificationType, String scheme);

    void sendAppSystemMessage(AppNotificationDTO notificationDTO);
}
