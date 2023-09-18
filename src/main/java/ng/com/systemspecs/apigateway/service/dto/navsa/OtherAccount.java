package ng.com.systemspecs.apigateway.service.dto.navsa;

public class OtherAccount {

    private String requestId;
    private String accountName;
    private String accountTypeId;
    private boolean isRestrictedWallet;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(String accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public boolean getIsRestrictedWallet() {
        return isRestrictedWallet;
    }

    public void setIsRestrictedWallet(boolean restrictedWallet) {
        isRestrictedWallet = restrictedWallet;
    }

    @Override
    public String toString() {
        return "OtherAccount{" +
            "requestId='" + requestId + '\'' +
            ", accountName='" + accountName + '\'' +
            ", accountTypeId='" + accountTypeId + '\'' +
            ", isRestrictedWallet=" + isRestrictedWallet +
            '}';
    }
}
