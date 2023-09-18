package ng.com.systemspecs.apigateway.domain;

import ng.com.systemspecs.apigateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InsuranceProviderTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceProvider.class);
        InsuranceProvider insuranceProvider1 = new InsuranceProvider();
        insuranceProvider1.setId(1L);
        InsuranceProvider insuranceProvider2 = new InsuranceProvider();
        insuranceProvider2.setId(insuranceProvider1.getId());
        assertThat(insuranceProvider1).isEqualTo(insuranceProvider2);
        insuranceProvider2.setId(2L);
        assertThat(insuranceProvider1).isNotEqualTo(insuranceProvider2);
        insuranceProvider1.setId(null);
        assertThat(insuranceProvider1).isNotEqualTo(insuranceProvider2);
    }
}
