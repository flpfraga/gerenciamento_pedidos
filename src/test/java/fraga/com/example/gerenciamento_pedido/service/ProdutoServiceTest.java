package fraga.com.example.gerenciamento_pedido.service;

import fraga.com.example.gerenciamento_pedido.controller.produto.ProdutoRequest;
import fraga.com.example.gerenciamento_pedido.controller.produto.ProdutoResponse;
import fraga.com.example.gerenciamento_pedido.exception.BusinessException;
import fraga.com.example.gerenciamento_pedido.model.Produto;
import fraga.com.example.gerenciamento_pedido.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private ProdutoService produtoService;

    private ProdutoRequest produtoRequest;
    private Produto produto;
    private ProdutoResponse produtoResponse;
    private Set<Produto> produtos;
    private Set<ProdutoResponse> produtoResponses;

    @BeforeEach
    void setUp() {
        produtoRequest = new ProdutoRequest();
        produtoRequest.setNome("Produto Teste");
        produtoRequest.setDescricao("Descrição do produto");
        produtoRequest.setPreco(new BigDecimal("100.0"));
        produtoRequest.setQuantidadeEstoque(10);
        produtoRequest.setCategoria("Categoria Teste");

        produto = new Produto();
        produto.setId("1");
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição do produto");
        produto.setPreco(new BigDecimal("100.0"));
        produto.setQuantidadeEstoque(10);
        produto.setCategoria("Categoria Teste");

        produtoResponse = new ProdutoResponse();
        produtoResponse.setId("1");
        produtoResponse.setNome("Produto Teste");
        produtoResponse.setDescricao("Descrição do produto");
        produtoResponse.setPreco(new BigDecimal("100.0"));
        produtoResponse.setQuantidadeEstoque(10);
        produtoResponse.setCategoria("Categoria Teste");

        produtos = new HashSet<>();
        produtos.add(produto);

        produtoResponses = new HashSet<>();
        produtoResponses.add(produtoResponse);
    }

    @Test
    void testSalvarComSucesso() {
        when(produtoRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
        when(mapper.map(any(ProdutoRequest.class), eq(Produto.class))).thenReturn(produto);
        when(mapper.map(any(Produto.class), eq(ProdutoResponse.class))).thenReturn(produtoResponse);

        ProdutoResponse response = produtoService.salvar(produtoRequest);

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals("Produto Teste", response.getNome());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testSalvarErroProdutoJaExistente() {
        when(produtoRepository.findByName(anyString())).thenReturn(Optional.of(produto));

        assertThrows(BusinessException.class, () -> {
            produtoService.salvar(produtoRequest);
        });
        
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void testAtualizarComSucesso() {
        when(produtoRepository.findById(anyString())).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
        when(mapper.map(any(Produto.class), eq(ProdutoResponse.class))).thenReturn(produtoResponse);

        ProdutoResponse response = produtoService.atualizar("1", produtoRequest);

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals("Produto Teste", response.getNome());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testAtualizarErroProdutoNaoEncontrado() {
        when(produtoRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            produtoService.atualizar("1", produtoRequest);
        });
        
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void testBuscarPorIdComSucesso() {
        when(produtoRepository.findById(anyString())).thenReturn(Optional.of(produto));
        when(mapper.map(any(Produto.class), eq(ProdutoResponse.class))).thenReturn(produtoResponse);

        ProdutoResponse response = produtoService.buscarPorId("1");

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals("Produto Teste", response.getNome());
    }

    @Test
    void testBuscarPorIdErroProdutoNaoEncontrado() {
        when(produtoRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            produtoService.buscarPorId("1");
        });
    }

    @Test
    void testBuscarTodosProdutosComSucesso() {
        when(produtoRepository.findAll()).thenReturn(new java.util.ArrayList<>(produtos));
        when(mapper.map(any(Produto.class), eq(ProdutoResponse.class))).thenReturn(produtoResponse);

        Set<ProdutoResponse> response = produtoService.buscarTodosProdutos();

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void testDeleteComSucesso() {
        when(produtoRepository.findById(anyString())).thenReturn(Optional.of(produto));
        doNothing().when(produtoRepository).delete(any(Produto.class));

        assertDoesNotThrow(() -> {
            produtoService.delete("1");
        });
        
        verify(produtoRepository, times(1)).delete(any(Produto.class));
    }

    @Test
    void testDeleteErroProdutoNaoEncontrado() {
        when(produtoRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            produtoService.delete("1");
        });
        
        verify(produtoRepository, never()).delete(any(Produto.class));
    }
} 