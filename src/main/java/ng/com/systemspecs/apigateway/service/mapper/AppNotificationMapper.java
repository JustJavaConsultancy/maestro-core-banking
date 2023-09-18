package ng.com.systemspecs.apigateway.service.mapper;

import ng.com.systemspecs.apigateway.domain.AppNotification;
import ng.com.systemspecs.apigateway.service.dto.AppNotificationDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AppNotificationMapper extends EntityMapper<AppNotificationDTO, AppNotification> {

    AppNotificationDTO toDto(AppNotification appNotification);

    AppNotification toEntity(AppNotificationDTO appNotificationDTO);

}
