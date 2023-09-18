package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebHookDetails {
    @JsonProperty("amount")
    private double amountInKobo;

    @JsonProperty("transaction_type")
    private String transactionType;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("transaction_ref")
    private String transactionRef;

    @JsonProperty("customer_ref")
    private String customerRef;

    @JsonProperty("customer_firstname")
    private String customerFirstName;

    @JsonProperty("customer_surname")
    private String customerSurnameName;

    @JsonProperty("customer_email")
    private String customerEmail;

    @JsonProperty("customer_mobile_no")
    private String customerMobileNo;

    public double getAmountInKobo() {
        return amountInKobo;
    }

    public void setAmountInKobo(double amountInKobo) {
        this.amountInKobo = amountInKobo;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getCustomerRef() {
        return customerRef;
    }

    public void setCustomerRef(String customerRef) {
        this.customerRef = customerRef;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerSurnameName() {
        return customerSurnameName;
    }

    public void setCustomerSurnameName(String customerSurnameName) {
        this.customerSurnameName = customerSurnameName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    public void setCustomerMobileNo(String customerMobileNo) {
        this.customerMobileNo = customerMobileNo;
    }

    @Override
    public String toString() {
        return "WebHookDetails{" +
            "amountInKobo=" + amountInKobo +
            ", transactionType='" + transactionType + '\'' +
            ", provider='" + provider + '\'' +
            ", transactionRef='" + transactionRef + '\'' +
            ", customerRef='" + customerRef + '\'' +
            ", customerFirstName='" + customerFirstName + '\'' +
            ", customerSurnameName='" + customerSurnameName + '\'' +
            ", customerEmail='" + customerEmail + '\'' +
            ", customerMobileNo='" + customerMobileNo + '\'' +
            '}';
    }
}
