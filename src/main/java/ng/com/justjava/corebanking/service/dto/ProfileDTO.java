package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.User;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * A DTO for the {@link Profile} entity.
 */
@ApiModel(description = "Profile of the user of the Solution this can be of any of the types :- Customer, Agent or Admin")
public class ProfileDTO implements Serializable {

    private Long id;

    private String profileID;

    private String phoneNumber;

    private String gender;

    private LocalDate dateOfBirth;

    private User user;

    @JsonIgnore
    private Instant createdDate;

    private String address;

    private String fullName;
    private String nin;

    private String profilePicture;

    @Lob
    private byte[] photo;

    private String photoContentType;
    private String bvn;

    private String validID;

    private Long userId;

    private String userLogin;

    private Long profileTypeId;

    private Long kycId;

    private Integer kycLevel;

    private String schemeID;

    private boolean activated;

    private String status;

    @JsonIgnore
    private String secretQuestion;

    @JsonIgnore
    private String secretAnswer;

    private double totalBonus;

    private List<AddressDTO> addresses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getValidID() {
        return validID;
    }

    public void setValidID(String validID) {
        this.validID = validID;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getProfileTypeId() {
        return profileTypeId;
    }

    public void setProfileTypeId(Long profileTypeId) {
        this.profileTypeId = profileTypeId;
    }

    public Long getKycId() {
        return kycId;
    }

    public void setKycId(Long kyclevelId) {
        this.kycId = kyclevelId;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public Integer getKycLevel() {
        return kycLevel;
    }

    public void setKycLevel(Integer kycLevel) {
        this.kycLevel = kycLevel;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getDateCreated() {
        if (createdDate != null) {
            return LocalDateTime.ofInstant(createdDate, ZoneId.systemDefault());
        }
        return null;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileDTO)) {
            return false;
        }

        return id != null && id.equals(((ProfileDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    public String getSchemeID() {
        return schemeID;
    }

    public void setSchemeID(String schemeID) {
        this.schemeID = schemeID;
    }

    public String getFullAddress() {

        if (!StringUtils.isEmpty(address)) {
            return address;
        } else if (addresses != null && !addresses.isEmpty()) {
            return addresses.get(0).getAddress();
        }
        return "";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }

    public double getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(double totalBonus) {
        this.totalBonus = totalBonus;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
            "id=" + id +
            ", profileID='" + profileID + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", gender='" + gender + '\'' +
            ", dateOfBirth=" + dateOfBirth +
            ", user=" + user +
            ", createdDate=" + createdDate +
            ", address='" + address + '\'' +
            ", photoContentType='" + photoContentType + '\'' +
            ", bvn='" + bvn + '\'' +
            ", validID='" + validID + '\'' +
            ", userId=" + userId +
            ", userLogin='" + userLogin + '\'' +
            ", profileTypeId=" + profileTypeId +
            ", kycId=" + kycId +
            ", schemeID='" + schemeID + '\'' +
            ", secretQuestion='" + secretQuestion + '\'' +
            ", secretAnswer='" + secretAnswer + '\'' +
            ", totalBonus=" + totalBonus +
            ", addresses=" + addresses +
            ", fullName=" + fullName +
            ", nin=" + nin +
            ", profilePicture=" + profilePicture +
            ", status=" + status +
            ", activated=" + activated +
            '}';
    }
}
