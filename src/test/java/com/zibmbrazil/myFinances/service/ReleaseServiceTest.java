package com.zibmbrazil.myFinances.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import com.zibmbrazil.myFinances.exception.BusinessRuleException;
import com.zibmbrazil.myFinances.model.entity.Release;
import com.zibmbrazil.myFinances.model.entity.User;
import com.zibmbrazil.myFinances.model.enums.Status;
import com.zibmbrazil.myFinances.model.enums.TypeRelease;
import com.zibmbrazil.myFinances.model.repository.ReleaseRepository;
import com.zibmbrazil.myFinances.model.repository.ReleaseRepositoryTest;
import com.zibmbrazil.myFinances.model.service.impl.ReleaseServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class ReleaseServiceTest {

	@SpyBean
	ReleaseServiceImpl service;

	@MockBean
	ReleaseRepository repository;

	@Test
	public void mustSaveARelease() {
		// SCENARIO
		Release releaseToSave = ReleaseRepositoryTest.createRelease();
		Mockito.doNothing().when(service).validate(releaseToSave);
		Release releaseSaved = ReleaseRepositoryTest.createRelease();
		releaseSaved.setId(1l);
		releaseSaved.setStatus(Status.PENDING);
		Mockito.when(repository.save(releaseToSave)).thenReturn(releaseSaved);
		service.save(releaseToSave);

		// ACTION
		Release release = service.save(releaseToSave);

		// VERIFY
		Assertions.assertThat(release.getId()).isEqualTo(releaseSaved.getId());
		Assertions.assertThat(release.getStatus()).isEqualTo(Status.PENDING);
	}

	@Test
	public void shouldNotSaveAReleaseWhenItHasAValidationError() {
		// SCENARIO
		Release releaseToSave = ReleaseRepositoryTest.createRelease();
		Mockito.doThrow(BusinessRuleException.class).when(service).validate(releaseToSave);

		// ACTION
		Assertions.catchThrowableOfType(() -> service.save(releaseToSave), BusinessRuleException.class);
		Mockito.verify(repository, Mockito.never()).save(releaseToSave);

	}

	@Test
	public void mustUpdateARelease() {
		// SCENARIO
		Release releaseSaved = ReleaseRepositoryTest.createRelease();
		releaseSaved.setId(1l);
		releaseSaved.setStatus(Status.PENDING);
		Mockito.doNothing().when(service).validate(releaseSaved);
		Mockito.when(repository.save(releaseSaved)).thenReturn(releaseSaved);

		// ACTION
		service.update(releaseSaved);

		// VERIFY
		Mockito.verify(repository, Mockito.times(1)).save(releaseSaved);

	}

	@Test
	public void shouldThrowErrorWhenTryingToUpdateNonExistentRelease() {
		// SCENARIO
		Release release = ReleaseRepositoryTest.createRelease();

		// ACTION
		Assertions.catchThrowableOfType(() -> service.update(release), NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).save(release);

	}

	@Test
	public void mustDeleteARelease() {
		// SCENARIO
		Release release = ReleaseRepositoryTest.createRelease();
		release.setId(1l);

		// ACTION
		service.delete(release);

		// VERIFY
		Mockito.verify(repository).delete(release);
	}

	@Test
	public void shouldThrowErrorWhenTryingToDeleteNonExistentRelease() {
		// SCENARIO
		Release release = ReleaseRepositoryTest.createRelease();

		// ACTION
		Assertions.catchThrowableOfType(() -> service.delete(release), NullPointerException.class);

		// VERIFY
		Mockito.verify(repository, Mockito.never()).delete(release);
	}

	@Test
	public void shouldFilterLaunches() {
		// SCENARIO
		Release release = ReleaseRepositoryTest.createRelease();
		release.setId(1l);

		List<Release> list = Arrays.asList(release);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(list);

		// ACTION
		List<Release> result = service.search(release);

		// VERIFY
		Assertions.assertThat(result).isNotEmpty().hasSize(1).contains(release);
	}

	@Test
	public void mustUpdateStatusOfARelease() {
		// SCENARIO
		Release release = ReleaseRepositoryTest.createRelease();
		release.setId(1l);
		release.setStatus(Status.PENDING);

		Status newStatus = Status.EFFECTED;
		Mockito.doReturn(release).when(service).update(release);

		// ACTION
		service.updateStatus(release, newStatus);

		// VERIFY
		Assertions.assertThat(release.getStatus()).isEqualTo(newStatus);
		Mockito.verify(service).updateStatus(release, newStatus);
		;

	}

	@Test
	public void mustGetAReleaseById() {
		// SCENARIO
		Long id = 1l;

		Release release = ReleaseRepositoryTest.createRelease();
		release.setId(id);

		Mockito.when(repository.findById(id)).thenReturn(Optional.of(release));

		// ACTION
		Optional<Release> resultado = service.findById(id);

		// VERIFY
		Assertions.assertThat(resultado.isPresent()).isTrue();
	}

	@Test
	public void mustReturnNullWhenNonExistentRelease() {
		// SCENARIO
		Long id = 1l;

		Release release = ReleaseRepositoryTest.createRelease();
		release.setId(id);

		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

		// ACTION
		Optional<Release> resultado = service.findById(id);

		// VERIFY
		Assertions.assertThat(resultado.isPresent()).isFalse();
	}

	@Test
	public void mustThrowErrorWhenValidatingARelease() {
		Release release = new Release();

		Throwable erro = Assertions.catchThrowable(() -> service.validate(release));
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a valid description.");

		release.setDescription("");

		erro = Assertions.catchThrowable(() -> service.validate(release));
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Enter a valid description.");

		release.setDescription("Salario");

		erro = Assertions.catchThrowable(() -> service.validate(release));
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Invalid Month.");

		release.setYear(0);

		erro = Assertions.catchThrowable(() -> service.validate(release));
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Invalid Month.");

		release.setYear(13);

		erro = Assertions.catchThrowable(() -> service.validate(release));
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Invalid Month.");

		release.setMonth(1);

		erro = Assertions.catchThrowable(() -> service.validate(release));
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Invalid Year.");

		release.setYear(202);

		erro = Assertions.catchThrowable(() -> service.validate(release));
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Invalid Year.");

		release.setYear(2020);

		erro = Assertions.catchThrowable(() -> service.validate(release));
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Invalid User.");

		release.setUser(new User());

		erro = Assertions.catchThrowable(() -> service.validate(release));
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Invalid User.");

		release.getUser().setId(1l);

		erro = Assertions.catchThrowable(() -> service.validate(release));
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Invalid Value.");

		release.setValue(BigDecimal.ZERO);

		erro = Assertions.catchThrowable(() -> service.validate(release));
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Invalid Value.");

		release.setValue(BigDecimal.valueOf(1));

		erro = Assertions.catchThrowable(() -> service.validate(release));
		Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Invalid Type.");

	}

	@Test
	public void deveObterSaldoPorUsuario() {
		// SCENARY
		Long idUser = 1l;

		Mockito.when(repository.getBalanceByTypeReleaseAndUser(idUser, TypeRelease.RECIPE, Status.EFFECTED))
				.thenReturn(BigDecimal.valueOf(100));

		Mockito.when(repository.getBalanceByTypeReleaseAndUser(idUser, TypeRelease.RECIPE, Status.EFFECTED))
				.thenReturn(BigDecimal.valueOf(50));

		// ACTION
		BigDecimal saldo = service.getBalancebyIdUser(idUser);

		// VERIFY
		Assertions.assertThat(saldo).isEqualTo(BigDecimal.valueOf(50));

	}

}
