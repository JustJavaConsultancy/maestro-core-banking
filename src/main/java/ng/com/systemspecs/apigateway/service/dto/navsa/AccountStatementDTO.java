package ng.com.systemspecs.apigateway.service.dto.navsa;

import java.util.List;


public class AccountStatementDTO {

    List<AccountStatement> transactions;

    public List<AccountStatement> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<AccountStatement> transactions) {
        this.transactions = transactions;
    }
}
