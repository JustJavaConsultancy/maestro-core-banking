package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.beans.factory.annotation.Value;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Username",
    "Password"
})
public class RequestHeaderDTO {

    @Value("${app.serm}")
    private String serm;

    @Value("${app.dough}")
    private String dough;

    @JsonProperty("Username")
    private String username = serm;
    @JsonProperty("Password")
    private String password = dough;

    public RequestHeaderDTO() {
        this.password = dough;
        this.username = serm;
    }

    @JsonProperty("Username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("Username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("Password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("Password")
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RequestHeaderDTO{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }


}
