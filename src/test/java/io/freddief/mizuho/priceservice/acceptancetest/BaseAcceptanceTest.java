package io.freddief.mizuho.priceservice.acceptancetest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment. RANDOM_PORT)
public abstract class BaseAcceptanceTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @LocalServerPort
    private int serverPort;

    protected String formatUrl(String path) {
        return "http://localhost:" + serverPort + path;
    }

    @After
    public void teardown() {
    }

}
