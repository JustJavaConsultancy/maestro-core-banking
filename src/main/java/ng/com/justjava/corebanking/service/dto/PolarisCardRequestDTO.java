package ng.com.justjava.corebanking.service.dto;

public class PolarisCardRequestDTO {


    private FundDTO fund;
    private String department;
    private String faculty;
    private String matricno;
    private String scheme;
    private String image;
    private String cardtype;

    private String customerid;

    private String deliveryBranchCode;


    public String getDeliveryBranchCode() {
        return deliveryBranchCode;
    }

    public void setDeliveryBranchCode(String deliveryBranchCode) {
        this.deliveryBranchCode = deliveryBranchCode;
    }


/*    private PolarisCollectionAccountRequestDTO request;
    public PolarisCollectionAccountRequestDTO getRequest() {
        return request;
    }

    public void setRequest(PolarisCollectionAccountRequestDTO request) {
        this.request = request;
    }*/

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMatricno() {
        return matricno;
    }

    public void setMatricno(String matricno) {
        this.matricno = matricno;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public FundDTO getFund() {
        return fund;
    }

    public void setFund(FundDTO fund) {
        this.fund = fund;
    }

    @Override
    public String toString() {
        return "PolarisCardRequestDTO{" +
            /*"request=" + request +*/
            ", fund=" + fund +
            ", department='" + department + '\'' +
            ", faculty='" + faculty + '\'' +
            ", matricno='" + matricno + '\'' +
            ", scheme='" + scheme + '\'' +
            ", image='" + image + '\'' +
            ", cardtype='" + cardtype + '\'' +
            ", customerid='" + customerid + '\'' +
            ", deliveryBranchCode='" + deliveryBranchCode + '\'' +
            '}';
    }
}
