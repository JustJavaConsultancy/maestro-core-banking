package ng.com.justjava.corebanking.service.dto;

public class Bvn {

    private String bvn;

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    @Override
    public String toString() {
        return "Bvn{" +
            "bvn='" + bvn + '\'' +
            '}';
    }
}
