package ng.com.systemspecs.apigateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A WalletAccount.
 */
@Entity
@Table(name = "access_right")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AccessRight {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @JsonIgnore
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JsonIgnore
    @JsonIgnoreProperties(value = "accessRight", allowSetters = true)
    private Profile profile;

    @OneToMany(mappedBy = "accessRight")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AccessItem> accessItems  = new HashSet<>();

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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Set<AccessItem> getAccessItems() {
        return accessItems;
    }

    public void setAccessItems(Set<AccessItem> accessItems) {
        this.accessItems = accessItems;
    }

    @Override
    public String toString() {
        return "AccessRight{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", accessItems=" + accessItems +
            '}';
    }
}
