package fraga.com.example.gerenciamento_pedido.security.repository;

import fraga.com.example.gerenciamento_pedido.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, String> {
    UserDetails findByLogin(String login);
}
