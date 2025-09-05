package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "コース申し込み状況")
public class StudentCourseStatus {

  private Integer id;


  private int idCourses;

  @NotBlank
  private String status;
}
