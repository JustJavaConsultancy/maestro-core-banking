package ng.com.systemspecs.apigateway.service.dto;

public class UploadCorporateDocument {
    private String photoFormat; //format of doc Jpg/pdf
    private String photo;  //encoded String
    private String docType; // UTILITY_BILL, CAC_CO7, CAC_CO2, CAC_CERTIFICATE

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

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    @Override
    public String toString() {
        return "UploadCorporateDocument{" +
            "photoFormat='" + photoFormat + '\'' +
            ", photo='" + photo + '\'' +
            ", docType='" + docType + '\'' +
            '}';
    }
}
