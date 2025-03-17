package fraga.com.example.gerenciamento_pedido.service;

import fraga.com.example.gerenciamento_pedido.controller.produto.ProdutoRequest;
import fraga.com.example.gerenciamento_pedido.controller.produto.ProdutoResponse;
import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.model.Produto;
import fraga.com.example.gerenciamento_pedido.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private ProdutoService produtoService;

    private ProdutoRequest produtoRequest;
    private Produto produto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produtoRequest = new ProdutoRequest();
        produtoRequest.setNome("Produto 1");
        produtoRequest.setCategoria("Categoria 1");
        produtoRequest.setDescricao("Descrição do Produto");
        produtoRequest.setPreco(new BigDecimal("100.0"));
        produtoRequest.setQuantidadeEstoque(50);

        produto = new Produto();
        produto.setId("1");
        produto.setNome("Produto 1");
        produto.setCategoria("Categoria 1");
        produto.setDescricao("Descrição do Produto");
        produto.setPreco(new BigDecimal("100.0"));
        produto.setQuantidadeEstoque(50);
    }

    @Test
    void testSalvar() {
        // Setup
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
        when(produtoRepository.findByName("Produto 1")).thenReturn(Optional.empty());
        when(mapper.map(produtoRequest, Produto.class)).thenReturn(produto);
        when(mapper.map(produto, ProdutoResponse.class)).thenReturn(new ProdutoResponse("Produto 1", "Categoria 1", "Descrição do Produto", "", new BigDecimal("100.0"), 50));

        // Execução
        ProdutoResponse produtoResponse = produtoService.salvar(produtoRequest);

        // Validação
        assertNotNull(produtoResponse);
        assertEquals("Produto 1", produtoResponse.getNome());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testSalvarProdutoComNomeExistente() {
        // Setup
        when(produtoRepository.findByName("Produto 1")).thenReturn(Optional.of(produto));

        // Execução e Validação
        BusinessException exception = assertThrows(BusinessException.class, () -> produtoService.salvar(produtoRequest));
        assertEquals("Erro. Já existe um produto com o nome Produto 1", exception.getMessage());
    }

    @Test
    void testAtualizar() {
        // Setup
        when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
        when(mapper.map(produto, ProdutoResponse.class)).thenReturn(new ProdutoResponse("Produto 1", "Categoria 1", "Descrição do Produto", "",new BigDecimal("100.0"), 50));

        ProdutoRequest updatedRequest = new ProdutoRequest();
        updatedRequest.setNome("Produto Atualizado");
        updatedRequest.setCategoria("Categoria Atualizada");
        updatedRequest.setDescricao("Descrição Atualizada");
        updatedRequest.setPreco(new BigDecimal("150"));
        updatedRequest.setQuantidadeEstoque(60);

        // Execução
        ProdutoResponse produtoResponse = produtoService.atualizar("1", updatedRequest);

        // Validação
        assertNotNull(produtoResponse);
        assertEquals("Produto Atualizado", produtoResponse.getNome());
        assertEquals(150.0, produtoResponse.getPreco());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testBuscarPorId() {
        // Setup
        when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));
        when(mapper.map(produto, ProdutoResponse.class)).thenReturn(new ProdutoResponse("Produto 1", "Categoria 1", "Descrição do Produto","", new BigDecimal("100.0"), 50));

        // Execução
        ProdutoResponse produtoResponse = produtoService.buscarPorId("1");

        // Validação
        assertNotNull(produtoResponse);
        assertEquals("Produto 1", produtoResponse.getNome());
    }

    @Test
    void testBuscarPorIdNotFound() {
        // Setup
        when(produtoRepository.findById("2")).thenReturn(Optional.empty());

        // Execução e Validação
        BusinessException exception = assertThrows(BusinessException.class, () -> produtoService.buscarPorId("2"));
        assertEquals("O produto com o ID informado não foi encontrado.", exception.getMessage());
    }

    @Test
    void testBuscarTodosProdutos() {
        // Setup
        when(produtoRepository.findAll()).thenReturn((List<Produto>) Set.of(produto));
        when(mapper.map(produto, ProdutoResponse.class)).thenReturn(new ProdutoResponse("Produto 1", "Categoria 1", "Descrição do Produto", "", new BigDecimal("100.0"), 50));

        // Execução
        Set<ProdutoResponse> produtos = produtoService.buscarTodosProdutos();

        // Validação
        assertNotNull(produtos);
        assertEquals(1, produtos.size());
    }

    @Test
    void testDelete() {
        // Setup
        when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));

        // Execução
        produtoService.delete("1");

        // Validação
        verify(produtoRepository, times(1)).delete(produto);
    }

    @Test
    void testDeleteNotFound() {
        // Setup
        when(produtoRepository.findById("1")).thenReturn(Optional.empty());

        // Execução e Validação
        BusinessException exception = assertThrows(BusinessException.class, () -> produtoService.delete("1"));
        assertEquals("O produto com o ID informado não foi encontrado.", exception.getMessage());
    }
}
