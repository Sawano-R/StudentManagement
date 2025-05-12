package raisetech.StudentManagement;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourses {

  private String id;
  private String idStudents;
  private String course;
  private LocalDate startDay;
  private LocalDate endDay;

}
