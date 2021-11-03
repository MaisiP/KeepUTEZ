package mx.edu.utez.keeputez.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class NoteCreateDTO {

    @Getter
    @Setter
    private static class CategoryDTO{
        @Id
        @NotEmpty
        private Integer Id;
    }

    @NotEmpty
    private String title;

    private String body;

    private String image;

    @NotNull
    private LocalDate expiration;

    private CategoryDTO category;
}
