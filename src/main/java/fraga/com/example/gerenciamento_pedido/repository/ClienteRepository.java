package fraga.com.example.gerenciamento_pedido.repository;

import fraga.com.example.gerenciamento_pedido.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {
    @Query("SELECT c FROM Cliente c WHERE c.cpf = :cpf")
    Optional<Cliente> findByCpf(@Param("cpf")String cpf);

    Optional<Cliente> findByUserId(@Param("user_id")String userId);
}
