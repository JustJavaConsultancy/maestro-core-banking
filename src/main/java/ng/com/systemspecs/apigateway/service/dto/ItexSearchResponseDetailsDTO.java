package ng.com.systemspecs.apigateway.service.dto;

    import javax.annotation.Generated;
    import com.fasterxml.jackson.annotation.JsonInclude;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "code",
    "description"
})
@Generated("jsonschema2pojo")
public class ItexSearchResponseDetailsDTO {

    @JsonProperty("code")
    private String code;
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
        return "ItexSearchResponseDetailsDTO{" +
            "code='" + code + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
