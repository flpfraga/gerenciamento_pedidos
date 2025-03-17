package fraga.com.example.gerenciamento_pedido.repository.strategy;

import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.ClienteMaisGastos;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.FaturamentoMensal;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.TicketMedio;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@AllArgsConstructor
public class EstatisticaRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<ClienteMaisGastos> buscarUsuariosMaisCompras() {
        return jdbcTemplate.query(
                QueryData.queryBuscarUsuariosMaisCompras(), new EstatisticaRowMapper<>(
                        ClienteMaisGastos.class, rs -> {
                    try {
                        return new ClienteMaisGastos(
                                rs.getString("cliente_id"),
                                rs.getString("nome_cliente"),
                                rs.getBigDecimal("total_gasto"));
                    } catch (SQLException e) {
                        throw new BusinessException("Erro ao buscar clientes com mais gasto.");
                    }
                }));
    }

    public TicketMedio buscarTicketMedioPorCliente(String clientId) {
        return jdbcTemplate.queryForObject(
                QueryData.queryBuscarTicketMedioCompraCliente(), new EstatisticaRowMapper<>(
                        TicketMedio.class, rs -> {
                    try {
                        return new TicketMedio(
                                rs.getString("cliente_id"),
                                rs.getString("cliente_nome"),
                                rs.getBigDecimal("ticket_medio"));
                    } catch (SQLException e) {
                        throw new BusinessException("Erro ao ticket médio do cliente.");
                    }
                }), clientId);
    }

    public FaturamentoMensal getFaturamentoMensal(Integer ano, Integer mes) {
        return jdbcTemplate.queryForObject(
                QueryData.queryBuscarFaturamentoMensal(), new EstatisticaRowMapper<>(
                        FaturamentoMensal.class, rs -> {
                    try {
                        return new FaturamentoMensal(
                                rs.getBigDecimal("faturamento_total"));
                    } catch (SQLException e) {
                        throw new BusinessException("Erro ao ticket médio do cliente.");
                    }
                }), ano, mes);
    }


}
