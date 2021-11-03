package mx.edu.utez.keeputez.bean;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ErrorMessage {
    private final int statusCode = HttpStatus.BAD_REQUEST.value();
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message = "Operación fallida";
    private final String description;
    public ErrorMessage(String description) {
        this.description = description;
    }
}
