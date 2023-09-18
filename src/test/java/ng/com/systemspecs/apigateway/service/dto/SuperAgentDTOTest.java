package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SuperAgentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuperAgentDTO.class);
        SuperAgentDTO superAgentDTO1 = new SuperAgentDTO();
        superAgentDTO1.setId(1L);
        SuperAgentDTO superAgentDTO2 = new SuperAgentDTO();
        assertThat(superAgentDTO1).isNotEqualTo(superAgentDTO2);
        superAgentDTO2.setId(superAgentDTO1.getId());
        assertThat(superAgentDTO1).isEqualTo(superAgentDTO2);
        superAgentDTO2.setId(2L);
        assertThat(superAgentDTO1).isNotEqualTo(superAgentDTO2);
        superAgentDTO1.setId(null);
        assertThat(superAgentDTO1).isNotEqualTo(superAgentDTO2);
    }
}
