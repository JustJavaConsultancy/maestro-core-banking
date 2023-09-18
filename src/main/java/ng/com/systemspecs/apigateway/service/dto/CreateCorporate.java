package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.CorporateProfile;
import ng.com.systemspecs.apigateway.domain.User;

public class CreateCorporate {
    public User user;
    public CorporateProfile corporateProfile;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CorporateProfile getCorporateProfile() {
        return corporateProfile;
    }

    public void setCorporateProfile(CorporateProfile corporateProfile) {
        this.corporateProfile = corporateProfile;
    }
}
