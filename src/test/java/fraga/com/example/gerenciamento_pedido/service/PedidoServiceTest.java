package fraga.com.example.gerenciamento_pedido.service;

import fraga.com.example.gerenciamento_pedido.controller.pedido.PedidoResponse;
import fraga.com.example.gerenciamento_pedido.enums.EStatusPedido;
import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.exception.ForbiddenException;
import fraga.com.example.gerenciamento_pedido.model.Cliente;
import fraga.com.example.gerenciamento_pedido.model.ItemPedido;
import fraga.com.example.gerenciamento_pedido.model.Pedido;
import fraga.com.example.gerenciamento_pedido.model.Produto;
import fraga.com.example.gerenciamento_pedido.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;
    private Cliente cliente;
    private Produto produto1;
    private Produto produto2;
    private Set<ItemPedido> itensPedido;
    private Map<String, Integer> quantidadePorProdutoId;
    private PedidoResponse pedidoResponse;

    @BeforeEach
    void setUp() {
        // Setup cliente
        cliente = new Cliente();
        cliente.setId("cliente-1");
        cliente.setNome("João Silva");
        cliente.setCpf("12345678901");

        // Setup produtos
        produto1 = new Produto();
        produto1.setId("produto-1");
        produto1.setNome("Produto 1");
        produto1.setPreco(new BigDecimal("100.00"));
        produto1.setQuantidadeEstoque(10);

        produto2 = new Produto();
        produto2.setId("produto-2");
        produto2.setNome("Produto 2");
        produto2.setPreco(new BigDecimal("50.00"));
        produto2.setQuantidadeEstoque(20);

        // Setup pedido
        pedido = new Pedido();
        pedido.setId("pedido-1");
        pedido.setCliente(cliente);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatusPedido(EStatusPedido.AGUARDANDO);

        // Setup itens pedido
        itensPedido = new HashSet<>();
        ItemPedido item1 = new ItemPedido();
        item1.setProduto(produto1);
        item1.setQuantidade(2);
        item1.setPedido(pedido);

        ItemPedido item2 = new ItemPedido();
        item2.setProduto(produto2);
        item2.setQuantidade(3);
        item2.setPedido(pedido);

        itensPedido.add(item1);
        itensPedido.add(item2);
        pedido.setItemsPedidos(itensPedido);

        // Setup mapa de quantidades
        quantidadePorProdutoId = new HashMap<>();
        quantidadePorProdutoId.put("produto-1", 2);
        quantidadePorProdutoId.put("produto-2", 3);

        // Setup pedido response
        Map<Produto, Integer> quantidadePorProduto = new HashMap<>();
        quantidadePorProduto.put(produto1, 2);
        quantidadePorProduto.put(produto2, 3);

        pedidoResponse = new PedidoResponse(
                "pedido-1",
                quantidadePorProduto,
                new BigDecimal("350.00"),
                EStatusPedido.AGUARDANDO.name()
        );
    }

    @Test
    void testGerarPedidoComSucesso() {
        // Arrange
        Set<Produto> produtos = new HashSet<>();
        produtos.add(produto1);
        produtos.add(produto2);

        when(produtoService.buscarPorIdsIn(any())).thenReturn(produtos);
        when(clienteService.buscarClientPorIdUser(anyString())).thenReturn(cliente);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Act
        PedidoResponse result = pedidoService.gerarPedido(quantidadePorProdutoId, "user-id-1");

        // Assert
        assertNotNull(result);
        verify(produtoService, times(1)).buscarPorIdsIn(any());
        verify(clienteService, times(1)).buscarClientPorIdUser(anyString());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testGerarPedidoEstoqueInsuficiente() {
        // Arrange
        produto1.setQuantidadeEstoque(1); // Quantidade disponível menor que a solicitada (2)
        Set<Produto> produtos = new HashSet<>();
        produtos.add(produto1);
        produtos.add(produto2);

        when(produtoService.buscarPorIdsIn(any())).thenReturn(produtos);

        // Act & Assert
        assertThrows(BusinessException.class, () ->
                pedidoService.gerarPedido(quantidadePorProdutoId, "user-id-1"));
    }

    @Test
    void testRealizarPagamentoComSucesso() {
        // Arrange
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(produtoService.buscarPorIdsIn(any())).thenReturn(Set.of(produto1, produto2));

        // Act
        PedidoResponse result = pedidoService.realizarPagamento("pedido-1");

        // Assert
        assertNotNull(result);
        assertEquals(EStatusPedido.CONCLUIDO.name(), result.getStatus());
        verify(pedidoRepository, times(1)).findById(anyString());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testRealizarPagamentoPedidoNaoEncontrado() {
        // Arrange
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () ->
                pedidoService.realizarPagamento("pedido-inexistente"));
    }

    @Test
    void testRealizarPagamentoPedidoJaConcluido() {
        // Arrange
        pedido.setStatusPedido(EStatusPedido.CONCLUIDO);
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedido));

        // Act & Assert
        assertThrows(BusinessException.class, () ->
                pedidoService.realizarPagamento("pedido-1"));
    }

    @Test
    void testBuscarPedidosPorClienteComSucesso() {
        // Arrange
        List<Pedido> pedidos = List.of(pedido);
        when(clienteService.buscarClientPorIdUser(anyString())).thenReturn(cliente);
        when(pedidoRepository.findByClienteId(anyString())).thenReturn(pedidos);

        // Act
        List<PedidoResponse> result = pedidoService.buscarPedidosPorCliente("user-id-1", EStatusPedido.AGUARDANDO);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(clienteService, times(1)).buscarClientPorIdUser(anyString());
        verify(pedidoRepository, times(1)).findByClienteId(anyString());
    }

    @Test
    void testBuscarPedidosPorClienteSemFiltroStatus() {
        // Arrange
        List<Pedido> pedidos = List.of(pedido);
        when(clienteService.buscarClientPorIdUser(anyString())).thenReturn(cliente);
        when(pedidoRepository.findByClienteId(anyString())).thenReturn(pedidos);

        // Act
        List<PedidoResponse> result = pedidoService.buscarPedidosPorCliente("user-id-1", null);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(clienteService, times(1)).buscarClientPorIdUser(anyString());
        verify(pedidoRepository, times(1)).findByClienteId(anyString());
    }

    @Test
    void testBuscarPedidoPorIdClienteComSucesso() {
        // Arrange
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedido));
        when(clienteService.buscarClientPorIdUser(anyString())).thenReturn(cliente);
        when(mapper.map(any(Pedido.class), eq(PedidoResponse.class))).thenReturn(pedidoResponse);

        // Act
        PedidoResponse result = pedidoService.buscarPedidoPorIdCliente("user-id-1", "pedido-1");

        // Assert
        assertNotNull(result);
        verify(pedidoRepository, times(1)).findById(anyString());
        verify(clienteService, times(1)).buscarClientPorIdUser(anyString());
        verify(mapper, times(1)).map(any(Pedido.class), eq(PedidoResponse.class));
    }

    @Test
    void testBuscarPedidoPorIdClientePedidoNaoEncontrado() {
        // Arrange
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () ->
                pedidoService.buscarPedidoPorIdCliente("user-id-1", "pedido-inexistente"));
    }

    @Test
    void testBuscarPedidoPorIdClientePedidoDeOutroCliente() {
        // Arrange
        Cliente outroCliente = new Cliente();
        outroCliente.setId("outro-cliente");

        when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedido));
        when(clienteService.buscarClientPorIdUser(anyString())).thenReturn(outroCliente);

        // Act & Assert
        assertThrows(ForbiddenException.class, () ->
                pedidoService.buscarPedidoPorIdCliente("user-id-2", "pedido-1"));
    }
} 