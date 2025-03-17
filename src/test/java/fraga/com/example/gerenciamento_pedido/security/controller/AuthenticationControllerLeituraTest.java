package fraga.com.example.gerenciamento_pedido.security.controller;

import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.security.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerLeituraTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private AuthenticationControllerLeitura authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar a lista de roles com sucesso")
    void testBuscarListaRolesComSucesso() {
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        roles.add("USER");

        when(roleService.get()).thenReturn(roles);

        ResponseEntity<DefaultResponse<Set<String>>> response = authenticationController.getRoles();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200,response.getBody().getStatus());
        assertTrue(response.getBody().getDados().contains("ADMIN"));
        assertTrue(response.getBody().getDados().contains("USER"));
        assertEquals(2, response.getBody().getDados().size());
        verify(roleService, times(1)).get();
    }

    @Test
    @DisplayName("Deve lançar erro interno ao buscar roles")
    void getRoles_InternalServerError() {
        when(roleService.get()).thenThrow(new RuntimeException("Erro interno do servidor"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationController.getRoles();
        });

        assertEquals("Erro interno do servidor", exception.getMessage());
        verify(roleService, times(1)).get();
    }
}

