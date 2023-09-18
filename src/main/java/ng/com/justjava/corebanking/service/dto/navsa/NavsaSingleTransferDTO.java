package ng.com.justjava.corebanking.service.dto.navsa;

import javax.validation.constraints.NotEmpty;

public class NavsaSingleTransferDTO {

    private String toBank;
    @NotEmpty
    private String creditAccount;
    @NotEmpty
    private String narration;
    @NotEmpty
    private String amount;
    @NotEmpty
    private String transRef;
    private String fromBank;
    @NotEmpty
    private String debitAccount;
    private String beneficiaryEmail;
    private String requestId;


    public String getToBank() {
        return toBank;
    }

    public void setToBank(String toBank) {
        this.toBank = toBank;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public String getFromBank() {
        return fromBank;
    }

    public void setFromBank(String fromBank) {
        this.fromBank = fromBank;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getBeneficiaryEmail() {
        return beneficiaryEmail;
    }

    public void setBeneficiaryEmail(String beneficiaryEmail) {
        this.beneficiaryEmail = beneficiaryEmail;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "NavsaSingleTransferDTO{" +
            "toBank='" + toBank + '\'' +
            ", creditAccount='" + creditAccount + '\'' +
            ", narration='" + narration + '\'' +
            ", amount='" + amount + '\'' +
            ", transRef='" + transRef + '\'' +
            ", fromBank='" + fromBank + '\'' +
            ", debitAccount='" + debitAccount + '\'' +
            ", beneficiaryEmail='" + beneficiaryEmail + '\'' +
            ", requestId='" + requestId + '\'' +
            '}';
    }
}
