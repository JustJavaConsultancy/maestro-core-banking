package ng.com.systemspecs.apigateway.service.dto;

public class PolarisCardReqDTO {
    String accountToDebit;
    String accountToLinked;
    String branchCode;
    String cardName;
    String cardSchemeId;
    String deliveryOption;

    public String getAccountToDebit() {
        return accountToDebit;
    }

    public void setAccountToDebit(String accountToDebit) {
        this.accountToDebit = accountToDebit;
    }

    public String getAccountToLinked() {
        return accountToLinked;
    }

    public void setAccountToLinked(String accountToLinked) {
        this.accountToLinked = accountToLinked;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardSchemeId() {
        return cardSchemeId;
    }

    public void setCardSchemeId(String cardSchemeId) {
        this.cardSchemeId = cardSchemeId;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    @Override
    public String toString() {
        return "PolarisCardReqDTO{" +
            "accountToDebit='" + accountToDebit + '\'' +
            ", accountToLinked='" + accountToLinked + '\'' +
            ", branchCode='" + branchCode + '\'' +
            ", cardName='" + cardName + '\'' +
            ", cardSchemeId='" + cardSchemeId + '\'' +
            ", deliveryOption='" + deliveryOption + '\'' +
            '}';
    }
}
