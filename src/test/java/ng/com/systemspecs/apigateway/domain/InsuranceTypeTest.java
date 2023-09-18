package ng.com.systemspecs.apigateway.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.systemspecs.apigateway.web.rest.TestUtil;

public class InsuranceTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceType.class);
        InsuranceType insuranceType1 = new InsuranceType();
        insuranceType1.setId(1L);
        InsuranceType insuranceType2 = new InsuranceType();
        insuranceType2.setId(insuranceType1.getId());
        assertThat(insuranceType1).isEqualTo(insuranceType2);
        insuranceType2.setId(2L);
        assertThat(insuranceType1).isNotEqualTo(insuranceType2);
        insuranceType1.setId(null);
        assertThat(insuranceType1).isNotEqualTo(insuranceType2);
    }
}
