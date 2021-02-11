package com.zibmbrazil.myFinances.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zibmbrazil.myFinances.api.dto.UserDTO;
import com.zibmbrazil.myFinances.exception.AuthenticationError;
import com.zibmbrazil.myFinances.exception.BusinessRuleException;
import com.zibmbrazil.myFinances.model.entity.User;
import com.zibmbrazil.myFinances.model.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private UserService service;

	public UserController(UserService _service) {
		this.service = _service;
	}

	@PostMapping("/authenticate")
	public ResponseEntity<Object> authenticate(@RequestBody UserDTO dto) {
		try {
			User user = service.authenticate(dto.getEmail(), dto.getPassword());
			return ResponseEntity.ok(user);
		} catch (AuthenticationError e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@PostMapping
	public ResponseEntity<Object> save(@RequestBody UserDTO dto) {

		User user = User.builder().name(dto.getName()).email(dto.getEmail()).password(dto.getPassword()).build();

		try {
			User userSaved = service.saveUser(user);
			return new ResponseEntity<Object>(userSaved, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
