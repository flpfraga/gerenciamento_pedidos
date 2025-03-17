package fraga.com.example.gerenciamento_pedido.security.service;

import fraga.com.example.gerenciamento_pedido.security.domain.Role;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setLogin("testuser");
        user.setPassword("password");
        user.setRoles(new HashSet<Role>());
        user.setActive(true);
    }

    @Test
    void testGenerateTokenComSucesso() {
        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testValidateTokenComSucesso() {
        String token = tokenService.generateToken(user);
        String subject = tokenService.validateToken(token);

        assertNotNull(subject);
        assertEquals("testuser", subject);
    }

    @Test
    void testValidateTokenTokenInvalido() {
        String invalidToken = "invalid.token.string";
        String subject = tokenService.validateToken(invalidToken);

        assertEquals("", subject);
    }
} 