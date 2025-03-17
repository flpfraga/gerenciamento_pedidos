package fraga.com.example.gerenciamento_pedido.controller.pedido;

import fraga.com.example.gerenciamento_pedido.controller.DefaultController;
import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import fraga.com.example.gerenciamento_pedido.service.PedidoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/pedido")
@AllArgsConstructor
@Tag(name = "Pedido", description = "Endpoints para gerenciar Pedido")
public class PedidoController implements DefaultController {
    private final PedidoService pedidoService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/cadastrar")
    public ResponseEntity<DefaultResponse<PedidoResponse>> cadastrarPedido(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, @NonNull Integer> pedidoRequest) {
        return success(pedidoService.gerarPedido(pedidoRequest, user.getId()));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PatchMapping("/realizar_pagamento/{id}")
    public ResponseEntity<DefaultResponse<PedidoResponse>> realizarPagamento(@PathVariable String id) {
        return success(pedidoService.realizarPagamento(id));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/todos_por_cliente/")
    public ResponseEntity<DefaultResponse<List<PedidoResponse>>> buscarPedidosPorCLiente(
            @RequestBody PedidoRequest pedidoRequest,
            @AuthenticationPrincipal User user) {
        return success(pedidoService.buscarPedidosPorCliente(user.getId(), pedidoRequest.getStatusPedido()));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/id/{id}")
    public ResponseEntity<DefaultResponse<PedidoResponse>> buscarPedidoPorId(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        return success(pedidoService.buscarPedidoPorIdCliente(user.getId(), id));
    }

}
