package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BonusPointDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BonusPointDTO.class);
        BonusPointDTO bonusPointDTO1 = new BonusPointDTO();
        bonusPointDTO1.setId(1L);
        BonusPointDTO bonusPointDTO2 = new BonusPointDTO();
        assertThat(bonusPointDTO1).isNotEqualTo(bonusPointDTO2);
        bonusPointDTO2.setId(bonusPointDTO1.getId());
        assertThat(bonusPointDTO1).isEqualTo(bonusPointDTO2);
        bonusPointDTO2.setId(2L);
        assertThat(bonusPointDTO1).isNotEqualTo(bonusPointDTO2);
        bonusPointDTO1.setId(null);
        assertThat(bonusPointDTO1).isNotEqualTo(bonusPointDTO2);
    }
}
