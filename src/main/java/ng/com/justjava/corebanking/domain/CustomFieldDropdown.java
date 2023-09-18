package ng.com.justjava.corebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "field_drop_down")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomFieldDropdown {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "drop_down_id")
    private String fieldDropdownId;

    @Column(name = "fixed_price")
    private String fixedprice;

    @Column(name = "unit_price")
    private String unitprice;

    @Column(name = "code")
    private String code;

    @Column(name = "account_id")
    private String accountid;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = "billerServiceOptions", allowSetters = true)
    private BillerServiceOption billerServiceOption;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldDropdownId() {
        return fieldDropdownId;
    }

    public void setFieldDropdownId(String fieldDropdownId) {
        this.fieldDropdownId = fieldDropdownId;
    }

    public String getFixedprice() {
        return fixedprice;
    }

    public void setFixedprice(String fixedprice) {
        this.fixedprice = fixedprice;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BillerServiceOption getBillerServiceOption() {
        return billerServiceOption;
    }

    public void setBillerServiceOption(BillerServiceOption billerServiceOption) {
        this.billerServiceOption = billerServiceOption;
    }

    @Override
    public String toString() {
        return "CustomFieldDropdown{" +
            "id=" + id +
            ", fieldDropdownId='" + fieldDropdownId + '\'' +
            ", fixedprice='" + fixedprice + '\'' +
            ", unitprice='" + unitprice + '\'' +
            ", code='" + code + '\'' +
            ", accountid='" + accountid + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
