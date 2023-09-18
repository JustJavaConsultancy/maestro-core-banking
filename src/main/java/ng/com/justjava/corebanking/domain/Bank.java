package ng.com.justjava.corebanking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "bank")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bank {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "bank_accronym")
    private String bankAccronym;

    @Column(name = "bank_code")
    private String bankCode;

    @NotNull
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "type")
    private String type;

    @Column(name = "bank_short_code")
    private String shortCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankAccronym() {
        return bankAccronym;
    }

    public void setBankAccronym(String bankAccronym) {
        this.bankAccronym = bankAccronym;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    @Override
    public String toString() {
        return "Bank{" +
            "id=" + id +
            ", bankAccronym='" + bankAccronym + '\'' +
            ", bankCode='" + bankCode + '\'' +
            ", bankName='" + bankName + '\'' +
            ", type='" + type + '\'' +
            ", shortCode='" + shortCode + '\'' +
            '}';
    }


}
