package fraga.com.example.gerenciamento_pedido.service;

import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.repository.strategy.EstatisticaRepository;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.ClienteMaisGastos;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.FaturamentoMensal;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.TicketMedio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstatisticasVendasServiceTest {

    @Mock
    private EstatisticaRepository estatisticaRepository;

    @InjectMocks
    private EstatisticasVendasService estatisticasVendasService;

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
        when(estatisticaRepository.buscarUsuariosMaisCompras()).thenReturn(clienteMaisGastosList);

        List<ClienteMaisGastos> resultado = estatisticasVendasService.buscarUsuariosMaisCompras();

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("João Silva", resultado.get(0).clienteNome());
        assertEquals(new BigDecimal("1500.00"), resultado.get(0).totalGasto());
    }

    @Test
    void testBuscarTicketMedioPorClienteComSucesso() {
        when(estatisticaRepository.buscarTicketMedioPorCliente(anyString())).thenReturn(ticketMedio);

        TicketMedio resultado = estatisticasVendasService.buscarTicketMedioPorCliente("cliente1");

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.clienteNome());
        assertEquals(new BigDecimal("375.00"), resultado.ticket_medio());
    }

    @Test
    void testGetFaturamentoMensalComSucesso() {
        int anoAtual = LocalDate.now().getYear();
        int mesAtual = LocalDate.now().getMonthValue();
        
        when(estatisticaRepository.getFaturamentoMensal(anoAtual, mesAtual)).thenReturn(faturamentoMensal);

        FaturamentoMensal resultado = estatisticasVendasService.getFaturamentoMensal(anoAtual, mesAtual);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("25000.00"), resultado.faturamentoTotal());
    }

    @Test
    void testGetFaturamentoMensalAnoFuturo() {
        int anoFuturo = LocalDate.now().getYear() + 1;
        int mesAtual = LocalDate.now().getMonthValue();

        assertThrows(BusinessException.class, () -> 
            estatisticasVendasService.getFaturamentoMensal(anoFuturo, mesAtual)
        );
    }

    @Test
    void testGetFaturamentoMensalMesFuturo() {
        int anoAtual = LocalDate.now().getYear();
        int mesFuturo = LocalDate.now().getMonthValue() + 1;
        
        // Se estamos em dezembro, ir para janeiro do próximo ano
        if (mesFuturo > 12) {
            mesFuturo = 1;
            anoAtual++;
        }

        final int anoParaTeste = anoAtual;
        final int mesParaTeste = mesFuturo;

        assertThrows(BusinessException.class, () -> 
            estatisticasVendasService.getFaturamentoMensal(anoParaTeste, mesParaTeste)
        );
    }

    @Test
    void testGetFaturamentoMensalMesInvalido() {
        int anoAtual = LocalDate.now().getYear();
        int mesInvalido = 13;

        assertThrows(BusinessException.class, () -> 
            estatisticasVendasService.getFaturamentoMensal(anoAtual, mesInvalido)
        );
    }
} 