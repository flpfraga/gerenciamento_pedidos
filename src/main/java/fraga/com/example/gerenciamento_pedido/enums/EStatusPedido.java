package fraga.com.example.gerenciamento_pedido.enums;

public enum EStatusPedido {
    AGUARDANDO("Aguardando pagamento"),
    CONCLUIDO("Concluído"),
    CANCELADO("Cancelado pelo cliente"),
    EXPIRADO("Pedido expirado");

    private final String descricao;

    EStatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }


}
