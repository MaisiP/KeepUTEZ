package mx.edu.utez.keeputez.model;

import lombok.Data;

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

    @ManyToOne
    private User user;

}
