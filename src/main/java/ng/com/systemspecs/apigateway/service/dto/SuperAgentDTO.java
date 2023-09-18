package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.enumeration.AgentStatus;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link ng.com.systemspecs.apigateway.domain.SuperAgent} entity.
 */
public class SuperAgentDTO implements Serializable {

    private Long id;

    @NotNull
    private AgentStatus status;


    private Long agentId;

    private Long schemeId;

    private String schemeSchemeID;

    private String firstName;

    private String lastName;

    private String createdDate;

    private List<WalletAccountDTO> walletAccounts;

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

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(Long schemeId) {
        this.schemeId = schemeId;
    }

    public String getSchemeSchemeID() {
        return schemeSchemeID;
    }

    public void setSchemeSchemeID(String schemeSchemeID) {
        this.schemeSchemeID = schemeSchemeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<WalletAccountDTO> getWalletAccounts() {
        return walletAccounts;
    }

    public void setWalletAccounts(List<WalletAccountDTO> walletAccounts) {
        this.walletAccounts = walletAccounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuperAgentDTO)) {
            return false;
        }

        return id != null && id.equals(((SuperAgentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuperAgentDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", agentId=" + getAgentId() +
            ", schemeId=" + getSchemeId() +
            ", schemeSchemeID='" + getSchemeSchemeID() + "'" +
            ", firstname='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", schemeSchemeID='" + getSchemeSchemeID() + "'" +
            "}";
    }
}
