package ng.com.systemspecs.apigateway.service.mapper;

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
        assertThat(vehicleInsuranceRequestMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(vehicleInsuranceRequestMapper.fromId(null)).isNull();
    }
}
