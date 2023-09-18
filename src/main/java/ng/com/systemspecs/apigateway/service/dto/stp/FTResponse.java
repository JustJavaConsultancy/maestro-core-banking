package ng.com.systemspecs.apigateway.service.dto.stp;


import java.io.Serializable;

public class FTResponse implements Serializable {

    private String responseCode;
    private String responseDescription;
    private String debitAccountNo;
    private String creditAccountNo;
    private String narration;
    private String tranRefNo;
    private String currency;
    private String transitToMirror;
    private String orgPayerAccount;
    private int retryCount;
    private String requestCode = "POST_PAYMENT";
    private int processOrder;
    private Double amount;
    private String transDate;
    private String origDebitRefNo;
    private String toBankCode;
    private String bankCode;
    private String controlRequestId;
    private StatusCode status;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

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

    public String getTranRefNo() {
        return tranRefNo;
    }

    public void setTranRefNo(String tranRefNo) {
        this.tranRefNo = tranRefNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransitToMirror() {
        return transitToMirror;
    }

    public void setTransitToMirror(String transitToMirror) {
        this.transitToMirror = transitToMirror;
    }

    public String getOrgPayerAccount() {
        return orgPayerAccount;
    }

    public void setOrgPayerAccount(String orgPayerAccount) {
        this.orgPayerAccount = orgPayerAccount;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public int getProcessOrder() {
        return processOrder;
    }

    public void setProcessOrder(int processOrder) {
        this.processOrder = processOrder;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getOrigDebitRefNo() {
        return origDebitRefNo;
    }

    public void setOrigDebitRefNo(String origDebitRefNo) {
        this.origDebitRefNo = origDebitRefNo;
    }

    public String getToBankCode() {
        return toBankCode;
    }

    public void setToBankCode(String toBankCode) {
        this.toBankCode = toBankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getControlRequestId() {
        return controlRequestId;
    }

    public void setControlRequestId(String controlRequestId) {
        this.controlRequestId = controlRequestId;
    }

    public StatusCode getStatus() {
        return status;
    }

    public void setStatus(StatusCode status) {
        this.status = status;
    }

    public enum StatusCode {
        NEW("N", "New"), SUCCESS("A", "Successful"), REJECTED("R", "Rejected"), PENDING("P", "Pending"), PAYMENT_AUTH_SENT("PA",
            "Payment Authorisation Sent"), FAILURE("F", "Failure"), UNCLEAR("UC", "Unclear Feedback"), RETRY("RT", "SUBMITED FOR PROCESSS"), SUBMITTED("SP",
            "SUBMITED FOR PROCESSSING"), SP("SP", "SUBMITED FOR PROCESSSING"), FAILED_CLOSED("FC", "Failed Closed"), FAILED_RETRY("FR",
            "Failed Retry");

        private String code;

        private String description;


        StatusCode(String code, String description) {
            this.code = code;
            this.description = description;
        }


        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
