package ng.com.systemspecs.apigateway.service.dto.navsa;

public class NavsaBalanceResponseDTO {

    private String bankCode = "598";
    private Double totalInflowAmt;
    private Double totalOutflowAmt;
    private String ledgerBalance;
    private String accountName;
    private Double netBalance;
    private String currency;
    private String accountNumber;
    private Double availableBalance;


    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Double getTotalInflowAmt() {
        return totalInflowAmt;
    }

    public void setTotalInflowAmt(Double totalInflowAmt) {
        this.totalInflowAmt = totalInflowAmt;
    }

    public Double getTotalOutflowAmt() {
        return totalOutflowAmt;
    }

    public void setTotalOutflowAmt(Double totalOutflowAmt) {
        this.totalOutflowAmt = totalOutflowAmt;
    }

    public String getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(String ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Double getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(Double netBalance) {
        this.netBalance = netBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    @Override
    public String toString() {
        return "NavsaBalanceResponseDTO{" +
            "bankCode='" + bankCode + '\'' +
            ", totalInflowAmt=" + totalInflowAmt +
            ", totalOutflowAmt=" + totalOutflowAmt +
            ", ledgerBalance='" + ledgerBalance + '\'' +
            ", accountName='" + accountName + '\'' +
            ", netBalance=" + netBalance +
            ", currency='" + currency + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", availableBalance=" + availableBalance +
            '}';
    }
}
