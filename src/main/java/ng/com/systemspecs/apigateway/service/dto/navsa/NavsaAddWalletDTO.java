package ng.com.systemspecs.apigateway.service.dto.navsa;

public class NavsaAddWalletDTO {

    private String requestId;
    private String customerId;
    private String accountName;
    private String accountTypeId;
    private boolean isRestrictedWallet;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
        return "NavsaAddWalletDTO{" +
            "requestId='" + requestId + '\'' +
            ", customerId='" + customerId + '\'' +
            ", accountName='" + accountName + '\'' +
            ", accountTypeId='" + accountTypeId + '\'' +
            ", isRestrictedWallet=" + isRestrictedWallet +
            '}';
    }
}
