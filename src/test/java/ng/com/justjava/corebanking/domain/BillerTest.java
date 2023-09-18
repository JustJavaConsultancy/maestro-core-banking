package ng.com.justjava.corebanking.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.justjava.corebanking.web.rest.TestUtil;

public class BillerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Biller.class);
        Biller biller1 = new Biller();
        biller1.setId(1L);
        Biller biller2 = new Biller();
        biller2.setId(biller1.getId());
        assertThat(biller1).isEqualTo(biller2);
        biller2.setId(2L);
        assertThat(biller1).isNotEqualTo(biller2);
        biller1.setId(null);
        assertThat(biller1).isNotEqualTo(biller2);
    }
}
