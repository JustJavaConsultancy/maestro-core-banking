package ng.com.justjava.corebanking.service.dto;

public class BeneficiaryDTO {

    private Long id;

    private String name;

    private String accountNumber;

    private String bankCode;

    private String bankName;

    private boolean isWallet;

    private Long accountOwnerId;

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public boolean isWallet() {
        return isWallet;
    }

    public void setWallet(boolean wallet) {
        isWallet = wallet;
    }

    public Long getAccountOwnerId() {
        return accountOwnerId;
    }

    public void setAccountOwnerId(Long accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
    }

    @Override
    public String toString() {
        return "BeneficiaryDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", bankCode='" + bankCode + '\'' +
            ", bankNam='" + bankName + '\'' +
            ", isWallet=" + isWallet +
            ", addressOwner=" + accountOwnerId +
            '}';
    }
}
