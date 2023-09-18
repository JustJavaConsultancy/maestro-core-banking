package ng.com.systemspecs.apigateway.web.rest;

import ng.com.systemspecs.apigateway.ApiGatewayApp;
import ng.com.systemspecs.apigateway.service.IbileService;
import ng.com.systemspecs.apigateway.service.TransactionLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the IbileResource REST controller.
 *
 * @see IbileResource
 */
@SpringBootTest(classes = ApiGatewayApp.class)
public class IbileResourceIT {

    @Autowired
    IbileService ibileService;

    @Autowired
    TransactionLogService transactionLogService;

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        IbileResource ibileResource = new IbileResource(ibileService, transactionLogService);
        restMockMvc = MockMvcBuilders
            .standaloneSetup(ibileResource)
            .build();
    }

    /**
     * Test referenceVerification
     */
    @Test
    public void testReferenceVerification() throws Exception {
        restMockMvc.perform(post("/api/ibile/reference-verification"))
            .andExpect(status().isOk());
    }
}
