package mx.edu.utez.keeputez.bean;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SuccessMessage {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message = "Operación exitosa";
    private final String description;
    public SuccessMessage(String description) {
        this.description = description;
    }
}
