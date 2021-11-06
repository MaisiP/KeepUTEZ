package mx.edu.utez.keeputez.bean;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ListSuccessMessage {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message = "Operación exitosa";
    private final List<?> data;
    public ListSuccessMessage(List<?> data){this.data = data;}
}
