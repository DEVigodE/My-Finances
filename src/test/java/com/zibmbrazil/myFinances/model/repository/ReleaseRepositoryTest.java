package com.zibmbrazil.myFinances.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.zibmbrazil.myFinances.model.entity.Release;
import com.zibmbrazil.myFinances.model.enums.Status;
import com.zibmbrazil.myFinances.model.enums.TypeRelease;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ReleaseRepositoryTest {

	@Autowired
	ReleaseRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void mustSaveARelease() {
		Release release = createRelease();

		release = repository.save(release);
		assertThat(release.getId()).isNotNull();
	}

	@Test
	public void mustDeleteARelease() {
		Release release = saveAndPersistARelease();

		release = entityManager.find(Release.class, release.getId());

		repository.delete(release);

		Release releaseDeleted = entityManager.find(Release.class, release.getId());

		assertThat(releaseDeleted).isNull();
	}

	@Test
	public void mustUpdateARelease() {
		Release release = saveAndPersistARelease();
		release.setMonth(3);
		release.setDescription("Lançamento 6");
		release.setStatus(Status.CANCELED);

		repository.save(release);

		Release releaseUpdated = entityManager.find(Release.class, release.getId());

		assertThat(releaseUpdated.getMonth()).isEqualTo(3);
		assertThat(releaseUpdated.getDescription()).isEqualTo("Lançamento 6");
		assertThat(releaseUpdated.getStatus()).isEqualTo(Status.CANCELED);

	}

	@Test
	public void mustFindAReleaseById() {
		Release release = saveAndPersistARelease();

		Optional<Release> foundRelease = repository.findById(release.getId());

		assertThat(foundRelease.isPresent()).isTrue();
	}

	private Release saveAndPersistARelease() {
		Release release = createRelease();
		return entityManager.persist(release);
	}

	public static Release createRelease() {
		return Release.builder().year(2021).month(1).description("Lançamento").value(BigDecimal.valueOf(10))
				.type(TypeRelease.RECIPE).status(Status.PENDING).dateRegister(LocalDate.now()).build();
	}
}
