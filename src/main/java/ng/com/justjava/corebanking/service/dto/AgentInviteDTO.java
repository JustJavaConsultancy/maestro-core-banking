package ng.com.justjava.corebanking.service.dto;

import java.util.List;

public class AgentInviteDTO {

    private String superAgentPhoneNumber;
    private List<InviteeDTO> invitees;

    public String getSuperAgentPhoneNumber() {
        return superAgentPhoneNumber;
    }

    public void setSuperAgentPhoneNumber(String superAgentPhoneNumber) {
        this.superAgentPhoneNumber = superAgentPhoneNumber;
    }

    public List<InviteeDTO> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<InviteeDTO> invitees) {
        this.invitees = invitees;
    }

    @Override
    public String toString() {
        return "AgentInviteDTO{" +
            "superAgentPhoneNumber='" + superAgentPhoneNumber + '\'' +
            ", invitees=" + invitees +
            '}';
    }
}
