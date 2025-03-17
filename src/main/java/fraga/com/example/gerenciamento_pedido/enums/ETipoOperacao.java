package fraga.com.example.gerenciamento_pedido.enums;

public enum ETipoOperacao {
    COMPRA("Operação de aquisição de produtos para o estoque."),
    VENDA("Operação de venda  de produtos para clientes.");

    private final String descricao;

    ETipoOperacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
