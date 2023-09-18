package ng.com.justjava.corebanking.domain;

import ng.com.justjava.corebanking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SweepingConfigTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SweepingConfig.class);
        SweepingConfig sweepingConfig1 = new SweepingConfig();
        sweepingConfig1.setId(1L);
        SweepingConfig sweepingConfig2 = new SweepingConfig();
        sweepingConfig2.setId(sweepingConfig1.getId());
        assertThat(sweepingConfig1).isEqualTo(sweepingConfig2);
        sweepingConfig2.setId(2L);
        assertThat(sweepingConfig1).isNotEqualTo(sweepingConfig2);
        sweepingConfig1.setId(null);
        assertThat(sweepingConfig1).isNotEqualTo(sweepingConfig2);
    }
}
