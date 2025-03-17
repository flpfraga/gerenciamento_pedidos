package fraga.com.example.gerenciamento_pedido.repository.estrategia;

import fraga.com.example.gerenciamento_pedido.repository.strategy.EstatisticaRepository;
import fraga.com.example.gerenciamento_pedido.repository.strategy.QueryData;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.ClienteMaisGastos;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.FaturamentoMensal;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.TicketMedio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EstatisticaRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private EstatisticaRepository estatisticaRepository;

    @Test
    void testBuscarUsuariosMaisComprasComSucesso(){
        ClienteMaisGastos cliente = new ClienteMaisGastos(
                "id", "nome", new BigDecimal("100"));
        when(jdbcTemplate.query(
                eq(QueryData.queryBuscarUsuariosMaisCompras()),
                any(RowMapper.class)))
                .thenReturn(List.of(cliente));
        List<ClienteMaisGastos> retorno = estatisticaRepository.buscarUsuariosMaisCompras();
        assertEquals(1, retorno.size());
    }

    @Test
    void testBuscarUsuariosMaisComprasRetornoVazio(){
        when(jdbcTemplate.query(eq(QueryData.queryBuscarUsuariosMaisCompras()), any(RowMapper.class)))
                .thenReturn(List.of());
        List<ClienteMaisGastos> retorno = estatisticaRepository.buscarUsuariosMaisCompras();
        assertEquals(0, retorno.size());
    }

    @Test
    void testTicketMedioPorClienteComSucesso(){
        TicketMedio ticketMedio = new TicketMedio(
                "id", "nome", new BigDecimal("100"));
        String clientId = "1";
        when(jdbcTemplate.queryForObject(
                eq(QueryData.queryBuscarTicketMedioCompraCliente()),
                any(RowMapper.class),
                eq(clientId))
        ).thenReturn(ticketMedio);
        TicketMedio retorno = estatisticaRepository.buscarTicketMedioPorCliente(clientId);
        assertEquals("id", retorno.clienteId());
        assertEquals("nome", retorno.clienteNome());
        assertEquals(new BigDecimal("100"), retorno.ticket_medio());
    }

    @Test
    void testTicketMedioPorClienteRetornoVazio(){
        String clientId = "1";
        when(jdbcTemplate.queryForObject(
                eq(QueryData.queryBuscarTicketMedioCompraCliente()),
                any(RowMapper.class),
                eq(clientId))
        ).thenReturn(null);
        TicketMedio retorno = estatisticaRepository.buscarTicketMedioPorCliente(clientId);
        assertNull(retorno);
    }

    @Test
    void testGetFaturamentoMensalComSucesso(){
        FaturamentoMensal faturamentoMensal = new FaturamentoMensal(new BigDecimal("100"));
        int ano = 2025;
        int mes = 03;
        when(jdbcTemplate.queryForObject(
                eq(QueryData.queryBuscarFaturamentoMensal()),
                any(RowMapper.class),
                eq(ano),
                eq(mes))
        ).thenReturn(faturamentoMensal);
        FaturamentoMensal retorno = estatisticaRepository.getFaturamentoMensal(ano, mes);
        assertEquals(new BigDecimal("100"), retorno.faturamentoTotal());
    }
}
