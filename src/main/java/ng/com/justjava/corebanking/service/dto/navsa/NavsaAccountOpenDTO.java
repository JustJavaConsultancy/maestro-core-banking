package ng.com.justjava.corebanking.service.dto.navsa;

public class NavsaAccountOpenDTO {
    private String accountNo;
    private String bankCode = "598";
    private String accountName;
    private String customerId;


    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "NavsaAccountOpenDTO{" +
            "accountNo='" + accountNo + '\'' +
            ", bankCode='" + bankCode + '\'' +
            ", accountName='" + accountName + '\'' +
            ", customerId='" + customerId + '\'' +
            '}';
    }
}
