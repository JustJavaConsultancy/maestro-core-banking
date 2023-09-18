package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ng.com.justjava.corebanking.domain.ContactUs;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.enumeration.ComplaintCategory;
import ng.com.justjava.corebanking.domain.enumeration.ContactCategory;
import ng.com.justjava.corebanking.domain.enumeration.ContactStatus;
import ng.com.justjava.corebanking.domain.enumeration.EnquiryCategory;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * A DTO for the {@link ContactUs} entity.
 */
public class ContactUsDTO implements Serializable {

    private Long id;

    private ContactCategory contactCategory;

    private ComplaintCategory complaintCategory;

    private EnquiryCategory enquiryCategory;

    private ContactStatus status;

    @NotEmpty(message = "Sender cannot be empty")
    private String senderPhoneNumber;

    @JsonIgnore
    private Profile sender;

    private Long senderId;

    @JsonIgnore
    private Profile assignedTo;

    private Long assignedToId;

    private String assignedToPhoneNumber;

    private String supportingDoc;

    private String docType;

    private String message;


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

    public ComplaintCategory getComplaintCategory() {
        return complaintCategory;
    }

    public void setComplaintCategory(ComplaintCategory complaintCategory) {
        this.complaintCategory = complaintCategory;
    }

    public EnquiryCategory getEnquiryCategory() {
        return enquiryCategory;
    }

    public void setEnquiryCategory(EnquiryCategory enquiryCategory) {
        this.enquiryCategory = enquiryCategory;
    }

    public ContactStatus getStatus() {
        return status;
    }

    public void setStatus(ContactStatus status) {
        this.status = status;
    }

    public String getSenderPhoneNumber() {
        if (StringUtils.isEmpty(senderPhoneNumber) && sender != null) {
            return sender.getPhoneNumber();
        }
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public String getAssignedToPhoneNumber() {
        if (StringUtils.isEmpty(assignedToPhoneNumber) && assignedTo != null) {
            return assignedTo.getPhoneNumber();
        }

        return assignedToPhoneNumber;
    }

    public void setAssignedToPhoneNumber(String assignedToPhoneNumber) {
        this.assignedToPhoneNumber = assignedToPhoneNumber;
    }

    public String getSupportingDoc() {
        return supportingDoc;
    }

    public void setSupportingDoc(String supportingDoc) {
        this.supportingDoc = supportingDoc;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(Long assignedToId) {
        this.assignedToId = assignedToId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Profile getSender() {
        return sender;
    }

    public void setSender(Profile sender) {
        this.sender = sender;
    }

    public Profile getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Profile assignedTo) {
        this.assignedTo = assignedTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactUsDTO)) {
            return false;
        }

        return id != null && id.equals(((ContactUsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactUsDTO{" +
            "id=" + getId() +
            ", contactCategory='" + getContactCategory() + "'" +
            ", complaintCategory='" + getComplaintCategory() + "'" +
            ", enquiryCategory='" + getEnquiryCategory() + "'" +
            ", status='" + getStatus() + "'" +
            ", senderPhoneNumber=" + getSenderPhoneNumber() +
            ", assignedToPhoneNumber=" + getAssignedToPhoneNumber() +
            ", supportingDoc=" + getSupportingDoc() +
            ", docType=" + getDocType() +
            "}";
    }
}
