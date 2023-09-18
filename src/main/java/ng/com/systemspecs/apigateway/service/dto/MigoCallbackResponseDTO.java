package ng.com.systemspecs.apigateway.service.dto;

public class MigoCallbackResponseDTO {

    private String status;

    public MigoCallbackResponseDTO(String status) {
        this.status = status;
    }

    public MigoCallbackResponseDTO() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MigoCallbackResponseDTO{" +
            "status='" + status + '\'' +
            '}';
    }

}
