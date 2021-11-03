package mx.edu.utez.keeputez.bean;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class SuccessMessage {
    private final int statusCode = HttpStatus.OK.value();
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message = "Operación exitosa";
    private final String description;
    public SuccessMessage(String description) {
        this.description = description;
    }
}
