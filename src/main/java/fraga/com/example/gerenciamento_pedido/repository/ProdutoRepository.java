package fraga.com.example.gerenciamento_pedido.repository;

import fraga.com.example.gerenciamento_pedido.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, String> {

    @Query("SELECT p FROM Produto p WHERE p.nome = :nome")
    Optional<Produto> findByName(@Param("nome")String nome);

    Set<Produto> findByIdIn(Set<String> ids);
}
