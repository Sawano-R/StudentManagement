package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "コース申し込み状況")
public class CourseStatus {

  private Integer id;

  private int idCourses;

  private int idStudents;

  @NotBlank
  private String status;
}
