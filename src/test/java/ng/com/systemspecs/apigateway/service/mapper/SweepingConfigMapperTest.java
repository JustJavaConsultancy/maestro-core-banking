package ng.com.systemspecs.apigateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SweepingConfigMapperTest {

    private SweepingConfigMapper sweepingConfigMapper;

    @BeforeEach
    public void setUp() {
        sweepingConfigMapper = new SweepingConfigMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(sweepingConfigMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(sweepingConfigMapper.fromId(null)).isNull();
    }
}
