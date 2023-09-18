package ng.com.justjava.corebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ng.com.justjava.corebanking.domain.enumeration.KycRequestDocType;
import ng.com.justjava.corebanking.domain.enumeration.KycRequestStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A KycRequest.
 */
@Entity
@Table(name = "kyc_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class KycRequest extends AbstractDateAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "current_level", nullable = false)
    private Integer currentLevel;


    @Column(name = "next_level", nullable = true)
    private Integer nextLevel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private KycRequestStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_doc_type", nullable = true)
    private KycRequestDocType requestDocType = KycRequestDocType.ELECTRICITY_BILL;


    @Column(name = "doc_id", nullable = true)
    private String documentId;


    @Column(name = "doc_date_issued", nullable = true)
    private LocalDate docDateIssued;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "kycRequests", allowSetters = true)
    private Profile profile;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "kycRequests", allowSetters = true)
    private Profile senderProfile;

    @ManyToOne
    @JsonIgnoreProperties(value = "kycRequests", allowSetters = true)
    private Profile approver;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public KycRequest currentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
        return this;
    }

    public Integer getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(Integer nextLevel) {
		this.nextLevel = nextLevel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public KycRequestStatus getStatus() {
        return status;
    }

    public void setStatus(KycRequestStatus status) {
        this.status = status;
    }

    public KycRequest status(KycRequestStatus status) {
        this.status = status;
        return this;
    }

    public KycRequestDocType getRequestDocType() {
		return requestDocType;
	}

	public void setRequestDocType(KycRequestDocType requestDocType) {
		this.requestDocType = requestDocType;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public LocalDate getDocDateIssued() {
		return docDateIssued;
	}

	public void setDocDateIssued(LocalDate docDateIssued) {
		this.docDateIssued = docDateIssued;
	}

	public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public KycRequest profile(Profile profile) {
        this.profile = profile;
        return this;
    }

    public Profile getSenderProfile() {
        return senderProfile;
    }

    public void setSenderProfile(Profile profile) {
        this.senderProfile = profile;
    }

    public KycRequest senderProfile(Profile profile) {
        this.senderProfile = profile;
        return this;
    }

    public Profile getApprover() {
        return approver;
    }

    public void setApprover(Profile profile) {
        this.approver = profile;
    }

    public KycRequest approver(Profile profile) {
        this.approver = profile;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KycRequest)) {
            return false;
        }
        return id != null && id.equals(((KycRequest) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KycRequest{" +
            "id=" + getId() +
            ", currentLevel=" + getCurrentLevel() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
