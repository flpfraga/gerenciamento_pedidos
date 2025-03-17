package fraga.com.example.gerenciamento_pedido.security.service;

import fraga.com.example.gerenciamento_pedido.security.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoleService {
    private RoleRepository roleRepository;

    public Set<String> get() {
        return roleRepository.findAll().stream().map(
                role -> role.getName().getRole()).collect(Collectors.toSet());
    }
}
