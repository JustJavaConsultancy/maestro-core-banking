package ng.com.justjava.corebanking.service.dto.stp;

import java.util.ArrayList;

public class RemitaPaymentNotificationDTO {

    private String rrr;
    private String channel;
    private String billerName;
    private double amount;
    private String transactiondate;
    private String debitdate;
    private String bank;
    private String branch;
    private String serviceTypeId;
    private String orderRef;
    private String orderId;
    private String payerName;
    private String payerPhoneNumber;
    private String payerEmail;
    private String type;
    private ArrayList<String> customFieldData = new ArrayList<>();
    private Object parentRRRDetails;
    private double chargeFee;
    private String paymentDescription;


    public String getRrr() {
        return rrr;
    }

    public void setRrr(String rrr) {
        this.rrr = rrr;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactiondate() {
        return transactiondate;
    }

    public void setTransactiondate(String transactiondate) {
        this.transactiondate = transactiondate;
    }

    public String getDebitdate() {
        return debitdate;
    }

    public void setDebitdate(String debitdate) {
        this.debitdate = debitdate;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getOrderRef() {
        return orderRef;
    }

    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerPhoneNumber() {
        return payerPhoneNumber;
    }

    public void setPayerPhoneNumber(String payerPhoneNumber) {
        this.payerPhoneNumber = payerPhoneNumber;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getCustomFieldData() {
        return customFieldData;
    }

    public void setCustomFieldData(ArrayList<String> customFieldData) {
        this.customFieldData = customFieldData;
    }

    public Object getParentRRRDetails() {
        return parentRRRDetails;
    }

    public void setParentRRRDetails(Object parentRRRDetails) {
        this.parentRRRDetails = parentRRRDetails;
    }

    public double getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(double chargeFee) {
        this.chargeFee = chargeFee;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    @Override
    public String toString() {
        return "RemitaPaymentNotificationDTO{" +
            "rrr='" + rrr + '\'' +
            ", channel='" + channel + '\'' +
            ", billerName='" + billerName + '\'' +
            ", amount=" + amount +
            ", transactiondate=" + transactiondate +
            ", debitdate=" + debitdate +
            ", bank='" + bank + '\'' +
            ", branch='" + branch + '\'' +
            ", serviceTypeId='" + serviceTypeId + '\'' +
            ", orderRef='" + orderRef + '\'' +
            ", orderId='" + orderId + '\'' +
            ", payerName='" + payerName + '\'' +
            ", payerPhoneNumber='" + payerPhoneNumber + '\'' +
            ", payerEmail='" + payerEmail + '\'' +
            ", type='" + type + '\'' +
            ", customFieldData=" + customFieldData +
            ", parentRRRDetails=" + parentRRRDetails +
            ", chargeFee='" + chargeFee + '\'' +
            ", paymentDescription='" + paymentDescription + '\'' +
            '}';
    }
}
