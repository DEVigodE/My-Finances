package com.zibmbrazil.myFinances.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.zibmbrazil.myFinances.exception.BusinessRuleException;
import com.zibmbrazil.myFinances.model.repository.UserRepository;
import com.zibmbrazil.myFinances.model.service.UserService;
import com.zibmbrazil.myFinances.model.service.impl.UserServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class UserServiceTest {

	UserService service;
	
	@MockBean
	UserRepository repository;

	@BeforeAll
	public void setUp() {
		service = new UserServiceImpl(repository);
	}

	@Test()
	public void mustValidateEmail() {
		Assertions.assertDoesNotThrow(() -> {

			// SCENARIO
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

			// ACTION
			service.validateEmail("tester@gmail.com");

		});
	}

	@Test()
	public void shouldThrowErrorWhenValidatingEmailThereIsRegisteredEmail() {
		Assertions.assertThrows(BusinessRuleException.class, () -> {
			// SCENARIO
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			// ACTION / EXECUTE
			service.validateEmail("tester@email.com");
		});
	}

}
