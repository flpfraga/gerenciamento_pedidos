package fraga.com.example.gerenciamento_pedido.controller.cliente;

import fraga.com.example.gerenciamento_pedido.controller.DefaultController;
import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.controller.pedido.PedidoRequest;
import fraga.com.example.gerenciamento_pedido.controller.pedido.PedidoResponse;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import fraga.com.example.gerenciamento_pedido.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cliente")
@AllArgsConstructor
@Tag(name = "Cliente", description = "Endpoints para gerenciar Clientes")
public class ClienteController implements DefaultController {
    private final ClienteService clienteService;

    /**
     * Cadastra um novo cliente no sistema.
     * 
     * @param user Usuário autenticado que está realizando a ação
     * @param clienteRequest Dados do cliente a ser cadastrado
     * @return ResponseEntity contendo um DefaultResponse com o ClienteResponse
     */
    @Operation(
        summary = "Cadastrar novo cliente",
        description = "Cadastra um novo cliente no sistema para o usuário autenticado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique os dados enviados."),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para realizar esta operação."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/cadastrar")
    public ResponseEntity<DefaultResponse<ClienteResponse>> cadastrarCliente(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ClienteRequest clienteRequest) {
        return success(clienteService.cadastrarCliente(clienteRequest, user.getId()));
    }

    /**
     * Atualiza os dados do cliente associado ao usuário autenticado.
     * 
     * @param user Usuário autenticado que está realizando a ação
     * @param clienteRequest Novos dados do cliente
     * @return ResponseEntity contendo um DefaultResponse com o ClienteResponse atualizado
     */
    @Operation(
        summary = "Atualizar dados do cliente",
        description = "Atualiza os dados do cliente associado ao usuário autenticado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique os dados enviados."),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para realizar esta operação."),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado para o usuário autenticado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping
    public ResponseEntity<DefaultResponse<ClienteResponse>> atualizar(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ClienteRequest clienteRequest) {
        return success(clienteService.atualizar(clienteRequest, user.getId()));
    }
}
