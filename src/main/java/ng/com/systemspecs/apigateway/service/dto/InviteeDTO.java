package ng.com.systemspecs.apigateway.service.dto;

public class InviteeDTO {

    private String name;
    private String email;
    private String message;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "InviteeDTO{" +
            "name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
