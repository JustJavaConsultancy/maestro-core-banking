package ng.com.justjava.corebanking.service.dto;

public class StatusCheckDTO {

    private String service;
    private String status;
    private String message;
    private String transRef;
    private String description;


    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "StatusCheckDTO{" +
            "service='" + service + '\'' +
            ", status='" + status + '\'' +
            ", message='" + message + '\'' +
            ", transRef='" + transRef + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
