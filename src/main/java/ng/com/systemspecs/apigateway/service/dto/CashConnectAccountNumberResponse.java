
package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "AccessLevel",
    "AccountNumber",
    "AccountStatus",
    "AccountType",
    "AvailableBalance",
    "WithdrawableBalance",
    "Branch",
    "CustomerID",
    "CustomerName",
    "DateCreated",
    "LastActivityDate",
    "NUBAN",
    "Refree1CustomerID",
    "Refree2CustomerID",
    "ReferenceNo",
    "PNDStatus",
    "AccountTier"
})
@Generated("jsonschema2pojo")
public class CashConnectAccountNumberResponse {

    @JsonProperty("AccessLevel")
    private String accessLevel;
    @JsonProperty("AccountNumber")
    private String accountNumber;
    @JsonProperty("AccountStatus")
    private String accountStatus;
    @JsonProperty("AccountType")
    private String accountType;
    @JsonProperty("AvailableBalance")
    private String availableBalance;
    @JsonProperty("WithdrawableBalance")
    private String withdrawableBalance;
    @JsonProperty("Branch")
    private Object branch;
    @JsonProperty("CustomerID")
    private String customerID;
    @JsonProperty("CustomerName")
    private String customerName;
    @JsonProperty("DateCreated")
    private String dateCreated;
    @JsonProperty("LastActivityDate")
    private Object lastActivityDate;
    @JsonProperty("NUBAN")
    private String nuban;
    @JsonProperty("Refree1CustomerID")
    private Object refree1CustomerID;
    @JsonProperty("Refree2CustomerID")
    private Object refree2CustomerID;
    @JsonProperty("ReferenceNo")
    private Object referenceNo;
    @JsonProperty("PNDStatus")
    private Boolean pNDStatus;
    @JsonProperty("AccountTier")
    private String accountTier;

    @JsonProperty("AccessLevel")
    public String getAccessLevel() {
        return accessLevel;
    }

    @JsonProperty("AccessLevel")
    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    @JsonProperty("AccountNumber")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("AccountNumber")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonProperty("AccountStatus")
    public String getAccountStatus() {
        return accountStatus;
    }

    @JsonProperty("AccountStatus")
    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    @JsonProperty("AccountType")
    public String getAccountType() {
        return accountType;
    }

    @JsonProperty("AccountType")
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @JsonProperty("AvailableBalance")
    public String getAvailableBalance() {
        return availableBalance;
    }

    @JsonProperty("AvailableBalance")
    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    @JsonProperty("WithdrawableBalance")
    public String getWithdrawableBalance() {
        return withdrawableBalance;
    }

    @JsonProperty("WithdrawableBalance")
    public void setWithdrawableBalance(String withdrawableBalance) {
        this.withdrawableBalance = withdrawableBalance;
    }

    @JsonProperty("Branch")
    public Object getBranch() {
        return branch;
    }

    @JsonProperty("Branch")
    public void setBranch(Object branch) {
        this.branch = branch;
    }

    @JsonProperty("CustomerID")
    public String getCustomerID() {
        return customerID;
    }

    @JsonProperty("CustomerID")
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    @JsonProperty("CustomerName")
    public String getCustomerName() {
        return customerName;
    }

    @JsonProperty("CustomerName")
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @JsonProperty("DateCreated")
    public String getDateCreated() {
        return dateCreated;
    }

    @JsonProperty("DateCreated")
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @JsonProperty("LastActivityDate")
    public Object getLastActivityDate() {
        return lastActivityDate;
    }

    @JsonProperty("LastActivityDate")
    public void setLastActivityDate(Object lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    @JsonProperty("NUBAN")
    public String getNuban() {
        return nuban;
    }

    @JsonProperty("NUBAN")
    public void setNuban(String nuban) {
        this.nuban = nuban;
    }

    @JsonProperty("Refree1CustomerID")
    public Object getRefree1CustomerID() {
        return refree1CustomerID;
    }

    @JsonProperty("Refree1CustomerID")
    public void setRefree1CustomerID(Object refree1CustomerID) {
        this.refree1CustomerID = refree1CustomerID;
    }

    @JsonProperty("Refree2CustomerID")
    public Object getRefree2CustomerID() {
        return refree2CustomerID;
    }

    @JsonProperty("Refree2CustomerID")
    public void setRefree2CustomerID(Object refree2CustomerID) {
        this.refree2CustomerID = refree2CustomerID;
    }

    @JsonProperty("ReferenceNo")
    public Object getReferenceNo() {
        return referenceNo;
    }

    @JsonProperty("ReferenceNo")
    public void setReferenceNo(Object referenceNo) {
        this.referenceNo = referenceNo;
    }

    @JsonProperty("PNDStatus")
    public Boolean getPNDStatus() {
        return pNDStatus;
    }

    @JsonProperty("PNDStatus")
    public void setPNDStatus(Boolean pNDStatus) {
        this.pNDStatus = pNDStatus;
    }

    @JsonProperty("AccountTier")
    public String getAccountTier() {
        return accountTier;
    }

    @JsonProperty("AccountTier")
    public void setAccountTier(String accountTier) {
        this.accountTier = accountTier;
    }

    @Override
    public String toString() {
        return "CashConnectAccountNumberResponse{" +
            "accessLevel='" + accessLevel + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", accountStatus='" + accountStatus + '\'' +
            ", accountType='" + accountType + '\'' +
            ", availableBalance='" + availableBalance + '\'' +
            ", withdrawableBalance='" + withdrawableBalance + '\'' +
            ", branch=" + branch +
            ", customerID='" + customerID + '\'' +
            ", customerName='" + customerName + '\'' +
            ", dateCreated='" + dateCreated + '\'' +
            ", lastActivityDate=" + lastActivityDate +
            ", nuban='" + nuban + '\'' +
            ", refree1CustomerID=" + refree1CustomerID +
            ", refree2CustomerID=" + refree2CustomerID +
            ", referenceNo=" + referenceNo +
            ", pNDStatus=" + pNDStatus +
            ", accountTier='" + accountTier + '\'' +
            '}';
    }
}
