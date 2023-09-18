package ng.com.justjava.corebanking.service.dto.stp;

import javax.persistence.*;


public class StpRequestDetailsInstantUpload {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String debitAccountNo;

    private String creditAccountNo;

    private String narration;

    private String bankCode;

    private String tranRefNo;

    private Double amount = 0d;

    private String requestId;

    private String responseCode;

    private String lastResponseCode;

    private String transDate;

    private String responseUpdatedDate;

    private String hashStr;

    private String stan;

    private String origDebitRefNo;

    private int retryCount = 0;

    private boolean transitToMirror;

    private String orgPayerName;

    private String orgPayerAccount;

    private String orgPayerAddress;

    private String orgPayerBank;

    private int processOrder;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "DRACCTNO")
    public String getDebitAccountNo() {
        return debitAccountNo;
    }


    public void setDebitAccountNo(String debitAccountNo) {
        this.debitAccountNo = debitAccountNo;
    }


    @Column(name = "CRACCTNO")
    public String getCreditAccountNo() {
        return creditAccountNo;
    }


    public void setCreditAccountNo(String creditAccountNo) {
        this.creditAccountNo = creditAccountNo;
    }


    @Column(name = "NARRATION")
    public String getNarration() {
        return narration;
    }


    public void setNarration(String narration) {
        this.narration = narration;
    }


    @Column(name = "TRANREFNO")
    public String getTranRefNo() {
        return tranRefNo;
    }


    public void setTranRefNo(String tranRefNo) {
        this.tranRefNo = tranRefNo;
    }


    @Column(name = "AMOUNT")
    public Double getAmount() {
        return amount;
    }


    public void setAmount(Double amount) {
        this.amount = amount;
    }


    @Column(name = "REQUESTID")
    public String getRequestId() {
        return requestId;
    }


    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    @Column(name = "RESPONSECODE")
    public String getResponseCode() {
        return responseCode;
    }


    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }


    @Column(name = "LAST_RESPONSECODE")
    public String getLastResponseCode() {
        return lastResponseCode;
    }


    public void setLastResponseCode(String lastResponseCode) {
        this.lastResponseCode = lastResponseCode;
    }


    @Column(name = "TRANSDATE")
    public String getTransDate() {
        return transDate;
    }


    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }


    @Column(name = "RESPONSE_UPDATED_DATE")
    public String getResponseUpdatedDate() {
        return responseUpdatedDate;
    }


    public void setResponseUpdatedDate(String responseUpdatedDate) {
        this.responseUpdatedDate = responseUpdatedDate;
    }


    @Column(name = "HASHSTR")
    public String getHashStr() {
        return hashStr;
    }


    public void setHashStr(String hashStr) {
        this.hashStr = hashStr;
    }


    @Column(name = "STAN")
    public String getStan() {
        return stan;
    }


    public void setStan(String stan) {
        this.stan = stan;
    }


    @Column(name = "ORIGDEBITREFNO")
    public String getOrigDebitRefNo() {
        return origDebitRefNo;
    }


    public void setOrigDebitRefNo(String origDebitRefNo) {
        this.origDebitRefNo = origDebitRefNo;
    }


    @Column(name = "RETRYCOUNT", nullable = true)
    public int getRetryCount() {
        return retryCount;
    }


    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }


    @Column(name = "TRANSIT_TO_MIRROR")
    public Boolean getTransitToMirror() {
        return transitToMirror;
    }


    public void setTransitToMirror(Boolean transitToMirror) {
        this.transitToMirror = transitToMirror;
    }


    @Column(name = "ORG_PAYER_NAME")
    public String getOrgPayerName() {
        return orgPayerName;
    }


    public void setOrgPayerName(String orgPayerName) {
        this.orgPayerName = orgPayerName;
    }


    @Column(name = "ORG_PAYER_ACCOUNT")
    public String getOrgPayerAccount() {
        return orgPayerAccount;
    }


    public void setOrgPayerAccount(String orgPayerAccount) {
        this.orgPayerAccount = orgPayerAccount;
    }


    @Column(name = "ORG_PAYER_ADDRESS")
    public String getOrgPayerAddress() {
        return orgPayerAddress;
    }


    public void setOrgPayerAddress(String orgPayerAddress) {
        this.orgPayerAddress = orgPayerAddress;
    }


    @Transient
    public int getProcessOrder() {
        return processOrder;
    }


    public void setProcessOrder(int processOrder) {
        this.processOrder = processOrder;
    }


    @Column(name = "ORG_PAYER_BANK")
    public String getOrgPayerBank() {
        return orgPayerBank;
    }


    public void setOrgPayerBank(String orgPayerBank) {
        this.orgPayerBank = orgPayerBank;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public String toString() {
        return "StpRequestDetailsInstantUpload{" +
            "id=" + id +
            ", debitAccountNo='" + debitAccountNo + '\'' +
            ", creditAccountNo='" + creditAccountNo + '\'' +
            ", narration='" + narration + '\'' +
            ", bankCode='" + bankCode + '\'' +
            ", tranRefNo=" + tranRefNo +
            ", amount=" + amount +
            ", requestId='" + requestId + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", lastResponseCode='" + lastResponseCode + '\'' +
            ", transDate='" + transDate + '\'' +
            ", responseUpdatedDate='" + responseUpdatedDate + '\'' +
            ", hashStr='" + hashStr + '\'' +
            ", stan='" + stan + '\'' +
            ", origDebitRefNo='" + origDebitRefNo + '\'' +
            ", retryCount=" + retryCount +
            ", transitToMirror=" + transitToMirror +
            ", orgPayerName='" + orgPayerName + '\'' +
            ", orgPayerAccount='" + orgPayerAccount + '\'' +
            ", orgPayerAddress='" + orgPayerAddress + '\'' +
            ", orgPayerBank='" + orgPayerBank + '\'' +
            ", processOrder=" + processOrder +
            '}';
    }
}
