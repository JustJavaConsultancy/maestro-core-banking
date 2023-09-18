package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BankDTO {

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @JsonIgnore
    private Long id;
    private String bankCode;
    private String bankAccronym;
    private String type;
    private String bankName;
    private String shortCode;

    public String getBankAccronym() {
        return bankAccronym;
    }

    public void setBankAccronym(String bankAccronym) {
        this.bankAccronym = bankAccronym;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    @Override
    public String toString() {
        return "BankDTO{" +
            "id=" + id +
            ", bankCode='" + bankCode + '\'' +
            ", bankName='" + bankName + '\'' +
            ", bankAccronym='" + bankAccronym + '\'' +
            ", type='" + type + '\'' +
            ", shortCode='" + shortCode + '\'' +
            '}';
    }


}
