package zima.springboot.repository;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.repository.JpaRepository;

import zima.springboot.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(@NotBlank String username);
	
	Optional<User> findByEmail(@NotBlank String email);

	Optional<User> findByUsernameOrEmail(String username, String email);
	
	Boolean existsByUsername(@NotBlank String username);
	
	Boolean existsByEmail(@NotBlank String email);
	
}
