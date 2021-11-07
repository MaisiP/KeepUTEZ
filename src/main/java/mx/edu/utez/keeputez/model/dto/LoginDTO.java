package mx.edu.utez.keeputez.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LoginDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    public void setUsername(String username) {
        this.username = username != null ? username.trim() : null;
    }
}
