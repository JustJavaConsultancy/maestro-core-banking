package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "code"
})
@Generated("jsonschema2pojo")
public class MigoGetBanksResponseDTO {

    @JsonProperty("name")
    private String name;
    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "MigoGetBanksResponseDTO{" +
            "name='" + name + '\'' +
            ", code='" + code + '\'' +
            '}';
    }
}
