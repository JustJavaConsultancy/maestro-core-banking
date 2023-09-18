package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.enumeration.KycRequestDocType;
import ng.com.systemspecs.apigateway.domain.enumeration.KycRequestStatus;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link ng.com.systemspecs.apigateway.domain.KycRequest} entity.
 */
public class KycRequestDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer currentLevel;


    @NotNull
    private Integer nextLevel;


/*bvn
    currentLevel

    documentNo

    expiryDate
    documentUrl
    createdDate*/


    @NotNull
    private KycRequestStatus status;


    private Long profileId;

    private String profilePhoneNumber;

    private String profileFullName;

    private Long senderProfileId;

    private String senderProfilePhoneNumber;

    private String senderProfileFullName;

    private Long approverId;

    private String approverPhoneNumber;

    private String approverFullName;

    private String documentPath;

    private String lastModifiedDate;

    private String bvn;

    private String nin;

    
    private String documentId;
    
    @NotNull
    private KycRequestDocType documentType;
    
    private LocalDate dateissued;


	public LocalDate getDateissued() {
		return dateissued;
	}

	public void setDateissued(LocalDate dateissued) {
		this.dateissued = dateissued;
	}

	public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

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

    public KycRequestStatus getStatus() {
        return status;
    }

    public void setStatus(KycRequestStatus status) {
        this.status = status;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getProfilePhoneNumber() {
        return profilePhoneNumber;
    }

    public void setProfilePhoneNumber(String profilePhoneNumber) {
        this.profilePhoneNumber = profilePhoneNumber;
    }

    public Long getSenderProfileId() {
        return senderProfileId;
    }

    public void setSenderProfileId(Long profileId) {
        this.senderProfileId = profileId;
    }

    public String getSenderProfilePhoneNumber() {
        return senderProfilePhoneNumber;
    }

    public void setSenderProfilePhoneNumber(String profilePhoneNumber) {
        this.senderProfilePhoneNumber = profilePhoneNumber;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long profileId) {
        this.approverId = profileId;
    }

    public String getApproverPhoneNumber() {
        return approverPhoneNumber;
    }

    public void setApproverPhoneNumber(String profilePhoneNumber) {
        this.approverPhoneNumber = profilePhoneNumber;
    }

    public String getProfileFullName() {
        return profileFullName;
    }

    public void setProfileFullName(String profileFullName) {
        this.profileFullName = profileFullName;
    }

    public String getSenderProfileFullName() {
        return senderProfileFullName;
    }

    public void setSenderProfileFullName(String senderProfileFullName) {
        this.senderProfileFullName = senderProfileFullName;
    }

    public String getApproverFullName() {
        return approverFullName;
    }

    public void setApproverFullName(String approverFullName) {
        this.approverFullName = approverFullName;
    }

    public Integer getNextLevel() {

        return nextLevel;
    }

    public void setNextLevel(Integer nextLevel) {
        this.nextLevel = nextLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KycRequestDTO)) {
            return false;
        }

        return id != null && id.equals(((KycRequestDTO) o).id);
    }


    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public KycRequestDocType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(KycRequestDocType documentType) {
		this.documentType = documentType;
	}

	@Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KycRequestDTO{" +
            "id=" + getId() +
            ", currentLevel=" + getCurrentLevel() +
            ", status='" + getStatus() + "'" +
            ", profileId=" + getProfileId() +
            ", profilePhoneNumber='" + getProfilePhoneNumber() + "'" +
            ", senderProfileId=" + getSenderProfileId() +
            ", senderProfilePhoneNumber='" + getSenderProfilePhoneNumber() + "'" +
            ", approverId=" + getApproverId() +
            ", approverPhoneNumber='" + getApproverPhoneNumber() + "'" +
            ", profileFullName=" + getProfileFullName() + "'" +
            ", senderProfileFullName=" + getSenderProfileFullName() + "'" +
            ", approverFullName=" + getApproverFullName() + "'" +
            ", documentPath='" + documentPath + '\'' +
            ", lastModifiedDate='" + lastModifiedDate + '\'' +
            ", bvn='" + bvn + '\'' +
            ", nin='" + nin + '\'' +
            ", documentId=" + documentId +
            ", documentType" + documentType +
            '}';
    }
}
