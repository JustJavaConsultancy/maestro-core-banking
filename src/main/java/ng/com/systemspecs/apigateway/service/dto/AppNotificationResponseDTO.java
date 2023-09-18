package ng.com.systemspecs.apigateway.service.dto;

import java.util.List;

public class AppNotificationResponseDTO {
    List<AppNotificationDTO> IN_APP;
    List<AppNotificationDTO> PUSH_NOTIFICATION;

    public List<AppNotificationDTO> getIN_APP() {
        return IN_APP;
    }

    public void setIN_APP(List<AppNotificationDTO> IN_APP) {
        this.IN_APP = IN_APP;
    }

    public List<AppNotificationDTO> getPUSH_NOTIFICATION() {
        return PUSH_NOTIFICATION;
    }

    public void setPUSH_NOTIFICATION(List<AppNotificationDTO> PUSH_NOTIFICATION) {
        this.PUSH_NOTIFICATION = PUSH_NOTIFICATION;
    }

    public AppNotificationResponseDTO(List<AppNotificationDTO> IN_APP, List<AppNotificationDTO> PUSH_NOTIFICATION) {
        this.IN_APP = IN_APP;
        this.PUSH_NOTIFICATION = PUSH_NOTIFICATION;
    }
}
