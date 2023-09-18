package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.Notification;
import ng.com.justjava.corebanking.service.dto.NotificationDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {


    default Notification fromId(Long id) {
        if (id == null) {
            return null;
        }
        Notification notification = new Notification();
        notification.setId(id);
        return notification;
    }
}
