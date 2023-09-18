package ng.com.justjava.corebanking.service.dto;

public class MigoQueryParamDTO {

    private String clientNo;
    private Boolean lastUsed;

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public Boolean getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Boolean lastUsed) {
        if (this.lastUsed == null){
            lastUsed = false;
        }
        this.lastUsed = lastUsed;
    }

    @Override
    public String toString() {
        return "MigoQueryParamDTO{" +
            "clientNo='" + clientNo + '\'' +
            ", lastUsed=" + lastUsed +
            '}';
    }
}
