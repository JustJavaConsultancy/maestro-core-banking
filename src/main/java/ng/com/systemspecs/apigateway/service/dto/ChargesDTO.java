package ng.com.systemspecs.apigateway.service.dto;

public class ChargesDTO {

    double transactionFee;
    double vat;

    public double getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(double transactionFee) {
        this.transactionFee = transactionFee;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    @Override
    public String toString() {
        return "ChargesDTO{" +
            "transactionFee=" + transactionFee +
            ", vat=" + vat +
            '}';
    }
}
