package fraga.com.example.gerenciamento_pedido.controller.produto;

import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    private ProdutoRequest produtoRequest;
    private ProdutoResponse produtoResponse;
    private Set<ProdutoResponse> produtoResponseSet;

    @BeforeEach
    void setUp() {
        produtoRequest = new ProdutoRequest();
        produtoRequest.setNome("Produto Teste");
        produtoRequest.setDescricao("Descrição do produto");
        produtoRequest.setPreco(new BigDecimal("100.0"));
        produtoRequest.setQuantidadeEstoque(10);
        produtoRequest.setCategoria("Categoria Teste");

        produtoResponse = new ProdutoResponse();
        produtoResponse.setId("1");
        produtoResponse.setNome("Produto Teste");
        produtoResponse.setDescricao("Descrição do produto");
        produtoResponse.setPreco(new BigDecimal("100.0"));
        produtoResponse.setQuantidadeEstoque(10);
        produtoResponse.setCategoria("Categoria Teste");

        produtoResponseSet = new HashSet<>();
        produtoResponseSet.add(produtoResponse);
    }

    @Test
    void testSalvarComSucesso() {
        when(produtoService.salvar(any(ProdutoRequest.class))).thenReturn(produtoResponse);

        ResponseEntity<DefaultResponse<ProdutoResponse>> response = produtoController.salvar(produtoRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(produtoResponse, response.getBody().getDados());
    }

    @Test
    void testAtualizarComSucesso() {
        when(produtoService.atualizar(anyString(), any(ProdutoRequest.class))).thenReturn(produtoResponse);

        ResponseEntity<DefaultResponse<ProdutoResponse>> response = 
                produtoController.atualizar("1", produtoRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(produtoResponse, response.getBody().getDados());
    }

    @Test
    void testBuscarPorIdComSucesso() {
        when(produtoService.buscarPorId(anyString())).thenReturn(produtoResponse);

        ResponseEntity<DefaultResponse<ProdutoResponse>> response = produtoController.buscarPorId("1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(produtoResponse, response.getBody().getDados());
    }

    @Test
    void testBuscarTodosComSucesso() {
        when(produtoService.buscarTodosProdutos()).thenReturn(produtoResponseSet);

        ResponseEntity<DefaultResponse<Set<ProdutoResponse>>> response = produtoController.buscarTodos();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(produtoResponseSet, response.getBody().getDados());
    }

    @Test
    void testDeletarPorIdComSucesso() {
        doNothing().when(produtoService).delete(anyString());

        ResponseEntity<DefaultResponse<String>> response = produtoController.deletarPorId("1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Produto deletado com sucesso", response.getBody().getDados());
    }
} 