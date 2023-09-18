package ng.com.systemspecs.apigateway.service.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "account_currency",
})
public class PolarisVulteMeta {

    @JsonProperty("account_currency")
    private String accountCurrency;

    @JsonProperty("account_currency")
    public String getAccountCurrency() {
        return accountCurrency;
    }

    @JsonProperty("account_currency")
    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }

    @Override
    public String toString() {
        return "PolarisVulteMeta{" +
            "accountCurrency='" + accountCurrency + '\'' +
            '}';
    }
}
