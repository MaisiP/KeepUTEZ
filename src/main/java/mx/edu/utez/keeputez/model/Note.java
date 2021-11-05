package mx.edu.utez.keeputez.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String body;
    @Lob
    private Byte[] image;
    private LocalDate expiration;

    @JsonIgnore
    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;
}
