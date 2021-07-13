package activitytracker;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "activities")
public class Activity {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(generator = "Act_Gen")
    @TableGenerator(name = "Act_Gen",table = "act_id_gen",pkColumnName = "id_gen",pkColumnValue = "id_val")
    private Long id;

    @Column(name = "start_time",nullable = false)
    private LocalDateTime startTime;

    @Column(name = "description",nullable = false,length = 200)
    private String desc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Type type;

    public Activity(LocalDateTime startTime, String desc, Type type) {
        this.startTime = startTime;
        this.desc = desc;
        this.type = type;
    }
}
