package com.zibmbrazil.myFinances.model.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zibmbrazil.myFinances.exception.BusinessRuleException;
import com.zibmbrazil.myFinances.model.entity.Release;
import com.zibmbrazil.myFinances.model.enums.Status;
import com.zibmbrazil.myFinances.model.enums.TypeRelease;
import com.zibmbrazil.myFinances.model.repository.ReleaseRepository;
import com.zibmbrazil.myFinances.model.service.ReleaseService;

@Service
public class ReleaseServiceImpl implements ReleaseService {

	private ReleaseRepository repository;

	public ReleaseServiceImpl(ReleaseRepository _repository) {
		this.repository = _repository;
	}

	@Override
	@Transactional
	public Release save(Release release) {
		validate(release);
		release.setStatus(Status.PENDING);
		return repository.save(release);
	}

	@Override
	@Transactional
	public Release update(Release release) {
		Objects.requireNonNull(release.getId());
		validate(release);
		return repository.save(release);
	}

	@Override
	@Transactional
	public void delete(Release release) {
		Objects.requireNonNull(release.getId());
		repository.delete(release);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Release> search(Release releaseFilter) {

		Example<Release> example = Example.of(releaseFilter,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));

		return repository.findAll(example);
	}

	@Override
	public void updateStatus(Release release, Status status) {
		release.setStatus(status);
		repository.save(release);
	}

	@Override
	public void validate(Release release) {
		if (release.getDescription() == null || release.getDescription().trim().equals("")) {
			throw new BusinessRuleException("Enter a valid description.");
		}

		if (release.getMonth() == null || release.getMonth() < 1 || release.getMonth() > 12) {
			throw new BusinessRuleException("Invalid Month.");
		}

		if (release.getYear() == null || release.getYear().toString().length() != 4) {
			throw new BusinessRuleException("Invalid Year.");
		}

		if (release.getUser() == null || release.getUser().getId() == null) {
			throw new BusinessRuleException("Invalid User.");
		}

		if (release.getValue() == null || release.getValue().compareTo(BigDecimal.ZERO) < 1) {
			throw new BusinessRuleException("Invalid Value.");
		}

		if (release.getType() == null) {
			throw new BusinessRuleException("Invalid Type.");
		}
	}

	@Override
	public Optional<Release> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getBalancebyIdUser(Long id) {
		BigDecimal recipes = repository.getBalanceByTypeReleaseAndUser(id, TypeRelease.RECIPE, Status.EFFECTED);
		BigDecimal expense = repository.getBalanceByTypeReleaseAndUser(id, TypeRelease.EXPENSE, Status.EFFECTED);
		if (recipes == null)
			recipes = BigDecimal.ZERO;
		if (expense == null)
			expense = BigDecimal.ZERO;

		return recipes.subtract(expense);

	}

}
