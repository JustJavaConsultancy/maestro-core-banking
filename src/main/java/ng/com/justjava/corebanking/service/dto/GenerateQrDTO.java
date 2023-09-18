package ng.com.justjava.corebanking.service.dto;

public class GenerateQrDTO {
    String merchantName, phoneNo, accountNo;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    @Override
    public String toString() {
        return "GenerateQrDTO{" +
            "merchantName='" + merchantName + '\'' +
            ", phoneNo='" + phoneNo + '\'' +
            ", accountNo='" + accountNo + '\'' +
            '}';
    }
}
