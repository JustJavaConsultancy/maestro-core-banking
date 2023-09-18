package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.math.BigInteger;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "account_number",
    "closing_balance",
    "opening_balance",
    "statement_list"
})
@Generated("jsonschema2pojo")
public class PolarisGetStatementsResponse {

    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("closing_balance")
    private BigInteger closingBalance;
    @JsonProperty("opening_balance")
    private BigInteger openingBalance;
    @JsonProperty("statement_list")
    private List<PolarisStatement> statementList = null;

    @JsonProperty("account_number")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("account_number")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonProperty("closing_balance")
    public BigInteger getClosingBalance() {
        return closingBalance;
    }

    @JsonProperty("closing_balance")
    public void setClosingBalance(BigInteger closingBalance) {
        this.closingBalance = closingBalance;
    }

    @JsonProperty("opening_balance")
    public BigInteger getOpeningBalance() {
        return openingBalance;
    }

    @JsonProperty("opening_balance")
    public void setOpeningBalance(BigInteger openingBalance) {
        this.openingBalance = openingBalance;
    }

    @JsonProperty("statement_list")
    public List<PolarisStatement> getStatementList() {
        return statementList;
    }

    @JsonProperty("statement_list")
    public void setStatementList(List<PolarisStatement> statementList) {
        this.statementList = statementList;
    }

    @Override
    public String toString() {
        return "PolarisGetStatementsResponse{" +
            "accountNumber='" + accountNumber + '\'' +
            ", closingBalance=" + closingBalance +
            ", openingBalance=" + openingBalance +
            ", statementList=" + statementList +
            '}';
    }
}
