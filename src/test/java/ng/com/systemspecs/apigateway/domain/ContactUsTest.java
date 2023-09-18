package ng.com.systemspecs.apigateway.domain;

import ng.com.systemspecs.apigateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactUsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactUs.class);
        ContactUs contactUs1 = new ContactUs();
        contactUs1.setId(1L);
        ContactUs contactUs2 = new ContactUs();
        contactUs2.setId(contactUs1.getId());
        assertThat(contactUs1).isEqualTo(contactUs2);
        contactUs2.setId(2L);
        assertThat(contactUs1).isNotEqualTo(contactUs2);
        contactUs1.setId(null);
        assertThat(contactUs1).isNotEqualTo(contactUs2);
    }
}
