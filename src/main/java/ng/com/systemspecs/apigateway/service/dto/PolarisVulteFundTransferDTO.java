package ng.com.systemspecs.apigateway.service.dto;

public class PolarisVulteFundTransferDTO {

    private String accountNumber;
    private String destinationAccountNumber;
    private String destinationBankCode;
    private String sourceAccountFirstName;
    private String sourceAccountLastName;
    private String phoneNumber;
    private String email;
    private Double amount;
    private String transactionDescription;
    private String customerRef;
    private String apiKey;
    private String secretKey;

    private String transRef;

    private String schemeId;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public String getDestinationBankCode() {
        return destinationBankCode;
    }

    public void setDestinationBankCode(String destinationBankCode) {
        this.destinationBankCode = destinationBankCode;
    }

    public String getSourceAccountFirstName() {
        return sourceAccountFirstName;
    }

    public void setSourceAccountFirstName(String sourceAccountFirstName) {
        this.sourceAccountFirstName = sourceAccountFirstName;
    }

    public String getSourceAccountLastName() {
        return sourceAccountLastName;
    }

    public void setSourceAccountLastName(String sourceAccountLastName) {
        this.sourceAccountLastName = sourceAccountLastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public String getCustomerRef() {
        return customerRef;
    }

    public void setCustomerRef(String customerRef) {
        this.customerRef = customerRef;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    @Override
    public String toString() {
        return "PolarisVulteFundTransferDTO{" +
            "accountNumber='" + accountNumber + '\'' +
            ", destinationAccountNumber='" + destinationAccountNumber + '\'' +
            ", destinationBankCode='" + destinationBankCode + '\'' +
            ", sourceAccountFirstName='" + sourceAccountFirstName + '\'' +
            ", sourceAccountLastName='" + sourceAccountLastName + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", email='" + email + '\'' +
            ", amount=" + amount +
            ", transactionDescription='" + transactionDescription + '\'' +
            ", customerRef='" + customerRef + '\'' +
            ", apiKey='" + apiKey + '\'' +
            ", secretKey='" + secretKey + '\'' +
            ", transRef='" + transRef + '\'' +
            ", schemeId='" + schemeId + '\'' +
            '}';
    }
}
