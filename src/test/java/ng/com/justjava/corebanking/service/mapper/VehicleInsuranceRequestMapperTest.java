package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class VehicleInsuranceRequestMapperTest {

    private VehicleInsuranceRequestMapper vehicleInsuranceRequestMapper;

    @BeforeEach
    public void setUp() {
        vehicleInsuranceRequestMapper = new VehicleInsuranceRequestMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(vehicleInsuranceRequestMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(vehicleInsuranceRequestMapper.fromId(null)).isNull();
    }
}
