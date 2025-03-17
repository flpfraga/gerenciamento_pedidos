package fraga.com.example.gerenciamento_pedido.security.controller;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(

        @Schema(description = "Token JWT gerado após autenticação bem-sucedida", example = "eyJhbGciOiJIUzI1...")
        String token
) {
}
