package com.zibmbrazil.myFinances.model.service;

import com.zibmbrazil.myFinances.model.entity.User;

public interface UserService {
	User authenticate(String email, String password);

	User saveUser(User user);
	
	void validateEmail(String email);
}
