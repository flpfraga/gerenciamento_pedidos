package fraga.com.example.gerenciamento_pedido.repository.strategy.record;

import java.math.BigDecimal;

public record ClienteMaisGastos(String clienteId, String clienteNome, BigDecimal totalGasto) {
}
