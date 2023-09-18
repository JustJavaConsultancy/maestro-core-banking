package ng.com.systemspecs.apigateway.service.dto;

public class MigoCallbackRequestDTO {

    private String loanid;
    private String status;

    public String getLoanid() {
        return loanid;
    }

    public void setLoanid(String loanid) {
        this.loanid = loanid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MigoCallbackRequestDTO{" +
            "loanid='" + loanid + '\'' +
            ", status='" + status + '\'' +
            '}';
    }

}
