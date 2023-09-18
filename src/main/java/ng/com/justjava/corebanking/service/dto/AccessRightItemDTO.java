package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AccessRightItemDTO {

    @JsonIgnore
    private long id;
    private String rightName;
    private boolean maker;

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public boolean isMaker() {
        return maker;
    }

    public void setMaker(boolean maker) {
        this.maker = maker;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AccessRightItemDTO{" +
            "rightName='" + rightName + '\'' +
            ", maker=" + maker +
            '}';
    }

}
