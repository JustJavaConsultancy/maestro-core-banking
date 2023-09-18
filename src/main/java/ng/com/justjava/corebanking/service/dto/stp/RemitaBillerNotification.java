package ng.com.justjava.corebanking.service.dto.stp;

/* Copyright 2020 freecodeformat.com */

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RemitaBillerNotification {

    private String rrr;
    private String channnel;
    @JsonProperty("billerName")
    private String billername;
    private String channel;
    private double amount;
    private String transactiondate;
    private String debitdate;
    private String bank;
    private String branch;
    @JsonProperty("serviceTypeId")
    private String servicetypeid;
    @JsonProperty("orderRef")
    private String orderref;
    @JsonProperty("orderId")
    private String orderid;
    @JsonProperty("payerName")
    private String payername;
    @JsonProperty("payerPhoneNumber")
    private String payerphonenumber;
    @JsonProperty("payerEmail")
    private String payeremail;
    private String type;
    @JsonProperty("customFieldData")
    private List<Customfielddata> customfielddata;
    @JsonProperty("parentRRRDetails")
    private Parentrrrdetails parentrrrdetails;
    @JsonProperty("chargeFee")
    private double chargefee;
    @JsonProperty("paymentDescription")
    private String paymentdescription;
    @JsonProperty("integratorsEmail")
    private String integratorsemail;
    @JsonProperty("integratorsPhonenumber")
    private String integratorsphonenumber;
    public void setRrr(String rrr) {
         this.rrr = rrr;
     }
     public String getRrr() {
         return rrr;
     }

    public void setChannnel(String channnel) {
         this.channnel = channnel;
     }
     public String getChannnel() {
         return channnel;
     }

    public void setBillername(String billername) {
         this.billername = billername;
     }
     public String getBillername() {
         return billername;
     }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTransactiondate(String transactiondate) {
        this.transactiondate = transactiondate;
    }

    public String getTransactiondate() {
        return transactiondate;
    }

    public void setDebitdate(String debitdate) {
        this.debitdate = debitdate;
     }
     public String getDebitdate() {
         return debitdate;
     }

    public void setBank(String bank) {
         this.bank = bank;
     }
     public String getBank() {
         return bank;
     }

    public void setBranch(String branch) {
         this.branch = branch;
     }
     public String getBranch() {
         return branch;
     }

    public void setServicetypeid(String servicetypeid) {
         this.servicetypeid = servicetypeid;
     }
     public String getServicetypeid() {
         return servicetypeid;
     }

    public void setOrderref(String orderref) {
         this.orderref = orderref;
     }
     public String getOrderref() {
         return orderref;
     }

    public void setOrderid(String orderid) {
         this.orderid = orderid;
     }
     public String getOrderid() {
         return orderid;
     }

    public void setPayername(String payername) {
         this.payername = payername;
     }
     public String getPayername() {
         return payername;
     }

    public void setPayerphonenumber(String payerphonenumber) {
         this.payerphonenumber = payerphonenumber;
     }
     public String getPayerphonenumber() {
         return payerphonenumber;
     }

    public void setPayeremail(String payeremail) {
         this.payeremail = payeremail;
     }
     public String getPayeremail() {
         return payeremail;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setCustomfielddata(List<Customfielddata> customfielddata) {
         this.customfielddata = customfielddata;
     }
     public List<Customfielddata> getCustomfielddata() {
         return customfielddata;
     }

    public void setParentrrrdetails(Parentrrrdetails parentrrrdetails) {
         this.parentrrrdetails = parentrrrdetails;
     }
     public Parentrrrdetails getParentrrrdetails() {
         return parentrrrdetails;
     }

    public void setChargefee(double chargefee) {
         this.chargefee = chargefee;
     }
     public double getChargefee() {
         return chargefee;
     }

    public void setPaymentdescription(String paymentdescription) {
         this.paymentdescription = paymentdescription;
     }
     public String getPaymentdescription() {
         return paymentdescription;
     }

    public void setIntegratorsemail(String integratorsemail) {
         this.integratorsemail = integratorsemail;
     }
     public String getIntegratorsemail() {
         return integratorsemail;
     }

    public void setIntegratorsphonenumber(String integratorsphonenumber) {
         this.integratorsphonenumber = integratorsphonenumber;
     }
     public String getIntegratorsphonenumber() {
         return integratorsphonenumber;
     }

}
