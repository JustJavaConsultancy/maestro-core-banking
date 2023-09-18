package ng.com.systemspecs.apigateway.service.dto;

public class IbilePaymentDetailsDTO {

    private String status;
    private double totalAmountPaid;
    private String internalRef;
    private String lastModifiedDate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(double totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public String getInternalRef() {
        return internalRef;
    }

    public void setInternalRef(String internalRef) {
        this.internalRef = internalRef;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return "IbilePaymentDetailsDTO{" +
            "status='" + status + '\'' +
            ", totalAmountPaid=" + totalAmountPaid +
            ", internalRef='" + internalRef + '\'' +
            ", lastModifiedDate='" + lastModifiedDate + '\'' +
            '}';
    }
}
