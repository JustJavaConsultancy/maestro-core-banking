package ng.com.systemspecs.apigateway.service.dto;

import java.time.LocalDate;

public class UpgradeKYCLevelHMDTO {

    private String bvn;
    private String docType; //Type of Document
    private String docFormat; //format of doc Jpg/pdf
    private String docFile;  //encoded String
    private String verificationId; //documentNumber
    private String employmentDetails;
    private LocalDate expiryDate;
    private String phoneNumber;
    private String dateOfBirth;

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public String getDocFile() {
        return docFile;
    }

    public void setDocFile(String docFile) {
        this.docFile = docFile;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public String getEmploymentDetails() {
        return employmentDetails;
    }

    public void setEmploymentDetails(String employmentDetails) {
        this.employmentDetails = employmentDetails;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getBvn() {
        return bvn;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "UpgradeKYCLevelHMDTO{" +
            "bvn='" + bvn + '\'' +
            ", docType='" + docType + '\'' +
            ", docFormat='" + docFormat + '\'' +
            ", docFile='" + docFile + '\'' +
            ", verificationId='" + verificationId + '\'' +
            ", employmentDetails='" + employmentDetails + '\'' +
            ", expiryDate=" + expiryDate +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", dateOfBirth='" + dateOfBirth + '\'' +
            '}';
    }

}
