package fraga.com.example.gerenciamento_pedido.service;

import fraga.com.example.gerenciamento_pedido.controller.cliente.ClienteRequest;
import fraga.com.example.gerenciamento_pedido.controller.cliente.ClienteResponse;
import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.model.Cliente;
import fraga.com.example.gerenciamento_pedido.repository.ClienteRepository;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import fraga.com.example.gerenciamento_pedido.security.repository.UserRepository;
import fraga.com.example.gerenciamento_pedido.security.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final RoleService roleService;

    @Transactional
    public ClienteResponse cadastrarCliente(ClienteRequest clienteRequest, String id) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new BusinessException("")
        );
        Cliente cliente = new Cliente();
        cliente.setUser(user);
        cliente.setCpf(clienteRequest.getCpf());
        cliente.setNome(clienteRequest.getNome());
        return mapper.map(clienteRepository.save(cliente), ClienteResponse.class);
    }

    public ClienteResponse atualizar(ClienteRequest clienteRequest, String id) {
        var cliente = buscarClientPorIdUser(id);
        cliente.setNome(clienteRequest.getNome());
        return mapper.map(clienteRepository.save(cliente), ClienteResponse.class);
    }

    @Transactional
    public User buscarUserPorId(String id) {
        return userRepository.findById(id).orElseThrow(
                () -> new BusinessException("Cliente com o ID informado não foi encontrado."));
    }

    public Cliente buscarClientPorIdUser(String userId) {
        return clienteRepository.findByUserId(userId).orElseThrow(
                () -> new BusinessException("Erro ao localizar o Cliente do usuãrio logado."));
    }
}

