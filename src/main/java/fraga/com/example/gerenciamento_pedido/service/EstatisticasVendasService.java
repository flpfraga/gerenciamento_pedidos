package fraga.com.example.gerenciamento_pedido.service;

import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.repository.strategy.EstatisticaRepository;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.ClienteMaisGastos;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.FaturamentoMensal;
import fraga.com.example.gerenciamento_pedido.repository.strategy.record.TicketMedio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class EstatisticasVendasService {

    private final EstatisticaRepository estatisticaRepository;

    public List<ClienteMaisGastos> buscarUsuariosMaisCompras(){
        return estatisticaRepository.buscarUsuariosMaisCompras();
    }

    public TicketMedio buscarTicketMedioPorCliente(String clienteId){
        return estatisticaRepository.buscarTicketMedioPorCliente(clienteId);
    }

    public FaturamentoMensal getFaturamentoMensal(Integer ano, Integer mes){
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
            throw new BusinessException("Ano/mês informado inválido");
        }
    }
}
