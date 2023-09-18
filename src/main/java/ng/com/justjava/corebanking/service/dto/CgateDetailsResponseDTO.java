package ng.com.justjava.corebanking.service.dto;

public class CgateDetailsResponseDTO {

    private String traceId;
    private String customerName;
    private double amount;
    private String displayMessage;
    private String responseCode;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return "CgateDetailsResponseDTO{" +
            "traceId='" + traceId + '\'' +
            ", customerName='" + customerName + '\'' +
            ", amount='" + amount + '\'' +
            ", displayMessage='" + displayMessage + '\'' +
            ", responseCode='" + responseCode + '\'' +
            '}';
    }
}
