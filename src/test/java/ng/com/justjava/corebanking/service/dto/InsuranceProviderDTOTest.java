package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InsuranceProviderDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceProviderDTO.class);
        InsuranceProviderDTO insuranceProviderDTO1 = new InsuranceProviderDTO();
        insuranceProviderDTO1.setId(1L);
        InsuranceProviderDTO insuranceProviderDTO2 = new InsuranceProviderDTO();
        assertThat(insuranceProviderDTO1).isNotEqualTo(insuranceProviderDTO2);
        insuranceProviderDTO2.setId(insuranceProviderDTO1.getId());
        assertThat(insuranceProviderDTO1).isEqualTo(insuranceProviderDTO2);
        insuranceProviderDTO2.setId(2L);
        assertThat(insuranceProviderDTO1).isNotEqualTo(insuranceProviderDTO2);
        insuranceProviderDTO1.setId(null);
        assertThat(insuranceProviderDTO1).isNotEqualTo(insuranceProviderDTO2);
    }
}
