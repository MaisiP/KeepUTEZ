package mx.edu.utez.keeputez.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategoryDeleteDTO {

    @Id
    @NotNull
    private Integer id;

}
