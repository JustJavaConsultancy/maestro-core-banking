package ng.com.systemspecs.apigateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ng.com.systemspecs.apigateway.domain.enumeration.DeviceStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A MyDevice.
 */
@Entity
@Table(name = "my_device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MyDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeviceStatus status;

    @Column(name = "last_login_date")
    private ZonedDateTime last_login_date;

    @NotNull
    @Column(name = "device_id", nullable = false, unique = true)
    private String deviceId;

    @Column(name = "device_notification_token")
    private String deviceNotificationToken;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "myDevices", allowSetters = true)
    private Profile profile;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MyDevice name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public MyDevice status(DeviceStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public ZonedDateTime getLast_login_date() {
        return last_login_date;
    }

    public MyDevice last_login_date(ZonedDateTime last_login_date) {
        this.last_login_date = last_login_date;
        return this;
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

    public MyDevice deviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getDeviceNotificationToken() {
        return deviceNotificationToken;
    }

    public void setDeviceNotificationToken(String deviceNotificationToken) {
        this.deviceNotificationToken = deviceNotificationToken;
    }

    public MyDevice deviceNotificationToken(String deviceNotificationToken) {
        this.deviceNotificationToken = deviceNotificationToken;
        return this;
    }

    public Profile getProfile() {
        return profile;
    }

    public MyDevice profile(Profile profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyDevice)) {
            return false;
        }
        return id != null && id.equals(((MyDevice) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MyDevice{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", last_login_date='" + getLast_login_date() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", deviceNotificationToken='" + getDeviceNotificationToken() + "'" +
            "}";
    }
}
