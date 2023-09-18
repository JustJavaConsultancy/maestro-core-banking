package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AccessItemDTO {

    @JsonIgnore
    private Long id;
    private String rightCode;
    private boolean maker;
    private String accessRightName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRightCode() {
        return rightCode;
    }

    public void setRightCode(String rightCode) {
        this.rightCode = rightCode;
    }

    public boolean isMaker() {
        return maker;
    }

    public void setMaker(boolean maker) {
        this.maker = maker;
    }

    public String getAccessRightName() {
        return accessRightName;
    }

    public void setAccessRightName(String accessRightName) {
        this.accessRightName = accessRightName;
    }

    @Override
    public String toString() {
        return "AccessItemDTO{" +
            "rightCode='" + rightCode + '\'' +
            ", maker=" + maker +
            ", accessRightName='" + accessRightName + '\'' +
            '}';
    }
}
