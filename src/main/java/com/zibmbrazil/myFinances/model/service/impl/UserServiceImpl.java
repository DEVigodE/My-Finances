package com.zibmbrazil.myFinances.model.service.impl;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.zibmbrazil.myFinances.exception.BusinessRuleException;
import com.zibmbrazil.myFinances.model.entity.User;
import com.zibmbrazil.myFinances.model.repository.UserRepository;
import com.zibmbrazil.myFinances.model.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository repository;

	public UserServiceImpl(UserRepository repository) {
		super();
		this.repository = repository;
	}

	public User authenticate(String email, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public User saveUser(User user) {
		validateEmail(user.getEmail());
		return repository.save(user);
	}

	public void validateEmail(String email) {
		boolean exist = repository.existsByEmail(email);
		if (exist) {
			throw new BusinessRuleException("Have user with this email");
		}
	}

}
