package ng.com.justjava.corebanking.service.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class UpgradeTierDTO {

    @SerializedName("accountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("AccountTier")
    @Expose
    private Integer accountTier;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getAccountTier() {
        return accountTier;
    }

    public void setAccountTier(Integer accountTier) {
        this.accountTier = accountTier;
    }

    @Override
    public String toString() {
        return "UpgradeTierDTO{" +
            "accountNumber='" + accountNumber + '\'' +
            ", accountTier=" + accountTier +
            '}';
    }
}
