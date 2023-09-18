package ng.com.justjava.corebanking.domain;

import ng.com.justjava.corebanking.domain.enumeration.DeviceType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A AppUpdate.
 */
@Entity
@Table(name = "app_update")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AppUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "device")
    private DeviceType device;

    @Column(name = "androidurl")
    private String androidurl;

    @Column(name = "iosurl")
    private String iosurl;

    @Column(name = "comments")
    private String comments;

    @Column(name = "androidversion")
    private String androidversion;

    @Column(name = "iosversion")
    private String iosversion;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeviceType getDevice() {
        return device;
    }

    public AppUpdate device(DeviceType device) {
        this.device = device;
        return this;
    }

    public void setDevice(DeviceType device) {
        this.device = device;
    }

    public String getAndroidurl() {
        return androidurl;
    }

    public AppUpdate androidurl(String androidurl) {
        this.androidurl = androidurl;
        return this;
    }

    public void setAndroidurl(String androidurl) {
        this.androidurl = androidurl;
    }

    public String getIosurl() {
        return iosurl;
    }

    public AppUpdate iosurl(String iosurl) {
        this.iosurl = iosurl;
        return this;
    }

    public void setIosurl(String iosurl) {
        this.iosurl = iosurl;
    }

    public String getComments() {
        return comments;
    }

    public AppUpdate comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAndroidversion() {
        return androidversion;
    }

    public AppUpdate androidversion(String androidversion) {
        this.androidversion = androidversion;
        return this;
    }

    public void setAndroidversion(String androidversion) {
        this.androidversion = androidversion;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public String getIosversion() {
		return iosversion;
	}

	public void setIosversion(String iosversion) {
		this.iosversion = iosversion;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUpdate)) {
            return false;
        }
        return id != null && id.equals(((AppUpdate) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUpdate{" +
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
