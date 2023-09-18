package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipient(String phoneNumber);
}
