package ng.com.justjava.corebanking.service.dto;

import java.io.Serializable;

public class DeviceTokenDTO implements Serializable {
    private String deviceToken;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public String toString() {
        return "DeviceTokenDTO{" +
            "deviceToken='" + deviceToken + '\'' +
            '}';
    }
}
