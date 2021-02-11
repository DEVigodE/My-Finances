package com.zibmbrazil.myFinances.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.zibmbrazil.myFinances.model.entity.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

	@Autowired
	UserRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void shouldCheckForAnEmail() {

		// SCENARIO
		User user = createUser();
		entityManager.persist(user);

		// ACTION / EXECUTE
		boolean result = repository.existsByEmail("igor@email.com");

		// VERIFY
		Assertions.assertThat(result).isTrue();
	}

	@Test
	public void iMustReturnFalseWhenThereIsNoUserRegisteredWithTheEmail() {

		// ACTION / EXECUTE
		boolean result = repository.existsByEmail("igor@email.com");

		// VERIFY
		Assertions.assertThat(result).isFalse();
	}

	@Test
	public void aUserMustPersistInTheDatabase() {
		// SCENARIO
		User user = createUser();

		// ACTION / EXECUTE
		User userSaved = repository.save(user);

		// VERIFY
		Assertions.assertThat(userSaved.getId()).isNotNull();

	}

	@Test
	public void mustSearchForAUserByEmail() {
		// SCENARIO
		User user = createUser();
		entityManager.persist(user);

		// VERIFY
		Optional<User> result = repository.findByEmail(user.getEmail());

		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void mustReturnEmptyWhenSearchingForUserByEmailWhenItDoesNotExistInTheDatabase() {

		// VERIFY
		Optional<User> result = repository.findByEmail("tester@gmail.com");

		Assertions.assertThat(result.isPresent()).isFalse();
	}

	public static User createUser() {

		return User.builder().name("Igor").email("igor@email.com").password("senha").build();
	}

}
