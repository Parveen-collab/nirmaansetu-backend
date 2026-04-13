package com.nirmaansetu.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

//🧠 Why this is IMPORTANT
//Even though it looks empty, it’s powerful:
//✅ It catches:
//1.Missing dependencies
//2.Bean creation errors
//3.Configuration issues
//4.Wrong annotations

@SpringBootTest
@ActiveProfiles("test")
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
