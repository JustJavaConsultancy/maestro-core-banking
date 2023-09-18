package ng.com.justjava.corebanking.service.dto;

public class RemitaBillerDTO {

    private String billerId;
    private String description;
    private String billerName;

    public String getBillerId() {
        return billerId;
    }

    public void setBillerId(String billerId) {
        this.billerId = billerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    @Override
    public String toString() {
        return "RemitaBillerDTO{" +
            "billerId='" + billerId + '\'' +
            ", description='" + description + '\'' +
            ", billerName='" + billerName + '\'' +
            '}';
    }
}
