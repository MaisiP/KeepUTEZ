package mx.edu.utez.keeputez.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer intervalDays;
    private LocalTime time;

    @ManyToOne
    private User user;
}
