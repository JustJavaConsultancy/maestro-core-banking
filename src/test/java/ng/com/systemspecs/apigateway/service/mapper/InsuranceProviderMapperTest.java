package ng.com.systemspecs.apigateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InsuranceProviderMapperTest {

    private InsuranceProviderMapper insuranceProviderMapper;

    @BeforeEach
    public void setUp() {
        insuranceProviderMapper = new InsuranceProviderMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(insuranceProviderMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(insuranceProviderMapper.fromId(null)).isNull();
    }
}
