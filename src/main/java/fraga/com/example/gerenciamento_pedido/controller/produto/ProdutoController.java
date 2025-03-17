package fraga.com.example.gerenciamento_pedido.controller.produto;

import fraga.com.example.gerenciamento_pedido.controller.DefaultController;
import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.service.ProdutoService;
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/cadastrar")
    public ResponseEntity<DefaultResponse<ProdutoResponse>> salvar(@Valid @RequestBody ProdutoRequest produtoRequest) {
        return success(produtoService.salvar(produtoRequest));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<DefaultResponse<ProdutoResponse>> atualizar(@PathVariable String id,
                                                                      @Valid @RequestBody ProdutoRequest produtoRequest) {
        return success(produtoService.atualizar(id, produtoRequest));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<DefaultResponse<ProdutoResponse>> buscarPorId(@PathVariable String id) {
        return success(produtoService.buscarPorId(id));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/produtos")
    public ResponseEntity<DefaultResponse<Set<ProdutoResponse>>> buscarTodos() {
        return success(produtoService.buscarTodosProdutos());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public  ResponseEntity<DefaultResponse<String>> deletarPorId(@PathVariable String id){
        produtoService.delete(id);
        return success("Produto deletado com sucesso");
    }
}
