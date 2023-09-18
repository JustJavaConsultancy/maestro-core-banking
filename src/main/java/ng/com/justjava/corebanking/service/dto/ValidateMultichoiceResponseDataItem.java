package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "code",
    "price",
    "name",
    "description"
})
@Generated("jsonschema2pojo")
public class ValidateMultichoiceResponseDataItem {

    @JsonProperty("code")
    private String code;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("price")
    public Integer getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Integer price) {
        this.price = price;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ValidateMultichoiceResponseDataItem{" +
            "code='" + code + '\'' +
            ", price=" + price +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
