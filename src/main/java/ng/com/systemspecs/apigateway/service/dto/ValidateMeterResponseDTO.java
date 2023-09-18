package ng.com.systemspecs.apigateway.service.dto;

public class ValidateMeterResponseDTO {

    private String address;
    private String name;
    private String balance;
    private String vendingAmount;
    private String minimumAmount;
    private String amountDue;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getVendingAmount() {
        return vendingAmount;
    }

    public void setVendingAmount(String vendingAmount) {
        this.vendingAmount = vendingAmount;
    }

    public String getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(String minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public String getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(String amountDue) {
        this.amountDue = amountDue;
    }

    @Override
    public String toString() {
        return "ValidateMeterResponseDTO{" +
            "address='" + address + '\'' +
            ", name='" + name + '\'' +
            ", balance='" + balance + '\'' +
            ", vendingAmount='" + vendingAmount + '\'' +
            ", minimumAmount='" + minimumAmount + '\'' +
            ", amountDue='" + amountDue + '\'' +
            '}';
    }
}
