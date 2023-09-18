package ng.com.systemspecs.apigateway.service.dto;

public class CgatePaymentNotificationRequestDTO {

    private String passBackReference;
    private String traceId;
    private String paymentReference;
    private String customerRef;
    private String responseCode;
    private String merchantId;
    private String mobileNumber;
    private Double amount;
    private String transactionDate;
    private String shortCode;
    private String currency;
    private String channel;
    private String hash;

    public String getPassBackReference() {
        return passBackReference;
    }

    public void setPassBackReference(String passBackReference) {
        this.passBackReference = passBackReference;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getCustomerRef() {
        return customerRef;
    }

    public void setCustomerRef(String customerRef) {
        this.customerRef = customerRef;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "CgatePaymentNotificationRequestDTO{" +
            "passBackReference='" + passBackReference + '\'' +
            ", traceId='" + traceId + '\'' +
            ", paymentReference='" + paymentReference + '\'' +
            ", customerRef='" + customerRef + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", merchantId='" + merchantId + '\'' +
            ", mobileNumber='" + mobileNumber + '\'' +
            ", amount=" + amount +
            ", transactionDate='" + transactionDate + '\'' +
            ", shortCode='" + shortCode + '\'' +
            ", currency='" + currency + '\'' +
            ", channel='" + channel + '\'' +
            ", hash='" + hash + '\'' +
            '}';
    }
}
