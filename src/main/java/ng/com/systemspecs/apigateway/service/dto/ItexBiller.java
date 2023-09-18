package ng.com.systemspecs.apigateway.service.dto;

import java.util.List;

public class ItexBiller {
    private String name;
    private String service;
    private List<AccountTypeDto> accountTypes;

    public ItexBiller(String name, String service, List<AccountTypeDto> accountTypes) {
        this.name = name;
        this.service = service;
        this.accountTypes = accountTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<AccountTypeDto> getAccountType() {
        return accountTypes;
    }

    public void setAccountType(List<AccountTypeDto> accountTypes) {
        this.accountTypes = accountTypes;
    }

    @Override
    public String toString() {
        return "ItexBiller{" +
            "name='" + name + '\'' +
            ", service='" + service + '\'' +
            ", accountType='" + accountTypes + '\'' +
            '}';
    }
}
