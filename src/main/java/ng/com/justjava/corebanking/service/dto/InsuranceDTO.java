package ng.com.justjava.corebanking.service.dto;

public class InsuranceDTO {

    private String accountNumber;

    private Double amount;

    private Double charges;

    private String insuranceAccount;

    private Long lenderId;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getCharges() {
        return charges;
    }

    public void setCharges(Double charges) {
        this.charges = charges;
    }

    public String getInsuranceAccount() {
        return insuranceAccount;
    }

    public void setInsuranceAccount(String insuranceAccount) {
        this.insuranceAccount = insuranceAccount;
    }

    public Long getLenderId() {
        return lenderId;
    }

    public void setLenderId(Long lenderId) {
        this.lenderId = lenderId;
    }

    @Override
    public String toString() {
        return "InsuranceDTO{" +
            "accountNumber='" + accountNumber + '\'' +
            ", amount=" + amount +
            ", charges='" + charges + '\'' +
            ", insuranceAccount='" + insuranceAccount + '\'' +
            ", lenderId='" + lenderId + '\'' +
            '}';
    }
}
