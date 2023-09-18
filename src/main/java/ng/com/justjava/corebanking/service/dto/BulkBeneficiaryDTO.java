package ng.com.justjava.corebanking.service.dto;

public class BulkBeneficiaryDTO {
    String accountNumber;
    double amount;
    String bankCode;
    String beneficiaryFullName;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBeneficiaryFullName() {
        return beneficiaryFullName;
    }

    public void setBeneficiaryFullName(String beneficiaryFullName) {
        this.beneficiaryFullName = beneficiaryFullName;
    }

    @Override
    public String toString() {
        return "BulkBeneficiaryDTO{" +
            "\"accountNumber\":'" + accountNumber + '\'' +
            ", \"amount\":" + amount +
            ", \"bankCode\":'" + bankCode + '\'' +
            '}';
    }
}
