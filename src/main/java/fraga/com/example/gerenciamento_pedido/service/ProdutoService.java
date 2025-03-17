package fraga.com.example.gerenciamento_pedido.service;

import fraga.com.example.gerenciamento_pedido.controller.produto.ProdutoRequest;
import fraga.com.example.gerenciamento_pedido.controller.produto.ProdutoResponse;
import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.model.Produto;
import fraga.com.example.gerenciamento_pedido.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private ModelMapper mapper;

    /**
     * Salva um novo produto no sistema.
     *
     * @param produtoRequest Dados do produto a ser salvo
     * @return ProdutoResponse com os dados do produto salvo
     * @throws BusinessException se já existir um produto com o mesmo nome
     */
    public ProdutoResponse salvar(ProdutoRequest produtoRequest) {
        if (buscarPorNome(produtoRequest.getNome()).isPresent()) {
            log.error("m:salvar produtoRequest={} BusinessException={}",
                    produtoRequest, "Cadastro de produto com mesmo nome");
            throw new BusinessException("Erro. Já existe um produto com o nome " + produtoRequest.getNome());
        }
        var produto = produtoRepository.save(mapper.map(produtoRequest, Produto.class));
        log.info("m:salvar produtoRequest={}",produtoRequest);
        return mapper.map(produto, ProdutoResponse.class);
    }

    /**
     * Atualiza um produto existente.
     *
     * @param id             ID do produto a ser atualizado
     * @param produtoRequest Novos dados do produto
     * @return ProdutoResponse com os dados atualizados do produto
     * @throws BusinessException se o produto não for encontrado
     */
    public ProdutoResponse atualizar(String id, ProdutoRequest produtoRequest) {
        var produto = buscarPorId(id, true);

        produto.setCategoria(produtoRequest.getCategoria());
        produto.setDescricao(produtoRequest.getDescricao());
        produto.setPreco(produtoRequest.getPreco());
        produto.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());

        produto = produtoRepository.save(produto);
        log.info("m:atualizar idProduto={} produtoRequest={}",id, produtoRequest);
        return mapper.map(produto, ProdutoResponse.class);
    }

    /**
     * Busca um produto pelo ID.
     *
     * @param id ID do produto a ser buscado
     * @return ProdutoResponse com os dados do produto encontrado
     * @throws BusinessException se o produto não for encontrado
     */
    public ProdutoResponse buscarPorId(String id) {
        return mapper.map(buscarPorId(id, true), ProdutoResponse.class);
    }

    /**
     * Busca todos os produtos cadastrados.
     *
     * @return Conjunto de ProdutoResponse com todos os produtos
     */
    public Set<ProdutoResponse> buscarTodosProdutos() {
        log.info("m:buscarTodosProdutos");
        return produtoRepository.findAll().stream().map(
                produto -> mapper.map(produto, ProdutoResponse.class)
        ).collect(Collectors.toSet());
    }

    /**
     * Remove um produto pelo ID.
     *
     * @param id ID do produto a ser removido
     * @throws BusinessException se o produto não for encontrado
     */
    public void delete(String id) {
        var produto = buscarPorId(id, true);
        produtoRepository.delete(produto);
        log.info("m:delete");
    }

    private Optional<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByName(nome);
    }

    private Produto buscarPorId(String id, Boolean throwable) {
        return produtoRepository.findById(id)
                .orElseGet(() -> {
                    if (throwable) {
                        log.error("m:buscarPorId idProduto={} BusinessException={}",
                                id, "Produto não encontrado");
                        throw new BusinessException("O produto com o ID informado não foi encontrado.");
                    }
                    return null;
                });
    }

    /**
     * Busca produtos por IDs.
     *
     * @param ids Conjunto de IDs dos produtos a serem buscados
     * @return Conjunto de produtos encontrados
     */
    public Set<Produto> buscarPorIdsIn(Set<String> ids) {
        log.info("m:buscarPorIdsIn");
        return produtoRepository.findByIdIn(ids);
    }
}
