package fraga.com.example.gerenciamento_pedido.service;

import fraga.com.example.gerenciamento_pedido.controller.pedido.PedidoResponse;
import fraga.com.example.gerenciamento_pedido.enums.EStatusPedido;
import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.exception.ForbiddenException;
import fraga.com.example.gerenciamento_pedido.model.ItemPedido;
import fraga.com.example.gerenciamento_pedido.model.Pedido;
import fraga.com.example.gerenciamento_pedido.model.Produto;
import fraga.com.example.gerenciamento_pedido.repository.PedidoRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;
    private final ModelMapper mapper;

    @Transactional
    public PedidoResponse gerarPedido(Map<String, Integer> pedidoRequest, String clienteId) {
        Pedido pedido = new Pedido();
        Map<String, Produto> produtosPorId = produtoService.buscarPorIdsIn(pedidoRequest.keySet()).stream()
                .collect(Collectors.toMap(Produto::getId, produto -> produto));
        gerenciarEstoquePorStatusPedido(pedidoRequest, produtosPorId, EStatusPedido.AGUARDANDO);
        Set<ItemPedido> itemsPedidos = gerarItemPedido(pedidoRequest, produtosPorId, pedido);

        var cliente = clienteService.buscarClientPorIdUser(clienteId);
        pedido.setItemsPedidos(itemsPedidos);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatusPedido(EStatusPedido.AGUARDANDO);
        pedido.setCliente(cliente);
        pedido = pedidoRepository.save(pedido);

        return criarPedidoResponse(pedido, itemsPedidos);
    }

    @Transactional
    public PedidoResponse realizarPagamento(String id) {
        var pedido = buscarPorId(id, Boolean.TRUE);

        Map<String, Integer> quantidadeIdProduto = pedido.getItemsPedidos().stream().collect(Collectors.toMap(
                item -> item.getProduto().getId(), ItemPedido::getQuantidade));
        statuPedidoAguardando(pedido);
        gerenciarEstoquePorStatusPedido(quantidadeIdProduto, EStatusPedido.CONCLUIDO);

        pedido.setDataPagamento(LocalDateTime.now());
        pedido.setStatusPedido(EStatusPedido.CONCLUIDO);
        pedido = pedidoRepository.save(pedido);
        return criarPedidoResponse(pedido, pedido.getItemsPedidos());
    }

    private static void statuPedidoAguardando(Pedido pedido) {
        if (!pedido.getStatusPedido().equals(EStatusPedido.AGUARDANDO)) {
            throw new BusinessException("Não é possível fazer pagamento deste pedido. Status do pedido: "
                    + pedido.getStatusPedido());
        }
    }

    public List<PedidoResponse> buscarPedidosPorCliente(String clienteId, @Nullable EStatusPedido statusPedido) {
        var cliente = clienteService.buscarClientPorIdUser(clienteId);
        List<Pedido> pedidos = pedidoRepository.findByClienteId(cliente.getId());

        if (ObjectUtils.isEmpty(statusPedido)) {
            return pedidos.stream().map(pedido -> criarPedidoResponse(pedido, pedido.getItemsPedidos()))
                    .collect(Collectors.toList());
        }
        return pedidos.stream().filter(pedido -> pedido.getStatusPedido().equals(statusPedido))
                .map(pedido -> criarPedidoResponse(pedido, pedido.getItemsPedidos()))
                .collect(Collectors.toList());
    }

    public PedidoResponse buscarPedidoPorIdCliente(String clienteId, String pedidoId) {
        var pedido = buscarPorId(pedidoId, Boolean.TRUE);
        var cliente = clienteService.buscarClientPorIdUser(clienteId);
        if (pedido.getCliente().equals(cliente)) {
            return mapper.map(pedido, PedidoResponse.class);
        }
        throw new ForbiddenException("Esse pedido não pertence a esse usuário");
    }

    private Pedido buscarPorId(String id, Boolean throwable) {
        return pedidoRepository.findById(id).orElseGet(
                () -> {
                    if (throwable) {
                        throw new BusinessException("Não foi encontrado um pedido com o ID fornecido");
                    }
                    return null;
                }
        );
    }

    private BigDecimal calcularTotalPedido(Set<ItemPedido> itemsPedidos) {
        return itemsPedidos.stream().map(item -> item.getProduto().getPreco().multiply(
                new BigDecimal(item.getQuantidade()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Set<ItemPedido> gerarItemPedido(Map<String, Integer> quantidadeIdProduto,
                                            Map<String, Produto> produtosPorId,
                                            Pedido pedido) {
        return quantidadeIdProduto.entrySet().stream().map(item -> new ItemPedido(
                        item.getValue(), produtosPorId.get(item.getKey()), pedido))
                .collect(Collectors.toSet());
    }

    private void gerenciarEstoquePorStatusPedido(Map<String, Integer> quantidadePorProduto, EStatusPedido statusPedido) {
        Map<String, Produto> produtosPorId = produtoService.buscarPorIdsIn(quantidadePorProduto.keySet()).stream()
                .collect(Collectors.toMap(Produto::getId, produto -> produto));
        gerenciarEstoquePorStatusPedido(quantidadePorProduto, produtosPorId, statusPedido);
    }

    private void gerenciarEstoquePorStatusPedido(Map<String, Integer> quantidadePorProduto,
                                                 Map<String, Produto> produtosPorId,
                                                 EStatusPedido statusPedido) {
        quantidadePorProduto.forEach((key, value) -> {
            var produto = produtosPorId.get(key);
            int estoquePosVenda = ehPossivelVender(produto, value);

            if (statusPedido.equals(EStatusPedido.CONCLUIDO)) {
                produto.setQuantidadeEstoque(estoquePosVenda);
            }
        });
    }

    private Integer ehPossivelVender(Produto produto, Integer vendido) {
        int estoquePosVenda = produto.getQuantidadeEstoque() - vendido;
        if (estoquePosVenda >= 0) {
            return estoquePosVenda;
        }
        throw new BusinessException(
                "Pedido cancelado. Não existe em estoque quantidade suficiente do produto " + produto.getNome());
    }

    private PedidoResponse criarPedidoResponse(Pedido pedido, Set<ItemPedido> itemPedido) {
        return new PedidoResponse(
                pedido.getId(),
                itemPedido.stream().collect(
                        Collectors.toMap(ItemPedido::getProduto, ItemPedido::getQuantidade)
                ),
                calcularTotalPedido(itemPedido),
                pedido.getStatusPedido().name());
    }
}
