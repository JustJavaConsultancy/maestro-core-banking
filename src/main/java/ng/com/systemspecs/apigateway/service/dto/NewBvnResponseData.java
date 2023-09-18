package ng.com.systemspecs.apigateway.service.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewBvnResponseData {

    @SerializedName("bvn")
    @Expose
    private String bvn;
    @SerializedName("phonenumber")
    @Expose
    private String phonenumber;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("othernames")
    @Expose
    private String othernames;
    @SerializedName("dob")
    @Expose
    private String dob;

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOthernames() {
        return othernames;
    }

    public void setOthernames(String othernames) {
        this.othernames = othernames;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "NewBvnResponseData{" +
            "bvn='" + bvn + '\'' +
            ", phonenumber='" + phonenumber + '\'' +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", othernames='" + othernames + '\'' +
            ", dob='" + dob + '\'' +
            '}';
    }
}
