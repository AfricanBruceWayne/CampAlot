package zima.springboot.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import zima.springboot.model.Role;
import zima.springboot.model.RoleName;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Optional<Role> findByName(RoleName name);
	
}
