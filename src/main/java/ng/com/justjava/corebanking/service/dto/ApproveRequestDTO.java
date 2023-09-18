package ng.com.justjava.corebanking.service.dto;

public class ApproveRequestDTO {
    private String requestId;
    private String additionalComment;
    private String action;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAdditionalComment() {
        return additionalComment;
    }

    public void setAdditionalComment(String additionalComment) {
        this.additionalComment = additionalComment;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "ApproveRequestDTO{" +
            "requestId='" + requestId + '\'' +
            ", additionalComment='" + additionalComment + '\'' +
            ", action='" + action + '\'' +
            '}';
    }
}
