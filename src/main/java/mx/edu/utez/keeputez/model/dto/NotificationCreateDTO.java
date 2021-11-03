package mx.edu.utez.keeputez.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class NotificationCreateDTO {

    @NotEmpty
    private Integer intervalDays;
    @NotEmpty
    private String time;

}
