package ng.com.systemspecs.apigateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactUsMapperTest {

    private ContactUsMapper contactUsMapper;

    @BeforeEach
    public void setUp() {
        contactUsMapper = new ContactUsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(contactUsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(contactUsMapper.fromId(null)).isNull();
    }
}
