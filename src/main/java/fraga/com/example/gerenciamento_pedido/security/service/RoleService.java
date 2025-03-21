package fraga.com.example.gerenciamento_pedido.security.service;

import fraga.com.example.gerenciamento_pedido.security.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class RoleService {
    private RoleRepository roleRepository;

    /**
     * Obtém todas as roles disponíveis no sistema.
     * 
     * @return Conjunto com os nomes de todas as roles cadastradas
     */
    public Set<String> get() {
        log.info("m=get()");
        return roleRepository.findAll().stream().map(
                role -> role.getName().getRole()).collect(Collectors.toSet());
    }
}
