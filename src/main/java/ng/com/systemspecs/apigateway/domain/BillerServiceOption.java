package ng.com.systemspecs.apigateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "biller_service_option")
@JsonIgnoreProperties(value = {"billerPlatform", "billerCustomFieldOption"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BillerServiceOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private long id;

    @Column(name = "service_option_id")
    private long serviceOptionId;

    @Column(name = "column_name")
    private String name;

    @JsonIgnore
    @Column(name = "column_type")
    private String type;

    @JsonIgnore
    @OneToMany(mappedBy = "billerServiceOption")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<CustomFieldDropdown> customFieldDropdowns = new HashSet<>();

    @JsonIgnore
    @Column(name = "column_length")
    private String length;

    @Column(name = "required")
    private boolean required;


    @ManyToOne
    @JsonIgnoreProperties(value = "billerServiceOptions", allowSetters = true)
    private BillerCustomFieldOption billerCustomFieldOption;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public BillerCustomFieldOption getBillerCustomFieldOption() {
        return billerCustomFieldOption;
    }

    public void setBillerCustomFieldOption(BillerCustomFieldOption billerCustomFieldOption) {
        this.billerCustomFieldOption = billerCustomFieldOption;
    }

    public Set<CustomFieldDropdown> getCustomFieldDropdowns() {
        return customFieldDropdowns;
    }

    public void setCustomFieldDropdowns(Set<CustomFieldDropdown> customFieldDropdowns) {
        this.customFieldDropdowns = customFieldDropdowns;
    }

    public long getServiceOptionId() {
        return serviceOptionId;
    }

    public void setServiceOptionId(long serviceOptionId) {
        this.serviceOptionId = serviceOptionId;
    }

    @Override
    public String toString() {
        return "BillerServiceOption{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", type='" + type + '\'' +
            ", length=" + length +
            ", required=" + required +
            '}';
    }

}
