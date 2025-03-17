package fraga.com.example.gerenciamento_pedido.repository;

import fraga.com.example.gerenciamento_pedido.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, String> {

    List<Pedido> findByClienteId(String clienteId);
}
