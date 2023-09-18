package ng.com.systemspecs.apigateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KycRequestMapperTest {

    private KycRequestMapper kycRequestMapper;

    @BeforeEach
    public void setUp() {
        kycRequestMapper = new KycRequestMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(kycRequestMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(kycRequestMapper.fromId(null)).isNull();
    }
}
