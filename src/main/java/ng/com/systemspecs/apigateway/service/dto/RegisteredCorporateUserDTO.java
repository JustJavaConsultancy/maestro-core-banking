package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.enumeration.CorporateProfileRegistrationType;

import java.util.List;

public class RegisteredCorporateUserDTO extends RegisteredUserDTO{
    public String businessName;
    public String businessAddress;
    public CorporateProfileRegistrationType regType;
    public String category;
    public String rcNO;
    public String businessPhoneNo;
    public String tin;
    public boolean cacCertificate = false;
    public boolean cacCo7 = false;
    public boolean cacCo2 = false;
    public boolean utilityBill = false;
    public List<CorporateDocuments> corporateDocuments;

    public List<CorporateDocuments> getCorporateDocuments() {
        return corporateDocuments;
    }

    public void setCorporateDocuments(List<CorporateDocuments> corporateDocuments) {
        this.corporateDocuments = corporateDocuments;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public CorporateProfileRegistrationType getRegType() {
        return regType;
    }

    public void setRegType(CorporateProfileRegistrationType regType) {
        this.regType = regType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRcNO() {
        return rcNO;
    }

    public void setRcNO(String rcNO) {
        this.rcNO = rcNO;
    }

    public String getBusinessPhoneNo() {
        return businessPhoneNo;
    }

    public void setBusinessPhoneNo(String businessPhoneNo) {
        this.businessPhoneNo = businessPhoneNo;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public boolean isCacCertificate() {
        return cacCertificate;
    }

    public void setCacCertificate(boolean cacCertificate) {
        this.cacCertificate = cacCertificate;
    }

    public boolean isCacCo7() {
        return cacCo7;
    }

    public void setCacCo7(boolean cacCo7) {
        this.cacCo7 = cacCo7;
    }

    public boolean isCacCo2() {
        return cacCo2;
    }

    public void setCacCo2(boolean cacCo2) {
        this.cacCo2 = cacCo2;
    }

    public boolean isUtilityBill() {
        return utilityBill;
    }

    public void setUtilityBill(boolean utilityBill) {
        this.utilityBill = utilityBill;
    }

    @Override
    public String toString() {
        return "RegisteredCorporateUserDTO{" +
            "businessName='" + businessName + '\'' +
            ", businessAddress='" + businessAddress + '\'' +
            ", regType='" + regType + '\'' +
            ", category='" + category + '\'' +
            ", rcNO='" + rcNO + '\'' +
            ", businessPhoneNo='" + businessPhoneNo + '\'' +
            ", tin='" + tin + '\'' +
            ", cacCertificate=" + cacCertificate +
            ", cacCo7=" + cacCo7 +
            ", cacCo2=" + cacCo2 +
            ", utilityBill=" + utilityBill +
            "} " + super.toString();
    }
}
