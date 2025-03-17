package fraga.com.example.gerenciamento_pedido.controller.estatistica;

import fraga.com.example.gerenciamento_pedido.controller.DefaultController;
import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.ClienteMaisGastos;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.FaturamentoMensal;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.TicketMedio;
import fraga.com.example.gerenciamento_pedido.service.EstatisticasVendasService;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/estatisticas")
@AllArgsConstructor
public class EstatisticasVendasController implements DefaultController {

    private final EstatisticasVendasService estatisticasVendasService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/top_5-clientes")
    public ResponseEntity<DefaultResponse<List<ClienteMaisGastos>>> buscarUsuariosMaiorNumeroCompras() {
        return success(estatisticasVendasService.buscarUsuariosMaisCompras());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/ticket_medio_cliente/{id}")
    public ResponseEntity<DefaultResponse<TicketMedio>> buscarTicketMedioPorCliente(
            @PathVariable("id") String id) {
        return success(estatisticasVendasService.buscarTicketMedioPorCliente(id));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/faturamento_mes")
    public ResponseEntity<DefaultResponse<FaturamentoMensal>> getFaturamentoMensal(
            @PathParam("ano") Integer ano,
            @PathParam("mes") Integer mes) {
        return success(estatisticasVendasService.getFaturamentoMensal(ano,mes));
    }
}
