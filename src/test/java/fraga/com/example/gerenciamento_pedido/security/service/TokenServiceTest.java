package fraga.com.example.gerenciamento_pedido.security.service;

import fraga.com.example.gerenciamento_pedido.security.domain.Role;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private User user;
    private static final String SECRET_TEST_KEY = "qt5DRbuVnHHlpHmocTb0AJL9BsyQPpo1edUexquLo_s92FQqacCcw5MjAfY7LtBz4ri0qvyAGQ1bUsTzXLo6fw";

    @BeforeEach
    void setUp() {
        // Configura o valor do secret via reflection
        ReflectionTestUtils.setField(tokenService, "secret", SECRET_TEST_KEY);
        
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