package ng.com.systemspecs.apigateway.service.dto;

public class TransactionCallBackDTO {

    private String reference;
    private String source;
    private String destination;
    private String status;
    private String narration;
    private String externalRef;
    private String transType;

    private String transCategory;
    private String phoneNumber;
    private String sourceAccountName;
    private String destinationAccountName;
    private double amount;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getSourceAccountName() {
        return sourceAccountName;
    }

    public void setSourceAccountName(String sourceAccountName) {
        this.sourceAccountName = sourceAccountName;
    }

    public String getDestinationAccountName() {
        return destinationAccountName;
    }

    public void setDestinationAccountName(String destinationAccountName) {
        this.destinationAccountName = destinationAccountName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransCategory() {
        return transCategory;
    }

    public void setSpecificTransType(String transCategory) {
        this.transCategory = transCategory;
    }

    public void setTransCategory(String transCategory) {
        this.transCategory = transCategory;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "TransactionCallBackDTO{" +
            "reference='" + reference + '\'' +
            ", source='" + source + '\'' +
            ", destination='" + destination + '\'' +
            ", status='" + status + '\'' +
            ", narration='" + narration + '\'' +
            ", externalRef='" + externalRef + '\'' +
            ", transType='" + transType + '\'' +
            ", sourceAccountName='" + sourceAccountName + '\'' +
            ", destinationAccountName='" + destinationAccountName + '\'' +
            ", amount='" + amount + '\'' +
            ", transCategory='" + transCategory + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            '}';
    }

}
