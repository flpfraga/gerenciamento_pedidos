package fraga.com.example.gerenciamento_pedido.service;

import fraga.com.example.gerenciamento_pedido.controller.pedido.PedidoResponse;
import fraga.com.example.gerenciamento_pedido.enums.EStatusPedido;
import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.model.Cliente;
import fraga.com.example.gerenciamento_pedido.model.ItemPedido;
import fraga.com.example.gerenciamento_pedido.model.Pedido;
import fraga.com.example.gerenciamento_pedido.model.Produto;
import fraga.com.example.gerenciamento_pedido.repository.PedidoRepository;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PedidoServiceTest {

    private static Produto produtoComEstoque;
    private static Produto produtoSemEstoque;
    private static Cliente cliente;
    private static Pedido pedidoAguardando;
    private static Pedido pedidoConcluido;
    private static ItemPedido itemPedido;
    private static PedidoResponse pedidoResponse;


    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private PedidoService pedidoService;
    @Mock
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produtoComEstoque = new Produto("id", "nome", "desc", new BigDecimal("100"), "categ", 2,
                LocalDateTime.now(), LocalDateTime.now());
        produtoSemEstoque = new Produto("id", "nome", "desc", new BigDecimal("100"), "categ", 0,
                LocalDateTime.now(), LocalDateTime.now());
        cliente = new Cliente("id", "nome", "cpf", new User());
        itemPedido = new ItemPedido(1, produtoComEstoque, pedidoAguardando);
        pedidoAguardando = new Pedido("id", Set.of(itemPedido), LocalDateTime.now(), null, EStatusPedido.AGUARDANDO, cliente);
        pedidoConcluido = new Pedido("id", Set.of(itemPedido), LocalDateTime.now(), null, EStatusPedido.CONCLUIDO, cliente);
        pedidoResponse = new PedidoResponse("id", Map.of(produtoComEstoque, 1), new BigDecimal("100"), EStatusPedido.CONCLUIDO.name());
    }

    @Test
    void testGerarPedidoComSucesso() {
        when(produtoService.buscarPorIdsIn(anySet())).thenReturn(Set.of(produtoComEstoque));
        when(clienteService.buscarClientPorIdUser(anyString())).thenReturn(cliente);
        when(pedidoRepository.save(any())).thenReturn(pedidoAguardando);

        PedidoResponse response = pedidoService.gerarPedido(Map.of("id", 1), "123");
        assertEquals(new BigDecimal("100"), response.getValorTotalPedido());
        assertEquals(EStatusPedido.AGUARDANDO.name(), response.getStatus());
    }

    @Test
    void testGerarPedidoExceptionProdutoSemEstoque() {
        when(produtoService.buscarPorIdsIn(anySet())).thenReturn(Set.of(produtoSemEstoque));

        BusinessException response = assertThrows(BusinessException.class, () ->
                pedidoService.gerarPedido(Map.of("id", 1), "123"));
        assertEquals("Pedido cancelado. Não existe em estoque quantidade suficiente do produto nome", response.getErro().getMensagem());
    }

    @Test
    void testRealizarPagamentoComSucesso() {
        when(produtoService.buscarPorIdsIn(anySet())).thenReturn(Set.of(produtoComEstoque));
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedidoAguardando));
        when(pedidoRepository.save(any())).thenReturn(pedidoConcluido);

        PedidoResponse response = pedidoService.realizarPagamento("123");
        assertEquals(new BigDecimal("100"), response.getValorTotalPedido());
        assertEquals(EStatusPedido.CONCLUIDO.name(), response.getStatus());
    }

    @Test
    void testRealizarPagamentoPedidoStatusConcluidoException() {
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedidoConcluido));

        BusinessException response = assertThrows(BusinessException.class,
                () -> pedidoService.realizarPagamento("123"));
        assertEquals("Não é possível fazer pagamento deste pedido. Status do pedido: CONCLUIDO",
                response.getErro().getMensagem());
    }

    @Test
    void testBuscarPedidosPorClienteComSucesso() {
        when(pedidoRepository.findByClienteId(anyString())).thenReturn(List.of(pedidoAguardando));
        when(clienteService.buscarClientPorIdUser(anyString())).thenReturn(cliente);

        List<PedidoResponse> response = pedidoService.buscarPedidosPorCliente("id", EStatusPedido.AGUARDANDO);
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void testBuscarPedidosPorClienteQualquerStatusComSucesso() {
        when(pedidoRepository.findByClienteId(anyString())).thenReturn(List.of(pedidoAguardando));
        when(clienteService.buscarClientPorIdUser(anyString())).thenReturn(cliente);

        List<PedidoResponse> response = pedidoService.buscarPedidosPorCliente("id", null);
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void testBuscarPedidoPorIdClienteComSucesso() {
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedidoAguardando));
        when(clienteService.buscarClientPorIdUser(anyString())).thenReturn(cliente);
        when(mapper.map(pedidoAguardando, PedidoResponse.class)).thenReturn(pedidoResponse);

        PedidoResponse response = pedidoService.buscarPedidoPorIdCliente("id", "123");
        assertEquals(new BigDecimal("100"), response.getValorTotalPedido());
        assertEquals(EStatusPedido.CONCLUIDO.name(), response.getStatus());
        assertEquals("id", response.getId());
    }

    @Test
    void testBuscarPedidoPorIdClienteExceptionPedidoNaoEncontrado() {
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.empty());

        BusinessException response = assertThrows(BusinessException.class,
                () -> pedidoService.buscarPedidoPorIdCliente("id", "123"));
        assertEquals("Não foi encontrado um pedido com o ID fornecido", response.getErro().getMensagem());
    }

}
