package fraga.com.example.gerenciamento_pedido.security.controller;

import fraga.com.example.gerenciamento_pedido.security.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resposta ao registrar um novo usuário no sistema")
public class RegisterResponse {

    @Schema(description = "Identificador único do usuário registrado", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Login do usuário registrado", example = "usuario123")
    private String login;

    @Schema(description = "Conjunto de roles associadas ao usuário", example = "[\"ROLE_ADMIN\", \"ROLE_USER\"]")
    private Set<Role> roles;
}
