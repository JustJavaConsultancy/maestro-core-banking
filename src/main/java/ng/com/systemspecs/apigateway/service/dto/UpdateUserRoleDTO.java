package ng.com.systemspecs.apigateway.service.dto;

public class UpdateUserRoleDTO {

    private String phoneNumber;
    private String userRole;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "UpdateUserRoleDTO{" +
            "phoneNumber='" + phoneNumber + '\'' +
            ", userRole='" + userRole + '\'' +
            '}';
    }

}
