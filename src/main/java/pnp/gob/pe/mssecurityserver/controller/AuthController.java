package pnp.gob.pe.mssecurityserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pnp.gob.pe.mssecurityserver.model.dto.AuthRequestDto;
import pnp.gob.pe.mssecurityserver.service.SecurityService;

@RestController
@RequestMapping(path = "v1.0")
@RequiredArgsConstructor
public class AuthController {
    
    private final SecurityService securityService;
    
    @PostMapping()
    public ResponseEntity<?> post(@RequestBody AuthRequestDto request) throws Exception {
    	return ResponseEntity.ok(securityService.login(request));
    }
  
}
