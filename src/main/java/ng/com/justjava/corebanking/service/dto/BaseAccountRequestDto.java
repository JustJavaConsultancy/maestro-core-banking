package ng.com.justjava.corebanking.service.dto;

import javax.validation.constraints.NotNull;


public class BaseAccountRequestDto {


    private String customerId;

    @NotNull(message = "Account number not specified")
    private String accountNumber;

    private String bankCode;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
