package ng.com.justjava.corebanking.service.dto;

public class UploadProfilePicture {

    private String photoFormat; //format of doc Jpg/pdf
    private String photo;  //encoded String

    public String getPhotoFormat() {
        return photoFormat;
    }

    public void setPhotoFormat(String photoFormat) {
        this.photoFormat = photoFormat;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "UploadProfilePicture{" +
            "photoFormat='" + photoFormat + '\'' +
            ", photo='" + photo + '\'' +
            '}';
    }
}
