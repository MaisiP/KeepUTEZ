package mx.edu.utez.keeputez.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@Setter
public class NotificationCreateDTO {

    @NotNull
    private Integer intervalDays;
    @NotNull
    private LocalTime time;

}
