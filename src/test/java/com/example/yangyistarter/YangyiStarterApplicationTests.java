package com.example.yangyistarter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;
import org.mockito.MockedStatic;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class YangyiStarterApplicationTests {

    @Test
    void contextLoads() {
        assertDoesNotThrow(YangyiStarterApplication::new);
        try (MockedStatic<SpringApplication> application = mockStatic(SpringApplication.class)) {
            assertNotNull(application);
            YangyiStarterApplication.main(null);
        }
    }

}
