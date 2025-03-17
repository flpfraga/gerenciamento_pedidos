package fraga.com.example.gerenciamento_pedido.security.controller;

import fraga.com.example.gerenciamento_pedido.security.enums.EUserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record RegisterRequest(
        @NotBlank(message = "O login não pode estar em branco.")
        String login,
        @NotBlank(message = "A senha não pode estar em branco.")
        @Size(min = 3, message = "A senha deve ter no mínimo 3 caracteres.")
        String password,
        @NotEmpty(message = "O usuário deve ter pelo menos uma role.")
        Set<EUserRole> roles
) {
}
