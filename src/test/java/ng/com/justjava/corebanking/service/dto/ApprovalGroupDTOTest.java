package ng.com.justjava.corebanking.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.justjava.corebanking.web.rest.TestUtil;

public class ApprovalGroupDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovalGroupDTO.class);
        ApprovalGroupDTO approvalGroupDTO1 = new ApprovalGroupDTO();
        approvalGroupDTO1.setId(1L);
        ApprovalGroupDTO approvalGroupDTO2 = new ApprovalGroupDTO();
        assertThat(approvalGroupDTO1).isNotEqualTo(approvalGroupDTO2);
        approvalGroupDTO2.setId(approvalGroupDTO1.getId());
        assertThat(approvalGroupDTO1).isEqualTo(approvalGroupDTO2);
        approvalGroupDTO2.setId(2L);
        assertThat(approvalGroupDTO1).isNotEqualTo(approvalGroupDTO2);
        approvalGroupDTO1.setId(null);
        assertThat(approvalGroupDTO1).isNotEqualTo(approvalGroupDTO2);
    }
}
