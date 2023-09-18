package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PolarisVulteWebHookRequest {

    @JsonProperty("request_ref")
    private String requestRef;

    @JsonProperty("request_type")
    private String requestType;

    @JsonProperty("requester")
    private String requester;

    @JsonProperty("mock_mode")
    private String mock_mode;

    @JsonProperty("details")
    private WebHookDetails details;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("app_info")
    private WebHookAppInfo appInfo;

    public String getRequestRef() {
        return requestRef;
    }

    public void setRequestRef(String requestRef) {
        this.requestRef = requestRef;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getMock_mode() {
        return mock_mode;
    }

    public void setMock_mode(String mock_mode) {
        this.mock_mode = mock_mode;
    }

    public WebHookDetails getDetails() {
        return details;
    }

    public void setDetails(WebHookDetails details) {
        this.details = details;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public WebHookAppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(WebHookAppInfo appInfo) {
        this.appInfo = appInfo;
    }

    @Override
    public String toString() {
        return "PolarisVulteWebHookRequest{" +
            "requestRef='" + requestRef + '\'' +
            ", requestType='" + requestType + '\'' +
            ", requester='" + requester + '\'' +
            ", mock_mode='" + mock_mode + '\'' +
            ", details=" + details +
            ", data=" + data +
            ", appInfo=" + appInfo +
            '}';
    }
}

class WebHookAppInfo {

    @JsonProperty("app_code")
    private String appCode;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    @Override
    public String toString() {
        return "WebHookAppInfo{" +
            "appCode='" + appCode + '\'' +
            '}';
    }
}
