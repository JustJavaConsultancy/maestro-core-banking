package ng.com.systemspecs.apigateway.domain;

import ng.com.systemspecs.apigateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SuperAgentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuperAgent.class);
        SuperAgent superAgent1 = new SuperAgent();
        superAgent1.setId(1L);
        SuperAgent superAgent2 = new SuperAgent();
        superAgent2.setId(superAgent1.getId());
        assertThat(superAgent1).isEqualTo(superAgent2);
        superAgent2.setId(2L);
        assertThat(superAgent1).isNotEqualTo(superAgent2);
        superAgent1.setId(null);
        assertThat(superAgent1).isNotEqualTo(superAgent2);
    }
}
