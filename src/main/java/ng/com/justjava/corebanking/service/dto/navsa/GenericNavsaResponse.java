package ng.com.justjava.corebanking.service.dto.navsa;

public class GenericNavsaResponse {
    private String respoinceCode;
    private String responseMsg;
    private Object responseData;

    public String getRespoincecode() {
        return respoinceCode;
    }

    public void setRespoincecode(String responsecode) {
        this.respoinceCode = responsecode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Object getResponseData() {
        return responseData;
    }

    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

    @Override
    public String toString() {
        return "GenericNavsaResponse{" +
            "responsecode='" + respoinceCode + '\'' +
            ", responseMsg='" + responseMsg + '\'' +
            ", responseData=" + responseData +
            '}';
    }
}
