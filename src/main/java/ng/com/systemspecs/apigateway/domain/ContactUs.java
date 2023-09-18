package ng.com.systemspecs.apigateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ng.com.systemspecs.apigateway.domain.enumeration.ComplaintCategory;
import ng.com.systemspecs.apigateway.domain.enumeration.ContactCategory;
import ng.com.systemspecs.apigateway.domain.enumeration.ContactStatus;
import ng.com.systemspecs.apigateway.domain.enumeration.EnquiryCategory;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A ContactUs.
 */
@Entity
@Table(name = "contact_us")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContactUs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_category")
    private ContactCategory contactCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "complaint_category")
    private ComplaintCategory complaintCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "enquiry_category")
    private EnquiryCategory enquiryCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ContactStatus status;

    @Column(name = "document")
    private String supportingDoc;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JsonIgnoreProperties(value = "contactuses", allowSetters = true)
    private Profile sender;

    @ManyToOne
    @JsonIgnoreProperties(value = "contactuses", allowSetters = true)
    private Profile assignedTo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContactCategory getContactCategory() {
        return contactCategory;
    }

    public void setContactCategory(ContactCategory contactCategory) {
        this.contactCategory = contactCategory;
    }

    public ContactUs contactCategory(ContactCategory contactCategory) {
        this.contactCategory = contactCategory;
        return this;
    }

    public ComplaintCategory getComplaintCategory() {
        return complaintCategory;
    }

    public void setComplaintCategory(ComplaintCategory complaintCategory) {
        this.complaintCategory = complaintCategory;
    }

    public ContactUs complaintCategory(ComplaintCategory complaintCategory) {
        this.complaintCategory = complaintCategory;
        return this;
    }

    public EnquiryCategory getEnquiryCategory() {
        return enquiryCategory;
    }

    public void setEnquiryCategory(EnquiryCategory enquiryCategory) {
        this.enquiryCategory = enquiryCategory;
    }

    public ContactUs enquiryCategory(EnquiryCategory enquiryCategory) {
        this.enquiryCategory = enquiryCategory;
        return this;
    }

    public ContactStatus getStatus() {
        return status;
    }

    public void setStatus(ContactStatus status) {
        this.status = status;
    }

    public ContactUs status(ContactStatus status) {
        this.status = status;
        return this;
    }

    public Profile getSender() {
        return sender;
    }

    public void setSender(Profile profile) {
        this.sender = profile;
    }

    public ContactUs sender(Profile profile) {
        this.sender = profile;
        return this;
    }

    public Profile getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Profile profile) {
        this.assignedTo = profile;
    }

    public ContactUs assignedTo(Profile profile) {
        this.assignedTo = profile;
        return this;
    }

    public String getSupportingDoc() {
        return supportingDoc;
    }

    public void setSupportingDoc(String supportingDoc) {
        this.supportingDoc = supportingDoc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactUs)) {
            return false;
        }
        return id != null && id.equals(((ContactUs) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "ContactUs{" +
            "id=" + id +
            ", contactCategory=" + contactCategory +
            ", complaintCategory=" + complaintCategory +
            ", enquiryCategory=" + enquiryCategory +
            ", status=" + status +
            ", supportingDoc='" + supportingDoc + '\'' +
            ", sender=" + sender +
            ", assignedTo=" + assignedTo +
            ", message=" + message +
            '}';
    }
}
