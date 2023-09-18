package ng.com.justjava.corebanking.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ng.com.justjava.corebanking.web.rest.TestUtil;

public class CustomersubscriptionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomersubscriptionDTO.class);
        CustomersubscriptionDTO customersubscriptionDTO1 = new CustomersubscriptionDTO();
        customersubscriptionDTO1.setId(1L);
        CustomersubscriptionDTO customersubscriptionDTO2 = new CustomersubscriptionDTO();
        assertThat(customersubscriptionDTO1).isNotEqualTo(customersubscriptionDTO2);
        customersubscriptionDTO2.setId(customersubscriptionDTO1.getId());
        assertThat(customersubscriptionDTO1).isEqualTo(customersubscriptionDTO2);
        customersubscriptionDTO2.setId(2L);
        assertThat(customersubscriptionDTO1).isNotEqualTo(customersubscriptionDTO2);
        customersubscriptionDTO1.setId(null);
        assertThat(customersubscriptionDTO1).isNotEqualTo(customersubscriptionDTO2);
    }
}
