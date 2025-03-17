package fraga.com.example.gerenciamento_pedido.service;

import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.repository.strategy.EstatisticaRepository;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.ClienteMaisGastos;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.FaturamentoMensal;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.TicketMedio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class EstatisticaVendaServiceTest {

    @Mock
    private EstatisticaRepository estatisticaRepository;

    @InjectMocks
    private EstatisticasVendasService estatisticasVendasService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarUsuariosMaisComprasComSucesso(){
        ClienteMaisGastos cliente = new ClienteMaisGastos(
                "id", "nome", new BigDecimal("100"));

        when(estatisticaRepository.buscarUsuariosMaisCompras()).thenReturn(List.of(cliente));
         var retorno = estatisticasVendasService.buscarUsuariosMaisCompras();

         assertEquals(1, retorno.size());
    }
    @Test
    void testBuscarTicketMedioPorClienteComSucesso(){
        TicketMedio ticketMedio = new TicketMedio(
                "id", "nome", new BigDecimal("100"));
        when(estatisticaRepository.buscarTicketMedioPorCliente(anyString())).thenReturn(ticketMedio);
        TicketMedio retorno = estatisticasVendasService.buscarTicketMedioPorCliente("id");
        assertEquals("id", retorno.clienteId());
        assertEquals("nome", retorno.clienteNome());
        assertEquals(new BigDecimal("100"), retorno.ticket_medio());
    }

    @Test
    void testFaturamentoMensalComSucesso(){
        FaturamentoMensal faturamentoMensal = new FaturamentoMensal(new BigDecimal("100"));
        when(estatisticaRepository.getFaturamentoMensal(anyInt(), anyInt())).thenReturn(faturamentoMensal);
        FaturamentoMensal retorno = estatisticasVendasService.getFaturamentoMensal(2025, 3);
        assertEquals(new BigDecimal("100"), retorno.faturamentoTotal());
    }

    @Test
    void testFaturamentoMensalExceptionDefinicaoPeriodo(){
        BusinessException retorno = assertThrows(BusinessException.class, () ->
                estatisticasVendasService.getFaturamentoMensal(2025, 13));

        assertEquals("Ano/mês informado inválido", retorno.getErro().getMensagem());
    }
}
