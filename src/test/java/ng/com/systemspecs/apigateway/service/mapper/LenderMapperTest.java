package ng.com.systemspecs.apigateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LenderMapperTest {

    private LenderMapper lenderMapper;

    @BeforeEach
    public void setUp() {
        lenderMapper = new LenderMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(lenderMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(lenderMapper.fromId(null)).isNull();
    }
}
