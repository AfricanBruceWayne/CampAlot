package zima.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zima.springboot.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);

}
