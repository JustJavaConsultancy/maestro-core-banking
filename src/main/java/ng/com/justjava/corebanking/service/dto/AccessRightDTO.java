package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class AccessRightDTO {

    @JsonIgnore
    private Long id;

    private String accessRightName;

    private String profileName;

    private List<AccessRightItemDTO> accessItems;

    public String getAccessRightName() {
        return accessRightName;
    }

    public void setAccessRightName(String accessRightName) {
        this.accessRightName = accessRightName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public List<AccessRightItemDTO> getAccessItems() {
        return accessItems;
    }

    public void setAccessItems(List<AccessRightItemDTO> accessItems) {
        this.accessItems = accessItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AccessRightDTO{" +
            "id='" + id + '\'' +
            "accessRightName='" + accessRightName + '\'' +
            ", profileName='" + profileName + '\'' +
            ", accessItems=" + accessItems +
            '}';
    }
}
