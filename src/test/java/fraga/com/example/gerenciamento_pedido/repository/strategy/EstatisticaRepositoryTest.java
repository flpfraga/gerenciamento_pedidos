package fraga.com.example.gerenciamento_pedido.repository.strategy;

import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.ClienteMaisGastos;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.FaturamentoMensal;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.TicketMedio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstatisticaRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private EstatisticaRepository estatisticaRepository;

    private List<ClienteMaisGastos> clienteMaisGastosList;
    private TicketMedio ticketMedio;
    private FaturamentoMensal faturamentoMensal;

    @BeforeEach
    void setUp() {
        clienteMaisGastosList = Arrays.asList(
                new ClienteMaisGastos("cliente1", "João Silva", new BigDecimal("1500.00")),
                new ClienteMaisGastos("cliente2", "Maria Souza", new BigDecimal("1200.00")),
                new ClienteMaisGastos("cliente3", "Carlos Oliveira", new BigDecimal("950.00"))
        );

        ticketMedio = new TicketMedio("cliente1", "João Silva", new BigDecimal("375.00"));

        faturamentoMensal = new FaturamentoMensal(new BigDecimal("25000.00"));
    }

    @Test
    void testBuscarUsuariosMaisComprasComSucesso() {
        when(jdbcTemplate.query(eq(QueryData.queryBuscarUsuariosMaisCompras()), any(RowMapper.class)))
                .thenReturn(clienteMaisGastosList);

        List<ClienteMaisGastos> resultado = estatisticaRepository.buscarUsuariosMaisCompras();

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("João Silva", resultado.get(0).clienteNome());
        assertEquals(new BigDecimal("1500.00"), resultado.get(0).totalGasto());
    }

    @Test
    void testBuscarTicketMedioPorClienteComSucesso() {
        when(jdbcTemplate.queryForObject(
                eq(QueryData.queryBuscarTicketMedioCompraCliente()),
                any(RowMapper.class),
                eq("cliente1")
        )).thenReturn(ticketMedio);

        TicketMedio resultado = estatisticaRepository.buscarTicketMedioPorCliente("cliente1");

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.clienteNome());
        assertEquals(new BigDecimal("375.00"), resultado.ticket_medio());
    }

    @Test
    void testGetFaturamentoMensalComSucesso() {
        Integer ano = 2023;
        Integer mes = 5;

        when(jdbcTemplate.queryForObject(
                eq(QueryData.queryBuscarFaturamentoMensal()),
                any(RowMapper.class),
                eq(ano),
                eq(mes)
        )).thenReturn(faturamentoMensal);

        FaturamentoMensal resultado = estatisticaRepository.getFaturamentoMensal(ano, mes);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("25000.00"), resultado.faturamentoTotal());
    }

    @Test
    void testBuscarTicketMedioPorClienteClienteNaoEncontrado() {
        when(jdbcTemplate.queryForObject(
                eq(QueryData.queryBuscarTicketMedioCompraCliente()),
                any(RowMapper.class),
                eq("clienteInexistente")
        )).thenReturn(null);

        TicketMedio resultado = estatisticaRepository.buscarTicketMedioPorCliente("clienteInexistente");

        assertNull(resultado);
    }

    @Test
    void testGetFaturamentoMensalErroSQL() {
        Integer ano = 2023;
        Integer mes = 5;

        when(jdbcTemplate.queryForObject(
                eq(QueryData.queryBuscarFaturamentoMensal()),
                any(RowMapper.class),
                eq(ano),
                eq(mes)
        )).thenThrow(new RuntimeException("Erro de SQL"));

        assertThrows(RuntimeException.class, () -> 
            estatisticaRepository.getFaturamentoMensal(ano, mes)
        );
    }
} 