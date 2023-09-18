package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(approvalGroupMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(approvalGroupMapper.fromId(null)).isNull();
    }
}
