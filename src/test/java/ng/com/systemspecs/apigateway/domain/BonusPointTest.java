package ng.com.systemspecs.apigateway.domain;

import ng.com.systemspecs.apigateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BonusPointTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BonusPoint.class);
        BonusPoint bonusPoint1 = new BonusPoint();
        bonusPoint1.setId(1L);
        BonusPoint bonusPoint2 = new BonusPoint();
        bonusPoint2.setId(bonusPoint1.getId());
        assertThat(bonusPoint1).isEqualTo(bonusPoint2);
        bonusPoint2.setId(2L);
        assertThat(bonusPoint1).isNotEqualTo(bonusPoint2);
        bonusPoint1.setId(null);
        assertThat(bonusPoint1).isNotEqualTo(bonusPoint2);
    }
}
