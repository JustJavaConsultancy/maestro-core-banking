package ng.com.systemspecs.apigateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AppUpdateMapperTest {

    private AppUpdateMapper appUpdateMapper;

    @BeforeEach
    public void setUp() {
        appUpdateMapper = new AppUpdateMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(appUpdateMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(appUpdateMapper.fromId(null)).isNull();
    }
}
