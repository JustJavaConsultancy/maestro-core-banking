package ng.com.justjava.corebanking.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.justjava.corebanking.web.rest.TestUtil;

public class SchemeCategoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchemeCategory.class);
        SchemeCategory schemeCategory1 = new SchemeCategory();
        schemeCategory1.setId(1L);
        SchemeCategory schemeCategory2 = new SchemeCategory();
        schemeCategory2.setId(schemeCategory1.getId());
        assertThat(schemeCategory1).isEqualTo(schemeCategory2);
        schemeCategory2.setId(2L);
        assertThat(schemeCategory1).isNotEqualTo(schemeCategory2);
        schemeCategory1.setId(null);
        assertThat(schemeCategory1).isNotEqualTo(schemeCategory2);
    }
}
