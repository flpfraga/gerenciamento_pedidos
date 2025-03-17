package fraga.com.example.gerenciamento_pedido.security.service;

import fraga.com.example.gerenciamento_pedido.security.domain.User;
import fraga.com.example.gerenciamento_pedido.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve carregar um usuário pelo nome de usuário com sucesso")
    void loadUserByUsername_Success() {
        String username = "usuario_teste";
        User user = new User();
        user.setLogin(username);
        user.setPassword("senha123");

        when(userRepository.findByLogin(username)).thenReturn(user);

        UserDetails userDetails = authorizationService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("senha123", userDetails.getPassword());
        verify(userRepository, times(1)).findByLogin(username);
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando usuário não for encontrado")
    void loadUserByUsername_UserNotFound() {
        String username = "usuario_inexistente";
        when(userRepository.findByLogin(username)).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authorizationService.loadUserByUsername(username);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByLogin(username);
    }

    @Test
    @DisplayName("Deve lançar erro ao carregar usuário com valor nulo")
    void loadUserByUsername_NullUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            authorizationService.loadUserByUsername(null);
        });
        verify(userRepository, never()).findByLogin(null);
    }
}

