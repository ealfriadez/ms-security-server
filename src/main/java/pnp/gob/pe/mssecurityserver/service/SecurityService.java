package pnp.gob.pe.mssecurityserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnp.gob.pe.mssecurityserver.configuration.error.InvalidCredentialsException;
import pnp.gob.pe.mssecurityserver.configuration.security.JwtUtil;
import pnp.gob.pe.mssecurityserver.model.dto.AuthRequestDto;
import pnp.gob.pe.mssecurityserver.model.dto.AuthResponseDto;
import pnp.gob.pe.mssecurityserver.model.entity.UserEntity;
import pnp.gob.pe.mssecurityserver.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class SecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtUtil jwtTokenCross;

    public AuthResponseDto login(AuthRequestDto request){
        UserEntity userEntity = validateCredentials(request);
        List<String> roles = userEntity.getRoles().stream().map(rol -> rol.getName()).collect(Collectors.toList());
        AuthResponseDto response = new AuthResponseDto();
        response.setAccessToken(jwtTokenCross.generateToken(userEntity.getUser(), roles));
        return response;
    }

    public UserEntity validateCredentials(AuthRequestDto request){
        Optional<UserEntity> result = userRepository.findByUser(request.getUserName());
        if (result.isEmpty() || !bCryptPasswordEncoder.matches(request.getPassword(), result.get().getPassword())){
            throw new InvalidCredentialsException("Invalid Credentials", HttpStatus.UNAUTHORIZED);
        }
        return result.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername");
        Optional<UserEntity> optional = userRepository.findByUser(username);
        if (optional.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        UserEntity userEntity = optional.get();
        List<String> roles = userEntity.getRoles().stream().map(rol -> rol.getName()).collect(Collectors.toList());
        Collection<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        log.info("loadUserByUsername ok");
        return new User(userEntity.getUser(), userEntity.getPassword(), authorities);
    }
}
