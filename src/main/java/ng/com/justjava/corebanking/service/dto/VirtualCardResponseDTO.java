package ng.com.justjava.corebanking.service.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VirtualCardResponseDTO {

    @XmlAttribute
    String pan;

    @XmlAttribute
    String responseCode;

    @XmlAttribute
    String responseMessage;

    @XmlAttribute
    String clientCode;

    @XmlAttribute
    String cvv2;

    @XmlAttribute
    String expiryDate;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "VirtualCardResponseDTO{" +
            "pan='" + pan + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", responseMessage='" + responseMessage + '\'' +
            ", clientCode='" + clientCode + '\'' +
            ", cvv2=" + cvv2 +
            ", expiryDate='" + expiryDate + '\'' +
            '}';
    }
}
