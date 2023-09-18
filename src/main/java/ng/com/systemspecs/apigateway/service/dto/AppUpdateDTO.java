package ng.com.systemspecs.apigateway.service.dto;

import java.io.Serializable;
import ng.com.systemspecs.apigateway.domain.enumeration.DeviceType;

/**
 * A DTO for the {@link ng.com.systemspecs.apigateway.domain.AppUpdate} entity.
 */
public class AppUpdateDTO implements Serializable {
    
    private Long id;

    private DeviceType device;

    private String androidurl;

    private String iosurl;

    private String comments;

    private String androidversion;

    private String iosversion;
    
    public String getIosversion() {
		return iosversion;
	}

	public void setIosversion(String iosversion) {
		this.iosversion = iosversion;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeviceType getDevice() {
        return device;
    }

    public void setDevice(DeviceType device) {
        this.device = device;
    }

    public String getAndroidurl() {
        return androidurl;
    }

    public void setAndroidurl(String androidurl) {
        this.androidurl = androidurl;
    }

    public String getIosurl() {
        return iosurl;
    }

    public void setIosurl(String iosurl) {
        this.iosurl = iosurl;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAndroidversion() {
        return androidversion;
    }

    public void setAndroidversion(String androidversion) {
        this.androidversion = androidversion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUpdateDTO)) {
            return false;
        }

        return id != null && id.equals(((AppUpdateDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUpdateDTO{" +
            "id=" + getId() +
            ", device='" + getDevice() + "'" +
            ", androidurl='" + getAndroidurl() + "'" +
            ", iosurl='" + getIosurl() + "'" +
            ", comments='" + getComments() + "'" +
            ", androidversion='" + getAndroidversion() + "'" +
            ", iosversion='" + getIosversion() + "'" +
            "}";
    }
}
