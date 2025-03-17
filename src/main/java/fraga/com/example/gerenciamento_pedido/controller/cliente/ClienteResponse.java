package fraga.com.example.gerenciamento_pedido.controller.cliente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponse {
    private String id;
    private String nome;
    private String cpf;
}
