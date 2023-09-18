package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ng.com.systemspecs.apigateway.domain.enumeration.TransactionStatus;
import ng.com.systemspecs.apigateway.util.NotEqualAccountNumber;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
//@JsonIgnoreProperties(value = {"specificChannel"})
@NotEqualAccountNumber
public class FundDTO implements Serializable {

    private long id;
    private String specificChannel;
    private String accountNumber;
    private Double amount;
    private String channel;
    private String sourceBankCode;
    private String sourceAccountNumber;
    private String sourceAccountName;
    private String destBankCode;
    private String pin;
    private String transRef;
    @Size(min = 11, max = 15, message = "phone number must be greater than 10 and less than 15 characters")
    private String phoneNumber;

    private String narration;

    private String shortComment;

    private boolean isWalletAccount;

    private boolean isToBeSaved;

    private String beneficiaryName;

    private String rrr;

    private TransactionStatus status;

    private Instant createdDate;

    private String agentRef;

    private boolean redeemBonus;

    private Double bonusAmount;

    private Double charges;

    private List<BulkBeneficiaryDTO> bulkAccountNos;
    private boolean isMultipleCredit;
    private boolean isBulkTrans = false;

    public List<BulkBeneficiaryDTO> getBulkAccountNos() {
        return bulkAccountNos;
    }

    public void setBulkAccountNos(List<BulkBeneficiaryDTO> bulkAccountNos) {
        this.bulkAccountNos = bulkAccountNos;
    }

    public boolean isMultipleCredit() {
        return isMultipleCredit;
    }

    public void setMultipleCredit(boolean multipleCredit) {
        isMultipleCredit = multipleCredit;
    }

    public boolean isBulkTrans() {
        return isBulkTrans;
    }

    public void setBulkTrans(boolean bulkTrans) {
        isBulkTrans = bulkTrans;
    }

    public Double getBonusAmount() {
        if (bonusAmount == null) {
            return 0.0;
        }
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        if (bonusAmount == null) {
            bonusAmount = 0.0;
        }
        this.bonusAmount = bonusAmount;
    }

    public boolean isRedeemBonus() {
        return redeemBonus;
    }

    public void setRedeemBonus(boolean redeemBonus) {
        this.redeemBonus = redeemBonus;
    }

    public String getSpecificChannel() {
        return specificChannel;
    }

    public void setSpecificChannel(String specificChannel) {
        this.specificChannel = specificChannel;
    }

    public Double getAmount() {
        return Double.valueOf(formatMoney(amount));
    }

    public void setAmount(Double amount) {
        if (amount == null) {
            amount = 0.0;
        }

        this.amount = amount;
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef.toUpperCase();
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

    public boolean isWalletAccount() {
        return isWalletAccount;
    }


    public void setWalletAccount(boolean walletAccount) {
        isWalletAccount = walletAccount;
    }

    public boolean isToBeSaved() {
        return isToBeSaved;
    }

    public void setToBeSaved(boolean toBeSaved) {
        isToBeSaved = toBeSaved;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getShortComment() {
        return shortComment;
    }

    public void setShortComment(String shortComment) {
        this.shortComment = shortComment;
    }

    public String getRrr() {
        return rrr;
    }

    public void setRrr(String rrr) {
        this.rrr = rrr;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSourceAccountName() {
        return sourceAccountName;
    }

    public void setSourceAccountName(String sourceAccountName) {
        this.sourceAccountName = sourceAccountName;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getAgentRef() {
        return agentRef;
    }

    public void setAgentRef(String agentRef) {
        this.agentRef = agentRef;
    }

    public Double getCharges() {
        if (charges == null) {
            return 0.0;
        }
        return charges;
    }

    public void setCharges(Double charges) {
        if (charges == null) {
            charges = 0.0;
        }
        this.charges = charges;
    }

    public String getTransactionDescription() {
        if ("BankToWallet".equalsIgnoreCase(channel)) {
            return "Wallet funding";
        } else if ("WalletToWallet".equalsIgnoreCase(channel)) {
            if (specificChannel != null) {
                if (specificChannel.toLowerCase().contains("disco")) {
                    return "Electricity purchase";
                } else if (specificChannel.toLowerCase().contains("vtu")) {
                    return "Airtime purchase";
                } else if (specificChannel.toLowerCase().contains("data")) {
                    return "Data Subscription";
                } else if (specificChannel.toLowerCase().contains("rrr")) {
                    return "RRR purchase";
                } else if (specificChannel.toLowerCase().contains("liberty")) {
                    return "Loan purchase";
                } else if (specificChannel.toLowerCase().contains("ibile")) {
                    return "Ibile Pay";
                } else if (specificChannel.toLowerCase().contains("cable")) {
                    return "Cable TV subscription";
                } else if (specificChannel.toLowerCase().contains("startimes")) {
                    return "Cable TV subscription";
                } else if (specificChannel.toLowerCase().contains("internet")) {
                    return "Internet subscription";
                } else if (specificChannel.toLowerCase().contains("insurance")) {
                    return "Insurance purchase";
                } else if (specificChannel.toLowerCase().contains("payFees")) {
                    return rrr+" - School Fees Payment";
                } else {
                    return "Wallet to Wallet Transfer";
                }
            }
        } else if ("WalletToBank".equalsIgnoreCase(channel)) {

            if (rrr != null && rrr.length() == 11) {
                return "STP Transfer";
            }

            return "NIP Transfer";

        }
        else if ("WalletToBanks".equalsIgnoreCase(channel)) {

            if (rrr != null && rrr.length() == 11) {
                return "Bulk STP Transfer";
            }

            return "Bulk NIP Transfer";

        }
        else if ("WalletToWallets".equalsIgnoreCase(channel)) {
            return "Bulk Wallet to Wallet Transfers";
        }

        return "Invalid transaction";

    }

    @Override
    public String toString() {
        return "FundDTO{" +
            "id=" + id +
            ", specificChannel='" + specificChannel + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", amount=" + amount +
            ", channel='" + channel + '\'' +
            ", sourceBankCode='" + sourceBankCode + '\'' +
            ", sourceAccountNumber='" + sourceAccountNumber + '\'' +
            ", sourceAccountName='" + sourceAccountName + '\'' +
            ", destBankCode='" + destBankCode + '\'' +
            ", pin='" + pin + '\'' +
            ", transRef='" + transRef + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", narration='" + narration + '\'' +
            ", shortComment='" + shortComment + '\'' +
            ", isWalletAccount=" + isWalletAccount +
            ", isToBeSaved=" + isToBeSaved +
            ", beneficiaryName='" + beneficiaryName + '\'' +
            ", rrr='" + rrr + '\'' +
            ", status=" + status +
            ", createdDate=" + createdDate +
            ", agentRef='" + agentRef + '\'' +
            ", redeemBonus=" + redeemBonus +
            ", bonusAmount=" + bonusAmount +
            ", charges=" + charges +
            ", bulkAccountNos=" + bulkAccountNos +
            ", isMultipleCredit=" + isMultipleCredit +
            ", isBulkTrans=" + isBulkTrans +
            '}';
    }

    public static String formatMoney(Double amount) {

        if (amount == null) {
            return null;
        }

        return String.format("%.2f", amount);

    }

}
