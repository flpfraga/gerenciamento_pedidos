package fraga.com.example.gerenciamento_pedido.service;

import fraga.com.example.gerenciamento_pedido.controller.cliente.ClienteRequest;
import fraga.com.example.gerenciamento_pedido.controller.cliente.ClienteResponse;
import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.model.Cliente;
import fraga.com.example.gerenciamento_pedido.repository.ClienteRepository;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import fraga.com.example.gerenciamento_pedido.security.repository.UserRepository;
import fraga.com.example.gerenciamento_pedido.security.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteRequest clienteRequest;
    private Cliente cliente;
    private User user;
    private ClienteResponse clienteResponse;

    @BeforeEach
    void setUp() {
        clienteRequest = new ClienteRequest();
        clienteRequest.setNome("Nome Teste");
        clienteRequest.setCpf("12345678901");

        user = new User();
        user.setId("user-id-1");

        cliente = new Cliente();
        cliente.setId("1");
        cliente.setNome("Nome Teste");
        cliente.setCpf("12345678901");
        cliente.setUser(user);

        clienteResponse = new ClienteResponse("1", "Nome Teste", "12345678901");
    }

    @Test
    void testCadastrarClienteComSucesso() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(mapper.map(any(Cliente.class), any())).thenReturn(clienteResponse);

        ClienteResponse response = clienteService.cadastrarCliente(clienteRequest, "user-id-1");

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals("Nome Teste", response.getNome());
        assertEquals("12345678901", response.getCpf());
    }

    @Test
    void testCadastrarClienteErroUsuarioNaoEncontrado() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            clienteService.cadastrarCliente(clienteRequest, "user-id-inexistente");
        });
    }

    @Test
    void testAtualizarComSucesso() {
        when(clienteRepository.findByUserId(anyString())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(mapper.map(any(Cliente.class), any())).thenReturn(clienteResponse);

        ClienteResponse response = clienteService.atualizar(clienteRequest, "user-id-1");

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals("Nome Teste", response.getNome());
    }

    @Test
    void testAtualizarErroClienteNaoEncontrado() {
        when(clienteRepository.findByUserId(anyString())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            clienteService.atualizar(clienteRequest, "user-id-inexistente");
        });
    }

    @Test
    void testBuscarUserPorIdComSucesso() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        User userEncontrado = clienteService.buscarUserPorId("user-id-1");

        assertNotNull(userEncontrado);
        assertEquals("user-id-1", userEncontrado.getId());
    }

    @Test
    void testBuscarUserPorIdErroUsuarioNaoEncontrado() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            clienteService.buscarUserPorId("user-id-inexistente");
        });
    }

    @Test
    void testBuscarClientePorIdUserComSucesso() {
        when(clienteRepository.findByUserId(anyString())).thenReturn(Optional.of(cliente));

        Cliente clienteEncontrado = clienteService.buscarClientPorIdUser("user-id-1");

        assertNotNull(clienteEncontrado);
        assertEquals("1", clienteEncontrado.getId());
    }

    @Test
    void testBuscarClientePorIdUserErroClienteNaoEncontrado() {
        when(clienteRepository.findByUserId(anyString())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            clienteService.buscarClientPorIdUser("user-id-inexistente");
        });
    }
} 