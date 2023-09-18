package ng.com.justjava.corebanking.service.dto;

    import java.util.List;
    import javax.annotation.Generated;
    import com.fasterxml.jackson.annotation.JsonInclude;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "error",
    "details",
    "description"
})
@Generated("jsonschema2pojo")
public class ItexSearchResponseDataDTO {

    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("details")
    private List<ItexSearchResponseDetailsDTO> details = null;
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

    @JsonProperty("details")
    public List<ItexSearchResponseDetailsDTO> getDetails() {
        return details;
    }

    @JsonProperty("details")
    public void setDetails(List<ItexSearchResponseDetailsDTO> details) {
        this.details = details;
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
        return "ItexSearchResponseDataDTO{" +
            "error=" + error +
            ", details=" + details +
            ", description='" + description + '\'' +
            '}';
    }
}
