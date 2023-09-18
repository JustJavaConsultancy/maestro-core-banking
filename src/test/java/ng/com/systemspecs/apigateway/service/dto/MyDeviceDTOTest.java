package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MyDeviceDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyDeviceDTO.class);
        MyDeviceDTO myDeviceDTO1 = new MyDeviceDTO();
        myDeviceDTO1.setId(1L);
        MyDeviceDTO myDeviceDTO2 = new MyDeviceDTO();
        assertThat(myDeviceDTO1).isNotEqualTo(myDeviceDTO2);
        myDeviceDTO2.setId(myDeviceDTO1.getId());
        assertThat(myDeviceDTO1).isEqualTo(myDeviceDTO2);
        myDeviceDTO2.setId(2L);
        assertThat(myDeviceDTO1).isNotEqualTo(myDeviceDTO2);
        myDeviceDTO1.setId(null);
        assertThat(myDeviceDTO1).isNotEqualTo(myDeviceDTO2);
    }
}
