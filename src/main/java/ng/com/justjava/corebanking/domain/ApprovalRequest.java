package ng.com.justjava.corebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApprovalRequest extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "request_ref")
    private String requestRef;

    @Column(name = "request_type_code")
    private String requestTypeCode;

    @Column(name = "status")
    private String status;

    @Column(name = "comment")
    private String comment;

    @Column(name = "document")
    private String supportingDoc;

    @Column(name = "completed")
    private boolean completed;

    @Column(name = "point_of_failure")
    private String pointOfFailure;

    @ManyToOne
    @JsonIgnoreProperties(value = "requests", allowSetters = true)
    @JsonIgnore
    private ApprovalGroup approver;

    @JsonIgnore
    @ManyToOne
    private ApprovalGroup initiator;

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

    public Instant getDateCreated() {
        return super.getCreatedDate();
    }

    public Instant getDateLastModified() {
        return super.getLastModifiedDate();
    }

    @Override
    public String toString() {
        return "ApprovalRequest{" +
            "id=" + id +
            ", requestId='" + requestId + '\'' +
            ", requestRef='" + requestRef + '\'' +
            ", requestTypeCode='" + requestTypeCode + '\'' +
            ", status=" + status +
            ", comment='" + comment + '\'' +
            ", supportingDoc='" + supportingDoc + '\'' +
            ", completed=" + completed +
            ", pointOfFailure='" + pointOfFailure + '\'' +
            ", profile=" + approver +
            ", initiator=" + initiator +
            '}';
    }
}

// operation (InsuranceOperation)
// policyNo (String)
// certificateNo (String)
// occupation (String)
// sector (String)
// idType (String)
// idNumber (String) required
// vehicleType (String) required
// registrationNo (String) required
// vehMake (String) required
// vehModel (String) required
// vehYear (String) required
// registrationStart (String) required
// expiryDate (String) required
// mileage (String)
// vehColor (String) required
// chassisNo (String) required
// engineNo (String) required
// engineCapacity (String) required
// seatCapacity (String) required
// balance (Double) required
