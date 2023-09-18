package ng.com.justjava.corebanking.service.dto;

public class CorporateDocuments {
    String docType;
    String encodedString;
    String format;

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getEncodedString() {
        return encodedString;
    }

    public void setEncodedString(String encodedString) {
        this.encodedString = encodedString;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
