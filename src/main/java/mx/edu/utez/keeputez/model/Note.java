package mx.edu.utez.keeputez.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String body;
    private String image;
    private LocalDate expiration;

    @ManyToOne
    private User user;
    @ManyToOne
    private Category category;
}
