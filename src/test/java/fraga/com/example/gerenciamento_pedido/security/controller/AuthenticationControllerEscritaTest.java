package fraga.com.example.gerenciamento_pedido.security.controller;

import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import fraga.com.example.gerenciamento_pedido.security.domain.Role;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerEscritaTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationControllerEscrita controller;

    private AuthenticationRequest authRequest;
    private LoginResponse loginResponse;
    private RegisterRequest registerRequest;
    private RegisterResponse registerResponse;

    @BeforeEach
    void setUp() {
        authRequest = new AuthenticationRequest("usuario", "senha123");
        loginResponse = new LoginResponse("jwt-token-123456");
        
        Set<Role> roles = new HashSet<>();
        registerRequest = new RegisterRequest("novousuario", "senha123", null);
        registerResponse = new RegisterResponse("1", "novousuario", roles);
    }

    @Test
    void testLoginComSucesso() {
        when(userService.login(any(AuthenticationRequest.class))).thenReturn(loginResponse);
        
        ResponseEntity<DefaultResponse<LoginResponse>> response = controller.login(authRequest);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loginResponse, response.getBody().getDados());
    }

    @Test
    void testRegisterComSucesso() {
        when(userService.register(any(RegisterRequest.class))).thenReturn(registerResponse);
        
        ResponseEntity<DefaultResponse<RegisterResponse>> response = controller.register(registerRequest);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(registerResponse, response.getBody().getDados());
    }
} 