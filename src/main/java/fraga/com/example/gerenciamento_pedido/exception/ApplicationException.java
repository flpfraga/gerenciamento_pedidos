package fraga.com.example.gerenciamento_pedido.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final transient DefaultError erro;

    public ApplicationException() {
        super();
        erro = new DefaultError();
    }

    public ApplicationException(final String mensagem, final HttpStatus status) {
        super();
        erro = new DefaultError(mensagem, status);
    }

    public ApplicationException(final String mensagem) {
        this(mensagem, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public DefaultError getErro() {
        return erro;
    }

    @Override
    public String toString() {
        return String.format("ExemploException [erro=%s]", erro);
    }
}
