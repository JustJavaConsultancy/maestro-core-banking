package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "wallet",
    "username",
    "password",
    "identifier"
})
@Generated("jsonschema2pojo")
public class AuthenticationDTO {

    @JsonProperty("wallet")
    private String wallet;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("wallet")
    public String getWallet() {
        return wallet;
    }

    @JsonProperty("wallet")
    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty("identifier")
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "AuthenticationDTO{" +
            "wallet='" + wallet + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", identifier='" + identifier + '\'' +
            '}';
    }
}
