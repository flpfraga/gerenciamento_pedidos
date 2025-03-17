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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final RoleService roleService;

    /**
     * Cadastra um novo cliente no sistema.
     * 
     * @param clienteRequest Dados do cliente a ser cadastrado
     * @param id ID do usuário associado ao cliente
     * @return ClienteResponse com os dados do cliente cadastrado
     * @throws BusinessException se o usuário não for encontrado
     */
    @Transactional
    public ClienteResponse cadastrarCliente(ClienteRequest clienteRequest, String id) {
        var user = userRepository.findById(id).orElseThrow(
                () -> {
                    log.error("m:cadastrarCliente clienteRequest={} BusinessException{}", clienteRequest,
                            "Cliente não cadastrado");
                    throw new BusinessException("Este usuário precisa ser registrado.");
                });
        Cliente cliente = new Cliente();
        cliente.setUser(user);
        cliente.setCpf(clienteRequest.getCpf());
        cliente.setNome(clienteRequest.getNome());
        log.info("m:cadastrarCliente clienteRequest={}", clienteRequest);
        return mapper.map(clienteRepository.save(cliente), ClienteResponse.class);
    }

    /**
     * Atualiza os dados de um cliente existente.
     * 
     * @param clienteRequest Novos dados do cliente
     * @param id ID do usuário associado ao cliente
     * @return ClienteResponse com os dados atualizados do cliente
     * @throws BusinessException se o cliente não for encontrado
     */
    public ClienteResponse atualizar(ClienteRequest clienteRequest, String id) {
        var cliente = buscarClientPorIdUser(id);
        cliente.setNome(clienteRequest.getNome());
        return mapper.map(clienteRepository.save(cliente), ClienteResponse.class);
    }

    /**
     * Busca um usuário pelo seu ID.
     * 
     * @param id ID do usuário a ser buscado
     * @return User encontrado
     * @throws BusinessException se o cliente com o ID informado não for encontrado
     */
    @Transactional
    public User buscarUserPorId(String id) {
        return userRepository.findById(id).orElseThrow(
                () -> new BusinessException("Cliente com o ID informado não foi encontrado."));
    }

    /**
     * Busca um cliente pelo ID do usuário associado.
     * 
     * @param userId ID do usuário associado ao cliente
     * @return Cliente encontrado
     * @throws BusinessException se o cliente não for encontrado para o usuário logado
     */
    public Cliente buscarClientPorIdUser(String userId) {
        log.error("m:buscarClientPorIdUser userId={} BusinessException={}", userId, "Usuário logado não localizado");
        return clienteRepository.findByUserId(userId).orElseThrow(
                () -> new BusinessException("Erro ao localizar o Cliente do usuário logado."));
    }
}

