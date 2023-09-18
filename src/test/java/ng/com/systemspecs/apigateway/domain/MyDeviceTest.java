package ng.com.systemspecs.apigateway.domain;

import ng.com.systemspecs.apigateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MyDeviceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyDevice.class);
        MyDevice myDevice1 = new MyDevice();
        myDevice1.setId(1L);
        MyDevice myDevice2 = new MyDevice();
        myDevice2.setId(myDevice1.getId());
        assertThat(myDevice1).isEqualTo(myDevice2);
        myDevice2.setId(2L);
        assertThat(myDevice1).isNotEqualTo(myDevice2);
        myDevice1.setId(null);
        assertThat(myDevice1).isNotEqualTo(myDevice2);
    }
}
