package ng.com.justjava.corebanking.service.dto.navsa;

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
