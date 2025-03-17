package fraga.com.example.gerenciamento_pedido.controller.cliente;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClienteRequest {
    @NotNull(message = "O cpf do cliente deve ser informado.")
    private String cpf;
    @NotNull(message = "O nome do cliente deve ser informado.")
    private String nome;
}
