package fraga.com.example.gerenciamento_pedido.service;

import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.repository.strategy.EstatisticaRepository;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.ClienteMaisGastos;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.FaturamentoMensal;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.TicketMedio;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class EstatisticasVendasService {

    private final EstatisticaRepository estatisticaRepository;

    /**
     * Busca os usuários que mais realizaram compras no sistema.
     * 
     * @return Lista de clientes com seus valores totais de compras em ordem decrescente
     */
    public List<ClienteMaisGastos> buscarUsuariosMaisCompras(){
        log.info("m:buscarUsuariosMaisCompras");
        return estatisticaRepository.buscarUsuariosMaisCompras();
    }

    /**
     * Busca o ticket médio de compras de um cliente específico.
     * 
     * @param clienteId ID do cliente para o qual se deseja calcular o ticket médio
     * @return Objeto TicketMedio contendo o valor médio das compras do cliente
     */
    public TicketMedio buscarTicketMedioPorCliente(String clienteId){
        log.info("m:buscarTicketMedioPorCliente clienteId={}", clienteId);
        return estatisticaRepository.buscarTicketMedioPorCliente(clienteId);
    }

    /**
     * Obtém o faturamento mensal para um mês e ano específicos.
     * 
     * @param ano Ano para o qual se deseja obter o faturamento
     * @param mes Mês para o qual se deseja obter o faturamento (1 a 12)
     * @return Objeto FaturamentoMensal contendo o valor total do faturamento
     * @throws BusinessException se o mês/ano informado for inválido ou futuro
     */
    public FaturamentoMensal getFaturamentoMensal(Integer ano, Integer mes){
        log.info("m:getFaturamentoMensal ano={} mes={}", ano, mes);
        mesAnoInformadoCorretamente(ano, mes);
        return estatisticaRepository.getFaturamentoMensal(ano, mes);
    }

    private void mesAnoInformadoCorretamente(Integer ano, Integer mes){
        try{
            LocalDate mesAlvo = LocalDate.of(ano,mes, 1);
            if(LocalDate.now().isBefore(mesAlvo)){
                throw new DateTimeException("");
            }
        }catch (DateTimeException e){
            log.info("m:getFaturamentoMensal ano={} mes={} BusinessException={}", ano, mes, "Data inválida");
            throw new BusinessException("Ano/mês informado inválido");
        }
    }
}
