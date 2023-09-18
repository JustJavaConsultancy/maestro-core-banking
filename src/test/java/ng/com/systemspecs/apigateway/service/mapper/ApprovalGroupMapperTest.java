package ng.com.systemspecs.apigateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ApprovalGroupMapperTest {

    private ApprovalGroupMapper approvalGroupMapper;

    @BeforeEach
    public void setUp() {
        approvalGroupMapper = new ApprovalGroupMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(approvalGroupMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(approvalGroupMapper.fromId(null)).isNull();
    }
}
