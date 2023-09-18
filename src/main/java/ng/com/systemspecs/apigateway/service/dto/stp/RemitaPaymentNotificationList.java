package ng.com.systemspecs.apigateway.service.dto.stp;

import java.util.ArrayList;
import java.util.Collection;


@SuppressWarnings("serial")
public class RemitaPaymentNotificationList extends ArrayList<RemitaBillerNotification> {

    public RemitaPaymentNotificationList() {
        super();
    }


    public RemitaPaymentNotificationList(Collection<? extends RemitaBillerNotification> c) {
        super(c);
    }
}
