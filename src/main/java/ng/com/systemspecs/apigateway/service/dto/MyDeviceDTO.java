package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.enumeration.DeviceStatus;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DTO for the {@link ng.com.systemspecs.apigateway.domain.MyDevice} entity.
 */
public class MyDeviceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private DeviceStatus status;

    private ZonedDateTime last_login_date;

    @NotNull
    private String deviceId;

    @NotNull
    private String deviceNotificationToken;

    private Long profileId;

    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public ZonedDateTime getLast_login_date() {
        return last_login_date;
    }

    public void setLast_login_date(ZonedDateTime last_login_date) {
        this.last_login_date = last_login_date;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceNotificationToken() {
        return deviceNotificationToken;
    }

    public void setDeviceNotificationToken(String deviceNotificationToken) {
        this.deviceNotificationToken = deviceNotificationToken;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyDeviceDTO)) {
            return false;
        }

        return id != null && id.equals(((MyDeviceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MyDeviceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", last_login_date='" + getLast_login_date() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", deviceNotificationToken='" + getDeviceNotificationToken() + "'" +
            ", profileId=" + getProfileId() +
            ", phoneNumber=" + getPhoneNumber() +
            "}";
    }
}
