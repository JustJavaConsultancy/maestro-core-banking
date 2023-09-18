package ng.com.justjava.corebanking.service.dto;


public class BulkCorrespondentPaymentDTO {

    private String sourceAccountNumber;
    private String accountNumber;
    private double amount;
    private String narration;

    public BulkCorrespondentPaymentDTO() {
    }

    public BulkCorrespondentPaymentDTO(String sourceAccountNumber, String accountNumber, double amount, String narration) {
        this.sourceAccountNumber = sourceAccountNumber;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.narration = narration;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

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

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    @Override
    public String toString() {
        return "BulkCorrespondentPaymentDTO{" +
            "sourceAccountNumber='" + sourceAccountNumber + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", amount=" + amount +
            ", narration='" + narration + '\'' +
            '}';
    }
}
