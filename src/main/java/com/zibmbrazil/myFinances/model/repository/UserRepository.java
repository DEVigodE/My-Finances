package com.zibmbrazil.myFinances.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zibmbrazil.myFinances.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String email);

}
