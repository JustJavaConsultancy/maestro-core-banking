package ng.com.systemspecs.apigateway.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.systemspecs.apigateway.web.rest.TestUtil;

public class VehicleInsuranceRequestTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleInsuranceRequest.class);
        VehicleInsuranceRequest vehicleInsuranceRequest1 = new VehicleInsuranceRequest();
        vehicleInsuranceRequest1.setId(1L);
        VehicleInsuranceRequest vehicleInsuranceRequest2 = new VehicleInsuranceRequest();
        vehicleInsuranceRequest2.setId(vehicleInsuranceRequest1.getId());
        assertThat(vehicleInsuranceRequest1).isEqualTo(vehicleInsuranceRequest2);
        vehicleInsuranceRequest2.setId(2L);
        assertThat(vehicleInsuranceRequest1).isNotEqualTo(vehicleInsuranceRequest2);
        vehicleInsuranceRequest1.setId(null);
        assertThat(vehicleInsuranceRequest1).isNotEqualTo(vehicleInsuranceRequest2);
    }
}
