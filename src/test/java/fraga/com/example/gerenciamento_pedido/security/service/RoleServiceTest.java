package fraga.com.example.gerenciamento_pedido.security.service;

import fraga.com.example.gerenciamento_pedido.security.domain.Role;
import fraga.com.example.gerenciamento_pedido.security.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar lista de roles com sucesso")
    void get_Success() {
        // Arrange
        Role roleAdmin = new Role();
        roleAdmin.setName(RoleEnum.ADMIN);

        Role roleUser = new Role();
        roleUser.setName(RoleEnum.USER);

        when(roleRepository.findAll()).thenReturn(List.of(roleAdmin, roleUser));

        // Act
        Set<String> roles = roleService.get();

        // Assert
        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertTrue(roles.contains("ADMIN"));
        assertTrue(roles.contains("USER"));
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar um conjunto vazio quando não houver roles")
    void get_EmptyList() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(List.of());

        // Act
        Set<String> roles = roleService.get();

        // Assert
        assertNotNull(roles);
        assertTrue(roles.isEmpty());
        verify(roleRepository, times(1)).findAll(); // Verifica se o método foi chamado
    }

    @Test
    @DisplayName("Deve lançar exceção em caso de erro interno")
    void get_InternalServerError() {
        // Arrange
        when(roleRepository.findAll()).thenThrow(new RuntimeException("Erro no banco de dados"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roleService.get();
        });

        assertEquals("Erro no banco de dados", exception.getMessage());
        verify(roleRepository, times(1)).findAll(); // Verifica se o método foi chamado
    }
}

