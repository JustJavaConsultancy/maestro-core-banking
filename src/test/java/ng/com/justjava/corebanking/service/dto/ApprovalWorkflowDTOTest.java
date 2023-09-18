package ng.com.justjava.corebanking.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.justjava.corebanking.web.rest.TestUtil;

public class ApprovalWorkflowDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalWorkflowDTO.class);
        ApprovalWorkflowDTO approvalWorkflowDTO1 = new ApprovalWorkflowDTO();
        approvalWorkflowDTO1.setId(1L);
        ApprovalWorkflowDTO approvalWorkflowDTO2 = new ApprovalWorkflowDTO();
        assertThat(approvalWorkflowDTO1).isNotEqualTo(approvalWorkflowDTO2);
        approvalWorkflowDTO2.setId(approvalWorkflowDTO1.getId());
        assertThat(approvalWorkflowDTO1).isEqualTo(approvalWorkflowDTO2);
        approvalWorkflowDTO2.setId(2L);
        assertThat(approvalWorkflowDTO1).isNotEqualTo(approvalWorkflowDTO2);
        approvalWorkflowDTO1.setId(null);
        assertThat(approvalWorkflowDTO1).isNotEqualTo(approvalWorkflowDTO2);
    }
}
