package fraga.com.example.gerenciamento_pedido.controller.pedido;

import fraga.com.example.gerenciamento_pedido.model.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponse {
    private String id;
    private Map<Produto, Integer> quantidadePorProduto;
    private BigDecimal valorTotalPedido;
    private String status;
}
