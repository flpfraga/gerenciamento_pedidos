package fraga.com.example.gerenciamento_pedido.controller.produto;

import fraga.com.example.gerenciamento_pedido.controller.DefaultController;
import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/v1/produto")
@AllArgsConstructor
@Tag(name = "Produto", description = "Endpoints para gerenciar Produto")
public class ProdutoController implements DefaultController {

    private final ProdutoService produtoService;

    /**
     * Cadastra um novo produto no sistema.
     * 
     * @param produtoRequest Dados do produto a ser cadastrado
     * @return ResponseEntity contendo um DefaultResponse com o ProdutoResponse
     */
    @Operation(
        summary = "Cadastrar novo produto",
        description = "Cadastra um novo produto no sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique os dados enviados."),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para realizar esta operação."),
        @ApiResponse(responseCode = "409", description = "Já existe um produto com este nome."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/cadastrar")
    public ResponseEntity<DefaultResponse<ProdutoResponse>> salvar(@Valid @RequestBody ProdutoRequest produtoRequest) {
        return success(produtoService.salvar(produtoRequest));
    }

    /**
     * Atualiza os dados de um produto existente.
     * 
     * @param id ID do produto a ser atualizado
     * @param produtoRequest Novos dados do produto
     * @return ResponseEntity contendo um DefaultResponse com o ProdutoResponse atualizado
     */
    @Operation(
        summary = "Atualizar produto",
        description = "Atualiza os dados de um produto existente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique os dados enviados."),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para realizar esta operação."),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<DefaultResponse<ProdutoResponse>> atualizar(@PathVariable String id,
                                                                      @Valid @RequestBody ProdutoRequest produtoRequest) {
        return success(produtoService.atualizar(id, produtoRequest));
    }

    /**
     * Busca um produto pelo ID.
     * 
     * @param id ID do produto a ser buscado
     * @return ResponseEntity contendo um DefaultResponse com o ProdutoResponse encontrado
     */
    @Operation(
        summary = "Buscar produto por ID",
        description = "Busca um produto específico pelo seu identificador."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para realizar esta operação."),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<DefaultResponse<ProdutoResponse>> buscarPorId(@PathVariable String id) {
        return success(produtoService.buscarPorId(id));
    }

    /**
     * Busca todos os produtos cadastrados no sistema.
     * 
     * @return ResponseEntity contendo um DefaultResponse com um conjunto de ProdutoResponse
     */
    @Operation(
        summary = "Listar todos os produtos",
        description = "Retorna todos os produtos cadastrados no sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para realizar esta operação."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/produtos")
    public ResponseEntity<DefaultResponse<Set<ProdutoResponse>>> buscarTodos() {
        return success(produtoService.buscarTodosProdutos());
    }

    /**
     * Remove um produto do sistema pelo ID.
     * 
     * @param id ID do produto a ser removido
     * @return ResponseEntity contendo um DefaultResponse com mensagem de sucesso
     */
    @Operation(
        summary = "Remover produto",
        description = "Remove um produto do sistema pelo seu identificador."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto removido com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para realizar esta operação."),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public  ResponseEntity<DefaultResponse<String>> deletarPorId(@PathVariable String id){
        produtoService.delete(id);
        return success("Produto deletado com sucesso");
    }
}
