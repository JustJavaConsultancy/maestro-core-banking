package ng.com.justjava.corebanking.service.mapper;

import ng.com.justjava.corebanking.domain.AppNotification;
import ng.com.justjava.corebanking.service.dto.AppNotificationDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AppNotificationMapper extends EntityMapper<AppNotificationDTO, AppNotification> {

    AppNotificationDTO toDto(AppNotification appNotification);

    AppNotification toEntity(AppNotificationDTO appNotificationDTO);

}
