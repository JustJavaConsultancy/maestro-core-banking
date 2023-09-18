package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SweepingConfigDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SweepingConfigDTO.class);
        SweepingConfigDTO sweepingConfigDTO1 = new SweepingConfigDTO();
        sweepingConfigDTO1.setId(1L);
        SweepingConfigDTO sweepingConfigDTO2 = new SweepingConfigDTO();
        assertThat(sweepingConfigDTO1).isNotEqualTo(sweepingConfigDTO2);
        sweepingConfigDTO2.setId(sweepingConfigDTO1.getId());
        assertThat(sweepingConfigDTO1).isEqualTo(sweepingConfigDTO2);
        sweepingConfigDTO2.setId(2L);
        assertThat(sweepingConfigDTO1).isNotEqualTo(sweepingConfigDTO2);
        sweepingConfigDTO1.setId(null);
        assertThat(sweepingConfigDTO1).isNotEqualTo(sweepingConfigDTO2);
    }
}
