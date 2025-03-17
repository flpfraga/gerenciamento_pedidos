package fraga.com.example.gerenciamento_pedido.repository.strategy.record;

import java.math.BigDecimal;

public record TicketMedio(String clienteId, String clienteNome, BigDecimal ticket_medio) {
}
