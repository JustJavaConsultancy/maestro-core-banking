package ng.com.justjava.corebanking.service.dto;

public class ServiceTypeDTO {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ServiceTypeDTO{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
