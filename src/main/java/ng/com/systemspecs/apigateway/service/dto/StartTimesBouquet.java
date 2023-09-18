package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "cycles"
})
@Generated("jsonschema2pojo")
public class StartTimesBouquet {

    @JsonProperty("name")
    private String name;
    @JsonProperty("cycles")
    private Cycles cycles;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("cycles")
    public Cycles getCycles() {
        return cycles;
    }

    @JsonProperty("cycles")
    public void setCycles(Cycles cycles) {
        this.cycles = cycles;
    }

    @Override
    public String toString() {
        return "StartTimesBouquet{" +
            "name='" + name + '\'' +
            ", cycles=" + cycles +
            '}';
    }
}
