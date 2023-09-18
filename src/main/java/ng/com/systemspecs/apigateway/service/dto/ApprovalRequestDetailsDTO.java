package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.enumeration.RequestStatus;

public class ApprovalRequestDetailsDTO {

    private ApprovalRequestDTO approvalRequest;

    private Object data;

    public ApprovalRequestDTO getApprovalRequest() {
        return approvalRequest;
    }

    public void setApprovalRequest(ApprovalRequestDTO approvalRequest) {
        this.approvalRequest = approvalRequest;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApprovalRequestDetailsDTO{" +
            "approvalRequest=" + approvalRequest +
            ", data=" + data +
            '}';
    }
}
