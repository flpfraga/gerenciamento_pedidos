package fraga.com.example.gerenciamento_pedido.service;

import fraga.com.example.gerenciamento_pedido.controller.produto.ProdutoRequest;
import fraga.com.example.gerenciamento_pedido.controller.produto.ProdutoResponse;
import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.model.Produto;
import fraga.com.example.gerenciamento_pedido.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private ModelMapper mapper;

    public ProdutoResponse salvar(ProdutoRequest produtoRequest) {
        if (buscarPorNome(produtoRequest.getNome()).isPresent()) {
            throw new BusinessException("Erro. Já existe um produto com o nome " + produtoRequest.getNome());
        }
        var produto = produtoRepository.save(mapper.map(produtoRequest, Produto.class));
        return mapper.map(produto, ProdutoResponse.class);
    }

    public ProdutoResponse atualizar(String id, ProdutoRequest produtoRequest) {
        var produto = buscarPorId(id, true);

        produto.setCategoria(produtoRequest.getCategoria());
        produto.setDescricao(produtoRequest.getDescricao());
        produto.setPreco(produtoRequest.getPreco());
        produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());

        produto = produtoRepository.save(produto);
        return mapper.map(produto, ProdutoResponse.class);
    }

    public ProdutoResponse buscarPorId(String id) {
        return mapper.map(buscarPorId(id, true), ProdutoResponse.class);
    }

    public Set<ProdutoResponse> buscarTodosProdutos() {
        return produtoRepository.findAll().stream().map(
                produto -> mapper.map(produto, ProdutoResponse.class)
        ).collect(Collectors.toSet());
    }

    public void delete(String id) {
        var produto = buscarPorId(id, true);
        produtoRepository.delete(produto);
    }

    private Optional<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByName(nome);
    }

    private Produto buscarPorId(String id, Boolean throwable) {
        return produtoRepository.findById(id)
                .orElseGet(() -> {
                    if (throwable) {
                        throw new BusinessException("O produto com o ID informado não foi encontrado.");
                    }
                    return null;
                });
    }

    public Set<Produto> buscarPorIdsIn(Set<String> ids) {
        return produtoRepository.findByIdIn(ids);
    }
}
