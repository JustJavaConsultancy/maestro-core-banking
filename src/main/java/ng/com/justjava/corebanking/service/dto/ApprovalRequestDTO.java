package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ng.com.justjava.corebanking.domain.ApprovalGroup;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;

public class ApprovalRequestDTO {

    @JsonIgnore
    private Long id;

    private String requestId;

    @NotEmpty(message = "RequestRef cannot be empty")
    private String requestRef;

    @NotEmpty(message = "RequestTypeCode cannot be empty")
    private String requestTypeCode;

    private String status;

    private String comment;

    private String supportingDoc;

    private boolean completed;

    private String pointOfFailure;

    @JsonIgnore
    private ApprovalGroup initiator;

    @JsonIgnore
    private ApprovalGroup approver;

    private Instant dateCreated;

    private Instant dateLastModified;

    private String docType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestRef() {
        return requestRef;
    }

    public void setRequestRef(String requestRef) {
        this.requestRef = requestRef;
    }

    public String getRequestTypeCode() {
        return requestTypeCode;
    }

    public void setRequestTypeCode(String requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSupportingDoc() {
        return supportingDoc;
    }

    public void setSupportingDoc(String supportingDoc) {
        this.supportingDoc = supportingDoc;
    }

    public ApprovalGroup getApprover() {
        return approver;
    }

    public void setApprover(ApprovalGroup approver) {
        this.approver = approver;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getPointOfFailure() {
        return pointOfFailure;
    }

    public void setPointOfFailure(String pointOfFailure) {
        this.pointOfFailure = pointOfFailure;
    }

    public ApprovalGroup getInitiator() {
        return initiator;
    }

    public void setInitiator(ApprovalGroup initiator) {
        this.initiator = initiator;
    }

    public String getInitiatorName() {
        if (initiator != null) {
            return initiator.getName();
        }
        return null;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(Instant dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    @Override
    public String toString() {
        return "ApprovalRequestDTO{" +
            "id=" + id +
            ", requestId='" + requestId + '\'' +
            ", requestRef='" + requestRef + '\'' +
            ", requestTypeCode='" + requestTypeCode + '\'' +
            ", status=" + status +
            ", comment='" + comment + '\'' +
            ", supportingDoc='" + supportingDoc + '\'' +
            ", completed=" + completed +
            ", pointOfFailure='" + pointOfFailure + '\'' +
            ", approver=" + approver +
            ", initiator=" + initiator +
            ", dateCreated=" + dateCreated +
            ", dateLastModified=" + dateLastModified +
            ", docType=" + getDocType() +
            '}';
    }
}
