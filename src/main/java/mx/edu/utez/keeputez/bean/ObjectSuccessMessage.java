package mx.edu.utez.keeputez.bean;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ObjectSuccessMessage {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message = "Operación exitosa";
    private final Object data;
    public ObjectSuccessMessage(Object data) {
        this.data = data;
    }
}
