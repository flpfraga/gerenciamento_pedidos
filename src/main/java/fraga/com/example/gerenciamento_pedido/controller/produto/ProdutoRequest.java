package fraga.com.example.gerenciamento_pedido.controller.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoRequest {

    @NotBlank(message = "O nome do produto deve ser informado.")
    private String nome;
    @NotNull(message = "O preço do produto deve ser informado.")
    private BigDecimal preco;
    @NotBlank(message = "A categoria do produto deve ser informada.")
    private String categoria;
    @NotNull(message = "A quantidade do produto deve ser informado.")
    private Integer quantidadeEstoque;
    private String descricao;
}
