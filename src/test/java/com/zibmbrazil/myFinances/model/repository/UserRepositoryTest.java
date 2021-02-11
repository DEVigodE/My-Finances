package com.zibmbrazil.myFinances.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.zibmbrazil.myFinances.model.entity.User;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserRepositoryTest {

	@Autowired
	UserRepository repository;

	@Test
	public void shouldCheckForAnEmail() {

		// SCENARIO
		User user = User.builder().name("Igor").email("igor@email.com").build();
		repository.save(user);

		// ACTION / EXECUTE
		boolean result = repository.existsByEmail("igor@email.com");

		// VERIFY
		Assertions.assertThat(result).isTrue();
	}

	@Test
	public void iMustReturnFalseWhenThereIsNoUserRegisteredWithTheEmail() {
		repository.deleteAll();

		// ACTION / EXECUTE
		boolean result = repository.existsByEmail("igor@email.com");
		

		// VERIFY
		Assertions.assertThat(result).isFalse();
	}

}
