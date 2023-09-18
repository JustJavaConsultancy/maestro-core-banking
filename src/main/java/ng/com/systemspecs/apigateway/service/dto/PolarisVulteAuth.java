package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "secure",
    "auth_provider",
    "route_mode"
})
@Generated("jsonschema2pojo")
public class PolarisVulteAuth {

    @JsonProperty("type")
    private String type;
    @JsonProperty("secure")
    private String secure;
    @JsonProperty("auth_provider")
    private String authProvider;
    @JsonProperty("route_mode")
    private Object routeMode;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("secure")
    public String getSecure() {
        return secure;
    }

    @JsonProperty("secure")
    public void setSecure(String secure) {
        this.secure = secure;
    }

    @JsonProperty("auth_provider")
    public String getAuthProvider() {
        return authProvider;
    }

    @JsonProperty("auth_provider")
    public void setAuthProvider(String authProvider) {
        this.authProvider = authProvider;
    }

    @JsonProperty("route_mode")
    public Object getRouteMode() {
        return routeMode;
    }

    @JsonProperty("route_mode")
    public void setRouteMode(Object routeMode) {
        this.routeMode = routeMode;
    }

    @Override
    public String toString() {
        return "Auth{" +
            "type='" + type + '\'' +
            ", secure='" + secure + '\'' +
            ", authProvider='" + authProvider + '\'' +
            ", routeMode=" + routeMode +
            '}';
    }
}
