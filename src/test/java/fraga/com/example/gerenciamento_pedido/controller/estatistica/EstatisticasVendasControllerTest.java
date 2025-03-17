package fraga.com.example.gerenciamento_pedido.controller.estatistica;

import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.ClienteMaisGastos;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.FaturamentoMensal;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.TicketMedio;
import fraga.com.example.gerenciamento_pedido.service.EstatisticasVendasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstatisticasVendasControllerTest {

    @Mock
    private EstatisticasVendasService estatisticasVendasService;

    @InjectMocks
    private EstatisticasVendasController controller;

    private List<ClienteMaisGastos> listaClientes;
    private TicketMedio ticketMedio;
    private FaturamentoMensal faturamentoMensal;

    @BeforeEach
    void setUp() {
        // Prepara dados para o caso de teste dos top 5 clientes
        listaClientes = new ArrayList<>();
        listaClientes.add(new ClienteMaisGastos("cliente1", "João Silva", new BigDecimal("1500.00")));
        listaClientes.add(new ClienteMaisGastos("cliente2", "Maria Souza", new BigDecimal("1200.00")));
        listaClientes.add(new ClienteMaisGastos("cliente3", "Carlos Oliveira", new BigDecimal("950.00")));
        listaClientes.add(new ClienteMaisGastos("cliente4", "Ana Santos", new BigDecimal("800.00")));
        listaClientes.add(new ClienteMaisGastos("cliente5", "Paulo Ferreira", new BigDecimal("750.00")));

        // Prepara dados para o caso de teste do ticket médio
        ticketMedio = new TicketMedio("cliente1", "João Silva", new BigDecimal("375.00"));

        // Prepara dados para o caso de teste do faturamento mensal
        faturamentoMensal = new FaturamentoMensal(new BigDecimal("25000.00"));
    }

    @Test
    void testBuscarUsuariosMaiorNumeroComprasComSucesso() {
        when(estatisticasVendasService.buscarUsuariosMaisCompras()).thenReturn(listaClientes);

        ResponseEntity<DefaultResponse<List<ClienteMaisGastos>>> response = 
                controller.buscarUsuariosMaiorNumeroCompras();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().getDados().size());
        assertEquals(listaClientes, response.getBody().getDados());
    }

    @Test
    void testBuscarTicketMedioPorClienteComSucesso() {
        when(estatisticasVendasService.buscarTicketMedioPorCliente(anyString())).thenReturn(ticketMedio);

        ResponseEntity<DefaultResponse<TicketMedio>> response = 
                controller.buscarTicketMedioPorCliente("cliente1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketMedio, response.getBody().getDados());
        assertEquals("João Silva", response.getBody().getDados().clienteNome());
        assertEquals(new BigDecimal("375.00"), response.getBody().getDados().ticket_medio());
    }

    @Test
    void testGetFaturamentoMensalComSucesso() {
        when(estatisticasVendasService.getFaturamentoMensal(any(Integer.class), any(Integer.class)))
                .thenReturn(faturamentoMensal);

        ResponseEntity<DefaultResponse<FaturamentoMensal>> response = 
                controller.getFaturamentoMensal(2023, 5);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(faturamentoMensal, response.getBody().getDados());
        assertEquals(new BigDecimal("25000.00"), response.getBody().getDados().faturamentoTotal());
    }
} 