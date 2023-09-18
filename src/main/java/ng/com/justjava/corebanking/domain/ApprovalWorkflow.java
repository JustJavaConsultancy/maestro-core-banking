package ng.com.justjava.corebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ApprovalWorkflow.
 */
@Entity
@Table(name = "approval_workflow")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApprovalWorkflow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "approvalWorkflows", allowSetters = true)
    private ApprovalGroup initiator;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "approvalWorkflows", allowSetters = true)
    private ApprovalGroup approver;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Right transactionType;

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

    public ApprovalWorkflow name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApprovalGroup getInitiator() {
        return initiator;
    }

    public ApprovalWorkflow initiator(ApprovalGroup approvalGroup) {
        this.initiator = approvalGroup;
        return this;
    }

    public void setInitiator(ApprovalGroup approvalGroup) {
        this.initiator = approvalGroup;
    }

    public ApprovalGroup getApprover() {
        return approver;
    }

    public ApprovalWorkflow approver(ApprovalGroup approvalGroup) {
        this.approver = approvalGroup;
        return this;
    }

    public void setApprover(ApprovalGroup approvalGroup) {
        this.approver = approvalGroup;
    }

    public Right getTransactionType() {
        return transactionType;
    }

    public ApprovalWorkflow transactionType(Right right) {
        this.transactionType = right;
        return this;
    }

    public void setTransactionType(Right right) {
        this.transactionType = right;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovalWorkflow)) {
            return false;
        }
        return id != null && id.equals(((ApprovalWorkflow) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovalWorkflow{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
