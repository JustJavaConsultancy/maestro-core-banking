package ng.com.justjava.corebanking.service.dto.stp;

import ng.com.justjava.corebanking.domain.enumeration.RequestCode;

@SuppressWarnings("serial")
public class DefaultRequestPayload extends AbstractRequestPayload {

    public DefaultRequestPayload() {
        super();
    }

    public DefaultRequestPayload(RequestCode action, String bank) {
        super();

        this.bank = bank;
        this.requestCode = action;
    }
}
