package ng.com.systemspecs.apigateway.service.dto.navsa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "accountNumber",
    "amount",
    "currency",
    "channel",
    "debitOrCredit",
    "narration",
    "referenceId",
    "transactionTime",
    "transactionType",
    "valueDate",
    "balanceAfter",
    "expansionFields"
})
@Generated("jsonschema2pojo")
public class AccountStatement {

    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("channel")
    private String channel;
    @JsonProperty("debitOrCredit")
    private String debitOrCredit;
    @JsonProperty("narration")
    private String narration;
    @JsonProperty("referenceId")
    private String referenceId;
    @JsonProperty("transactionTime")
    private LocalDateTime transactionTime;
    @JsonProperty("transactionType")
    private String transactionType;
    @JsonProperty("valueDate")
    private LocalDate valueDate;
    @JsonProperty("balanceAfter")
    private Double balanceAfter;
    @JsonProperty("expansionFields")
    private List<ExpansionField> expansionFields = null;

    @JsonProperty("accountNumber")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("accountNumber")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("channel")
    public String getChannel() {
        return channel;
    }

    @JsonProperty("channel")
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @JsonProperty("debitOrCredit")
    public String getDebitOrCredit() {
        return debitOrCredit;
    }

    @JsonProperty("debitOrCredit")
    public void setDebitOrCredit(String debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    @JsonProperty("narration")
    public String getNarration() {
        return narration;
    }

    @JsonProperty("narration")
    public void setNarration(String narration) {
        this.narration = narration;
    }

    @JsonProperty("referenceId")
    public String getReferenceId() {
        return referenceId;
    }

    @JsonProperty("referenceId")
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @JsonProperty("transactionTime")
    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    @JsonProperty("transactionTime")
    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    @JsonProperty("transactionType")
    public String getTransactionType() {
        return transactionType;
    }

    @JsonProperty("transactionType")
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @JsonProperty("valueDate")
    public LocalDate getValueDate() {
        return valueDate;
    }

    @JsonProperty("valueDate")
    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    @JsonProperty("balanceAfter")
    public Double getBalanceAfter() {
        return balanceAfter;
    }

    @JsonProperty("balanceAfter")
    public void setBalanceAfter(Double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    @JsonProperty("expansionFields")
    public List<ExpansionField> getExpansionFields() {
        return expansionFields;
    }

    @JsonProperty("expansionFields")
    public void setExpansionFields(List<ExpansionField> expansionFields) {
        this.expansionFields = expansionFields;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
        "id",
        "description",
        "type",
        "value"
    })
    class ExpansionField {

        @JsonProperty("id")
        private String id;
        @JsonProperty("description")
        private String description;
        @JsonProperty("type")
        private String type;
        @JsonProperty("value")
        private String value;

        @JsonProperty("id")
        public String getId() {
            return id;
        }

        @JsonProperty("id")
        public void setId(String id) {
            this.id = id;
        }

        @JsonProperty("description")
        public String getDescription() {
            return description;
        }

        @JsonProperty("description")
        public void setDescription(String description) {
            this.description = description;
        }

        @JsonProperty("type")
        public String getType() {
            return type;
        }

        @JsonProperty("type")
        public void setType(String type) {
            this.type = type;
        }

        @JsonProperty("value")
        public String getValue() {
            return value;
        }

        @JsonProperty("value")
        public void setValue(String value) {
            this.value = value;
        }
    }

}
