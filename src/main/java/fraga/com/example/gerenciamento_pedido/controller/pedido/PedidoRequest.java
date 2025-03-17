package fraga.com.example.gerenciamento_pedido.controller.pedido;

import fraga.com.example.gerenciamento_pedido.enums.EStatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PedidoRequest {
    private EStatusPedido statusPedido;
    private LocalDate inicioPeriodoFaturamento;
    private LocalDate fimPeriodoFaturamento;
}
