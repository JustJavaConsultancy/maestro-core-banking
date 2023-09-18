package ng.com.systemspecs.apigateway.service.dto;

import java.time.LocalDate;

import ng.com.systemspecs.apigateway.domain.enumeration.KycRequestDocType;

public class UpgradeKYCLevel3DTO {
    private String docType;
    private String docFormat;
    private String docFile;
    private String documentNumber;
    private LocalDate dateIssued;

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

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDate getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(LocalDate dateIssued) {
        this.dateIssued = dateIssued;
    }

    @Override
    public String toString() {
        return "UpgradeKYCLevel3DTO{" +
            "docType='" + docType + '\'' +
            ", docFormat='" + docFormat + '\'' +
            ", docFile='" + docFile + '\'' +
            '}';
    }
}
