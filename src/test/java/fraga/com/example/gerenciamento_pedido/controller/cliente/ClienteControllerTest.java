package fraga.com.example.gerenciamento_pedido.controller.cliente;

import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import fraga.com.example.gerenciamento_pedido.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private ClienteRequest clienteRequest;
    private ClienteResponse clienteResponse;
    private User user;

    @BeforeEach
    void setUp() {
        clienteRequest = new ClienteRequest();
        clienteRequest.setNome("Nome Teste");
        clienteRequest.setCpf("12345678901");
        
        clienteResponse = new ClienteResponse("1", "Nome Teste", "12345678901");
        user = new User();
        user.setId("user-id-1");
    }

    @Test
    void testCadastrarClienteComSucesso() {
        when(clienteService.cadastrarCliente(any(ClienteRequest.class), anyString()))
                .thenReturn(clienteResponse);

        ResponseEntity<DefaultResponse<ClienteResponse>> response = 
                clienteController.cadastrarCliente(user, clienteRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clienteResponse, response.getBody().getDados());
    }

    @Test
    void testAtualizarComSucesso() {
        when(clienteService.atualizar(any(ClienteRequest.class), anyString()))
                .thenReturn(clienteResponse);

        ResponseEntity<DefaultResponse<ClienteResponse>> response = 
                clienteController.atualizar(user, clienteRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clienteResponse, response.getBody().getDados());
    }
} 