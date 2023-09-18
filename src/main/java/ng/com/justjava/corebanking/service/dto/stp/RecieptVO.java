package ng.com.justjava.corebanking.service.dto.stp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class RecieptVO implements Serializable {

    private static final long serialVersionUID = 2005636006583691436L;
    private Long id;
    private Long userId;
    private String status;
    private String paymentDate;
    private String requestedStop;
    private String parentcheckrrrdigit;
    private String applyVat;
    private String paymentStatus;
    private String paymentReferenceType;
    /**
     *
     */
    private Long collectorId;
    private String billerAddress;
    private String billerPhone;
    private String billerTaxNumber;
    private String billerEmail;
    private String benBankId;
    private String paymentType;
    private String p2p;
    private String showFee;
    private String address;
    private String currenySymbol;
    private String qrCode;
    private String rrrNumber;
    private String companyName;
    private String companyId;
    private String genCompanyId;
    private String genCompanyName;
    @JsonProperty
    @JsonSerialize(using = MyDoubleDesirializer.class)
    private Double amount;
    @JsonProperty
    @JsonSerialize(using = MyDoubleDesirializer.class)
    private Double vatAmount;
    private String payerBankId;
    private Integer maxUploadLimit;
    private String payerMobile;
    private String payerEmail;
    private String payerBank;
    private String payerAccountNumber;
    private String beneficiaryName;
    private String beneficiaryMobile;
    private String beneficiaryEmail;
    private String beneficiaryBank;
    private String beneficiaryAccountNumber;
    private Date startDate;
    private Date endDate;
    private Date dueDate;
    private String frequency;
    @JsonProperty
    @JsonSerialize(using = MyDoubleDesirializer.class)
    private Double chargeFee;
    private String serviceType;
    @JsonProperty
    @JsonSerialize(using = MyDoubleDesirializer.class)
    private Double netAmount;
    private String payerName;
    private String descriptionOfPayment;
    private Long tellerUserId;
    private String tellerName;
    private boolean wrongTeller;
    private String createdDate;
    private Long serviceTypeId;
    private String billerId;
    private String profileType;
    private String rrrType;
    private boolean bulk = false;
    private boolean vtu;
    private List<Map<String, Object>> customFieldData = new ArrayList<>();
    private String currency;
    private boolean confirmationEnabled = false;
    private String benCompanyId;
    private List<Map<String, String>> extraData = new ArrayList<>();
    private String paymentMode;
    private String paymentChannel;
    private String billerPaymentNotificationUrl;
    private String billerPaymentNotificationEmail;
    private String tranDate;
    private String branchCode;
    private String bankId;
    private String acceptPartPayment;
    @JsonProperty
    @JsonSerialize(using = MyDoubleDesirializer.class)
    private Double amountDue;
    private Long recievingAccountId;
    private Long beneficiaryId;
    private String tellerNumber;
    @JsonProperty
    @JsonSerialize(using = MyDoubleDesirializer.class)
    private Double originalAmount;
    private Long transactionRef;
    @JsonProperty
    @JsonSerialize(using = MyDoubleDesirializer.class)
    private Double convenienceFee;
    private String thirdPartyReceiptUrl;
    private String receiptTitle;
    private String activationRef;
    @JsonProperty
    @JsonSerialize(using = MyDoubleDesirializer.class)
    private Double activationAmount;
    private String activationNaration;
    // CARD//ACCOUNT
    private String activationType;
    private Double partAmountPaid = 0.00d;
    private String paymentTypeInfo;
    private Boolean isBuyOnCredit;
    private String notificationRequestBodyType;
    private List<Map<String, Object>> lineItems = new ArrayList<>();

    public String getNotificationRequestBodyType() {
        return notificationRequestBodyType;
    }

    public void setNotificationRequestBodyType(String notificationRequestBodyType) {
        this.notificationRequestBodyType = notificationRequestBodyType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getRequestedStop() {
        return requestedStop;
    }

    public void setRequestedStop(String requestedStop) {
        this.requestedStop = requestedStop;
    }

    public String getParentcheckrrrdigit() {
        return parentcheckrrrdigit;
    }

    public void setParentcheckrrrdigit(String parentcheckrrrdigit) {
        this.parentcheckrrrdigit = parentcheckrrrdigit;
    }

    public String getApplyVat() {
        return applyVat;
    }

    public void setApplyVat(String applyVat) {
        this.applyVat = applyVat;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentReferenceType() {
        return paymentReferenceType;
    }

    public void setPaymentReferenceType(String paymentReferenceType) {
        this.paymentReferenceType = paymentReferenceType;
    }

    public Long getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(Long collectorId) {
        this.collectorId = collectorId;
    }

    public String getBillerAddress() {
        return billerAddress;
    }

    public void setBillerAddress(String billerAddress) {
        this.billerAddress = billerAddress;
    }

    public String getBillerPhone() {
        return billerPhone;
    }

    public void setBillerPhone(String billerPhone) {
        this.billerPhone = billerPhone;
    }

    public String getBillerTaxNumber() {
        return billerTaxNumber;
    }

    public void setBillerTaxNumber(String billerTaxNumber) {
        this.billerTaxNumber = billerTaxNumber;
    }

    public String getBillerEmail() {
        return billerEmail;
    }

    public void setBillerEmail(String billerEmail) {
        this.billerEmail = billerEmail;
    }

    public String getBenBankId() {
        return benBankId;
    }

    public void setBenBankId(String benBankId) {
        this.benBankId = benBankId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getP2p() {
        return p2p;
    }

    public void setP2p(String p2p) {
        this.p2p = p2p;
    }

    public String getShowFee() {
        return showFee;
    }

    public void setShowFee(String showFee) {
        this.showFee = showFee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrenySymbol() {
        return currenySymbol;
    }

    public void setCurrenySymbol(String currenySymbol) {
        this.currenySymbol = currenySymbol;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getRrrNumber() {
        return rrrNumber;
    }

    public void setRrrNumber(String rrrNumber) {
        this.rrrNumber = rrrNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getGenCompanyId() {
        return genCompanyId;
    }

    public void setGenCompanyId(String genCompanyId) {
        this.genCompanyId = genCompanyId;
    }

    public String getGenCompanyName() {
        return genCompanyName;
    }

    public void setGenCompanyName(String genCompanyName) {
        this.genCompanyName = genCompanyName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(Double vatAmount) {
        if (vatAmount == null) {
            vatAmount = 0d;
        }
        this.vatAmount = vatAmount;
    }

    public String getPayerBankId() {
        return payerBankId;
    }

    public void setPayerBankId(String payerBankId) {
        this.payerBankId = payerBankId;
    }

    public Integer getMaxUploadLimit() {
        return maxUploadLimit;
    }

    public void setMaxUploadLimit(Integer maxUploadLimit) {
        this.maxUploadLimit = maxUploadLimit;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getPayerBank() {
        return payerBank;
    }

    public void setPayerBank(String payerBank) {
        this.payerBank = payerBank;
    }

    public String getPayerAccountNumber() {
        return payerAccountNumber;
    }

    public void setPayerAccountNumber(String payerAccountNumber) {
        this.payerAccountNumber = payerAccountNumber;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryMobile() {
        return beneficiaryMobile;
    }

    public void setBeneficiaryMobile(String beneficiaryMobile) {
        this.beneficiaryMobile = beneficiaryMobile;
    }

    public String getBeneficiaryEmail() {
        return beneficiaryEmail;
    }

    public void setBeneficiaryEmail(String beneficiaryEmail) {
        this.beneficiaryEmail = beneficiaryEmail;
    }

    public String getBeneficiaryBank() {
        return beneficiaryBank;
    }

    public void setBeneficiaryBank(String beneficiaryBank) {
        this.beneficiaryBank = beneficiaryBank;
    }

    public String getBeneficiaryAccountNumber() {
        return beneficiaryAccountNumber;
    }

    public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Double getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(Double chargeFee) {
        if (chargeFee == null) {
            chargeFee = 0d;
        }
        this.chargeFee = chargeFee;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getDescriptionOfPayment() {
        return descriptionOfPayment;
    }

    public void setDescriptionOfPayment(String descriptionOfPayment) {
        this.descriptionOfPayment = descriptionOfPayment;
    }

    public Long getTellerUserId() {
        return tellerUserId;
    }

    public void setTellerUserId(Long tellerUserId) {
        this.tellerUserId = tellerUserId;
    }

    public String getTellerName() {
        return tellerName;
    }

    public void setTellerName(String tellerName) {
        this.tellerName = tellerName;
    }

    public boolean isWrongTeller() {
        return wrongTeller;
    }

    public void setWrongTeller(boolean wrongTeller) {
        this.wrongTeller = wrongTeller;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getBillerId() {
        return billerId;
    }

    public void setBillerId(String billerId) {
        this.billerId = billerId;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getRrrType() {
        return rrrType;
    }

    public void setRrrType(String rrrType) {
        this.rrrType = rrrType;
    }

    public boolean isBulk() {
        return bulk;
    }

    public void setBulk(boolean bulk) {
        this.bulk = bulk;
    }

    public boolean isVtu() {
        return vtu;
    }

    public void setVtu(boolean vtu) {
        this.vtu = vtu;
    }

    public List<Map<String, Object>> getCustomFieldData() {
        return customFieldData;
    }

    public void setCustomFieldData(List<Map<String, Object>> customFieldData) {
        this.customFieldData = customFieldData;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isConfirmationEnabled() {
        return confirmationEnabled;
    }

    public void setConfirmationEnabled(boolean confirmationEnabled) {
        this.confirmationEnabled = confirmationEnabled;
    }

    public String getBenCompanyId() {
        return benCompanyId;
    }

    public void setBenCompanyId(String benCompanyId) {
        this.benCompanyId = benCompanyId;
    }

    public List<Map<String, String>> getExtraData() {
        return extraData;
    }

    public void setExtraData(List<Map<String, String>> extraData) {
        this.extraData = extraData;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getBillerPaymentNotificationUrl() {
        return billerPaymentNotificationUrl;
    }

    public void setBillerPaymentNotificationUrl(String billerPaymentNotificationUrl) {
        this.billerPaymentNotificationUrl = billerPaymentNotificationUrl;
    }

    public String getBillerPaymentNotificationEmail() {
        return billerPaymentNotificationEmail;
    }

    public void setBillerPaymentNotificationEmail(String billerPaymentNotificationEmail) {
        this.billerPaymentNotificationEmail = billerPaymentNotificationEmail;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getAcceptPartPayment() {
        return acceptPartPayment;
    }

    public void setAcceptPartPayment(String acceptPartPayment) {
        this.acceptPartPayment = acceptPartPayment;
    }

    public Double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(Double amountDue) {
        this.amountDue = amountDue;
    }

    public Long getRecievingAccountId() {
        return recievingAccountId;
    }

    public void setRecievingAccountId(Long recievingAccountId) {
        this.recievingAccountId = recievingAccountId;
    }

    public Long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(Long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getTellerNumber() {
        return tellerNumber;
    }

    public void setTellerNumber(String tellerNumber) {
        this.tellerNumber = tellerNumber;
    }

    public Double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(Double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public Long getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(Long transactionRef) {
        this.transactionRef = transactionRef;
    }

    public Double getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(Double convenienceFee) {
        if (convenienceFee == null) {
            convenienceFee = 0d;
        }
        this.convenienceFee = convenienceFee;
    }

    public String getThirdPartyReceiptUrl() {
        return thirdPartyReceiptUrl;
    }

    public void setThirdPartyReceiptUrl(String thirdPartyReceiptUrl) {
        this.thirdPartyReceiptUrl = thirdPartyReceiptUrl;
    }

    public String getReceiptTitle() {
        return receiptTitle;
    }

    public void setReceiptTitle(String receiptTitle) {
        this.receiptTitle = receiptTitle;
    }

    public String getActivationRef() {
        return activationRef;
    }

    public void setActivationRef(String activationRef) {
        this.activationRef = activationRef;
    }

    public Double getActivationAmount() {
        return activationAmount;
    }

    public void setActivationAmount(Double activationAmount) {
        this.activationAmount = activationAmount;
    }

    public String getActivationNaration() {
        return activationNaration;
    }

    public void setActivationNaration(String activationNaration) {
        this.activationNaration = activationNaration;
    }

    public String getActivationType() {
        return activationType;
    }

    public void setActivationType(String activationType) {
        this.activationType = activationType;
    }

    public Double getPartAmountPaid() {
        return partAmountPaid;
    }

    public void setPartAmountPaid(Double partAmountPaid) {
        this.partAmountPaid = partAmountPaid;
    }

    public String getPaymentTypeInfo() {
        return paymentTypeInfo;
    }

    public void setPaymentTypeInfo(String paymentTypeInfo) {
        this.paymentTypeInfo = paymentTypeInfo;
    }

    public Boolean getBuyOnCredit() {
        return isBuyOnCredit;
    }

    public void setBuyOnCredit(Boolean buyOnCredit) {
        isBuyOnCredit = buyOnCredit;
    }

    public List<Map<String, Object>> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<Map<String, Object>> lineItems) {
        this.lineItems = lineItems;
    }

    @Override
    public String toString() {
        return "RecieptVO{" +
            "id=" + id +
            ", userId=" + userId +
            ", status='" + status + '\'' +
            ", paymentDate='" + paymentDate + '\'' +
            ", requestedStop='" + requestedStop + '\'' +
            ", parentcheckrrrdigit='" + parentcheckrrrdigit + '\'' +
            ", applyVat='" + applyVat + '\'' +
            ", paymentStatus='" + paymentStatus + '\'' +
            ", paymentReferenceType='" + paymentReferenceType + '\'' +
            ", collectorId=" + collectorId +
            ", billerAddress='" + billerAddress + '\'' +
            ", billerPhone='" + billerPhone + '\'' +
            ", billerTaxNumber='" + billerTaxNumber + '\'' +
            ", billerEmail='" + billerEmail + '\'' +
            ", benBankId='" + benBankId + '\'' +
            ", paymentType='" + paymentType + '\'' +
            ", p2p='" + p2p + '\'' +
            ", showFee='" + showFee + '\'' +
            ", address='" + address + '\'' +
            ", currenySymbol='" + currenySymbol + '\'' +
            ", qrCode='" + qrCode + '\'' +
            ", rrrNumber='" + rrrNumber + '\'' +
            ", companyName='" + companyName + '\'' +
            ", companyId='" + companyId + '\'' +
            ", genCompanyId='" + genCompanyId + '\'' +
            ", genCompanyName='" + genCompanyName + '\'' +
            ", amount=" + amount +
            ", vatAmount=" + vatAmount +
            ", payerBankId='" + payerBankId + '\'' +
            ", maxUploadLimit=" + maxUploadLimit +
            ", payerMobile='" + payerMobile + '\'' +
            ", payerEmail='" + payerEmail + '\'' +
            ", payerBank='" + payerBank + '\'' +
            ", payerAccountNumber='" + payerAccountNumber + '\'' +
            ", beneficiaryName='" + beneficiaryName + '\'' +
            ", beneficiaryMobile='" + beneficiaryMobile + '\'' +
            ", beneficiaryEmail='" + beneficiaryEmail + '\'' +
            ", beneficiaryBank='" + beneficiaryBank + '\'' +
            ", beneficiaryAccountNumber='" + beneficiaryAccountNumber + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", dueDate=" + dueDate +
            ", frequency='" + frequency + '\'' +
            ", chargeFee=" + chargeFee +
            ", serviceType='" + serviceType + '\'' +
            ", netAmount=" + netAmount +
            ", payerName='" + payerName + '\'' +
            ", descriptionOfPayment='" + descriptionOfPayment + '\'' +
            ", tellerUserId=" + tellerUserId +
            ", tellerName='" + tellerName + '\'' +
            ", wrongTeller=" + wrongTeller +
            ", createdDate='" + createdDate + '\'' +
            ", serviceTypeId=" + serviceTypeId +
            ", billerId='" + billerId + '\'' +
            ", profileType='" + profileType + '\'' +
            ", rrrType='" + rrrType + '\'' +
            ", bulk=" + bulk +
            ", vtu=" + vtu +
            ", customFieldData=" + customFieldData +
            ", currency='" + currency + '\'' +
            ", confirmationEnabled=" + confirmationEnabled +
            ", benCompanyId='" + benCompanyId + '\'' +
            ", extraData=" + extraData +
            ", paymentMode='" + paymentMode + '\'' +
            ", paymentChannel='" + paymentChannel + '\'' +
            ", billerPaymentNotificationUrl='" + billerPaymentNotificationUrl + '\'' +
            ", billerPaymentNotificationEmail='" + billerPaymentNotificationEmail + '\'' +
            ", tranDate='" + tranDate + '\'' +
            ", branchCode='" + branchCode + '\'' +
            ", bankId='" + bankId + '\'' +
            ", acceptPartPayment='" + acceptPartPayment + '\'' +
            ", amountDue=" + amountDue +
            ", recievingAccountId=" + recievingAccountId +
            ", beneficiaryId=" + beneficiaryId +
            ", tellerNumber='" + tellerNumber + '\'' +
            ", originalAmount=" + originalAmount +
            ", transactionRef=" + transactionRef +
            ", convenienceFee=" + convenienceFee +
            ", thirdPartyReceiptUrl='" + thirdPartyReceiptUrl + '\'' +
            ", receiptTitle='" + receiptTitle + '\'' +
            ", activationRef='" + activationRef + '\'' +
            ", activationAmount=" + activationAmount +
            ", activationNaration='" + activationNaration + '\'' +
            ", activationType='" + activationType + '\'' +
            ", partAmountPaid=" + partAmountPaid +
            ", paymentTypeInfo='" + paymentTypeInfo + '\'' +
            ", isBuyOnCredit=" + isBuyOnCredit +
            ", notificationRequestBodyType='" + notificationRequestBodyType + '\'' +
            ", lineItems=" + lineItems +
            '}';
    }

    public static class MyDoubleDesirializer extends JsonSerializer<Double> {

        public MyDoubleDesirializer() {
            super();
        }


        @Override
        public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            BigDecimal d = new BigDecimal(value);
            gen.writeNumber(d.toPlainString());
        }
    }
}
