package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LenderDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LenderDTO.class);
        LenderDTO lenderDTO1 = new LenderDTO();
        lenderDTO1.setId(1L);
        LenderDTO lenderDTO2 = new LenderDTO();
        assertThat(lenderDTO1).isNotEqualTo(lenderDTO2);
        lenderDTO2.setId(lenderDTO1.getId());
        assertThat(lenderDTO1).isEqualTo(lenderDTO2);
        lenderDTO2.setId(2L);
        assertThat(lenderDTO1).isNotEqualTo(lenderDTO2);
        lenderDTO1.setId(null);
        assertThat(lenderDTO1).isNotEqualTo(lenderDTO2);
    }
}
