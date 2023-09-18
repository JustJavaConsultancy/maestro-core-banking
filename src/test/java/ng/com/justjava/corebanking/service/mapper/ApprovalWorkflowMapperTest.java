package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ApprovalWorkflowMapperTest {

    private ApprovalWorkflowMapper approvalWorkflowMapper;

    @BeforeEach
    public void setUp() {
        approvalWorkflowMapper = new ApprovalWorkflowMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(approvalWorkflowMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(approvalWorkflowMapper.fromId(null)).isNull();
    }
}
