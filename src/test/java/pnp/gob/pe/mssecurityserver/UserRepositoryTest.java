package pnp.gob.pe.mssecurityserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pnp.gob.pe.mssecurityserver.model.entity.UserEntity;
import pnp.gob.pe.mssecurityserver.repository.UserRepository;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void whenGetUser_ThenReturnUser(){
        Optional<UserEntity> userEntity = userRepository.findByUser("admin");
        assertEquals(true, userEntity.isPresent());
        assertEquals(2, userEntity.get().getRoles().size());
    }
}
