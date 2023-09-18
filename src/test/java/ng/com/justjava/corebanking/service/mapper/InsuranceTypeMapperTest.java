package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(insuranceTypeMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(insuranceTypeMapper.fromId(null)).isNull();
    }
}
