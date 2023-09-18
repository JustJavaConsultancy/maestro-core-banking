package ng.com.systemspecs.apigateway.service.dto.stp;


@SuppressWarnings("serial")
public class PaymentPostRequestPayload extends DefaultRequestPayload {

    private String paymentDetail;

    private String bankCode;

    public PaymentPostRequestPayload() {
        super();
    }

    public String getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(String paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public String toString() {
        return "PaymentPostRequestPayload{" +
            "paymentDetail='" + paymentDetail + '\'' +
            ", bankCode='" + bankCode + '\'' +
            '}';
    }
}
