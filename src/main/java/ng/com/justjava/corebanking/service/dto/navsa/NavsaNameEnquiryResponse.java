package ng.com.justjava.corebanking.service.dto.navsa;

public class NavsaNameEnquiryResponse {

    private String accountNo;
    private String bankCode = "598";
    private String accountName;


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

    @Override
    public String toString() {
        return "NavsaNameEnquiryResponse{" +
            "accountNo='" + accountNo + '\'' +
            ", bankCode='" + bankCode + '\'' +
            ", accountName='" + accountName + '\'' +
            '}';
    }
}
