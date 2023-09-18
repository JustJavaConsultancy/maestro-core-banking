package ng.com.systemspecs.apigateway.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.systemspecs.apigateway.web.rest.TestUtil;

public class ApprovalGroupTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalGroup.class);
        ApprovalGroup approvalGroup1 = new ApprovalGroup();
        approvalGroup1.setId(1L);
        ApprovalGroup approvalGroup2 = new ApprovalGroup();
        approvalGroup2.setId(approvalGroup1.getId());
        assertThat(approvalGroup1).isEqualTo(approvalGroup2);
        approvalGroup2.setId(2L);
        assertThat(approvalGroup1).isNotEqualTo(approvalGroup2);
        approvalGroup1.setId(null);
        assertThat(approvalGroup1).isNotEqualTo(approvalGroup2);
    }
}
