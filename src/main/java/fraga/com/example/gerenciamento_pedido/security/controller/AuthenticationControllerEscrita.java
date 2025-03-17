package fraga.com.example.gerenciamento_pedido.security.controller;

import fraga.com.example.gerenciamento_pedido.controller.DefaultController;
import fraga.com.example.gerenciamento_pedido.controller.DefaultResponse;
import fraga.com.example.gerenciamento_pedido.security.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para login e registro de usuários")
public class AuthenticationControllerEscrita implements DefaultController {

    private UserService userService;

    /**
     * Autentica um usuário e retorna um token JWT.
     *
     * @param data Dados de login (usuário e senha)
     * @return ResponseEntity contendo um DefaultResponse com o token JWT
     */
    @Operation(summary = "Realizar login", description = "Autentica um usuário no sistema e retorna um token JWT para acesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso, token JWT retornado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique os dados enviados."),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<DefaultResponse<LoginResponse>> login(@RequestBody @Valid AuthenticationRequest data){
        return success(userService.login(data));
    }

    /**
     * Registra um novo usuário no sistema.
     *
     * @param data Dados do novo usuário
     * @return ResponseEntity contendo um DefaultResponse com os dados do usuário cadastrado
     */
    @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário no sistema e retorna suas informações.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique os dados enviados."),
            @ApiResponse(responseCode = "409", description = "Usuário já existe."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/register")
    public ResponseEntity<DefaultResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest data){
        return success(userService.register(data));
    }  
}
