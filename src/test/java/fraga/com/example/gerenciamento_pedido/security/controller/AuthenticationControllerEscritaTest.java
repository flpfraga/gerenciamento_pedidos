package fraga.com.example.gerenciamento_pedido.security.controller;


import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.security.domain.Role;
import fraga.com.example.gerenciamento_pedido.security.enums.EUserRole;
import fraga.com.example.gerenciamento_pedido.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerEscritaTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationControllerEscrita authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve realizar login com sucesso")
    void testRealizarLoginComSucesso() {
        AuthenticationRequest request = new AuthenticationRequest("usuario", "senha");
        LoginResponse loginResponse = new LoginResponse("token-jwt");
        when(userService.login(request)).thenReturn(loginResponse);

        ResponseEntity<DefaultResponse<LoginResponse>> response = authenticationController.login(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token-jwt", response.getBody().getDados().token());
        verify(userService, times(1)).login(request);
    }

    @Test
    @DisplayName("Deve retornar erro 401 quando credenciais inválidas")
    void testRealizarLoginErroCredenciaisInvalidas() {
        AuthenticationRequest request = new AuthenticationRequest("usuario", "senha");
        when(userService.login(request)).thenThrow(new RuntimeException("Credenciais inválidas"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationController.login(request);
        });

        assertEquals("Credenciais inválidas", exception.getMessage());
        verify(userService, times(1)).login(request);
    }

    @Test
    @DisplayName("Deve registrar novo usuário com sucesso")
    void testRealizarRegistroComSucesso() {
        RegisterRequest request = new RegisterRequest("novo_usuario", "senha", Set.of(EUserRole.ADMIN));
        RegisterResponse registerResponse = new RegisterResponse(
                "id", "novo_usuario", Set.of(new Role(1L, EUserRole.ADMIN, "descricao")));

        when(userService.register(request)).thenReturn(registerResponse);

        ResponseEntity<DefaultResponse<RegisterResponse>> response = authenticationController.register(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200,response.getBody().getStatus());
        assertEquals("id", response.getBody().getDados().getId());
        verify(userService, times(1)).register(request);
    }

    @Test
    @DisplayName("Deve retornar erro 409 quando usuário já existe")
    void testRegistrarUsuarioErroUsuarioJaRegistrdo() {
        RegisterRequest request = new RegisterRequest("novo_usuario", "senha", Set.of(EUserRole.USER));
        when(userService.register(request)).thenThrow(new RuntimeException("Usuário já existe"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationController.register(request);
        });

        assertEquals("Usuário já existe", exception.getMessage());
        verify(userService, times(1)).register(request);
    }
}

