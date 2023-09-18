package ng.com.justjava.corebanking.service.dto.stp;


public class StpRequestDetailsUpload {

    private static final long serialVersionUID = 1L;


    private String debitAccountNo;

    private String creditAccountNo;

    private String narration;

    private Long tranRefNo;

    private Double amount = 0d;

    private String requestId;

    private Long sessionNumber;

    private String responseCode;

    private String lastResponseCode;

    private String transDate;

    private String responseUpdatedDate;


    private String stan;

    private String origDebitRefNo;

    private int retryCount = 0;

    private int postingIndex = 0;


    private String partitionId;

    private String cbaFtIf;

    public String getDebitAccountNo() {
        return debitAccountNo;
    }

    public void setDebitAccountNo(String debitAccountNo) {
        this.debitAccountNo = debitAccountNo;
    }

    public String getCreditAccountNo() {
        return creditAccountNo;
    }

    public void setCreditAccountNo(String creditAccountNo) {
        this.creditAccountNo = creditAccountNo;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Long getTranRefNo() {
        return tranRefNo;
    }

    public void setTranRefNo(Long tranRefNo) {
        this.tranRefNo = tranRefNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(Long sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getLastResponseCode() {
        return lastResponseCode;
    }

    public void setLastResponseCode(String lastResponseCode) {
        this.lastResponseCode = lastResponseCode;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getResponseUpdatedDate() {
        return responseUpdatedDate;
    }

    public void setResponseUpdatedDate(String responseUpdatedDate) {
        this.responseUpdatedDate = responseUpdatedDate;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getOrigDebitRefNo() {
        return origDebitRefNo;
    }

    public void setOrigDebitRefNo(String origDebitRefNo) {
        this.origDebitRefNo = origDebitRefNo;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getPostingIndex() {
        return postingIndex;
    }

    public void setPostingIndex(int postingIndex) {
        this.postingIndex = postingIndex;
    }

    public String getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public String getCbaFtIf() {
        return cbaFtIf;
    }

    public void setCbaFtIf(String cbaFtIf) {
        this.cbaFtIf = cbaFtIf;
    }


}

