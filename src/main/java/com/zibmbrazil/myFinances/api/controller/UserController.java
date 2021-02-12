package com.zibmbrazil.myFinances.api.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zibmbrazil.myFinances.api.dto.UserDTO;
import com.zibmbrazil.myFinances.exception.AuthenticationError;
import com.zibmbrazil.myFinances.exception.BusinessRuleException;
import com.zibmbrazil.myFinances.model.entity.User;
import com.zibmbrazil.myFinances.model.service.ReleaseService;
import com.zibmbrazil.myFinances.model.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService service;
	private final ReleaseService releaseService;

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

	@GetMapping("{id}/balance")
	public ResponseEntity getBalance(@PathVariable("id") Long id) {
		Optional<User> user = service.getById(id);
		if (!user.isPresent())
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		BigDecimal balance = releaseService.getBalancebyIdUser(id);
		return ResponseEntity.ok(balance);
	}
}
