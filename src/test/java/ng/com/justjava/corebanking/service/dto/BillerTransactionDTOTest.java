package ng.com.justjava.corebanking.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.justjava.corebanking.web.rest.TestUtil;

public class BillerTransactionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillerTransactionDTO.class);
        BillerTransactionDTO billerTransactionDTO1 = new BillerTransactionDTO();
        billerTransactionDTO1.setId(1L);
        BillerTransactionDTO billerTransactionDTO2 = new BillerTransactionDTO();
        assertThat(billerTransactionDTO1).isNotEqualTo(billerTransactionDTO2);
        billerTransactionDTO2.setId(billerTransactionDTO1.getId());
        assertThat(billerTransactionDTO1).isEqualTo(billerTransactionDTO2);
        billerTransactionDTO2.setId(2L);
        assertThat(billerTransactionDTO1).isNotEqualTo(billerTransactionDTO2);
        billerTransactionDTO1.setId(null);
        assertThat(billerTransactionDTO1).isNotEqualTo(billerTransactionDTO2);
    }
}
