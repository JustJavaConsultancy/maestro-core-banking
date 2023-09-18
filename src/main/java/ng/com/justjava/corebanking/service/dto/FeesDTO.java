package ng.com.justjava.corebanking.service.dto;

public class FeesDTO {

    String matno;
    String label;
    String feeid;
    String session;
    String feetype;
    double amount;

    public String getMatno() {
        return matno;
    }

    public void setMatno(String matno) {
        this.matno = matno;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFeeid() {
        return feeid;
    }

    public void setFeeid(String feeid) {
        this.feeid = feeid;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getFeetype() {
        return feetype;
    }

    public void setFeetype(String feetype) {
        this.feetype = feetype;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return (
            "FeesDTO{" +
                "matno='" +
                matno +
                '\'' +
                ", label='" +
                label +
                '\'' +
                ", feeid='" +
                feeid +
                '\'' +
                ", session='" +
                session +
                '\'' +
                ", feetype=" +
                feetype +
                ", amount=" +
                amount +
                '}'
        );
    }
}
