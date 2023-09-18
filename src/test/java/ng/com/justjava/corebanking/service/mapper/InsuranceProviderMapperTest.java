package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(insuranceProviderMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(insuranceProviderMapper.fromId(null)).isNull();
    }
}
