package mx.edu.utez.keeputez.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CategoryUpdateListNotes {

    @Getter
    @Setter
    private static class NoteDTO{
        @Id
        @NotNull
        private Integer Id;
    }

    @Id
    @NotNull
    private Integer id;

    @Valid
    @NotEmpty
    private List<NoteDTO> notes;
}
