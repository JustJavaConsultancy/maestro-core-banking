package ng.com.systemspecs.apigateway.service.dto;

import java.util.List;

public class AgentDTO {

    private Long id;

    private String location;

    private double latitude;

    private double longitude;

    private String fullName;

    private String phoneNumber;

    private String email;

    private String superAgentPhoneNumber;

    private String superAgentFullName;

    private String superAgentEmail;

    private String agentType;

    private String cACDoc;

    private String c07Doc;

    private String c01Doc;

    private String c02Doc;

    private String firstName;

    private String lastName;

    private String createdDate;

    private List<WalletAccountDTO> walletAccounts;

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

    public String getSuperAgentPhoneNumber() {
        return superAgentPhoneNumber;
    }

    public void setSuperAgentPhoneNumber(String superAgentPhoneNumber) {
        this.superAgentPhoneNumber = superAgentPhoneNumber;
    }

    public String getSuperAgentEmail() {
        return superAgentEmail;
    }

    public void setSuperAgentEmail(String superAgentEmail) {
        this.superAgentEmail = superAgentEmail;
    }

    public String getSuperAgentFullName() {
        return superAgentFullName;
    }

    public void setSuperAgentFullName(String superAgentFullName) {
        this.superAgentFullName = superAgentFullName;
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

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getcACDoc() {
        return cACDoc;
    }

    public void setcACDoc(String cACDoc) {
        this.cACDoc = cACDoc;
    }

    public String getC07Doc() {
        return c07Doc;
    }

    public void setC07Doc(String c07Doc) {
        this.c07Doc = c07Doc;
    }

    public String getC01Doc() {
        return c01Doc;
    }

    public void setC01Doc(String c01Doc) {
        this.c01Doc = c01Doc;
    }

    public String getC02Doc() {
        return c02Doc;
    }

    public void setC02Doc(String c02Doc) {
        this.c02Doc = c02Doc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<WalletAccountDTO> getWalletAccounts() {
        return walletAccounts;
    }

    public void setWalletAccounts(List<WalletAccountDTO> walletAccounts) {
        this.walletAccounts = walletAccounts;
    }

    @Override
    public String toString() {
        return "AgentDTO{" +
            "id=" + id +
            ", location='" + location + '\'' +
            ", latitude='" + latitude + '\'' +
            ", longitude='" + longitude + '\'' +
            ", fullName='" + fullName + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", superAgentPhoneNumber='" + getSuperAgentPhoneNumber() + '\'' +
            ", superAgentFullName='" + getSuperAgentFullName() + '\'' +
            ", superAgentEmail='" + getSuperAgentEmail() + '\'' +
            ", agentType='" + getAgentType() + '\'' +
            ", email='" + email + '\'' +
            '}';
    }
}
