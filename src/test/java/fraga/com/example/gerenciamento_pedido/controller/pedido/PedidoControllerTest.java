package fraga.com.example.gerenciamento_pedido.controller.pedido;

import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.enums.EStatusPedido;
import fraga.com.example.gerenciamento_pedido.model.Produto;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import fraga.com.example.gerenciamento_pedido.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    private PedidoRequest pedidoRequest;
    private PedidoResponse pedidoResponse;
    private User user;
    private List<PedidoResponse> pedidoResponses;
    private Map<String, Integer> mapaQuantidadesProdutos;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user-id-1");

        // Mapa de produtos para cadastro
        mapaQuantidadesProdutos = new HashMap<>();
        mapaQuantidadesProdutos.put("produto-1", 2);
        mapaQuantidadesProdutos.put("produto-2", 3);

        // Request para busca
        pedidoRequest = new PedidoRequest();
        pedidoRequest.setStatusPedido(EStatusPedido.AGUARDANDO);
        pedidoRequest.setInicioPeriodoFaturamento(LocalDate.now().minusDays(30));
        pedidoRequest.setFimPeriodoFaturamento(LocalDate.now());

        // Response de pedido
        Produto produto1 = new Produto();
        produto1.setId("produto-1");
        produto1.setNome("Produto 1");
        produto1.setPreco(new BigDecimal("100.00"));

        Produto produto2 = new Produto();
        produto2.setId("produto-2");
        produto2.setNome("Produto 2");
        produto2.setPreco(new BigDecimal("50.00"));

        Map<Produto, Integer> quantidadePorProduto = new HashMap<>();
        quantidadePorProduto.put(produto1, 2);
        quantidadePorProduto.put(produto2, 3);

        pedidoResponse = new PedidoResponse();
        pedidoResponse.setId("pedido-1");
        pedidoResponse.setQuantidadePorProduto(quantidadePorProduto);
        pedidoResponse.setValorTotalPedido(new BigDecimal("350.00"));
        pedidoResponse.setStatus("AGUARDANDO");

        pedidoResponses = new ArrayList<>();
        pedidoResponses.add(pedidoResponse);
    }

    @Test
    void testCadastrarPedidoComSucesso() {
        when(pedidoService.gerarPedido(any(Map.class), anyString()))
                .thenReturn(pedidoResponse);

        ResponseEntity<DefaultResponse<PedidoResponse>> response = 
                pedidoController.cadastrarPedido(user, mapaQuantidadesProdutos);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidoResponse, response.getBody().getDados());
    }

    @Test
    void testRealizarPagamentoComSucesso() {
        when(pedidoService.realizarPagamento(anyString()))
                .thenReturn(pedidoResponse);

        ResponseEntity<DefaultResponse<PedidoResponse>> response = 
                pedidoController.realizarPagamento("pedido-1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidoResponse, response.getBody().getDados());
    }

    @Test
    void testBuscarPedidosPorClienteComSucesso() {
        when(pedidoService.buscarPedidosPorCliente(anyString(), any(EStatusPedido.class)))
                .thenReturn(pedidoResponses);

        ResponseEntity<DefaultResponse<List<PedidoResponse>>> response = 
                pedidoController.buscarPedidosPorCLiente(pedidoRequest, user);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidoResponses, response.getBody().getDados());
    }

    @Test
    void testBuscarPedidoPorIdComSucesso() {
        when(pedidoService.buscarPedidoPorIdCliente(anyString(), anyString()))
                .thenReturn(pedidoResponse);

        ResponseEntity<DefaultResponse<PedidoResponse>> response = 
                pedidoController.buscarPedidoPorId("pedido-1", user);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidoResponse, response.getBody().getDados());
    }
} 