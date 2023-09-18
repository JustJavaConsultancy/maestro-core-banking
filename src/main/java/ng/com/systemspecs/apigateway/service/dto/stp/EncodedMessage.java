package ng.com.systemspecs.apigateway.service.dto.stp;

import java.io.Serializable;

public class EncodedMessage implements Serializable {

    private static final long serialVersionUID = 638461037134298207L;

    private String payload;

    public EncodedMessage() {
        super();
    }

    public EncodedMessage(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
