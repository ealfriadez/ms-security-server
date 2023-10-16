package pnp.gob.pe.mssecurityserver.repository;

import org.springframework.data.repository.CrudRepository;
import pnp.gob.pe.mssecurityserver.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByUser(String user);
}
