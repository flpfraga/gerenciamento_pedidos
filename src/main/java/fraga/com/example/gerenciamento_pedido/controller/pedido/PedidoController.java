package fraga.com.example.gerenciamento_pedido.controller.pedido;

import fraga.com.example.gerenciamento_pedido.controller.DefaultController;
import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.security.domain.User;
import fraga.com.example.gerenciamento_pedido.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * Cadastra um novo pedido no sistema.
     * 
     * @param user Usuário autenticado que está realizando a ação
     * @param pedidoRequest Mapa contendo IDs dos produtos e suas respectivas quantidades
     * @return ResponseEntity contendo um DefaultResponse com o PedidoResponse do pedido cadastrado
     */
    @Operation(
        summary = "Cadastrar novo pedido",
        description = "Cria um novo pedido com os produtos e quantidades especificados."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida ou estoque insuficiente"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para realizar esta operação"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/cadastrar")
    public ResponseEntity<DefaultResponse<PedidoResponse>> cadastrarPedido(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, @NonNull Integer> pedidoRequest) {
        return success(pedidoService.gerarPedido(pedidoRequest, user.getId()));
    }

    /**
     * Realiza o pagamento de um pedido existente.
     * 
     * @param id ID do pedido a ser pago
     * @return ResponseEntity contendo um DefaultResponse com o PedidoResponse atualizado
     */
    @Operation(
        summary = "Realizar pagamento",
        description = "Efetua o pagamento de um pedido e atualiza seu status para CONCLUIDO."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamento realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Pedido não está no status AGUARDANDO"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para realizar esta operação"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PatchMapping("/realizar_pagamento/{id}")
    public ResponseEntity<DefaultResponse<PedidoResponse>> realizarPagamento(@PathVariable String id) {
        return success(pedidoService.realizarPagamento(id));
    }

    /**
     * Busca todos os pedidos de um cliente com filtro opcional de status.
     * 
     * @param pedidoRequest Objeto contendo filtros para a busca (status do pedido, período)
     * @param user Usuário autenticado que está realizando a ação
     * @return ResponseEntity contendo um DefaultResponse com a lista de PedidoResponse
     */
    @Operation(
        summary = "Buscar pedidos do cliente",
        description = "Retorna todos os pedidos do cliente autenticado, com opção de filtrar por status."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para realizar esta operação"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/todos_por_cliente/")
    public ResponseEntity<DefaultResponse<List<PedidoResponse>>> buscarPedidosPorCLiente(
            @RequestBody PedidoRequest pedidoRequest,
            @AuthenticationPrincipal User user) {
        return success(pedidoService.buscarPedidosPorCliente(user.getId(), pedidoRequest.getStatusPedido()));
    }

    /**
     * Busca um pedido específico pelo ID.
     * 
     * @param id ID do pedido a ser buscado
     * @param user Usuário autenticado que está realizando a ação
     * @return ResponseEntity contendo um DefaultResponse com o PedidoResponse encontrado
     */
    @Operation(
        summary = "Buscar pedido por ID",
        description = "Busca os detalhes de um pedido específico pelo seu identificador."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "403", description = "Pedido não pertence ao usuário autenticado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/id/{id}")
    public ResponseEntity<DefaultResponse<PedidoResponse>> buscarPedidoPorId(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        return success(pedidoService.buscarPedidoPorIdCliente(user.getId(), id));
    }
}
