package fraga.com.example.gerenciamento_pedido.exception.models;

import fraga.com.example.gerenciamento_pedido.exception.models.records.ErrorField;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {

    private final BodyErro body;
    private final int status;

    public ErrorResponse(BodyErro body, int status) {
        this.body = body;
        this.status = status;
    }

    public static class BodyErro {
        private final LocalDateTime timestamp;
        private final String message;
        private final String path;
        private final List<ErrorField> fields;

        public BodyErro(LocalDateTime timestamp, String message, String path, List<ErrorField> fields) {
            this.timestamp = timestamp;
            this.message = message;
            this.path = path;
            this.fields = fields;
        }

        public BodyErro(LocalDateTime timestamp, String message, String path) {
            this(timestamp, message, path, null);
        }

        public LocalDateTime getTimestamp() { return timestamp; }
        public String getMessage() { return message; }
        public String getPath() { return path; }
        public List<ErrorField> getFields() { return fields; }
    }

    public BodyErro getBody() { return body; }
    public int getStatus() { return status; }
}
