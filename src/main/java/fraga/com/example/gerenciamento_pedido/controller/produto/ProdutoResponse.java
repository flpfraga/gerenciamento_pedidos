package fraga.com.example.gerenciamento_pedido.controller.produto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProdutoResponse {
    private String id;
    private String nome;
    private String categoria;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidadeEstoque;
}
