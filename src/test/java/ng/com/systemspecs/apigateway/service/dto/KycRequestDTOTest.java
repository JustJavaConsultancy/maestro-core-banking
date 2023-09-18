package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KycRequestDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KycRequestDTO.class);
        KycRequestDTO kycRequestDTO1 = new KycRequestDTO();
        kycRequestDTO1.setId(1L);
        KycRequestDTO kycRequestDTO2 = new KycRequestDTO();
        assertThat(kycRequestDTO1).isNotEqualTo(kycRequestDTO2);
        kycRequestDTO2.setId(kycRequestDTO1.getId());
        assertThat(kycRequestDTO1).isEqualTo(kycRequestDTO2);
        kycRequestDTO2.setId(2L);
        assertThat(kycRequestDTO1).isNotEqualTo(kycRequestDTO2);
        kycRequestDTO1.setId(null);
        assertThat(kycRequestDTO1).isNotEqualTo(kycRequestDTO2);
    }
}
