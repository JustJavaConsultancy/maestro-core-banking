package ng.com.systemspecs.apigateway.service.dto;

public class SchemeCallBackDTO {

    private String scheme;
    private String callBackUrl;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    @Override
    public String toString() {
        return "SchemeCallDTO{" +
            "scheme='" + scheme + '\'' +
            ", callBackUrl='" + callBackUrl + '\'' +
            '}';
    }
}
