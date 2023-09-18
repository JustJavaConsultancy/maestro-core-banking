package ng.com.systemspecs.apigateway.service.dto;

public class CgateDetailsRequestDTO {

    private String customerRef;
    private String merchantId;
    private String shortCode;

    public String getCustomerRef() {
        return customerRef;
    }

    public void setCustomerRef(String customerRef) {
        this.customerRef = customerRef;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    @Override
    public String toString() {
        return "CgateDetailsRequestDTO{" +
            "customerRef='" + customerRef + '\'' +
            ", merchantId='" + merchantId + '\'' +
            ", shortCode='" + shortCode + '\'' +
            '}';
    }
}
