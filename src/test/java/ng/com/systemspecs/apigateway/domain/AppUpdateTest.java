package ng.com.systemspecs.apigateway.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.systemspecs.apigateway.web.rest.TestUtil;

public class AppUpdateTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUpdate.class);
        AppUpdate appUpdate1 = new AppUpdate();
        appUpdate1.setId(1L);
        AppUpdate appUpdate2 = new AppUpdate();
        appUpdate2.setId(appUpdate1.getId());
        assertThat(appUpdate1).isEqualTo(appUpdate2);
        appUpdate2.setId(2L);
        assertThat(appUpdate1).isNotEqualTo(appUpdate2);
        appUpdate1.setId(null);
        assertThat(appUpdate1).isNotEqualTo(appUpdate2);
    }
}
