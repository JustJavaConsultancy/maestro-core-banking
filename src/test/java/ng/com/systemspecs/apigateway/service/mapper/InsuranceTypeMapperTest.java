package ng.com.systemspecs.apigateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class InsuranceTypeMapperTest {

    private InsuranceTypeMapper insuranceTypeMapper;

    @BeforeEach
    public void setUp() {
        insuranceTypeMapper = new InsuranceTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(insuranceTypeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(insuranceTypeMapper.fromId(null)).isNull();
    }
}
