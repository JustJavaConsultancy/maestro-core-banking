package ng.com.systemspecs.apigateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ng.com.systemspecs.apigateway.domain.enumeration.AccountStatus;
import ng.com.systemspecs.apigateway.util.StringEncryptionConverter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A WalletAccount.
 */

@Entity
@Table(name = "wallet_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WalletAccount extends AbstractDateAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Version
    private Integer version;


    @Column(name = "account_name")
    @Convert(converter = StringEncryptionConverter.class)
    private String accountName;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Column(name = "account_number")
    @Convert(converter = StringEncryptionConverter.class)
    private String accountNumber;

    @Column(name = "current_balance")
    @Convert(converter = StringEncryptionConverter.class)
    private String currentBalance;

    @Column(name = "nuban_account_no")
    @Convert(converter = StringEncryptionConverter.class)
    private String nubanAccountNo;

    @Column(name = "tracking_ref")
    private String trackingRef;

    @Column(name = "date_opened")
    private LocalDate dateOpened;

    @Column(name = "status", columnDefinition = "varchar(20) default ACTIVE")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "actual_balance", columnDefinition = "varchar(20) default HPvehMn1z2mbh3I/fDtskA==")
    @Convert(converter = StringEncryptionConverter.class)
    private String actualBalance;

    @Column(name = "wallet_limit", columnDefinition = "varchar(20) default HPvehMn1z2mbh3I/fDtskA==")
    @Convert(converter = StringEncryptionConverter.class)
    private String walletLimit;

    @ManyToOne
    @JsonIgnoreProperties(value = "walletAccounts", allowSetters = true)
    private Scheme scheme;

    @ManyToOne
    @JsonIgnoreProperties(value = "walletAccounts", allowSetters = true)
    private WalletAccountType walletAccountType;

    @ManyToOne
    @JsonIgnoreProperties(value = "walletAccounts", allowSetters = true)
    private Profile accountOwner;

    @ManyToOne
    @JsonIgnoreProperties(value = "subWallets", allowSetters = true)
    private WalletAccount parent;

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<WalletAccount> subWallets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public WalletAccount accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public WalletAccount currentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
        return this;
    }

    public WalletAccount actualBalance(String actualBalance) {

        this.actualBalance = actualBalance;
        return this;
    }

    public LocalDate getDateOpened() {
        return dateOpened;
    }

    public WalletAccount dateOpened(LocalDate dateOpened) {
        this.dateOpened = dateOpened;
        return this;
    }

    public void setDateOpened(LocalDate dateOpened) {
        this.dateOpened = dateOpened;
    }

    public String getNubanAccountNo() {
        return nubanAccountNo;
    }

    public void setNubanAccountNo(String nubanAccountNo) {
        this.nubanAccountNo = nubanAccountNo;
    }

    public String getTrackingRef() {
        return trackingRef;
    }

    public void setTrackingRef(String trackingRef) {
        this.trackingRef = trackingRef;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public WalletAccount scheme(Scheme scheme) {
        this.scheme = scheme;
        return this;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public WalletAccountType getWalletAccountType() {
        return walletAccountType;
    }

    public WalletAccount walletAccountType(WalletAccountType walletAccountType) {
        this.walletAccountType = walletAccountType;
        return this;
    }

    public void setWalletAccountType(WalletAccountType walletAccountType) {
        this.walletAccountType = walletAccountType;
    }

    public Profile getAccountOwner() {
        return accountOwner;
    }

    public WalletAccount accountOwner(Profile profile) {
        this.accountOwner = profile;
        return this;
    }

    public void setAccountOwner(Profile profile) {
        this.accountOwner = profile;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getActualBalance() {
        return actualBalance;
    }

    public void setActualBalance(String actualBalance) {
            this.actualBalance = actualBalance;
    }

    public Set<WalletAccount> getSubWallets() {
        return subWallets;
    }

    public void setSubWallets(Set<WalletAccount> subWallets) {
        this.subWallets = subWallets;
    }

    public WalletAccount getParent() {
        return parent;
    }

    public void setParent(WalletAccount parent) {
        this.parent = parent;
    }

    public String getWalletLimit() {
        if (StringUtils.isEmpty(walletLimit)) {
            walletLimit = "0";
        }
        return walletLimit;
    }

    public void setWalletLimit(String limit) {
        if (StringUtils.isEmpty(limit)) {
            limit = "0";
        }
        this.walletLimit = limit;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof WalletAccount)) {
            return false;
        }

        return id != null && id.equals(((WalletAccount) o).id);
    }


    public String getAccountFullName() {
        return accountOwner != null ? accountOwner.getFullName() : accountName;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "WalletAccount{" +
            "id=" + id +
            ", accountName='" + accountName + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", currentBalance='" + currentBalance + '\'' +
            ", dateOpened=" + dateOpened +
            ", nubanAccountNo=" + nubanAccountNo +
            ", trackingRef=" + trackingRef +
            ", dateOpened=" + dateOpened +
            ", scheme=" + scheme +
            ", wal]letAccountType=" + walletAccountType +
            ", accountOwner=" + accountOwner +
            ", status=" + status +
            ", actualBalance=" + actualBalance +
            ", walletLimit=" + walletLimit +
            '}';
    }
}
