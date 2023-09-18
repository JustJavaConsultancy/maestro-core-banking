package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class CreateAccessRightDTO {

    private String accessRightName;
    private String profilePhoneNumber;
    private List<AccessItemDTO> accessItem;

    public String getAccessRightName() {
        return accessRightName;
    }

    public void setAccessRightName(String accessRightName) {
        this.accessRightName = accessRightName;
    }

    public String getProfilePhoneNumber() {
        return profilePhoneNumber;
    }

    public void setProfilePhoneNumber(String profilePhoneNumber) {
        this.profilePhoneNumber = profilePhoneNumber;
    }

    public List<AccessItemDTO> getAccessItem() {
        return accessItem;
    }

    public void setAccessItem(List<AccessItemDTO> accessItem) {
        this.accessItem = accessItem;
    }

    @Override
    public String toString() {
        return "AccessRightDTO{" +
            ", accessRightName='" + accessRightName + '\'' +
            ", profilePhoneNumber='" + profilePhoneNumber + '\'' +
            ", accessRight=" + accessItem +
            '}';
    }
}
