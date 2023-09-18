package ng.com.justjava.corebanking.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.justjava.corebanking.web.rest.TestUtil;

public class InsuranceTypeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceTypeDTO.class);
        InsuranceTypeDTO insuranceTypeDTO1 = new InsuranceTypeDTO();
        insuranceTypeDTO1.setId(1L);
        InsuranceTypeDTO insuranceTypeDTO2 = new InsuranceTypeDTO();
        assertThat(insuranceTypeDTO1).isNotEqualTo(insuranceTypeDTO2);
        insuranceTypeDTO2.setId(insuranceTypeDTO1.getId());
        assertThat(insuranceTypeDTO1).isEqualTo(insuranceTypeDTO2);
        insuranceTypeDTO2.setId(2L);
        assertThat(insuranceTypeDTO1).isNotEqualTo(insuranceTypeDTO2);
        insuranceTypeDTO1.setId(null);
        assertThat(insuranceTypeDTO1).isNotEqualTo(insuranceTypeDTO2);
    }
}
