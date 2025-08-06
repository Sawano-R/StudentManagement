package raisetech.StudentManagement.data;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private Integer id;
  private Integer idStudents;
  private String course;
  private Date startDay;
  private Date endDay;
  private boolean deleted;

}
