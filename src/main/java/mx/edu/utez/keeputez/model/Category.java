package mx.edu.utez.keeputez.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes;
}
