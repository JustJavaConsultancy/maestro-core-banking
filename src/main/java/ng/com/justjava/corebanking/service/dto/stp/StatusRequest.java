package ng.com.justjava.corebanking.service.dto.stp;

import java.io.Serializable;


/**
 *
 */
public class StatusRequest implements Serializable {

    private String transRef;


    public StatusRequest() {
        super();
    }


    public String getTransRef() {
        return transRef;
    }


    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }
}
