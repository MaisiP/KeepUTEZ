package mx.edu.utez.keeputez.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CategoryCreateDTO {

    @NotEmpty
    private String name;
}
