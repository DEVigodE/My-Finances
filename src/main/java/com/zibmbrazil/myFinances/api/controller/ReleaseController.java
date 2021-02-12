package com.zibmbrazil.myFinances.api.controller;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.h2.result.UpdatableRow;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zibmbrazil.myFinances.api.dto.ReleaseDTO;
import com.zibmbrazil.myFinances.api.dto.UpdateStatusDTO;
import com.zibmbrazil.myFinances.exception.BusinessRuleException;
import com.zibmbrazil.myFinances.model.entity.Release;
import com.zibmbrazil.myFinances.model.entity.User;
import com.zibmbrazil.myFinances.model.enums.Status;
import com.zibmbrazil.myFinances.model.enums.TypeRelease;
import com.zibmbrazil.myFinances.model.service.ReleaseService;
import com.zibmbrazil.myFinances.model.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/releases")
@RequiredArgsConstructor
public class ReleaseController {

	private final ReleaseService service;
	private final UserService userService;

	@PostMapping
	public ResponseEntity save(@RequestBody ReleaseDTO dto) {
		try {
			Release entity = convert(dto);
			entity = service.save(entity);
			return new ResponseEntity(entity, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity upadate(@PathVariable Long id, @RequestBody ReleaseDTO dto) {
		return service.findById(id).map(entity -> {
			try {
				Release release = convert(dto);
				release.setId(entity.getId());
				service.update(release);
				return ResponseEntity.ok(release);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("release not found in the database", HttpStatus.BAD_REQUEST));

	}

	@PutMapping("{id}/update-status")
	public ResponseEntity updateStatus(@PathVariable Long id, @RequestBody UpdateStatusDTO dto) {
		return service.findById(id).map(entity -> {
			Status status = Status.valueOf(dto.getStatus());
			if (status == null)
				return ResponseEntity.badRequest().body("");
			try {
				entity.setStatus(status);
				service.update(entity);
				return ResponseEntity.ok(entity);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}

		}).orElseGet(() -> new ResponseEntity("release not found in the database", HttpStatus.BAD_REQUEST));
	}

	@GetMapping
	public ResponseEntity search(@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "year", required = false) Integer year, @RequestParam("user") Long idUser) {
		Release releaseFilter = new Release();
		releaseFilter.setDescription(description);
		releaseFilter.setMonth(month);
		releaseFilter.setYear(year);
		Optional<User> user = userService.getById(idUser);
		if (!user.isPresent()) {
			return ResponseEntity.badRequest().body("User not found for the given id.");
		} else {
			releaseFilter.setUser(user.get());
		}
		List<Release> releases = service.search(releaseFilter);

		return ResponseEntity.ok(releases);
	}

	@DeleteMapping("{id}")
	public ResponseEntity delete(@PathVariable Long id) {
		return service.findById(id).map(entity -> {
			try {
				service.delete(entity);
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("release not found in the database", HttpStatus.BAD_REQUEST));
	}

	public Release convert(ReleaseDTO dto) {
		Release release = new Release();
		release.setId(dto.getId());
		release.setDescription(dto.getDescription());
		release.setYear(dto.getYear());
		release.setMonth(dto.getMonth());
		release.setValue(dto.getValue());
		User user = userService.getById(dto.getUser())
				.orElseThrow(() -> new BusinessRuleException("User not found for the given id."));
		release.setUser(user);
		if (dto.getType() != null)
			release.setType(TypeRelease.valueOf(dto.getType()));
		if (dto.getStatus() != null)
			release.setStatus(Status.valueOf(dto.getStatus()));
		return release;
	}

}
