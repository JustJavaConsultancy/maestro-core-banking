package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "refId",
    "authorisationCode",
    "authorisationChannel",
    "customFields"
})
@Generated("jsonschema2pojo")
public class NINRequestDTO {

    @JsonProperty("refId")
    private String refId = "nimcDetailsByNin";
    @JsonProperty("authorisationCode")
    private String authorisationCode = "67777777";
    @JsonProperty("authorisationChannel")
    private String authorisationChannel = "USSD";
    @JsonProperty("customFields")
    private List<NINExamplePayload> customFields = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("refId")
    public String getRefId() {
        return refId;
    }

    @JsonProperty("refId")
    public void setRefId(String refId) {
        this.refId = refId;
    }

    @JsonProperty("authorisationCode")
    public String getAuthorisationCode() {
        return authorisationCode;
    }

    @JsonProperty("authorisationCode")
    public void setAuthorisationCode(String authorisationCode) {
        this.authorisationCode = authorisationCode;
    }

    @JsonProperty("authorisationChannel")
    public String getAuthorisationChannel() {
        return authorisationChannel;
    }

    @JsonProperty("authorisationChannel")
    public void setAuthorisationChannel(String authorisationChannel) {
        this.authorisationChannel = authorisationChannel;
    }

    @JsonProperty("customFields")
    public List<NINExamplePayload> getCustomFields() {
        return customFields;
    }

    @JsonProperty("customFields")
    public void setCustomFields(List<NINExamplePayload> customFields) {
        this.customFields = customFields;
    }

}
