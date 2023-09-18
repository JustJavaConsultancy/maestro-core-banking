package ng.com.systemspecs.apigateway.service.dto;

public class ReferenceVerificationDTO {

    private String WebGuid;
    private String State;
    private String Date;
    private String Hash;
    private String Clientid;
    private String TellerID;
    private String Currency = "NGN";
    private String CbnCode;
    private String Type;


    public String getWebGuid() {
        return WebGuid;
    }

    public void setWebGuid(String webGuid) {
        WebGuid = webGuid;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getHash() {
        return Hash;
    }

    public void setHash(String hash) {
        Hash = hash;
    }

    public String getClientid() {
        return Clientid;
    }

    public void setClientid(String clientid) {
        Clientid = clientid;
    }

    public String getTellerID() {
        return TellerID;
    }

    public void setTellerID(String tellerID) {
        TellerID = tellerID;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getCbnCode() {
        return CbnCode;
    }

    public void setCbnCode(String cbnCode) {
        CbnCode = cbnCode;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @Override
    public String toString() {
        return "ReferenceVerificationDTO{" +
            "WebGuid='" + WebGuid + '\'' +
            ", State='" + State + '\'' +
            ", Date='" + Date + '\'' +
            ", Hash='" + Hash + '\'' +
            ", Clientid='" + Clientid + '\'' +
            ", TellerID='" + TellerID + '\'' +
            ", Currency='" + Currency + '\'' +
            ", CbnCode='" + CbnCode + '\'' +
            ", Type='" + Type + '\'' +
            '}';
    }
}
