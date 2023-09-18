package ng.com.justjava.corebanking.domain;

import ng.com.justjava.corebanking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KycRequestTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KycRequest.class);
        KycRequest kycRequest1 = new KycRequest();
        kycRequest1.setId(1L);
        KycRequest kycRequest2 = new KycRequest();
        kycRequest2.setId(kycRequest1.getId());
        assertThat(kycRequest1).isEqualTo(kycRequest2);
        kycRequest2.setId(2L);
        assertThat(kycRequest1).isNotEqualTo(kycRequest2);
        kycRequest1.setId(null);
        assertThat(kycRequest1).isNotEqualTo(kycRequest2);
    }
}
