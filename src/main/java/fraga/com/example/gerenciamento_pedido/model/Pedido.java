package fraga.com.example.gerenciamento_pedido.model;

import fraga.com.example.gerenciamento_pedido.enums.EStatusPedido;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemPedido> itemsPedidos;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dataPedido;
    @Column
    private LocalDateTime dataPagamento;
    @Enumerated(EnumType.STRING)
    private EStatusPedido statusPedido;
    @ManyToOne
    private Cliente cliente;

}
