package ng.com.justjava.corebanking.service.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "billerId",
    "billerName",
    "billerLogoUrl",
    "categoryId",
    "categoryName",
    "categoryDescription"
})
@Generated("jsonschema2pojo")
public class PayBiller {

    @JsonProperty("billerId")
    private String billerId;
    @JsonProperty("billerName")
    private String billerName;
    @JsonProperty("billerLogoUrl")
    private String billerLogoUrl;
    @JsonProperty("categoryId")
    private Integer categoryId;
    @JsonProperty("categoryName")
    private String categoryName;
    @JsonProperty("categoryDescription")
    private String categoryDescription;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("billerId")
    public String getBillerId() {
        return billerId;
    }

    @JsonProperty("billerId")
    public void setBillerId(String billerId) {
        this.billerId = billerId;
    }

    @JsonProperty("billerName")
    public String getBillerName() {
        return billerName;
    }

    @JsonProperty("billerName")
    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    @JsonProperty("billerLogoUrl")
    public String getBillerLogoUrl() {
        return billerLogoUrl;
    }

    @JsonProperty("billerLogoUrl")
    public void setBillerLogoUrl(String billerLogoUrl) {
        this.billerLogoUrl = billerLogoUrl;
    }

    @JsonProperty("categoryId")
    public Integer getCategoryId() {
        return categoryId;
    }

    @JsonProperty("categoryId")
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @JsonProperty("categoryName")
    public String getCategoryName() {
        return categoryName;
    }

    @JsonProperty("categoryName")
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @JsonProperty("categoryDescription")
    public String getCategoryDescription() {
        return categoryDescription;
    }

    @JsonProperty("categoryDescription")
    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "PayBiller{" +
            "billerId='" + billerId + '\'' +
            ", billerName='" + billerName + '\'' +
            ", billerLogoUrl='" + billerLogoUrl + '\'' +
            ", categoryId=" + categoryId +
            ", categoryName='" + categoryName + '\'' +
            ", categoryDescription='" + categoryDescription + '\'' +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
