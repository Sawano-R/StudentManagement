package raisetech.StudentManagement.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  @NotBlank
  @Pattern(regexp = "^\\d+$")
  private Integer id;

  @NotBlank
  @Pattern(regexp = "^\\d+$")
  private Integer idStudents;

  @NotBlank
  private String course;

  private Date startDay;
  private Date endDay;
  private boolean deleted;

}
