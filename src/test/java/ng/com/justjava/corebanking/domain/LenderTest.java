package ng.com.justjava.corebanking.domain;

import ng.com.justjava.corebanking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LenderTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lender.class);
        Lender lender1 = new Lender();
        lender1.setId(1L);
        Lender lender2 = new Lender();
        lender2.setId(lender1.getId());
        assertThat(lender1).isEqualTo(lender2);
        lender2.setId(2L);
        assertThat(lender1).isNotEqualTo(lender2);
        lender1.setId(null);
        assertThat(lender1).isNotEqualTo(lender2);
    }
}
