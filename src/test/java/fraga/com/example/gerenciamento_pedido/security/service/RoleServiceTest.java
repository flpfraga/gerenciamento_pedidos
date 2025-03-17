package fraga.com.example.gerenciamento_pedido.security.service;

import fraga.com.example.gerenciamento_pedido.security.domain.Role;
import fraga.com.example.gerenciamento_pedido.security.enums.EUserRole;
import fraga.com.example.gerenciamento_pedido.security.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private List<Role> roles;

    @BeforeEach
    void setUp() {
        Role adminRole = new Role();
        adminRole.setName(EUserRole.ADMIN);

        Role userRole = new Role();
        userRole.setName(EUserRole.USER);

        roles = List.of(adminRole, userRole);
    }

    @Test
    void testGetComSucesso() {
        when(roleRepository.findAll()).thenReturn(roles);

        Set<String> result = roleService.get();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(
            roles.stream().map(role -> role.getName().getRole()).collect(Collectors.toSet()),
            result
        );
    }
} 