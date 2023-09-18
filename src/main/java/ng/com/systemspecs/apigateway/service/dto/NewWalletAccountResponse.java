package ng.com.systemspecs.apigateway.service.dto;

import java.io.Serializable;

public class NewWalletAccountResponse implements Serializable {

    private String status;
    private String message;
    private String _meta;
    private String _links;

    private WalletAccountResponseData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}

public String get_meta() {
	return _meta;
}

public void set_meta(String _meta) {
	this._meta = _meta;
}

    public String get_links() {
        return _links;
    }

    public void set_links(String _links) {
        this._links = _links;
    }

    public WalletAccountResponseData getData() {
        return data;
    }

    public void setData(WalletAccountResponseData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NewWalletAccountResponse{" +
            "status='" + status + '\'' +
            ", message='" + message + '\'' +
            ", _meta='" + _meta + '\'' +
            ", _links='" + _links + '\'' +
            ", data=" + data +
            '}';
    }
}
