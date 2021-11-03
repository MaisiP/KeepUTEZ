package mx.edu.utez.keeputez.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class NoteUpdateDTO {

    @Getter
    @Setter
    private static class CategoryDTO{
        @Id
        @NotNull
        private Integer Id;
    }

    @Id
    @NotNull
    private Integer id;

    @NotEmpty
    private String title;

    private String body;

    @NotEmpty
    private String image;

    @NotNull
    private LocalDate expiration;

    private CategoryDTO category;
}
