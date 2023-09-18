package ng.com.systemspecs.apigateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ng.com.systemspecs.apigateway.domain.enumeration.AgentStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A SuperAgent.
 */
@Entity
@Table(name = "super_agent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SuperAgent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AgentStatus status;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Agent agent;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "superAgents", allowSetters = true)
    private Scheme scheme;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AgentStatus getStatus() {
        return status;
    }

    public void setStatus(AgentStatus status) {
        this.status = status;
    }

    public SuperAgent status(AgentStatus status) {
        this.status = status;
        return this;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public SuperAgent agent(Agent agent) {
        this.agent = agent;
        return this;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public SuperAgent scheme(Scheme scheme) {
        this.scheme = scheme;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuperAgent)) {
            return false;
        }
        return id != null && id.equals(((SuperAgent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuperAgent{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
