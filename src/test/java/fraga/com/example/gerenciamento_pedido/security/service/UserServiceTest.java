package fraga.com.example.gerenciamento_pedido.security.service;

import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.exception.ConflictException;
import fraga.com.example.gerenciamento_pedido.exception.NotFoundException;
import fraga.com.example.gerenciamento_pedido.exception.UnauthorizedException;
import fraga.com.example.gerenciamento_pedido.security.controller.AuthenticationRequest;
import fraga.com.example.gerenciamento_pedido.security.controller.LoginResponse;
import fraga.com.example.gerenciamento_pedido.security.controller.RegisterRequest;
import fraga.com.example.gerenciamento_pedido.security.controller.RegisterResponse;
import fraga.com.example.gerenciamento_pedido.security.domain.Role;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import fraga.com.example.gerenciamento_pedido.security.enums.EUserRole;
import fraga.com.example.gerenciamento_pedido.security.repository.RoleRepository;
import fraga.com.example.gerenciamento_pedido.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private AuthenticationRequest authRequest;
    private RegisterRequest registerRequest;
    private RegisterResponse registerResponse;
    private Role role;
    private Set<EUserRole> roleEnums;

    @BeforeEach
    void setUp() {
        authRequest = new AuthenticationRequest("testuser", "password");
        
        role = new Role();
        role.setId(1L);
        role.setName(EUserRole.USER);
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        
        user = new User();
        user.setId("1");
        user.setLogin("testuser");
        user.setPassword("encodedPassword");
        user.setRoles(roles);
        user.setActive(true);
        
        roleEnums = new HashSet<>();
        roleEnums.add(EUserRole.USER);
        
        registerRequest = new RegisterRequest("newuser", "password", roleEnums);
        registerResponse = new RegisterResponse("2", "newuser", roles);
    }

    @Test
    void testLoginComSucesso() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.generateToken(any(User.class))).thenReturn("test-token");

        LoginResponse response = userService.login(authRequest);

        assertNotNull(response);
        assertEquals("test-token", response.token());
    }

    @Test
    void testLoginUsuarioInativo() {
        Authentication authentication = mock(Authentication.class);
        user.setActive(false);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        assertThrows(UnauthorizedException.class, () -> {
            userService.login(authRequest);
        });
    }

    @Test
    void testLoginUsuarioNaoCadastrado() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new InternalAuthenticationServiceException("User not found"));

        assertThrows(NotFoundException.class, () -> {
            userService.login(authRequest);
        });
    }

    @Test
    void testLoginCredenciaisInvalidas() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(UnauthorizedException.class, () -> {
            userService.login(authRequest);
        });
    }

    @Test
    void testRegisterComSucesso() {
        User newUser = new User();
        newUser.setLogin("newuser");
        
        when(userRepository.findByLogin(anyString())).thenReturn(null);
        when(roleRepository.findByName(any(EUserRole.class))).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(modelMapper.map(any(User.class), any())).thenReturn(registerResponse);

        RegisterResponse response = userService.register(registerRequest);

        assertNotNull(response);
        assertEquals("2", response.getId());
        assertEquals("newuser", response.getLogin());
    }

    @Test
    void testRegisterUsuarioJaCadastrado() {
        assertThrows(BusinessException.class, () -> {
            userService.register(registerRequest);
        });
    }

    @Test
    void testRegisterRoleNaoEncontrada() {
        when(roleRepository.findByName(any(EUserRole.class))).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> {
            userService.register(registerRequest);
        });
    }
} 