package fraga.com.example.gerenciamento_pedido.security.service;

import fraga.com.example.gerenciamento_pedido.security.domain.User;
import fraga.com.example.gerenciamento_pedido.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private AuthorizationService authorizationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setLogin("testuser");
        user.setPassword("password");
        user.setActive(true);
    }

    @Test
    void testLoadUserByUsernameComSucesso() {
        when(repository.findByLogin(anyString())).thenReturn(user);

        UserDetails result = authorizationService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testLoadUserByUsernameUsuarioNaoEncontrado() {
        when(repository.findByLogin(anyString())).thenReturn(null);

        UserDetails result = authorizationService.loadUserByUsername("nonexistentuser");
        assertNull(result);

    }
} 