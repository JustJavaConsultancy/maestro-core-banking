package ng.com.justjava.corebanking.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.justjava.corebanking.web.rest.TestUtil;

public class ApprovalWorkflowTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalWorkflow.class);
        ApprovalWorkflow approvalWorkflow1 = new ApprovalWorkflow();
        approvalWorkflow1.setId(1L);
        ApprovalWorkflow approvalWorkflow2 = new ApprovalWorkflow();
        approvalWorkflow2.setId(approvalWorkflow1.getId());
        assertThat(approvalWorkflow1).isEqualTo(approvalWorkflow2);
        approvalWorkflow2.setId(2L);
        assertThat(approvalWorkflow1).isNotEqualTo(approvalWorkflow2);
        approvalWorkflow1.setId(null);
        assertThat(approvalWorkflow1).isNotEqualTo(approvalWorkflow2);
    }
}
