package ng.com.justjava.corebanking.service.dto;

public class ReverseDTO {

    private String transRef;
    private String status;
    private String pointOfFailure;

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPointOfFailure() {
        return pointOfFailure;
    }

    public void setPointOfFailure(String pointOfFailure) {
        this.pointOfFailure = pointOfFailure;
    }

    @Override
    public String toString() {
        return "ReverseDTO{" +
            "transRef='" + transRef + '\'' +
            ", status='" + status + '\'' +
            ", pointOfFailure='" + pointOfFailure + '\'' +
            '}';
    }
}
