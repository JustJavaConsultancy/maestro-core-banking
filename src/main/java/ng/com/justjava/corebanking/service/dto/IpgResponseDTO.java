package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IpgResponseDTO {

    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("data")
    private Object data;

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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "IpgResponseDTO{" +
            "status='" + status + '\'' +
            ", message='" + message + '\'' +
            ", code=" + code +
            ", data=" + data +
            '}';
    }
}
