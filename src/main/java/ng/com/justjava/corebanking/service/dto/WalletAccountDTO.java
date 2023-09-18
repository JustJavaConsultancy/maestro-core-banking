package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.AccountStatus;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link WalletAccount} entity.
 */
public class WalletAccountDTO implements Serializable {

    private Long id;

    private String accountNumber;

    private Double currentBalance;

    private LocalDate dateOpened;

    private Long schemeId;

    private String schemeName;

    private Long walletAccountTypeId;

    private Long accountOwnerId;

    private String accountOwnerName;

    private String accountOwnerPhoneNumber;

    private String accountName;

    private AccountStatus status;

    private Double actualBalance;

    private String walletLimit;

    private String trackingRef;

    private String nubanAccountNo;

    private String accountFullName;

    private double totalCustomerBalances;

    public double getTotalCustomerBalances() {
        return totalCustomerBalances;
    }

    public void setTotalCustomerBalances(double totalBalance) {
        this.totalCustomerBalances = totalBalance;
    }

    public String getAccountFullName() {
        return accountFullName;
    }

    public void setAccountFullName(String accountFullName) {
        this.accountFullName = accountFullName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getCurrentBalance() {
        return Double.valueOf(formatMoney(currentBalance));
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public LocalDate getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(LocalDate dateOpened) {
        this.dateOpened = dateOpened;
    }

    public Long getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(Long schemeId) {
        this.schemeId = schemeId;
    }

    public Long getWalletAccountTypeId() {
        return walletAccountTypeId;
    }

    public void setWalletAccountTypeId(Long walletAccountTypeId) {
        this.walletAccountTypeId = walletAccountTypeId;
    }

    public Long getAccountOwnerId() {
        return accountOwnerId;
    }

    public void setAccountOwnerId(Long profileId) {
        this.accountOwnerId = profileId;
    }

    public String getAccountOwnerPhoneNumber() {
        return accountOwnerPhoneNumber;
    }

    public void setAccountOwnerPhoneNumber(String profilePhoneNumber) {
        this.accountOwnerPhoneNumber = profilePhoneNumber;
    }

    public String getAccountOwnerName() {
        return accountOwnerName;
    }

    public void setAccountOwnerName(String accountOwnerName) {
        this.accountOwnerName = accountOwnerName;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Double getActualBalance() {
        return Double.valueOf(formatMoney(actualBalance));
    }

    public void setActualBalance(Double actualBalance) {
        this.actualBalance = actualBalance;
    }

    public String getWalletLimit() {
        return walletLimit;
    }

    public void setWalletLimit(String walletLimit) {
        this.walletLimit = walletLimit;
    }

    public String getNubanAccountNo() {
        return nubanAccountNo;
    }

    public void setNubanAccountNo(String nubanAccountNo) {
        this.nubanAccountNo = nubanAccountNo;
    }

    public String getTrackingRef() {
        return trackingRef;
    }

    public void setTrackingRef(String trackingRef) {
        this.trackingRef = trackingRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WalletAccountDTO)) {
            return false;
        }

        return id != null && id.equals(((WalletAccountDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore


    @Override
    public String toString() {
        return "WalletAccountDTO{" +
            "id=" + id +
            ", accountNumber=" + accountNumber +
            ", currentBalance=" + currentBalance +
            ", dateOpened=" + dateOpened +
            ", schemeId=" + schemeId +
            ", schemeName='" + schemeName + '\'' +
            ", walletAccountTypeId=" + walletAccountTypeId +
            ", accountOwnerId=" + accountOwnerId +
            ", accountOwnerName='" + accountOwnerName + '\'' +
            ", accountOwnerPhoneNumber='" + accountOwnerPhoneNumber + '\'' +
            ", accountName='" + accountName + '\'' +
            ", status='" + status + '\'' +
            ", actualBalance='" + actualBalance + '\'' +
            ", walletLimit='" + walletLimit + '\'' +
            ", nubanAccountNo='" + nubanAccountNo + '\'' +
            ", trackingRef='" + trackingRef + '\'' +
            ", accountFullName='" + accountFullName + '\'' +
            '}';
    }

    public static String formatMoney(Double amount) {
        if (amount == null) {
            return null;
        }

        return String.format("%.2f", amount);
    }
}
