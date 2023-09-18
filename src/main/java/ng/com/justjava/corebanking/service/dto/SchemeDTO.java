package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ng.com.justjava.corebanking.domain.Scheme;

import java.io.Serializable;

/**
 * A DTO for the {@link Scheme} entity.
 */
public class SchemeDTO implements Serializable {

    private Long id;

    private String schemeID;

    private String scheme;

    private String accountNumber;

    private String bankCode;

    private String callBackUrl;

    @JsonIgnore
    private Long schemeCategoryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchemeID() {
        return schemeID;
    }

    public void setSchemeID(String schemeID) {
        this.schemeID = schemeID;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public Long getSchemeCategoryId() {
        return schemeCategoryId;
    }

    public void setSchemeCategoryId(Long schemeCategoryId) {
        this.schemeCategoryId = schemeCategoryId;
    }

    private String apiKey;

    private String secretKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchemeDTO)) {
            return false;
        }

        return id != null && id.equals(((SchemeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SchemeDTO{" +
            "id=" + id +
            ", schemeID='" + schemeID + '\'' +
            ", scheme='" + scheme + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", bankCode='" + bankCode + '\'' +
            ", callBackUrl='" + callBackUrl + '\'' +
            ", schemeCategoryId=" + schemeCategoryId +
            ", apiKey='" + apiKey + '\'' +
            ", secretKey='" + secretKey + '\'' +
            '}';
    }
}
