package fraga.com.example.gerenciamento_pedido.controller.estatistica;

import fraga.com.example.gerenciamento_pedido.repository.strategy.record.ClienteMaisGastos;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.FaturamentoMensal;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.TicketMedio;
import fraga.com.example.gerenciamento_pedido.service.EstatisticasVendasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstatisticasVendasController.class)
class EstatisticasVendasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EstatisticasVendasService estatisticasVendasService;

    @InjectMocks
    private EstatisticasVendasController estatisticasVendasController;

    private ClienteMaisGastos cliente1, cliente2;
    private TicketMedio ticketMedio;
    private FaturamentoMensal faturamentoMensal;

    @BeforeEach
    void setUp() {
        cliente1 = new ClienteMaisGastos("Cliente A", "Cliente name", new BigDecimal("500"));
        cliente2 = new ClienteMaisGastos("Cliente B", "Cliente name", new BigDecimal("300"));

        ticketMedio = new TicketMedio("Cliente A", "Cliente name",new BigDecimal("250"));
        faturamentoMensal = new FaturamentoMensal(new BigDecimal("10000"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testBuscarUsuariosMaiorNumeroCompras() throws Exception {
        when(estatisticasVendasService.buscarUsuariosMaisCompras()).thenReturn(Arrays.asList(cliente1, cliente2));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/estatisticas/top_5-clientes"));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].nome").value("Cliente A"))
                .andExpect(jsonPath("$.data[0].valorTotalGasto").value(500))
                .andExpect(jsonPath("$.data[1].nome").value("Cliente B"))
                .andExpect(jsonPath("$.data[1].valorTotalGasto").value(300));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testBuscarTicketMedioPorCliente() throws Exception {
        when(estatisticasVendasService.buscarTicketMedioPorCliente("123")).thenReturn(ticketMedio);

        mockMvc.perform(get("/api/v1/estatisticas/ticket_medio_cliente/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("Cliente A"))
                .andExpect(jsonPath("$.data.ticketMedio").value(250));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testGetFaturamentoMensal() throws Exception {
        when(estatisticasVendasService.getFaturamentoMensal(2025, 3)).thenReturn(faturamentoMensal);

        mockMvc.perform(get("/api/v1/estatisticas/faturamento_mes?ano=2025&mes=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ano").value(2025))
                .andExpect(jsonPath("$.data.mes").value(3))
                .andExpect(jsonPath("$.data.faturamento").value(10000));
    }

    @Test
    void testAcessoNegadoSemPermissao() throws Exception {
        mockMvc.perform(get("/api/v1/estatisticas/top_5-clientes"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/v1/estatisticas/ticket_medio_cliente/123"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/v1/estatisticas/faturamento_mes?ano=2025&mes=3"))
                .andExpect(status().isForbidden());
    }
}
