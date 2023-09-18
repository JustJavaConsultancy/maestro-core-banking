package ng.com.systemspecs.apigateway.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.systemspecs.apigateway.web.rest.TestUtil;

public class VehicleInsuranceRequestDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleInsuranceRequestDTO.class);
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO1 = new VehicleInsuranceRequestDTO();
        vehicleInsuranceRequestDTO1.setId(1L);
        VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO2 = new VehicleInsuranceRequestDTO();
        assertThat(vehicleInsuranceRequestDTO1).isNotEqualTo(vehicleInsuranceRequestDTO2);
        vehicleInsuranceRequestDTO2.setId(vehicleInsuranceRequestDTO1.getId());
        assertThat(vehicleInsuranceRequestDTO1).isEqualTo(vehicleInsuranceRequestDTO2);
        vehicleInsuranceRequestDTO2.setId(2L);
        assertThat(vehicleInsuranceRequestDTO1).isNotEqualTo(vehicleInsuranceRequestDTO2);
        vehicleInsuranceRequestDTO1.setId(null);
        assertThat(vehicleInsuranceRequestDTO1).isNotEqualTo(vehicleInsuranceRequestDTO2);
    }
}
