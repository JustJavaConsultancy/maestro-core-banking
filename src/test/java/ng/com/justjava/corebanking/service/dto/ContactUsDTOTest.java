package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactUsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactUsDTO.class);
        ContactUsDTO contactUsDTO1 = new ContactUsDTO();
        contactUsDTO1.setId(1L);
        ContactUsDTO contactUsDTO2 = new ContactUsDTO();
        assertThat(contactUsDTO1).isNotEqualTo(contactUsDTO2);
        contactUsDTO2.setId(contactUsDTO1.getId());
        assertThat(contactUsDTO1).isEqualTo(contactUsDTO2);
        contactUsDTO2.setId(2L);
        assertThat(contactUsDTO1).isNotEqualTo(contactUsDTO2);
        contactUsDTO1.setId(null);
        assertThat(contactUsDTO1).isNotEqualTo(contactUsDTO2);
    }
}
