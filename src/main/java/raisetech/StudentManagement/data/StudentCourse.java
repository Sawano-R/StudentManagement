package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.sql.Date;
import lombok.Data;

@Schema(description = "受講生コース情報")
@Data
public class StudentCourse {

  private Integer id;

  private Integer idStudents;

  @NotBlank
  private String course;

  private Date startDay;
  private Date endDay;
  private boolean deleted;

}
