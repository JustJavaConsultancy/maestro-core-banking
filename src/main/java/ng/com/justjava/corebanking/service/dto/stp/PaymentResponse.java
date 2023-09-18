package ng.com.justjava.corebanking.service.dto.stp;


public class PaymentResponse extends ServiceResponse {

    private StpRequestDetailsUpload detail;
//
//    private List<StpTraceLog> traceLogs = new ArrayList<>();

    public PaymentResponse() {
        super();
    }

    public StpRequestDetailsUpload getDetail() {
        return detail;
    }


    public void setDetail(StpRequestDetailsUpload detail) {
        this.detail = detail;
    }


//    public List<StpTraceLog> getTraceLogs() {
//        return traceLogs;
//    }
//
//
//    public void setTraceLogs(List<StpTraceLog> traceLogs) {
//        this.traceLogs = traceLogs;
//    }
}
