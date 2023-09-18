package ng.com.systemspecs.apigateway.service.dto;

public class CustomFieldDTO {
    private String customFieldId;
    private String value;

    public CustomFieldDTO() {
    }

    public CustomFieldDTO(String customFieldId, String value) {
        this.customFieldId = customFieldId;
        this.value = value;
    }

    public String getCustomFieldId() {
        return customFieldId;
    }


    public void setCustomFieldId(String customFieldId) {
        this.customFieldId = customFieldId;
    }

    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CustomFieldDTO{" +
            "customFieldId='" + customFieldId + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
