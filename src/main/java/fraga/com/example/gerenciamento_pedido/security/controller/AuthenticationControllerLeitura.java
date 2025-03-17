package fraga.com.example.gerenciamento_pedido.security.controller;


import fraga.com.example.gerenciamento_pedido.controller.DefaultController;
import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.security.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para leitura de dados de autenticação, como roles de usuário.")
public class AuthenticationControllerLeitura implements DefaultController {

    private RoleService roleService;

    /**
     * Retorna a lista de roles (permissões) disponíveis no sistema.
     *
     * @return ResponseEntity contendo um DefaultResponse com o conjunto de roles disponíveis
     */
    @Operation(summary = "Listar roles do sistema", description = "Retorna todas as permissões (roles) disponíveis no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de roles retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Usuário sem permissão."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/roles")
    public ResponseEntity<DefaultResponse<Set<String>>> getRoles(){
        return success(roleService.get());
    }
}
