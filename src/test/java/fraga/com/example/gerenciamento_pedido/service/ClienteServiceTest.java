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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

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

    private User user;
    private ClienteRequest clienteRequest;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId("1");
        user.setLogin("john_doe");

        clienteRequest = new ClienteRequest();
        clienteRequest.setNome("John Doe");
        clienteRequest.setCpf("12345678901");

        cliente = new Cliente();
        cliente.setId("123");
        cliente.setUser(user);
        cliente.setCpf("12345678901");
        cliente.setNome("John Doe");
    }

    @Test
    @DisplayName("Deve cadastrar um novo cliente com sucesso")
    void testCadastrarCliente() {
        when(userRepository.findById("1")).thenReturn(java.util.Optional.of(user));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(mapper.map(cliente, ClienteResponse.class)).thenReturn(new ClienteResponse("123", "John Doe", "12345678901"));

        ClienteResponse clienteResponse = clienteService.cadastrarCliente(clienteRequest, "1");

        assertNotNull(clienteResponse);
        assertEquals("John Doe", clienteResponse.getNome());
        assertEquals("12345678901", clienteResponse.getCpf());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testAtualizar() {
        when(clienteRepository.findByUserId("1")).thenReturn(java.util.Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(mapper.map(cliente, ClienteResponse.class)).thenReturn(new ClienteResponse("123", "John Updated", "12345678901"));

        ClienteRequest updatedRequest = new ClienteRequest();
        updatedRequest.setNome("John Updated");
        ClienteResponse clienteResponse = clienteService.atualizar(updatedRequest, "1");

        assertNotNull(clienteResponse);
        assertEquals("John Updated", clienteResponse.getNome());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testBuscarUserPorId() {
        when(userRepository.findById("1")).thenReturn(java.util.Optional.of(user));

        User foundUser = clienteService.buscarUserPorId("1");

        assertNotNull(foundUser);
        assertEquals("john_doe", foundUser.getUsername());
    }

    @Test
    void testBuscarUserPorIdNotFound() {
        when(userRepository.findById("2")).thenReturn(java.util.Optional.empty());

        assertThrows(BusinessException.class, () -> clienteService.buscarUserPorId("2"));
    }

    @Test
    void testBuscarClientPorIdUser() {
        when(clienteRepository.findByUserId("1")).thenReturn(java.util.Optional.of(cliente));

        Cliente foundCliente = clienteService.buscarClientPorIdUser("1");

        assertNotNull(foundCliente);
        assertEquals("John Doe", foundCliente.getNome());
    }

    @Test
    void testBuscarClientPorIdUserNotFound() {
        when(clienteRepository.findByUserId("2")).thenReturn(java.util.Optional.empty());

        assertThrows(BusinessException.class, () -> clienteService.buscarClientPorIdUser("2"));
    }
}
