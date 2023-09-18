package ng.com.systemspecs.apigateway.service.dto;

public class P2VestCreateLoanDTO {

    private String phoneNumber;
    private String netPay;
    private String amount;
    private String duration;
    private String lenderId;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNetPay() {
        return netPay;
    }

    public void setNetPay(String netPay) {
        this.netPay = netPay;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLenderId() {
        return lenderId;
    }

    public void setLenderId(String lenderId) {
        this.lenderId = lenderId;
    }

    @Override
    public String toString() {
        return "P2VestCreateLoanDTO{" +
            "phoneNumber='" + phoneNumber + '\'' +
            ", netPay='" + netPay + '\'' +
            ", amount='" + amount + '\'' +
            ", duration='" + duration + '\'' +
            ", lenderId='" + lenderId + '\'' +
            '}';
    }
}
