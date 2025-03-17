package fraga.com.example.gerenciamento_pedido.security.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(

        @NotBlank(message = "O login não pode estar em branco.")
        @Schema(description = "Nome de usuário ou e-mail utilizado para autenticação", example = "usuario123")
        String login,

        @NotBlank(message = "A senha não pode estar em branco.")
        @Schema(description = "Senha do usuário", example = "Senha@123")
        String password
) {
}
