package ng.com.justjava.corebanking.service.dto;

public class AccountTypeDto {
    private String name;

    public AccountTypeDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AccountTypeDto{" +
            "name='" + name + '\'' +
            '}';
    }
}
