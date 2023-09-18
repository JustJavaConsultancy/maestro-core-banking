package ng.com.systemspecs.apigateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "beneficiary")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Beneficiary {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "is_wallet")
    private boolean isWallet;

    @ManyToOne
    @JsonIgnoreProperties(value = "beneficiaries", allowSetters = true)
    private Profile accountOwner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public boolean isWallet() {
        return isWallet;
    }

    public void setWallet(boolean wallet) {
        isWallet = wallet;
    }

    public Profile getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(Profile accountOwner) {
        this.accountOwner = accountOwner;
    }

    @Override
    public String toString() {
        return "Beneficiary{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", bankCode='" + bankCode + '\'' +
            ", bankNam='" + bankName + '\'' +
            ", isWallet=" + isWallet +
            ", accountOwner=" + accountOwner +
            '}';
    }
}
