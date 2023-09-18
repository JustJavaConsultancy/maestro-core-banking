package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class NINDataResponseDTO {
    private String status;
    private String responseCode;
    private String responseMsg;
    private NINResponseDTO data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public NINResponseDTO getData() {
        return data;
    }

    public void setData(NINResponseDTO data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NINDataResponseDTO{" +
            "status='" + status + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", responseMsg='" + responseMsg + '\'' +
            ", data=" + data +
            '}';
    }
}
