package tracker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseStatistics {
    private int id;
    private int points;
    private String completed;
}
