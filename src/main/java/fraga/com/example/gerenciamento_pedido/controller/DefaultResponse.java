package fraga.com.example.gerenciamento_pedido.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultResponse<T> {
    private Integer status;
    private  T dados;

    public DefaultResponse(final Integer status, final T dados) {
        this.status = status;
        this.dados = dados;
    }
}