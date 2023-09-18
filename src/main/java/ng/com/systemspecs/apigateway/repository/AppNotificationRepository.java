package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.AppNotification;
import ng.com.systemspecs.apigateway.domain.CorporateProfile;
import ng.com.systemspecs.apigateway.domain.enumeration.AppNotificationType;
import ng.com.systemspecs.apigateway.service.dto.AppNotificationDTO;
import ng.com.systemspecs.apigateway.service.dto.NotificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface AppNotificationRepository extends JpaRepository<AppNotification, Long> {

    AppNotification findOneById(Long id);

    List<AppNotification> findAllByScheme(String scheme);

    @Query("select a from AppNotification a where a.notificationType = ?1")
    List<AppNotification> findAllByNotificationType(AppNotificationType notificationtype);

    List<AppNotification> findAllByCreatedDateBetween(Instant startDate, Instant endDate);

    @Query("select a from AppNotification a where a.scheme = :scheme and a.createdDate between :startDate and :endDate")
    List<AppNotification> findAllBySchemeAndCreatedDateBetween(@Param("scheme") String scheme, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("select a from AppNotification a where concat(a.title, '') LIKE concat('%',:keyword, '%') or  lower(concat(a.audience, '')) LIKE lower(concat('%',:keyword, '%')) or  lower(a.notificationType, '') LIKE lower(concat('%',:keyword, '%')) order by a.createdDate")
    Page<AppNotification> findAllWithKeyword(Pageable pageable, @Param("keyword") String keyword);

    @Query("select a from AppNotification a where a.notificationType = ?1 and a.audience = ?2")
    List<AppNotification> findAllByNotificationTypeAndAudience(String notificationType, String audience);

    @Query("select a from AppNotification a where a.notificationType = ?1 and a.scheme = ?2")
    List<AppNotification> findAllByNotificationTypeAndScheme(AppNotificationType notificationType, String scheme);

    @Query("select a from AppNotification a where a.audience = ?1")
    List<AppNotificationDTO> findAllByAudience(String audience);
}
