package com.zibmbrazil.myFinances.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.zibmbrazil.myFinances.exception.BusinessRuleException;
import com.zibmbrazil.myFinances.model.entity.User;
import com.zibmbrazil.myFinances.model.repository.UserRepository;
import com.zibmbrazil.myFinances.model.service.UserService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

	@Autowired
	UserService service;

	@Autowired
	UserRepository repository;

	@Test()
	public void mustValidateEmail() {
		Assertions.assertDoesNotThrow(() -> {

			// SCENARIO
			repository.deleteAll();

			// ACTION
			service.validateEmail("tester@gmail.com");

		});
	}

	@Test()
	public void shouldThrowErrorWhenValidatingEmailThereIsRegisteredEmail() {
		Assertions.assertThrows( BusinessRuleException.class, () -> {
			// SCENARIO
			User user = User.builder().name("Tester").email("tester@email.com").build();
			repository.save(user);

			// ACTION / EXECUTE
			service.validateEmail("tester@email.com");
		});
	}

}
