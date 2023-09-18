package ng.com.justjava.corebanking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "transaction_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TransactionLog extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "amount")
    private Double amount;

//    @Version
//    private Integer version;

    @Column(name = "specific_channel")
    private String specificChannel;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "channel")
    private String channel;

    @Column(name = "source_bank_code")
    private String sourceBankCode;

    @Column(name = "source_account_number")
    private String sourceAccountNumber;

    @Column(name = "source_account_name")
    private String sourceAccountName;

    @Column(name = "dest_bank_code")
    private String destBankCode;

    @Column(name = "trans_ref")
    private String transRef;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "narration")
    private String narration;

    @Column(name = "short_comment")
    private String shortComment;

    @Column(name = "is_wallet_account")
    private Boolean isWalletAccount;

    @Column(name = "is_to_be_saved")
    private Boolean isToBeSaved;

    @Column(name = "beneficiary_name")
    private String beneficiaryName;

    @Column(name = "rrr", length = 20000)
    private String rrr;

    @Column(name = "status", columnDefinition = "varchar(20) default 'START'")
    private String status;

    @Column(name = "agent_ref")
    private String agentRef;

    @Column(name = "redeem_bonus")
    private Boolean redeemBonus;

    @Column(name = "bonus_amount")
    private Double bonusAmount;

    @Column(name = "charges")
    private Double charges;

    @Column(name = "beneficiary_accounts", columnDefinition = "TEXT")
    private String beneficiaryAccounts;

    @Column(name = "is_bulk_transaction")
    private Boolean isBulkTrans = false;

    public Boolean isBulkTrans() {
        return isBulkTrans;
    }

    public void setBulkTrans(Boolean bulkTrans) {

        if (bulkTrans == null){
            this.isBulkTrans = false;
        }
        this.isBulkTrans = bulkTrans;
    }


    public String getBeneficiaryAccounts() {
        return beneficiaryAccounts;
    }

    public void setBeneficiaryAccounts(String beneficiaryAccounts) {
        this.beneficiaryAccounts = beneficiaryAccounts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSpecificChannel() {
        return specificChannel;
    }

    public void setSpecificChannel(String specificChannel) {
        this.specificChannel = specificChannel;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSourceBankCode() {
        return sourceBankCode;
    }

    public void setSourceBankCode(String sourceBankCode) {
        this.sourceBankCode = sourceBankCode;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getDestBankCode() {
        return destBankCode;
    }

    public void setDestBankCode(String destBankCode) {
        this.destBankCode = destBankCode;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getShortComment() {
        return shortComment;
    }

    public void setShortComment(String shortComment) {
        this.shortComment = shortComment;
    }

    public Boolean isWalletAccount() {
        return isWalletAccount;
    }

    public void setWalletAccount(Boolean walletAccount) {
        isWalletAccount = walletAccount;
    }

    public Boolean isToBeSaved() {
        return isToBeSaved;
    }

    public void setToBeSaved(Boolean toBeSaved) {
        isToBeSaved = toBeSaved;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getRrr() {
        return rrr;
    }

    public void setRrr(String rrr) {
        this.rrr = rrr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSourceAccountName() {
        return sourceAccountName;
    }

    public void setSourceAccountName(String sourceAccountName) {
        this.sourceAccountName = sourceAccountName;
    }

    public String getAgentRef() {
        return agentRef;
    }

    public void setAgentRef(String agentRef) {
        this.agentRef = agentRef;
    }

    public Boolean isRedeemBonus() {
        return redeemBonus;
    }

    public void setRedeemBonus(Boolean redeemBonus) {
        this.redeemBonus = redeemBonus;
    }

    public Boolean getWalletAccount() {
        return isWalletAccount;
    }

    public Boolean getToBeSaved() {
        return isToBeSaved;
    }

    public Boolean getRedeemBonus() {
        return redeemBonus;
    }

    public Double getCharges() {
        return charges;
    }

    public void setCharges(Double charges) {
        this.charges = charges;
    }

    public Double getBonusAmount() {
        if (bonusAmount == null) {
            return 0.0;
        }
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        if (bonusAmount == null) {
            this.bonusAmount = 0.0;
        }
        this.bonusAmount = bonusAmount;
    }

    @Override
    public String toString() {
        return "TransactionLog{" +
            "id=" + id +
            ", amount=" + amount +
            ", specificChannel='" + specificChannel + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", channel='" + channel + '\'' +
            ", sourceBankCode='" + sourceBankCode + '\'' +
            ", sourceAccountNumber='" + sourceAccountNumber + '\'' +
            ", sourceAccountName='" + sourceAccountName + '\'' +
            ", destBankCode='" + destBankCode + '\'' +
            ", transRef='" + transRef + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", narration='" + narration + '\'' +
            ", shortComment='" + shortComment + '\'' +
            ", isWalletAccount=" + isWalletAccount +
            ", isToBeSaved=" + isToBeSaved +
            ", beneficiaryName='" + beneficiaryName + '\'' +
            ", rrr='" + rrr + '\'' +
            ", status='" + status + '\'' +
            ", agentRef='" + agentRef + '\'' +
            ", redeemBonus=" + redeemBonus +
            ", bonusAmount=" + bonusAmount +
            ", charges=" + charges +
            ", beneficiaryAccounts='" + beneficiaryAccounts + '\'' +
            ", isBulkTrans=" + isBulkTrans +
            "} " + super.toString();
    }
}
