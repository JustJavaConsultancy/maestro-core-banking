package ng.com.justjava.corebanking.service.dto.stp;

public class OpenAccountResponseDto extends BaseProcessedData {

    private String requestId;

    private String accountNumber;

    private String customerId;

    private String accountOpeningDate;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }


    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    public String getCustomerId() {
        return customerId;
    }


    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


    public String getAccountOpeningDate() {
        return accountOpeningDate;
    }


    public void setAccountOpeningDate(String accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }
}

