package fraga.com.example.gerenciamento_pedido.repository.strategy;

import fraga.com.example.gerenciamento_pedido.exception.BusinessException;

public class QueryData {

    public QueryData() {
        throw new BusinessException("Classe estática  não pode ser instanciada.");
    }

    public static String queryBuscarUsuariosMaisCompras() {
        return """
                SELECT c.id AS cliente_id,
                       c.nome AS nome_cliente,
                       SUM(ip.quantidade * p.preco) AS total_gasto
                FROM pedidos pd
                JOIN cliente c ON pd.cliente_id = c.id
                JOIN item_pedido ip ON pd.id = ip.pedido_id
                JOIN produtos p ON ip.produto_id = p.id
                WHERE pd.status_pedido = 'CONCLUIDO'
                GROUP BY c.id
                ORDER BY total_gasto DESC
                LIMIT 5
                """;
    }

    public static String queryBuscarTicketMedioCompraCliente() {
        return """
                SELECT
                    c.id AS cliente_id,
                    c.nome AS cliente_nome,
                    COALESCE(SUM(ip.quantidade * p.preco) / COUNT(DISTINCT pd.id), 0) AS ticket_medio
                FROM pedidos pd
                JOIN cliente c ON pd.cliente_id = c.id
                JOIN item_pedido ip ON pd.id = ip.pedido_id
                JOIN produtos p ON ip.produto_id = p.id
                WHERE pd.status_pedido = 'CONCLUIDO'
                AND pd.cliente_id = ?
                GROUP BY c.id, c.nome
                """;
    }

    public static String queryBuscarFaturamentoMensal() {
        return """
                SELECT COALESCE(SUM(ip.quantidade * p.preco), 0) AS faturamento_total
                FROM pedidos pd
                JOIN item_pedido ip ON pd.id = ip.pedido_id
                JOIN produtos p ON ip.produto_id = p.id
                WHERE YEAR(pd.data_pagamento) = ?
                AND MONTH(pd.data_pagamento) = ?
                """;
    }
}
