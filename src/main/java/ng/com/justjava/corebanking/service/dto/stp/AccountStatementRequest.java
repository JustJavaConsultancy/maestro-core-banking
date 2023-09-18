package ng.com.justjava.corebanking.service.dto.stp;

public class AccountStatementRequest {

    private String accountNumber;
    private String statementDate;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(String statementDate) {
        this.statementDate = statementDate;
    }

    @Override
    public String toString() {
        return "AccountStatementRequest{" +
            "accountNumber='" + accountNumber + '\'' +
            ", statementDate=" + statementDate +
            '}';
    }
}
