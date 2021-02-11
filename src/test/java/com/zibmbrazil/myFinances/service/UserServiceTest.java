package com.zibmbrazil.myFinances.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
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

import com.zibmbrazil.myFinances.exception.AuthenticationError;
import com.zibmbrazil.myFinances.exception.BusinessRuleException;
import com.zibmbrazil.myFinances.model.entity.User;
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

	@Test
	public void mustAuthenticateAUserSuccessfully() {
		// SCENARIO
		String email = "tester@gmail.com";
		String password = "password";

		User user = User.builder().email(email).password(password).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));

		// ACTION
		User result = service.authenticate(email, password);

		// VERIFY
		Assertions.assertThat(result).isNotNull();

	}

	@Test
	public void throwAnErrorWhenYouCantFindAUserRegisteredWithTheInformedEmail() {
		org.junit.jupiter.api.Assertions.assertThrows(AuthenticationError.class, () -> {
			// SCENARIO
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

			// ACTION
			//service.authenticate("tester@gmail.com", "password");
			// ACTION
			Throwable exception = Assertions.catchThrowable(() -> service.authenticate("tester@gmail.com", "123"));
			Assertions.assertThat(exception).isInstanceOf(AuthenticationError.class).hasMessage("User not found for the given email");
		});
	}

	@Test
	public void throwErrorWhenThePasswordIsNotTheSame() {

		// SCENARIO
		String email = "tester@gmail.com";
		String password = "password";

		User user = User.builder().email(email).password(password).id(1l).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

		// ACTION
		Throwable exception = Assertions.catchThrowable(() -> service.authenticate(email, "123"));
		Assertions.assertThat(exception).isInstanceOf(AuthenticationError.class).hasMessage("Invalid password");
		// service.authenticate(email, "123");

		// org.junit.jupiter.api.Assertions.assertThrows(AuthenticationError.class, ()
		// -> {
	}

	@Test()
	public void mustValidateEmail() {
		org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {

			// SCENARIO
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

			// ACTION
			service.validateEmail("tester@gmail.com");

		});
	}

	@Test()
	public void shouldThrowErrorWhenValidatingEmailThereIsRegisteredEmail() {
		org.junit.jupiter.api.Assertions.assertThrows(BusinessRuleException.class, () -> {
			// SCENARIO
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			// ACTION / EXECUTE
			service.validateEmail("tester@email.com");
		});
	}

}
