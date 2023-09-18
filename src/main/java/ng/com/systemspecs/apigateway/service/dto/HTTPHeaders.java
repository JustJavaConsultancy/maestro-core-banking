package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "date",
    "content-type",
    "content-length",
    "connection",
    "x-amzn-requestid"
})
@Generated("jsonschema2pojo")
public class HTTPHeaders {

    @JsonProperty("date")
    private String date;
    @JsonProperty("content-type")
    private String contentType;
    @JsonProperty("content-length")
    private String contentLength;
    @JsonProperty("connection")
    private String connection;
    @JsonProperty("x-amzn-requestid")
    private String xAmznRequestid;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("content-type")
    public String getContentType() {
        return contentType;
    }

    @JsonProperty("content-type")
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonProperty("content-length")
    public String getContentLength() {
        return contentLength;
    }

    @JsonProperty("content-length")
    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    @JsonProperty("connection")
    public String getConnection() {
        return connection;
    }

    @JsonProperty("connection")
    public void setConnection(String connection) {
        this.connection = connection;
    }

    @JsonProperty("x-amzn-requestid")
    public String getxAmznRequestid() {
        return xAmznRequestid;
    }

    @JsonProperty("x-amzn-requestid")
    public void setxAmznRequestid(String xAmznRequestid) {
        this.xAmznRequestid = xAmznRequestid;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
