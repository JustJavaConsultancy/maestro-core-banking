package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.domain.enumeration.CorporateProfileRegistrationType;

import java.io.Serializable;

public class CorporateProfileDto implements Serializable {

    public Long id;
    public String name;
    public String address;
    public CorporateProfileRegistrationType regType;
    public String category;
    public String rcNO;
    public String phoneNo;
    public String tin;
    public boolean cacCertificate;
    public boolean cacCo7;
    public boolean cacCo2;
    public boolean utilityBill;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        return "CorporateProfileDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", address='" + address + '\'' +
            ", regType=" + regType +
            ", category='" + category + '\'' +
            ", rcNO='" + rcNO + '\'' +
            ", phoneNo='" + phoneNo + '\'' +
            ", tin='" + tin + '\'' +
            ", cacCertificate=" + cacCertificate +
            ", cacCo7=" + cacCo7 +
            ", cacCo2=" + cacCo2 +
            ", utilityBill=" + utilityBill +
            ", profile=" + profile +
            '}';
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

    ProfileDTO profile;

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

}
