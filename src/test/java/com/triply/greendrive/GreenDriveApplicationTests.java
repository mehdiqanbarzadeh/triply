package com.triply.greendrive;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = GreenDriveApplication.class)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:error-messages.properties"})
public abstract class GreenDriveApplicationTests {

}
