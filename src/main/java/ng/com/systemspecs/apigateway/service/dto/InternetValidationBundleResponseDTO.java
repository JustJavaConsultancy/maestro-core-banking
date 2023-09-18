package ng.com.systemspecs.apigateway.service.dto;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ng.com.systemspecs.apigateway.service.dto.Bundle;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "error",
    "message",
    "bundles",
    "responseCode",
    "description"
})
@Generated("jsonschema2pojo")
public class InternetValidationBundleResponseDTO {

    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("message")
    private String message;
    @JsonProperty("bundles")
    private List<Bundle> bundles = null;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("description")
    private String description;

    @JsonProperty("error")
    public Boolean getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(Boolean error) {
        this.error = error;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("bundles")
    public List<Bundle> getBundles() {
        return bundles;
    }

    @JsonProperty("bundles")
    public void setBundles(List<Bundle> bundles) {
        this.bundles = bundles;
    }

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
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
        return "InternetValidationBundleResponseDTO{" +
            "error=" + error +
            ", message='" + message + '\'' +
            ", bundles=" + bundles +
            ", responseCode='" + responseCode + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
