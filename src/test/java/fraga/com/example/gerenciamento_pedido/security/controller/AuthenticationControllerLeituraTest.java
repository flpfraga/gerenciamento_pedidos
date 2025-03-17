package fraga.com.example.gerenciamento_pedido.security.controller;

import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.security.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerLeituraTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private AuthenticationControllerLeitura controller;

    private Set<String> roles;

    @BeforeEach
    void setUp() {
        roles = new HashSet<>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_USER");
    }

    @Test
    void testGetRolesComSucesso() {
        when(roleService.get()).thenReturn(roles);
        
        ResponseEntity<DefaultResponse<Set<String>>> response = controller.getRoles();
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roles, response.getBody().getDados());
        assertEquals(2, response.getBody().getDados().size());
    }
} 