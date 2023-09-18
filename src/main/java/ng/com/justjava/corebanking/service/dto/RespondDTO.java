package ng.com.justjava.corebanking.service.dto;

import java.util.List;

public class RespondDTO<T> {
    private String message;
    private String code;
    private String token;
    private T user;
    private String userType;
    private List<WalletAccountDTO> walletAccountList;

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<WalletAccountDTO> getWalletAccountList() {
        return walletAccountList;
    }

    public void setWalletAccountList(List<WalletAccountDTO> walletAccountList) {
        this.walletAccountList = walletAccountList;
    }

    @Override
    public String toString() {
        return "RespondDTO{" +
            "message='" + message + '\'' +
            ", code='" + code + '\'' +
            ", token='" + token + '\'' +
            ", user=" + user +
            ", userType=" + userType +
            ", walletAccountList=" + walletAccountList +
            '}';
    }
}
