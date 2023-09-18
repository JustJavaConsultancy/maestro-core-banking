package ng.com.justjava.corebanking.service.dto;

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
