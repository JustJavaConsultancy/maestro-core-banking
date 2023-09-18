package ng.com.justjava.corebanking.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.justjava.corebanking.web.rest.TestUtil;

public class AppUpdateDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUpdateDTO.class);
        AppUpdateDTO appUpdateDTO1 = new AppUpdateDTO();
        appUpdateDTO1.setId(1L);
        AppUpdateDTO appUpdateDTO2 = new AppUpdateDTO();
        assertThat(appUpdateDTO1).isNotEqualTo(appUpdateDTO2);
        appUpdateDTO2.setId(appUpdateDTO1.getId());
        assertThat(appUpdateDTO1).isEqualTo(appUpdateDTO2);
        appUpdateDTO2.setId(2L);
        assertThat(appUpdateDTO1).isNotEqualTo(appUpdateDTO2);
        appUpdateDTO1.setId(null);
        assertThat(appUpdateDTO1).isNotEqualTo(appUpdateDTO2);
    }
}
