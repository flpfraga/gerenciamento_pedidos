package fraga.com.example.gerenciamento_pedido.controller.estatistica;

import fraga.com.example.gerenciamento_pedido.controller.DefaultController;
import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.ClienteMaisGastos;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.FaturamentoMensal;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.TicketMedio;
import fraga.com.example.gerenciamento_pedido.service.EstatisticasVendasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Estatísticas de Vendas", description = "Endpoints para consulta de estatísticas e relatórios de vendas")
public class EstatisticasVendasController implements DefaultController {

    private final EstatisticasVendasService estatisticasVendasService;

    /**
     * Busca os 5 clientes que mais realizaram compras no sistema.
     * 
     * @return ResponseEntity contendo um DefaultResponse com a lista dos 5 clientes que mais gastaram
     */
    @Operation(
        summary = "Top 5 clientes em compras",
        description = "Retorna os 5 clientes que mais realizaram compras no sistema, ordenados pelo valor total gasto."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista dos top 5 clientes retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para realizar esta operação"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/top_5-clientes")
    public ResponseEntity<DefaultResponse<List<ClienteMaisGastos>>> buscarUsuariosMaiorNumeroCompras() {
        return success(estatisticasVendasService.buscarUsuariosMaisCompras());
    }

    /**
     * Busca o ticket médio de compras de um cliente específico.
     * 
     * @param id ID do cliente para o qual se deseja calcular o ticket médio
     * @return ResponseEntity contendo um DefaultResponse com o TicketMedio do cliente
     */
    @Operation(
        summary = "Ticket médio por cliente",
        description = "Calcula o valor médio das compras realizadas por um cliente específico."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ticket médio calculado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para realizar esta operação"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/ticket_medio_cliente/{id}")
    public ResponseEntity<DefaultResponse<TicketMedio>> buscarTicketMedioPorCliente(
            @PathVariable("id") String id) {
        return success(estatisticasVendasService.buscarTicketMedioPorCliente(id));
    }

    /**
     * Obtém o faturamento mensal para um mês e ano específicos.
     * 
     * @param ano Ano para o qual se deseja obter o faturamento
     * @param mes Mês para o qual se deseja obter o faturamento (1 a 12)
     * @return ResponseEntity contendo um DefaultResponse com o FaturamentoMensal
     */
    @Operation(
        summary = "Faturamento mensal",
        description = "Calcula o faturamento total em um mês e ano específicos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Faturamento mensal calculado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Mês ou ano inválido/futuro"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para realizar esta operação"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/faturamento_mes")
    public ResponseEntity<DefaultResponse<FaturamentoMensal>> getFaturamentoMensal(
            @PathParam("ano") Integer ano,
            @PathParam("mes") Integer mes) {
        return success(estatisticasVendasService.getFaturamentoMensal(ano,mes));
    }
}
