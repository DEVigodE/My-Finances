package com.zibmbrazil.myFinances.model.service;

import com.zibmbrazil.myFinances.model.entity.User;
import java.util.Optional;

public interface UserService {
	User authenticate(String email, String password);

	User saveUser(User user);

	void validateEmail(String email);

	Optional<User> getById(Long id);
}
