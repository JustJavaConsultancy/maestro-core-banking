package ng.com.systemspecs.apigateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * A Scheme.
 */
@Entity
@Table(name = "scheme")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Scheme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "scheme_id")
    private String schemeID;

    @Column(name = "scheme")
    private String scheme;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "callback_url")
    private String callBackUrl;

    @ManyToOne
    @JsonIgnoreProperties(value = "schemes", allowSetters = true)
    private SchemeCategory schemeCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchemeID() {
        return schemeID;
    }

    public void setSchemeID(String schemeID) {
        this.schemeID = schemeID;
    }

    public Scheme schemeID(String schemeID) {
        this.schemeID = schemeID;
        return this;
    }

    public String getScheme() {
        return scheme;
    }

    public Scheme scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public SchemeCategory getSchemeCategory() {
        return schemeCategory;
    }

    public Scheme schemeCategory(SchemeCategory schemeCategory) {
        this.schemeCategory = schemeCategory;
        return this;
    }

    public void setSchemeCategory(SchemeCategory schemeCategory) {
        this.schemeCategory = schemeCategory;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getCallbackUrl() {
        return callBackUrl;
    }

    public void setCallbackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Scheme)) {
            return false;
        }
        return id != null && id.equals(((Scheme) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Scheme{" +
            "id=" + id +
            ", schemeID='" + schemeID + '\'' +
            ", scheme='" + scheme + '\'' +
            ", bankCode='" + bankCode + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", apiKey='" + apiKey + '\'' +
            ", secretKey='" + secretKey + '\'' +
            ", callBackUrl='" + callBackUrl + '\'' +
            ", schemeCategory=" + schemeCategory +
            '}';
    }
}
