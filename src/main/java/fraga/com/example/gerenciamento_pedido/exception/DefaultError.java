package fraga.com.example.gerenciamento_pedido.exception;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(value = NON_NULL)
public class DefaultError {

    private String mensagem;
    private HttpStatus statusCode;
    private Map<String, Object> metaDado;

    public DefaultError() {
        this(null, null, null);
    }
    public DefaultError(String mensagem, HttpStatus statusCode) {
        this(mensagem, statusCode, null);
    }
}
