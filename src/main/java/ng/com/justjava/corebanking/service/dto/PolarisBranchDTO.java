package ng.com.justjava.corebanking.service.dto;

public class PolarisBranchDTO {
    int id;
    String name;
    String code;
    String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "PolarisBranchDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", code='" + code + '\'' +
            ", address='" + address + '\'' +
            '}';
    }
}
