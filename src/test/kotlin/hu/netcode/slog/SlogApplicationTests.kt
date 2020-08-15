package hu.netcode.slog

import org.junit.jupiter.api.Test
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.test.context.SpringBootTest

@ConfigurationPropertiesScan
@SpringBootTest
class SlogApplicationTests {
    @Test
    fun contextLoads() {
        //
    }
}
