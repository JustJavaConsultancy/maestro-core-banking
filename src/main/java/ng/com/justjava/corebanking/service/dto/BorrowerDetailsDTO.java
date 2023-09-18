package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import javax.validation.constraints.NotEmpty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "borrower_id"
})
@Generated("jsonschema2pojo")
public class BorrowerDetailsDTO {

    @JsonProperty("borrower_id")
    @NotEmpty(message = "BorrowerId cannot be null")
    private String borrowerId;

    @JsonProperty("borrower_id")
    public String getBorrowerId() {
        return borrowerId;
    }

    @JsonProperty("borrower_id")
    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    @Override
    public String toString() {
        return "BorrowerDetailsDTO{" +
            "borrowerId='" + borrowerId + '\'' +
            '}';
    }
}
