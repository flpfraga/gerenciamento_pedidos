package fraga.com.example.gerenciamento_pedido.controller.cliente;

import fraga.com.example.gerenciamento_pedido.controller.DefaultController;
import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.controller.pedido.PedidoRequest;
import fraga.com.example.gerenciamento_pedido.controller.pedido.PedidoResponse;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import fraga.com.example.gerenciamento_pedido.service.ClienteService;
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

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/cadastrar")
    public ResponseEntity<DefaultResponse<ClienteResponse>> cadastrarCliente(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ClienteRequest clienteRequest) {
        return success(clienteService.cadastrarCliente(clienteRequest, user.getId()));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping
    public ResponseEntity<DefaultResponse<ClienteResponse>> atualizar(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ClienteRequest clienteRequest) {
        return success(clienteService.atualizar(clienteRequest, user.getId()));
    }
}
