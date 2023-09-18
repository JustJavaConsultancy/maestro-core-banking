package ng.com.justjava.corebanking.service.dto.stp;

import java.util.ArrayList;
import java.util.Collection;


@SuppressWarnings("serial")
public class StpRequestDetailsUploadList extends ArrayList<StpRequestDetailsInstantUpload> {

    public StpRequestDetailsUploadList() {
        super();
    }


    public StpRequestDetailsUploadList(Collection<? extends StpRequestDetailsInstantUpload> c) {
        super(c);
    }
}
