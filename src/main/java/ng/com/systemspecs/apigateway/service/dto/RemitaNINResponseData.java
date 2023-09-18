package ng.com.systemspecs.apigateway.service.dto; ;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "firstname",
    "response_code",
    "birthdate",
    "gender",
    "signature",
    "pmiddlename",
    "title",
    "response_msg",
    "pfirstname",
    "nin",
    "employmentstatus",
    "telephoneno",
    "surname",
    "birthlga",
    "email",
    "trackingId",
    "educationallevel",
    "profession",
    "birthcountry",
    "nok_address2",
    "nok_firstname",
    "nok_address1",
    "nok_postalcode",
    "residencestatus",
    "photo",
    "middlename",
    "residence_address",
    "self_origin_state",
    "residence_state",
    "religion",
    "nok_state",
    "ospokenlang",
    "nok_middlename",
    "nok_surname",
    "self_origin_place",
    "self_origin_lga",
    "maritalstatus",
    "heigth",
    "birthstate",
    "nok_lga",
    "psurname",
    "nok_town",
    "residence_lga",
    "spoken_language",
    "residence_town",
    "status"
})
@Generated("jsonschema2pojo")
public class RemitaNINResponseData {

    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("response_code")
    private String response_code;
    @JsonProperty("birthdate")
    private String birthdate;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("signature")
    private String signature;
    @JsonProperty("pmiddlename")
    private String pmiddlename;
    @JsonProperty("title")
    private String title;
    @JsonProperty("response_msg")
    private String response_msg;
    @JsonProperty("pfirstname")
    private String pfirstname;
    @JsonProperty("nin")
    private String nin;
    @JsonProperty("employmentstatus")
    private String employmentstatus;
    @JsonProperty("telephoneno")
    private String telephoneno;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("birthlga")
    private String birthlga;
    @JsonProperty("email")
    private String email;
    @JsonProperty("trackingId")
    private String trackingId;
    @JsonProperty("educationallevel")
    private String educationallevel;
    @JsonProperty("profession")
    private String profession;
    @JsonProperty("birthcountry")
    private String birthcountry;
    @JsonProperty("nok_address2")
    private String nok_address2;
    @JsonProperty("nok_firstname")
    private String nok_firstname;
    @JsonProperty("nok_address1")
    private String nok_address1;
    @JsonProperty("nok_postalcode")
    private String nok_postalcode;
    @JsonProperty("residencestatus")
    private String residencestatus;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("middlename")
    private String middlename;
    @JsonProperty("residence_address")
    private String residence_address;
    @JsonProperty("self_origin_state")
    private String self_origin_state;
    @JsonProperty("residence_state")
    private String residence_state;
    @JsonProperty("religion")
    private String religion;
    @JsonProperty("nok_state")
    private String nok_state;
    @JsonProperty("ospokenlang")
    private String ospokenlang;
    @JsonProperty("nok_middlename")
    private String nok_middlename;
    @JsonProperty("nok_surname")
    private String nok_surname;
    @JsonProperty("self_origin_place")
    private String self_origin_place;
    @JsonProperty("self_origin_lga")
    private String self_origin_lga;
    @JsonProperty("maritalstatus")
    private String maritalstatus;
    @JsonProperty("heigth")
    private Integer heigth;
    @JsonProperty("birthstate")
    private String birthstate;
    @JsonProperty("nok_lga")
    private String nok_lga;
    @JsonProperty("psurname")
    private String psurname;
    @JsonProperty("nok_town")
    private String nok_town;
    @JsonProperty("residence_lga")
    private String residence_lga;
    @JsonProperty("spoken_language")
    private String spoken_language;
    @JsonProperty("residence_town")
    private String residence_town;
    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("firstname")
    public String getFirstname() {
        return firstname;
    }

    @JsonProperty("firstname")
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @JsonProperty("response_code")
    public String getResponse_code() {
        return response_code;
    }

    @JsonProperty("response_code")
    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    @JsonProperty("birthdate")
    public String getBirthdate() {
        return birthdate;
    }

    @JsonProperty("birthdate")
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("signature")
    public String getSignature() {
        return signature;
    }

    @JsonProperty("signature")
    public void setSignature(String signature) {
        this.signature = signature;
    }

    @JsonProperty("pmiddlename")
    public String getPmiddlename() {
        return pmiddlename;
    }

    @JsonProperty("pmiddlename")
    public void setPmiddlename(String pmiddlename) {
        this.pmiddlename = pmiddlename;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("response_msg")
    public String getResponse_msg() {
        return response_msg;
    }

    @JsonProperty("response_msg")
    public void setResponse_msg(String response_msg) {
        this.response_msg = response_msg;
    }

    @JsonProperty("pfirstname")
    public String getPfirstname() {
        return pfirstname;
    }

    @JsonProperty("pfirstname")
    public void setPfirstname(String pfirstname) {
        this.pfirstname = pfirstname;
    }

    @JsonProperty("nin")
    public String getNin() {
        return nin;
    }

    @JsonProperty("nin")
    public void setNin(String nin) {
        this.nin = nin;
    }

    @JsonProperty("employmentstatus")
    public String getEmploymentstatus() {
        return employmentstatus;
    }

    @JsonProperty("employmentstatus")
    public void setEmploymentstatus(String employmentstatus) {
        this.employmentstatus = employmentstatus;
    }

    @JsonProperty("telephoneno")
    public String getTelephoneno() {
        return telephoneno;
    }

    @JsonProperty("telephoneno")
    public void setTelephoneno(String telephoneno) {
        this.telephoneno = telephoneno;
    }

    @JsonProperty("surname")
    public String getSurname() {
        return surname;
    }

    @JsonProperty("surname")
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @JsonProperty("birthlga")
    public String getBirthlga() {
        return birthlga;
    }

    @JsonProperty("birthlga")
    public void setBirthlga(String birthlga) {
        this.birthlga = birthlga;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("trackingId")
    public String getTrackingId() {
        return trackingId;
    }

    @JsonProperty("trackingId")
    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    @JsonProperty("educationallevel")
    public String getEducationallevel() {
        return educationallevel;
    }

    @JsonProperty("educationallevel")
    public void setEducationallevel(String educationallevel) {
        this.educationallevel = educationallevel;
    }

    @JsonProperty("profession")
    public String getProfession() {
        return profession;
    }

    @JsonProperty("profession")
    public void setProfession(String profession) {
        this.profession = profession;
    }

    @JsonProperty("birthcountry")
    public String getBirthcountry() {
        return birthcountry;
    }

    @JsonProperty("birthcountry")
    public void setBirthcountry(String birthcountry) {
        this.birthcountry = birthcountry;
    }

    @JsonProperty("nok_address2")
    public String getNok_address2() {
        return nok_address2;
    }

    @JsonProperty("nok_address2")
    public void setNok_address2(String nok_address2) {
        this.nok_address2 = nok_address2;
    }

    @JsonProperty("nok_firstname")
    public String getNok_firstname() {
        return nok_firstname;
    }

    @JsonProperty("nok_firstname")
    public void setNok_firstname(String nok_firstname) {
        this.nok_firstname = nok_firstname;
    }

    @JsonProperty("nok_address1")
    public String getNok_address1() {
        return nok_address1;
    }

    @JsonProperty("nok_address1")
    public void setNok_address1(String nok_address1) {
        this.nok_address1 = nok_address1;
    }

    @JsonProperty("nok_postalcode")
    public String getNok_postalcode() {
        return nok_postalcode;
    }

    @JsonProperty("nok_postalcode")
    public void setNok_postalcode(String nok_postalcode) {
        this.nok_postalcode = nok_postalcode;
    }

    @JsonProperty("residencestatus")
    public String getResidencestatus() {
        return residencestatus;
    }

    @JsonProperty("residencestatus")
    public void setResidencestatus(String residencestatus) {
        this.residencestatus = residencestatus;
    }

    @JsonProperty("photo")
    public String getPhoto() {
        return photo;
    }

    @JsonProperty("photo")
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @JsonProperty("middlename")
    public String getMiddlename() {
        return middlename;
    }

    @JsonProperty("middlename")
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    @JsonProperty("residence_address")
    public String getResidence_address() {
        return residence_address;
    }

    @JsonProperty("residence_address")
    public void setResidence_address(String residence_address) {
        this.residence_address = residence_address;
    }

    @JsonProperty("self_origin_state")
    public String getSelf_origin_state() {
        return self_origin_state;
    }

    @JsonProperty("self_origin_state")
    public void setSelf_origin_state(String self_origin_state) {
        this.self_origin_state = self_origin_state;
    }

    @JsonProperty("residence_state")
    public String getResidence_state() {
        return residence_state;
    }

    @JsonProperty("residence_state")
    public void setResidence_state(String residence_state) {
        this.residence_state = residence_state;
    }

    @JsonProperty("religion")
    public String getReligion() {
        return religion;
    }

    @JsonProperty("religion")
    public void setReligion(String religion) {
        this.religion = religion;
    }

    @JsonProperty("nok_state")
    public String getNok_state() {
        return nok_state;
    }

    @JsonProperty("nok_state")
    public void setNok_state(String nok_state) {
        this.nok_state = nok_state;
    }

    @JsonProperty("ospokenlang")
    public String getOspokenlang() {
        return ospokenlang;
    }

    @JsonProperty("ospokenlang")
    public void setOspokenlang(String ospokenlang) {
        this.ospokenlang = ospokenlang;
    }

    @JsonProperty("nok_middlename")
    public String getNok_middlename() {
        return nok_middlename;
    }

    @JsonProperty("nok_middlename")
    public void setNok_middlename(String nok_middlename) {
        this.nok_middlename = nok_middlename;
    }

    @JsonProperty("nok_surname")
    public String getNok_surname() {
        return nok_surname;
    }

    @JsonProperty("nok_surname")
    public void setNok_surname(String nok_surname) {
        this.nok_surname = nok_surname;
    }

    @JsonProperty("self_origin_place")
    public String getSelf_origin_place() {
        return self_origin_place;
    }

    @JsonProperty("self_origin_place")
    public void setSelf_origin_place(String self_origin_place) {
        this.self_origin_place = self_origin_place;
    }

    @JsonProperty("self_origin_lga")
    public String getSelf_origin_lga() {
        return self_origin_lga;
    }

    @JsonProperty("self_origin_lga")
    public void setSelf_origin_lga(String self_origin_lga) {
        this.self_origin_lga = self_origin_lga;
    }

    @JsonProperty("maritalstatus")
    public String getMaritalstatus() {
        return maritalstatus;
    }

    @JsonProperty("maritalstatus")
    public void setMaritalstatus(String maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    @JsonProperty("heigth")
    public Integer getHeigth() {
        return heigth;
    }

    @JsonProperty("heigth")
    public void setHeigth(Integer heigth) {
        this.heigth = heigth;
    }

    @JsonProperty("birthstate")
    public String getBirthstate() {
        return birthstate;
    }

    @JsonProperty("birthstate")
    public void setBirthstate(String birthstate) {
        this.birthstate = birthstate;
    }

    @JsonProperty("nok_lga")
    public String getNok_lga() {
        return nok_lga;
    }

    @JsonProperty("nok_lga")
    public void setNok_lga(String nok_lga) {
        this.nok_lga = nok_lga;
    }

    @JsonProperty("psurname")
    public String getPsurname() {
        return psurname;
    }

    @JsonProperty("psurname")
    public void setPsurname(String psurname) {
        this.psurname = psurname;
    }

    @JsonProperty("nok_town")
    public String getNok_town() {
        return nok_town;
    }

    @JsonProperty("nok_town")
    public void setNok_town(String nok_town) {
        this.nok_town = nok_town;
    }

    @JsonProperty("residence_lga")
    public String getResidence_lga() {
        return residence_lga;
    }

    @JsonProperty("residence_lga")
    public void setResidence_lga(String residence_lga) {
        this.residence_lga = residence_lga;
    }

    @JsonProperty("spoken_language")
    public String getSpoken_language() {
        return spoken_language;
    }

    @JsonProperty("spoken_language")
    public void setSpoken_language(String spoken_language) {
        this.spoken_language = spoken_language;
    }

    @JsonProperty("residence_town")
    public String getResidence_town() {
        return residence_town;
    }

    @JsonProperty("residence_town")
    public void setResidence_town(String residence_town) {
        this.residence_town = residence_town;
    }

    @JsonProperty("status")
    public Boolean getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Data{" +
            "firstname='" + firstname + '\'' +
            ", response_code='" + response_code + '\'' +
            ", birthdate='" + birthdate + '\'' +
            ", gender='" + gender + '\'' +
            ", signature='" + signature + '\'' +
            ", pmiddlename='" + pmiddlename + '\'' +
            ", title='" + title + '\'' +
            ", response_msg='" + response_msg + '\'' +
            ", pfirstname='" + pfirstname + '\'' +
            ", nin='" + nin + '\'' +
            ", employmentstatus='" + employmentstatus + '\'' +
            ", telephoneno='" + telephoneno + '\'' +
            ", surname='" + surname + '\'' +
            ", birthlga='" + birthlga + '\'' +
            ", email='" + email + '\'' +
            ", trackingId='" + trackingId + '\'' +
            ", educationallevel='" + educationallevel + '\'' +
            ", profession='" + profession + '\'' +
            ", birthcountry='" + birthcountry + '\'' +
            ", nok_address2='" + nok_address2 + '\'' +
            ", nok_firstname='" + nok_firstname + '\'' +
            ", nok_address1='" + nok_address1 + '\'' +
            ", nok_postalcode='" + nok_postalcode + '\'' +
            ", residencestatus='" + residencestatus + '\'' +
            ", photo='" + photo + '\'' +
            ", middlename='" + middlename + '\'' +
            ", residence_address='" + residence_address + '\'' +
            ", self_origin_state='" + self_origin_state + '\'' +
            ", residence_state='" + residence_state + '\'' +
            ", religion='" + religion + '\'' +
            ", nok_state='" + nok_state + '\'' +
            ", ospokenlang='" + ospokenlang + '\'' +
            ", nok_middlename='" + nok_middlename + '\'' +
            ", nok_surname='" + nok_surname + '\'' +
            ", self_origin_place='" + self_origin_place + '\'' +
            ", self_origin_lga='" + self_origin_lga + '\'' +
            ", maritalstatus='" + maritalstatus + '\'' +
            ", heigth=" + heigth +
            ", birthstate='" + birthstate + '\'' +
            ", nok_lga='" + nok_lga + '\'' +
            ", psurname='" + psurname + '\'' +
            ", nok_town='" + nok_town + '\'' +
            ", residence_lga='" + residence_lga + '\'' +
            ", spoken_language='" + spoken_language + '\'' +
            ", residence_town='" + residence_town + '\'' +
            ", status=" + status +
            '}';
    }
}
