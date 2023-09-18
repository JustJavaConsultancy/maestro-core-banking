package ng.com.systemspecs.apigateway.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ApprovalGroup.
 */
@Entity
@Table(name = "approval_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApprovalGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "approvalGroup", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Profile> profiles = new HashSet<>();


    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ApprovalGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public ApprovalGroup profiles(Set<Profile> profiles) {
        this.profiles = profiles;
        return this;
    }

    public ApprovalGroup addProfile(Profile profile) {
        this.profiles.add(profile);
        profile.setApprovalGroup(this);
        return this;
    }

    public ApprovalGroup removeProfile(Profile profile) {
        this.profiles.remove(profile);
        profile.setApprovalGroup(null);
        return this;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovalGroup)) {
            return false;
        }
        return id != null && id.equals(((ApprovalGroup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovalGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
