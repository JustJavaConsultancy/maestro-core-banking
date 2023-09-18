package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.domain.ApprovalWorkflow;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link ApprovalWorkflow} entity.
 */
public class ApprovalWorkflowDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;


    private Long initiatorId;

    private String initiatorName;

    private Long approverId;

    private String approverName;

    private Long transactionTypeId;

    private String transactionTypeName;

    private String transactionTypeCode;

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

    public Long getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(Long approvalGroupId) {
        this.initiatorId = approvalGroupId;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String approvalGroupName) {
        this.initiatorName = approvalGroupName;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approvalGroupId) {
        this.approverId = approvalGroupId;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approvalGroupName) {
        this.approverName = approvalGroupName;
    }

    public Long getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Long rightId) {
        this.transactionTypeId = rightId;
    }

    public String getTransactionTypeName() {
        return transactionTypeName;
    }

    public void setTransactionTypeName(String rightName) {
        this.transactionTypeName = rightName;
    }

    public String getTransactionTypeCode() {
        return transactionTypeCode;
    }

    public void setTransactionTypeCode(String transactionTypeCode) {
        this.transactionTypeCode = transactionTypeCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovalWorkflowDTO)) {
            return false;
        }

        return id != null && id.equals(((ApprovalWorkflowDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "ApprovalWorkflowDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", initiatorId=" + initiatorId +
            ", initiatorName='" + initiatorName + '\'' +
            ", approverId=" + approverId +
            ", approverName='" + approverName + '\'' +
            ", transactionTypeId=" + transactionTypeId +
            ", transactionTypeName='" + transactionTypeName + '\'' +
            ", transactionTypeCode='" + transactionTypeCode + '\'' +
            '}';
    }
}
