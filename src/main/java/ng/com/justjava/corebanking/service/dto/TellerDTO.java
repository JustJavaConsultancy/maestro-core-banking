package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ng.com.justjava.corebanking.domain.Agent;
import ng.com.justjava.corebanking.domain.Profile;

public class TellerDTO {

    private Long id;

    private String location;
    private double latitude;
    private double longitude;

    private String fullName;

    private String phoneNumber;

    @JsonIgnore
    private Profile profile;

    @JsonIgnore
    private Agent agent;

    private String agentPhoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getAgentPhoneNumber() {
        return agentPhoneNumber;
    }

    public void setAgentPhoneNumber(String agentPhoneNumber) {
        this.agentPhoneNumber = agentPhoneNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "TellerDTO{" +
            "id=" + id +
            ", location='" + location + '\'' +
            ", latitude='" + latitude + '\'' +
            ", longitude='" + longitude + '\'' +
            ", fullName='" + fullName + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", profile=" + profile +
            ", agent=" + agent +
            ", agentPhoneNumber='" + agentPhoneNumber + '\'' +
            '}';
    }
}
